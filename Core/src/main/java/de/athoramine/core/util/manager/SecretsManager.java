package de.athoramine.core.util.manager;

import de.athoramine.core.api.AthoraPlayer;
import de.athoramine.core.database.DefaultDatabase;
import de.athoramine.core.database.SQLEntity;
import de.athoramine.core.util.Helper;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SecretsManager {

    public static Map<Player, int[]> setupAddList = new HashMap<>();
    public static Map<Player, Boolean> setupRemoveList = new HashMap<>();
    public static Map<Block, int[]> secrets = new HashMap<>();

    public static void addSecret(String world, int x, int y, int z, int coins, int ruhm) {
        DefaultDatabase.update("INSERT INTO athora_secrets (world, x, y, z, ruhm, coins, discovers) VALUES('" + world + "', " + x + ", " + y + ", " + z + ", " + ruhm + ", " + coins + ", 0);");
        loadSecrets();
    }

    public static void removeSecret(String world, int x, int y, int z) {
        DefaultDatabase.update("DELETE FROM athora_player_secrets WHERE secret_id = " + getSecretID(Server.getInstance().getLevelByName(world).getBlock(x, y, z)) + ";");
        DefaultDatabase.update("DELETE FROM athora_secrets WHERE world = '" + world + "' AND x = " + x + " AND y = " + y + " AND z = " + z + ";");
        loadSecrets();
    }

    public static void loadSecrets() {
        secrets.clear();
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT * FROM athora_secrets");
        try {
            if (!sqlEntity.resultSet.next()) {
                Server.getInstance().getLogger().info("Found no Secrets!");
            } else {
                do {
                    if (Helper.levelExists(sqlEntity.resultSet.getString("world"))) {
                        Level level = Server.getInstance().getLevelByName(sqlEntity.resultSet.getString("world"));
                        int x = sqlEntity.resultSet.getInt("x");
                        int y = sqlEntity.resultSet.getInt("y");
                        int z = sqlEntity.resultSet.getInt("z");
                        int ruhmReward = sqlEntity.resultSet.getInt("ruhm");
                        int coinsReward = sqlEntity.resultSet.getInt("coins");
                        int[] rewards = new int[]{coinsReward, ruhmReward};
                        Block secretBlock = level.getBlock(x, y, z);
                        secrets.put(secretBlock, rewards);
                    }
                } while (sqlEntity.resultSet.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
    }

//    public static void getAllSecrets() {
//        ResultSet resultSet = Database.query("SELECT * FROM athora_secrets");
//        try {
//            if (!resultSet.next()) {
//                Server.getInstance().getLogger().info("Found no Secrets!");
//            } else {
//                do {
//                    Level level = Server.getInstance().getLevelByName(resultSet.getString("world"));
//                    int x = resultSet.getInt("x");
//                    int y = resultSet.getInt("y");
//                    int z = resultSet.getInt("z");
//                    int ruhmReward = resultSet.getInt("ruhm");
//                    int coinsReward = resultSet.getInt("coins");
//                    int[] rewards = new int[]{coinsReward, ruhmReward};
//                    Block secretBlock = level.getBlock(x, y, z);
//                    secrets.put(secretBlock, rewards);
//                } while (resultSet.next());
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public static boolean hasPlayerFoundSecret(Player player, Block secret) {
        int playerId = AthoraPlayer.getPlayerID(player);
        int secretId = getSecretID(secret);
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT * FROM athora_player_secrets WHERE player_id = " + playerId + " AND secret_id = " + secretId + ";");
        try {
            if (!sqlEntity.resultSet.next()) {
                sqlEntity.close();
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return false;
    }

    public static int getPlayerPositionOnSecret(Player player, Block secret) {
        int playerId = AthoraPlayer.getPlayerID(player);
        int secretId = getSecretID(secret);
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT * FROM athora_player_secrets WHERE player_id = " + playerId + " AND secret_id = " + secretId + ";");
        try {
            if (sqlEntity.resultSet.next()) {
                int position = sqlEntity.resultSet.getInt("position");
                sqlEntity.close();
                return position;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return 0;
    }

    public static void playerFindSecret(Player player, Block secret) {
        int playerId = AthoraPlayer.getPlayerID(player);
        int secretId = getSecretID(secret);
        int currentSecretDiscovers = getSecretDiscovers(secret);
        int playerDiscoverPosition = currentSecretDiscovers + 1;
        DefaultDatabase.update("INSERT INTO athora_player_secrets (player_id, secret_id, position) VALUES(" + playerId + ", " + secretId + ", " + playerDiscoverPosition + ");");
        DefaultDatabase.update("UPDATE athora_secrets SET discovers = " + playerDiscoverPosition + " WHERE world = '" + secret.getLevel().getName() + "' AND x = " + (int) secret.x + " AND y = " + (int) secret.y + " AND z = " + (int) secret.z + ";");
    }

    public static int getSecretDiscovers(Block secret) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT discovers FROM athora_secrets WHERE world = '" + secret.getLevel().getName() + "' AND x = " + (int) secret.x + " AND y = " + (int) secret.y + " AND z = " + (int) secret.z + ";");
        try {
            if (sqlEntity.resultSet.next()) {
                int discovers = sqlEntity.resultSet.getInt("discovers");
                sqlEntity.close();
                return discovers;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return 0;
    }

    public static int getSecretID(Block secret) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT id FROM athora_secrets WHERE world = '" + secret.getLevel().getName() + "' AND x = " + (int) secret.x + " AND y = " + (int) secret.y + " AND z = " + (int) secret.z + ";");
        try {
            if (sqlEntity.resultSet.next()) {
                int id = sqlEntity.resultSet.getInt("id");
                sqlEntity.close();
                return id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
