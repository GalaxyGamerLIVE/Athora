package de.athoramine.core.api;

import cn.nukkit.Player;
import de.athoramine.core.database.DefaultDatabase;

public class AthoraBankAPI {

    public static void setLastInterestAmount(Player player, int value) {
        DefaultDatabase.update("UPDATE athora_bank, athora_players SET athora_bank.last_interest_amount = " + value + " WHERE athora_players.uuid = '" + player.getUniqueId().toString() + "' AND athora_bank.id = athora_players.bank_id;");
    }

    public static void setLastInterestAmount(int bankId, int value) {
        DefaultDatabase.update("UPDATE athora_bank SET athora_bank.last_interest_amount = " + value + " WHERE athora_bank.id = " + bankId +  ";");
    }

    public static void setBankMoney(Player player, double money) {
        DefaultDatabase.update("UPDATE athora_bank, athora_players SET athora_bank.money = " + money + " WHERE athora_players.uuid = '" + player.getUniqueId().toString() + "' AND athora_bank.id = athora_players.bank_id;");
    }

    public static void setBankMoney(int bankId, double money) {
        DefaultDatabase.update("UPDATE athora_bank SET athora_bank.money = " + money + " WHERE athora_bank.id = " + bankId +  ";");
    }

}
