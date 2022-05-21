package AthoraCore.api;

import AthoraCore.util.Database;
import AthoraCore.util.Helper;
import cn.nukkit.Player;
import cn.nukkit.Server;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class AthoraPlayer {

    public static boolean isNewPlayer(Player player) {
        ResultSet resultSet = Database.query("SELECT uuid FROM athora_players WHERE uuid = '" + player.getUniqueId().toString() + "';");
        try {
            if (resultSet.next()) {
                return resultSet.getString("uuid") == null;
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isNewPlayer(UUID uuid) {
        ResultSet resultSet = Database.query("SELECT uuid FROM athora_players WHERE uuid = '" + uuid.toString() + "';");
        try {
            if (resultSet.next()) {
                return resultSet.getString("uuid") == null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void createNewPlayer(Player player) {
        Database.update("INSERT INTO athora_bank (money, storage_level, salary_level, salary_time, experience) VALUES(0, 1, 1, 0, 0);");
        Database.update("INSERT INTO athora_players (bank_id, uuid, playername, ruhm, level, purse, playtime) VALUES(LAST_INSERT_ID(), '" + player.getUniqueId().toString() + "', '" + player.getName() + "', 0, 1, 0, 0);");
    }

    public static void createNewPlayer(UUID uuid, String playername) {
        Database.update("INSERT INTO athora_bank (money, storage_level, interest_level, experience) VALUES(10, 1, 1, 0);");
        Database.update("INSERT INTO athora_players (bank_id, uuid, playername, ruhm, level, purse, playtime) VALUES(LAST_INSERT_ID(), '" + uuid.toString() + "', '" + playername + "', 0, 1, 0, 0);");
    }

    public static String getUUIDbyPlayerName(String playerName) {
        ResultSet resultSet = Database.query("SELECT uuid FROM athora_players WHERE playername = '" + playerName + "';");
        try {
            if (resultSet.next()) {
                return resultSet.getString("uuid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int getPlayerID(Player player) {
        ResultSet resultSet = Database.query("SELECT id FROM athora_players WHERE uuid = '" + player.getUniqueId().toString() + "';");
        try {
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static void setInventory(Player player, String inventory) {
        if (inventory.equalsIgnoreCase("")) {
            Database.update("UPDATE athora_players SET inventory = NULL WHERE uuid = '" + player.getUniqueId().toString() + "';");
        }else {
            Database.update("UPDATE athora_players SET inventory = '" + inventory + "' WHERE uuid = '" + player.getUniqueId().toString() + "';");
        }
    }

    public static String getInventory(Player player) {
        ResultSet resultSet = Database.query("SELECT inventory FROM athora_players WHERE uuid = '" + player.getUniqueId().toString() + "';");
        try {
            if (resultSet.next()) {
                return resultSet.getString("inventory");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int getLevel(Player player) {
        ResultSet resultSet = Database.query("SELECT level FROM athora_players WHERE uuid = '" + player.getUniqueId().toString() + "';");
        try {
            if (resultSet.next()) {
                return resultSet.getInt("level");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static int getLevel(UUID uuid) {
        ResultSet resultSet = Database.query("SELECT level FROM athora_players WHERE uuid = '" + uuid.toString() + "';");
        try {
            if (resultSet.next()) {
                return resultSet.getInt("level");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static void setLevel(Player player, int level) {
        Database.update("UPDATE athora_players SET level = " + level + " WHERE uuid = '" + player.getUniqueId().toString() + "';");
    }

    public static void setLevel(UUID uuid, int level) {
        Database.update("UPDATE athora_players SET level = " + level + " WHERE uuid = '" + uuid.toString() + "';");
    }

    public static double getRuhm(Player player) {
        ResultSet resultSet = Database.query("SELECT ruhm FROM athora_players WHERE uuid = '" + player.getUniqueId().toString() + "';");
        try {
            if (resultSet.next()) {
                return resultSet.getDouble("ruhm");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static double getRuhm(UUID uuid) {
        ResultSet resultSet = Database.query("SELECT ruhm FROM athora_players WHERE uuid = '" + uuid.toString() + "';");
        try {
            if (resultSet.next()) {
                return resultSet.getDouble("ruhm");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static void setRuhm(Player player, double ruhm) {
        ruhm = Helper.round(ruhm, 3);
        Database.update("UPDATE athora_players SET ruhm = " + ruhm + " WHERE uuid = '" + player.getUniqueId().toString() + "';");
    }

    public static void setRuhm(UUID uuid, double ruhm) {
        ruhm = Helper.round(ruhm, 3);
        Database.update("UPDATE athora_players SET ruhm = " + ruhm + " WHERE uuid = '" + uuid.toString() + "';");
    }

    public static double getPurse(Player player) {
        ResultSet resultSet = Database.query("SELECT purse FROM athora_players WHERE uuid = '" + player.getUniqueId().toString() + "';");
        try {
            if (resultSet.next()) {
                return resultSet.getDouble("purse");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static double getPurse(UUID uuid) {
        ResultSet resultSet = Database.query("SELECT purse FROM athora_players WHERE uuid = '" + uuid.toString() + "';");
        try {
            if (resultSet.next()) {
                return resultSet.getDouble("purse");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static void setPurse(Player player, double purse) {
        purse = Helper.round(purse, 2);
        Database.update("UPDATE athora_players SET purse = " + purse + " WHERE uuid = '" + player.getUniqueId().toString() + "';");
    }

    public static void setPurse(UUID uuid, double purse) {
        purse = Helper.round(purse, 2);
        Database.update("UPDATE athora_players SET purse = " + purse + " WHERE uuid = '" + uuid.toString() + "';");
    }

    public static double getBankMoney(Player player) {
        ResultSet resultSet = Database.query("SELECT athora_bank.money FROM athora_bank, athora_players WHERE athora_players.uuid = '" + player.getUniqueId().toString() + "' AND athora_bank.id = athora_players.bank_id;");
        try {
            if (resultSet.next()) {
                return resultSet.getDouble("money");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static double getBankMoney(UUID uuid) {
        ResultSet resultSet = Database.query("SELECT athora_bank.money FROM athora_bank, athora_players WHERE athora_players.uuid = '" + uuid.toString() + "' AND athora_bank.id = athora_players.bank_id;");
        try {
            if (resultSet.next()) {
                return resultSet.getDouble("money");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static void setBankMoney(Player player, double money) {
        Database.update("UPDATE athora_bank, athora_players SET athora_bank.money = " + money + " WHERE athora_players.uuid = '" + player.getUniqueId().toString() + "' AND athora_bank.id = athora_players.bank_id;");
    }

    public static void setBankMoney(UUID uuid, double money) {
        Database.update("UPDATE athora_bank, athora_players SET athora_bank.money = " + money + " WHERE athora_players.uuid = '" + uuid.toString() + "' AND athora_bank.id = athora_players.bank_id;");
    }

    public static int getBankExperience(Player player) {
        ResultSet resultSet = Database.query("SELECT athora_bank.experience FROM athora_bank, athora_players WHERE athora_players.uuid = '" + player.getUniqueId().toString() + "' AND athora_bank.id = athora_players.bank_id;");
        try {
            if (resultSet.next()) {
                return resultSet.getInt("experience");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static int getBankExperience(UUID uuid) {
        ResultSet resultSet = Database.query("SELECT athora_bank.experience FROM athora_bank, athora_players WHERE athora_players.uuid = '" + uuid.toString() + "' AND athora_bank.id = athora_players.bank_id;");
        try {
            if (resultSet.next()) {
                return resultSet.getInt("experience");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static void setBankExperience(Player player, int experience) {
        Database.update("UPDATE athora_bank, athora_players SET athora_bank.experience = " + experience + " WHERE athora_players.uuid = '" + player.getUniqueId().toString() + "' AND athora_bank.id = athora_players.bank_id;");
    }

    public static void setBankExperience(UUID uuid, int experience) {
        Database.update("UPDATE athora_bank, athora_players SET athora_bank.experience = " + experience + " WHERE athora_players.uuid = '" + uuid.toString() + "' AND athora_bank.id = athora_players.bank_id;");
    }

    public static int getStorageLevel(Player player) {
        ResultSet resultSet = Database.query("SELECT athora_bank.storage_level FROM athora_bank, athora_players WHERE athora_players.uuid = '" + player.getUniqueId().toString() + "' AND athora_bank.id = athora_players.bank_id;");
        try {
            if (resultSet.next()) {
                return resultSet.getInt("storage_level");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static int getStorageLevel(UUID uuid) {
        ResultSet resultSet = Database.query("SELECT athora_bank.storage_level FROM athora_bank, athora_players WHERE athora_players.uuid = '" + uuid.toString() + "' AND athora_bank.id = athora_players.bank_id;");
        try {
            if (resultSet.next()) {
                return resultSet.getInt("storage_level");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static void setStorageLevel(Player player, int level) {
        Database.update("UPDATE athora_bank, athora_players SET athora_bank.storage_level = " + level + " WHERE athora_players.uuid = '" + player.getUniqueId().toString() + "' AND athora_bank.id = athora_players.bank_id;");
    }

    public static void setStorageLevel(UUID uuid, int level) {
        Database.update("UPDATE athora_bank, athora_players SET athora_bank.storage_level = " + level + " WHERE athora_players.uuid = '" + uuid.toString() + "' AND athora_bank.id = athora_players.bank_id;");
    }

    public static int getSalaryLevel(Player player) {
        ResultSet resultSet = Database.query("SELECT athora_bank.salary_level FROM athora_bank, athora_players WHERE athora_players.uuid = '" + player.getUniqueId().toString() + "' AND athora_bank.id = athora_players.bank_id;");
        try {
            if (resultSet.next()) {
                return resultSet.getInt("salary_level");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static int getSalaryLevel(UUID uuid) {
        ResultSet resultSet = Database.query("SELECT athora_bank.salary_level FROM athora_bank, athora_players WHERE athora_players.uuid = '" + uuid.toString() + "' AND athora_bank.id = athora_players.bank_id;");
        try {
            if (resultSet.next()) {
                return resultSet.getInt("salary_level");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static void setSalaryLevel(Player player, int level) {
        Database.update("UPDATE athora_bank, athora_players SET athora_bank.salary_level = " + level + " WHERE athora_players.uuid = '" + player.getUniqueId().toString() + "' AND athora_bank.id = athora_players.bank_id;");
    }

    public static void setSalaryLevel(UUID uuid, int level) {
        Database.update("UPDATE athora_bank, athora_players SET athora_bank.salary_level = " + level + " WHERE athora_players.uuid = '" + uuid.toString() + "' AND athora_bank.id = athora_players.bank_id;");
    }

    public static int getSalaryTime(Player player) {
        ResultSet resultSet = Database.query("SELECT athora_bank.salary_time FROM athora_bank, athora_players WHERE athora_players.uuid = '" + player.getUniqueId().toString() + "' AND athora_bank.id = athora_players.bank_id;");
        try {
            if (resultSet.next()) {
                return resultSet.getInt("salary_time");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static int getSalaryTime(UUID uuid) {
        ResultSet resultSet = Database.query("SELECT athora_bank.salary_time FROM athora_bank, athora_players WHERE athora_players.uuid = '" + uuid.toString() + "' AND athora_bank.id = athora_players.bank_id;");
        try {
            if (resultSet.next()) {
                return resultSet.getInt("salary_time");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static void setSalaryTime(Player player, long time) {
        Database.update("UPDATE athora_bank, athora_players SET athora_bank.salary_time = " + time + " WHERE athora_players.uuid = '" + player.getUniqueId().toString() + "' AND athora_bank.id = athora_players.bank_id;");
    }

    public static void setSalaryTime(UUID uuid, long time) {
        Database.update("UPDATE athora_bank, athora_players SET athora_bank.salary_time = " + time + " WHERE athora_players.uuid = '" + uuid.toString() + "' AND athora_bank.id = athora_players.bank_id;");
    }

    public static long getPlaytime(Player player) {
        ResultSet resultSet = Database.query("SELECT playtime FROM athora_players WHERE uuid = '" + player.getUniqueId().toString() + "';");
        try {
            if (resultSet.next()) {
                return resultSet.getLong("playtime");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getPlaytime(UUID uuid) {
        ResultSet resultSet = Database.query("SELECT playtime FROM athora_players WHERE uuid = '" + uuid.toString() + "';");
        try {
            if (resultSet.next()) {
                return resultSet.getLong("playtime");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void setPlaytime(Player player, long playtime) {
        Database.update("UPDATE athora_players SET playtime = " + playtime + " WHERE uuid = '" + player.getUniqueId().toString() + "';");
    }

    public static void setPlaytime(UUID uuid, long playtime) {
        Database.update("UPDATE athora_players SET playtime = " + playtime + " WHERE uuid = '" + uuid.toString() + "';");
    }

}
