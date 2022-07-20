package AthoraCore.util.manager;

import AthoraCore.database.DefaultDatabase;
import AthoraCore.database.SQLEntity;
import AthoraCore.util.Helper;
import AthoraCore.util.Vars;
import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InterestManager {

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