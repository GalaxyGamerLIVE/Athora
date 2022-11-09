package de.athoramine.core.api;

import de.athoramine.core.database.DefaultDatabase;
import de.athoramine.core.database.ProductionDatabase;
import de.athoramine.core.database.SQLEntity;
import de.athoramine.core.util.Helper;
import cn.nukkit.Player;

import java.sql.SQLException;
import java.util.UUID;

public class AthoraPlayer {
    public static boolean isNewPlayer(Player player) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT uuid FROM athora_players WHERE uuid = '" + player.getUniqueId().toString() + "';");
        try {
            if (sqlEntity.resultSet.next()) {
                boolean isNew = sqlEntity.resultSet.getString("uuid") == null;
                sqlEntity.close();
                return isNew;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            sqlEntity.close();
            return false;
        }
    }

    public static boolean isNewPlayer(UUID uuid) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT uuid FROM athora_players WHERE uuid = '" + uuid.toString() + "';");
        try {
            if (sqlEntity.resultSet.next()) {
                boolean isNew = sqlEntity.resultSet.getString("uuid") == null;
                sqlEntity.close();
                return isNew;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return false;
    }

    public static void createNewPlayer(Player player) {
        DefaultDatabase.update("INSERT INTO athora_bank (money, storage_level, salary_level, salary_time, experience) VALUES(0, 1, 1, 0, 0);");
        DefaultDatabase.update("INSERT INTO athora_players (bank_id, uuid, playername, ruhm, level, purse, playtime) VALUES(LAST_INSERT_ID(), '" + player.getUniqueId().toString() + "', '" + player.getName() + "', 0, 1, 100, 0);");
    }

    public static void createNewPlayer(UUID uuid, String playername) {
        DefaultDatabase.update("INSERT INTO athora_bank (money, storage_level, interest_level, experience) VALUES(10, 1, 1, 0);");
        DefaultDatabase.update("INSERT INTO athora_players (bank_id, uuid, playername, ruhm, level, purse, playtime) VALUES(LAST_INSERT_ID(), '" + uuid.toString() + "', '" + playername + "', 0, 1, 0, 0);");
    }

    public static String getUUIDbyPlayerName(String playerName) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT uuid FROM athora_players WHERE playername = '" + playerName + "';");
        try {
            if (sqlEntity.resultSet.next()) {
                String uuid = sqlEntity.resultSet.getString("uuid");
                sqlEntity.close();
                return uuid;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return null;
    }

    public static String getPlayerName(int playerID) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT playername FROM athora_players WHERE id = " + playerID + ";");
        try {
            if (sqlEntity.resultSet.next()) {
                String playerName = sqlEntity.resultSet.getString("playername");
                sqlEntity.close();
                return playerName;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return "";
    }

    public static int getPlayerID(Player player) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT id FROM athora_players WHERE uuid = '" + player.getUniqueId().toString() + "';");
        try {
            if (sqlEntity.resultSet.next()) {
                int id = sqlEntity.resultSet.getInt("id");
                sqlEntity.close();
                return id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return 0;
    }

    public static String getPlayerName(UUID uuid) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT playername FROM athora_players WHERE uuid = '" + uuid.toString() + "';");
        try {
            if (sqlEntity.resultSet.next()) {
                String playerName = sqlEntity.resultSet.getString("playername");
                sqlEntity.close();
                return playerName;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return "";
    }

    public static void setInventory(Player player, String inventory) {
        if (inventory.equalsIgnoreCase("")) {
            DefaultDatabase.update("UPDATE athora_players SET inventory = NULL WHERE uuid = '" + player.getUniqueId().toString() + "';");
        } else {
            DefaultDatabase.update("UPDATE athora_players SET inventory = '" + inventory + "' WHERE uuid = '" + player.getUniqueId().toString() + "';");
        }
    }

    public static String getInventory(Player player) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT inventory FROM athora_players WHERE uuid = '" + player.getUniqueId().toString() + "';");
        try {
            if (sqlEntity.resultSet.next()) {
                String inventory = sqlEntity.resultSet.getString("inventory");
                sqlEntity.close();
                return inventory;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return null;
    }

    public static int getLevel(Player player) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT level FROM athora_players WHERE uuid = '" + player.getUniqueId().toString() + "';");
        try {
            if (sqlEntity.resultSet.next()) {
                int level = sqlEntity.resultSet.getInt("level");
                sqlEntity.close();
                return level;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return 0;
    }

    public static int getLevel(UUID uuid) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT level FROM athora_players WHERE uuid = '" + uuid.toString() + "';");
        try {
            if (sqlEntity.resultSet.next()) {
                int level = sqlEntity.resultSet.getInt("level");
                sqlEntity.close();
                return level;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return 0;
    }

    public static void setLevel(Player player, int level) {
        DefaultDatabase.update("UPDATE athora_players SET level = " + level + " WHERE uuid = '" + player.getUniqueId().toString() + "';");
    }

    public static void setLevel(UUID uuid, int level) {
        DefaultDatabase.update("UPDATE athora_players SET level = " + level + " WHERE uuid = '" + uuid.toString() + "';");
    }

    public static double getRuhm(Player player) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT ruhm FROM athora_players WHERE uuid = '" + player.getUniqueId().toString() + "';");
        try {
            if (sqlEntity.resultSet.next()) {
                double ruhm = sqlEntity.resultSet.getDouble("ruhm");
                sqlEntity.close();
                return ruhm;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return 0.0D;
    }

    public static double getRuhm(UUID uuid) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT ruhm FROM athora_players WHERE uuid = '" + uuid.toString() + "';");
        try {
            if (sqlEntity.resultSet.next()) {
                double ruhm = sqlEntity.resultSet.getDouble("ruhm");
                sqlEntity.close();
                return ruhm;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return 0.0D;
    }

    public static void setRuhm(Player player, double ruhm) {
        ruhm = Helper.round(ruhm, 3);
        DefaultDatabase.update("UPDATE athora_players SET ruhm = " + ruhm + " WHERE uuid = '" + player.getUniqueId().toString() + "';");
    }

    public static void setRuhm(UUID uuid, double ruhm) {
        ruhm = Helper.round(ruhm, 3);
        DefaultDatabase.update("UPDATE athora_players SET ruhm = " + ruhm + " WHERE uuid = '" + uuid.toString() + "';");
    }

    public static double getPurse(Player player) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT purse FROM athora_players WHERE uuid = '" + player.getUniqueId().toString() + "';");
        try {
            if (sqlEntity.resultSet.next()) {
                double purse = sqlEntity.resultSet.getDouble("purse");
                sqlEntity.close();
                return purse;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return 0.0D;
    }

    public static double getPurse(UUID uuid) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT purse FROM athora_players WHERE uuid = '" + uuid.toString() + "';");
        try {
            if (sqlEntity.resultSet.next()) {
                double purse = sqlEntity.resultSet.getDouble("purse");
                sqlEntity.close();
                return purse;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return 0.0D;
    }

    public static void setPurse(Player player, double purse) {
        purse = Helper.round(purse, 2);
        DefaultDatabase.update("UPDATE athora_players SET purse = " + purse + " WHERE uuid = '" + player.getUniqueId().toString() + "';");
    }

    public static void setPurse(UUID uuid, double purse) {
        purse = Helper.round(purse, 2);
        DefaultDatabase.update("UPDATE athora_players SET purse = " + purse + " WHERE uuid = '" + uuid.toString() + "';");
    }

    public static double getBankMoney(Player player) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT athora_bank.money FROM athora_bank, athora_players WHERE athora_players.uuid = '" + player.getUniqueId().toString() + "' AND athora_bank.id = athora_players.bank_id;");
        try {
            if (sqlEntity.resultSet.next()) {
                double money = sqlEntity.resultSet.getDouble("money");
                sqlEntity.close();
                return money;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return 0.0D;
    }

    public static double getBankMoney(UUID uuid) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT athora_bank.money FROM athora_bank, athora_players WHERE athora_players.uuid = '" + uuid.toString() + "' AND athora_bank.id = athora_players.bank_id;");
        try {
            if (sqlEntity.resultSet.next()) {
                double money = sqlEntity.resultSet.getDouble("money");
                sqlEntity.close();
                return money;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return 0.0D;
    }

    public static void setBankMoney(Player player, double money) {
        DefaultDatabase.update("UPDATE athora_bank, athora_players SET athora_bank.money = " + money + " WHERE athora_players.uuid = '" + player.getUniqueId().toString() + "' AND athora_bank.id = athora_players.bank_id;");
    }

    public static void setBankMoney(UUID uuid, double money) {
        DefaultDatabase.update("UPDATE athora_bank, athora_players SET athora_bank.money = " + money + " WHERE athora_players.uuid = '" + uuid.toString() + "' AND athora_bank.id = athora_players.bank_id;");
    }

    public static int getBankExperience(Player player) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT athora_bank.experience FROM athora_bank, athora_players WHERE athora_players.uuid = '" + player.getUniqueId().toString() + "' AND athora_bank.id = athora_players.bank_id;");
        try {
            if (sqlEntity.resultSet.next()) {
                int experience = sqlEntity.resultSet.getInt("experience");
                sqlEntity.close();
                return experience;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return 0;
    }

    public static int getBankExperience(UUID uuid) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT athora_bank.experience FROM athora_bank, athora_players WHERE athora_players.uuid = '" + uuid.toString() + "' AND athora_bank.id = athora_players.bank_id;");
        try {
            if (sqlEntity.resultSet.next()) {
                int experience = sqlEntity.resultSet.getInt("experience");
                sqlEntity.close();
                return experience;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return 0;
    }

    public static void setBankExperience(Player player, int experience) {
        DefaultDatabase.update("UPDATE athora_bank, athora_players SET athora_bank.experience = " + experience + " WHERE athora_players.uuid = '" + player.getUniqueId().toString() + "' AND athora_bank.id = athora_players.bank_id;");
    }

    public static void setBankExperience(UUID uuid, int experience) {
        DefaultDatabase.update("UPDATE athora_bank, athora_players SET athora_bank.experience = " + experience + " WHERE athora_players.uuid = '" + uuid.toString() + "' AND athora_bank.id = athora_players.bank_id;");
    }

    public static int getStorageLevel(Player player) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT athora_bank.storage_level FROM athora_bank, athora_players WHERE athora_players.uuid = '" + player.getUniqueId().toString() + "' AND athora_bank.id = athora_players.bank_id;");
        try {
            if (sqlEntity.resultSet.next()) {
                int storageLevel = sqlEntity.resultSet.getInt("storage_level");
                sqlEntity.close();
                return storageLevel;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return 0;
    }

    public static int getStorageLevel(UUID uuid) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT athora_bank.storage_level FROM athora_bank, athora_players WHERE athora_players.uuid = '" + uuid.toString() + "' AND athora_bank.id = athora_players.bank_id;");
        try {
            if (sqlEntity.resultSet.next()) {
                int storageLevel = sqlEntity.resultSet.getInt("storage_level");
                sqlEntity.close();
                return storageLevel;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return 0;
    }

    public static void setStorageLevel(Player player, int level) {
        DefaultDatabase.update("UPDATE athora_bank, athora_players SET athora_bank.storage_level = " + level + " WHERE athora_players.uuid = '" + player.getUniqueId().toString() + "' AND athora_bank.id = athora_players.bank_id;");
    }

    public static void setStorageLevel(UUID uuid, int level) {
        DefaultDatabase.update("UPDATE athora_bank, athora_players SET athora_bank.storage_level = " + level + " WHERE athora_players.uuid = '" + uuid.toString() + "' AND athora_bank.id = athora_players.bank_id;");
    }

    public static int getSalaryLevel(Player player) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT athora_bank.salary_level FROM athora_bank, athora_players WHERE athora_players.uuid = '" + player.getUniqueId().toString() + "' AND athora_bank.id = athora_players.bank_id;");
        try {
            if (sqlEntity.resultSet.next()) {
                int salaryLevel = sqlEntity.resultSet.getInt("salary_level");
                sqlEntity.close();
                return salaryLevel;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return 0;
    }

    public static int getSalaryLevel(UUID uuid) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT athora_bank.salary_level FROM athora_bank, athora_players WHERE athora_players.uuid = '" + uuid.toString() + "' AND athora_bank.id = athora_players.bank_id;");
        try {
            if (sqlEntity.resultSet.next()) {
                int salaryLevel = sqlEntity.resultSet.getInt("salary_level");
                sqlEntity.close();
                return salaryLevel;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return 0;
    }

    public static void setSalaryLevel(Player player, int level) {
        DefaultDatabase.update("UPDATE athora_bank, athora_players SET athora_bank.salary_level = " + level + " WHERE athora_players.uuid = '" + player.getUniqueId().toString() + "' AND athora_bank.id = athora_players.bank_id;");
    }

    public static void setSalaryLevel(UUID uuid, int level) {
        DefaultDatabase.update("UPDATE athora_bank, athora_players SET athora_bank.salary_level = " + level + " WHERE athora_players.uuid = '" + uuid.toString() + "' AND athora_bank.id = athora_players.bank_id;");
    }

    public static int getSalaryTime(Player player) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT athora_bank.salary_time FROM athora_bank, athora_players WHERE athora_players.uuid = '" + player.getUniqueId().toString() + "' AND athora_bank.id = athora_players.bank_id;");
        try {
            if (sqlEntity.resultSet.next()) {
                int time = sqlEntity.resultSet.getInt("salary_time");
                sqlEntity.close();
                return time;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return 0;
    }

    public static int getSalaryTime(UUID uuid) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT athora_bank.salary_time FROM athora_bank, athora_players WHERE athora_players.uuid = '" + uuid.toString() + "' AND athora_bank.id = athora_players.bank_id;");
        try {
            if (sqlEntity.resultSet.next()) {
                int time = sqlEntity.resultSet.getInt("salary_time");
                sqlEntity.close();
                return time;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return 0;
    }

    public static void setSalaryTime(Player player, long time) {
        DefaultDatabase.update("UPDATE athora_bank, athora_players SET athora_bank.salary_time = " + time + " WHERE athora_players.uuid = '" + player.getUniqueId().toString() + "' AND athora_bank.id = athora_players.bank_id;");
    }

    public static void setSalaryTime(UUID uuid, long time) {
        DefaultDatabase.update("UPDATE athora_bank, athora_players SET athora_bank.salary_time = " + time + " WHERE athora_players.uuid = '" + uuid.toString() + "' AND athora_bank.id = athora_players.bank_id;");
    }

    public static long getPlaytime(Player player) {
        SQLEntity sqlEntity = ProductionDatabase.query("SELECT playtime FROM athora_players WHERE uuid = '" + player.getUniqueId().toString() + "';");
        try {
            if (sqlEntity.resultSet.next()) {
                long playtime = sqlEntity.resultSet.getLong("playtime");
                sqlEntity.close();
                return playtime;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return 0L;
    }

    public static long getPlaytime(UUID uuid) {
        SQLEntity sqlEntity = ProductionDatabase.query("SELECT playtime FROM athora_players WHERE uuid = '" + uuid.toString() + "';");
        try {
            if (sqlEntity.resultSet.next()) {
                long playtime = sqlEntity.resultSet.getLong("playtime");
                sqlEntity.close();
                return playtime;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return 0L;
    }

    public static void setPlaytime(Player player, long playtime) {
        ProductionDatabase.update("UPDATE athora_players SET playtime = " + playtime + " WHERE uuid = '" + player.getUniqueId().toString() + "';");
    }

    public static void setPlaytime(UUID uuid, long playtime) {
        ProductionDatabase.update("UPDATE athora_players SET playtime = " + playtime + " WHERE uuid = '" + uuid.toString() + "';");
    }

    public static int getExperienceLevel(Player player) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT experience_level FROM athora_players WHERE uuid = '" + player.getUniqueId().toString() + "';");
        try {
            if (sqlEntity.resultSet.next()) {
                int level = sqlEntity.resultSet.getInt("experience_level");
                sqlEntity.close();
                return level;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return 0;
    }

    public static void setExperienceLevel(Player player, int level) {
        DefaultDatabase.update("UPDATE athora_players SET experience_level = " + level + " WHERE uuid = '" + player.getUniqueId().toString() + "';");
    }

    public static int getExperienceValue(Player player) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT experience_value FROM athora_players WHERE uuid = '" + player.getUniqueId().toString() + "';");
        try {
            if (sqlEntity.resultSet.next()) {
                int level = sqlEntity.resultSet.getInt("experience_value");
                sqlEntity.close();
                return level;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return 0;
    }

    public static void setExperienceValue(Player player, int value) {
        DefaultDatabase.update("UPDATE athora_players SET experience_value = " + value + " WHERE uuid = '" + player.getUniqueId().toString() + "';");
    }
}
