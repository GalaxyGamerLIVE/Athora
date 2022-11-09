package de.athoramine.core.util.manager;

import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import de.athoramine.core.api.AthoraBankAPI;
import de.athoramine.core.database.DefaultDatabase;
import de.athoramine.core.database.SQLEntity;
import de.athoramine.core.main.Main;
import de.athoramine.core.util.Helper;
import de.athoramine.core.util.Vars;
import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import de.athoramine.core.util.configs.BankConfig;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;

public class InterestManager {

    public static void checkInterestDistribute() {
        int hour = LocalDateTime.now().getHour();
        int minute = LocalDateTime.now().getMinute();
        if (hour == 0 && minute == 1) {
            Server.getInstance().getScheduler().scheduleAsyncTask(Main.getInstance(), new AsyncTask() {
                @Override
                public void onRun() {
                    InterestManager.distributeInterest();
                }
            });
        }
    }

    public static void distributeInterest() {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT athora_players.bank_id, athora_bank.money, athora_bank.storage_level, athora_bank.last_interest_amount FROM athora_players, athora_bank WHERE athora_players.bank_id = athora_bank.id;");
        try {
            Map<Integer, Integer> storageLevelLimitMap = BankConfig.getMoneyLimitMapping();
            while (sqlEntity.resultSet.next()) {
                int bankId = sqlEntity.resultSet.getInt("bank_id");
                double bankMoney = sqlEntity.resultSet.getDouble("money");
                int storageLevel = sqlEntity.resultSet.getInt("storage_level");
                int lastInterestAmount = sqlEntity.resultSet.getInt("last_interest_amount");
                int moneyLimit = storageLevelLimitMap.get(storageLevel);
                double interest = bankMoney * 0.01;

                if (bankMoney >= (double) moneyLimit) {
                    continue;
                }

                if (bankMoney + interest > moneyLimit && bankMoney < (double) moneyLimit) {
                    interest = ((double) moneyLimit) - bankMoney;
                }

                lastInterestAmount += (int) interest;

                AthoraBankAPI.setLastInterestAmount(bankId, lastInterestAmount);
                AthoraBankAPI.setBankMoney(bankId, bankMoney + interest);
            }
            sqlEntity.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
    }

    public static void checkInterest(Player player, boolean joined) {
        int lastInterestAmount = getLastInterestAmount(player);
        if (lastInterestAmount > 0) {
            resetLastInterestAmount(player);
            String message = Vars.PREFIX + TextFormat.GREEN + "Du hast gerade §l" + TextFormat.GOLD + lastInterestAmount + "$" + TextFormat.RESET + TextFormat.GREEN + " Zinsen erhalten!";
            if (joined) message = Vars.PREFIX + TextFormat.GREEN + "Du hast insgesamt §l" + TextFormat.GOLD + lastInterestAmount + "$" + TextFormat.RESET + TextFormat.GREEN + " Zinsen erhalten";
            player.sendMessage(message);
            Helper.playSound("random.orb", player, 0.4f, 1.5f);
        }
    }

    public static int getLastInterestAmount(Player player) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT last_interest_amount FROM athora_bank, athora_players WHERE athora_players.uuid = '" + player.getUniqueId().toString() + "' AND athora_bank.id = athora_players.bank_id;");
        try {
            if (sqlEntity.resultSet.next()) {
                int amount = sqlEntity.resultSet.getInt("last_interest_amount");
                sqlEntity.close();
                return amount;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return 0;
    }

    public static void resetLastInterestAmount(Player player) {
        DefaultDatabase.update("UPDATE athora_bank, athora_players SET last_interest_amount = 0 WHERE athora_players.uuid = '" + player.getUniqueId().toString() + "' AND athora_bank.id = athora_players.bank_id;");
    }

}


// SQL Interest Event Code:
//
//DELIMITER @@;
//
//CREATE EVENT athora_bank_interest_event
//        ON SCHEDULE EVERY 1 DAY
//        STARTS '2022-05-20 00:00:00' ON COMPLETION PRESERVE ENABLE
//        DO
//        BEGIN
//
//        UPDATE athora_bank SET last_interest_amount = (last_interest_amount + ROUND(money * 0.1, 0)) WHERE money > 0;
//
//        UPDATE athora_bank SET money = ROUND(money * 1.1, 0) WHERE money > 0;
//
//        END;
//@@;
//
//DELIMITER ;