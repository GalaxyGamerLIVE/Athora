package AthoraCore.util.manager;

import AthoraCore.api.AthoraPlayer;
import AthoraCore.util.Database;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SecretsManager {

    public static Map<Player, int[]> setupAddList = new HashMap<>();
    public static Map<Player, Boolean> setupRemoveList = new HashMap<>();
    public static Map<Block, int[]> secrets = new HashMap<>();

    public static void addSecret(String world, int x, int y, int z, int coins, int ruhm) {
        Database.update("INSERT INTO athora_secrets (world, x, y, z, ruhm, coins, discovers) VALUES('" + world + "', " + x + ", " + y + ", " + z + ", " + ruhm + ", " + coins + ", 0);");
        loadSecrets();
    }

    public static void removeSecret(String world, int x, int y, int z) {
        Database.update("DELETE FROM athora_player_secrets WHERE secret_id = " + getSecretID(Server.getInstance().getLevelByName(world).getBlock(x, y, z)) + ";");
        Database.update("DELETE FROM athora_secrets WHERE world = '" + world + "' AND x = " + x + " AND y = " + y + " AND z = " + z + ";");
        loadSecrets();
    }

    public static void loadSecrets() {
        secrets.clear();
        ResultSet resultSet = Database.query("SELECT * FROM athora_secrets");
        try {
            if (!resultSet.next()) {
                Server.getInstance().getLogger().info("Found no Secrets!");
            } else {
                do {
                    Level level = Server.getInstance().getLevelByName(resultSet.getString("world"));
                    int x = resultSet.getInt("x");
                    int y = resultSet.getInt("y");
                    int z = resultSet.getInt("z");
                    int ruhmReward = resultSet.getInt("ruhm");
                    int coinsReward = resultSet.getInt("coins");
                    int[] rewards = new int[]{coinsReward, ruhmReward};
                    Block secretBlock = level.getBlock(x, y, z);
                    secrets.put(secretBlock, rewards);
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        ResultSet resultSet = Database.query("SELECT * FROM athora_player_secrets WHERE player_id = " + playerId + " AND secret_id = " + secretId + ";");
        try {
            if (!resultSet.next()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getPlayerPositionOnSecret(Player player, Block secret) {
        int playerId = AthoraPlayer.getPlayerID(player);
        int secretId = getSecretID(secret);
        ResultSet resultSet = Database.query("SELECT * FROM athora_player_secrets WHERE player_id = " + playerId + " AND secret_id = " + secretId + ";");
        try {
            if (resultSet.next()) {
                return resultSet.getInt("position");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void playerFindSecret(Player player, Block secret) {
        int playerId = AthoraPlayer.getPlayerID(player);
        int secretId = getSecretID(secret);
        int currentSecretDiscovers = getSecretDiscovers(secret);
        int playerDiscoverPosition = currentSecretDiscovers + 1;
        Database.update("INSERT INTO athora_player_secrets (player_id, secret_id, position) VALUES(" + playerId + ", " + secretId + ", " + playerDiscoverPosition + ");");
        Database.update("UPDATE athora_secrets SET discovers = " + playerDiscoverPosition + " WHERE world = '" + secret.getLevel().getName() + "' AND x = " + (int) secret.x + " AND y = " + (int) secret.y + " AND z = " + (int) secret.z + ";");
    }

    public static int getSecretDiscovers(Block secret) {
        ResultSet resultSet = Database.query("SELECT discovers FROM athora_secrets WHERE world = '" + secret.getLevel().getName() + "' AND x = " + (int) secret.x + " AND y = " + (int) secret.y + " AND z = " + (int) secret.z + ";");
        try {
            if (resultSet.next()) {
                return resultSet.getInt("discovers");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getSecretID(Block secret) {
        ResultSet resultSet = Database.query("SELECT id FROM athora_secrets WHERE world = '" + secret.getLevel().getName() + "' AND x = " + (int) secret.x + " AND y = " + (int) secret.y + " AND z = " + (int) secret.z + ";");
        try {
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
