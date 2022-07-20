package AthoraCore.util.manager;

import AthoraCore.api.AthoraPlayer;
import AthoraCore.database.DefaultDatabase;
import AthoraCore.database.SQLEntity;
import AthoraCore.util.Helper;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class LeaderboardManager {

    public static final float ENTITY_SIZE_PLAYTIME = 0.00001f;
    public static final float ENTITY_SIZE_SECRETS = 0.00002f;
    public static final float ENTITY_SIZE_RUHM = 0.00003f;
    public static final float ENTITY_SIZE_MONEY = 0.00004f;
    public static final float ENTITY_SIZE_LEVEL = 0.00005f;

    public static final String LEADERBOARD_PLAYTIME = "leaderboard_playtime";
    public static final String LEADERBOARD_SECRETS = "leaderboard_secrets";
    public static final String LEADERBOARD_RUHM = "leaderboard_ruhm";
    public static final String LEADERBOARD_MONEY = "leaderboard_money";
    public static final String LEADERBOARD_LEVEL = "leaderboard_level";

    public static final int LEADERBOARD_ROWS_PLAYTIME = 15;
    public static final int LEADERBOARD_ROWS_SECRETS = 15;
    public static final int LEADERBOARD_ROWS_RUHM = 15;
    public static final int LEADERBOARD_ROWS_MONEY = 15;
    public static final int LEADERBOARD_ROWS_LEVEL = 15;

    public static Map<String, Entity[]> leaderboardEntities = new HashMap<>();

    public static void loadLeaderboards(Level level) {
        fetchLeaderboardEntities(level);

        for (Map.Entry<String, Entity[]> entry : leaderboardEntities.entrySet()) {

            if (entry.getKey().equalsIgnoreCase(LEADERBOARD_PLAYTIME)) {
                String playtimeNameTag = getTopPlaytimeNameTag();
                for (Entity entity : entry.getValue()) {
                    entity.setNameTag(playtimeNameTag);
                }
            }
            if (entry.getKey().equalsIgnoreCase(LEADERBOARD_RUHM)) {
                String ruhmNameTag = getTopRuhmNameTag();
                for (Entity entity : entry.getValue()) {
                    entity.setNameTag(ruhmNameTag);
                }
            }
            if (entry.getKey().equalsIgnoreCase(LEADERBOARD_SECRETS)) {
                String secretsNameTag = getTopSecretsNameTag();
                for (Entity entity : entry.getValue()) {
                    entity.setNameTag(secretsNameTag);
                }
            }
            if (entry.getKey().equalsIgnoreCase(LEADERBOARD_MONEY)) {
                String moneyNameTag = getTopMoneyNameTag();
                for (Entity entity : entry.getValue()) {
                    entity.setNameTag(moneyNameTag);
                }
            }
            if (entry.getKey().equalsIgnoreCase(LEADERBOARD_LEVEL)) {
                String levelNameTag = getTopLevelNameTag();
                for (Entity entity : entry.getValue()) {
                    entity.setNameTag(levelNameTag);
                }
            }
        }
    }

    public static String getTopLevelNameTag() {
        int index = 1;
        StringBuilder nameTag = new StringBuilder();
        nameTag.append("§l§6>> §gBestenliste §7-§r §3Level §l§6<<\n");
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT athora_players.playername, athora_players.`level` FROM athora_players ORDER BY athora_players.`level` DESC LIMIT " + LEADERBOARD_ROWS_LEVEL + ";");
        try {
            while (sqlEntity.resultSet.next()) {
                String playername = sqlEntity.resultSet.getString("playername");
                int level = sqlEntity.resultSet.getInt("level");
                nameTag.append("§7" + index + ". §e" + playername + " §7- §3" + level + "\n");
                index++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return nameTag.toString();
    }

    public static String getTopMoneyNameTag() {
        int index = 1;
        StringBuilder nameTag = new StringBuilder();
        nameTag.append("§l§6>> §gBestenliste §7-§r §2$ §aGeld §2$ §l§6<<\n");
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT athora_players.playername, (athora_players.purse + athora_bank.money) AS total_money " +
                "FROM athora_players, athora_bank " +
                "WHERE athora_players.bank_id = athora_bank.id " +
                "ORDER BY (athora_players.purse + athora_bank.money) DESC LIMIT " + LEADERBOARD_ROWS_MONEY + ";");
        try {
            while (sqlEntity.resultSet.next()) {
                int totalMoney = sqlEntity.resultSet.getInt("total_money");
                String playername = sqlEntity.resultSet.getString("playername");
                nameTag.append("§7" + index + ". §e" + playername + " §7- §a" + totalMoney + "§2$§r\n");
                index++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return nameTag.toString();
    }

    public static String getTopRuhmNameTag() {
        int index = 1;
        StringBuilder nameTag = new StringBuilder();
        nameTag.append("§l§6>> §gBestenliste §7-§r §cRuhm §l§6<<\n");
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT playername, ruhm FROM athora_players ORDER BY ruhm DESC LIMIT " + LEADERBOARD_ROWS_RUHM + ";");
        try {
            while (sqlEntity.resultSet.next()) {
                String playername = sqlEntity.resultSet.getString("playername");
                int ruhm = sqlEntity.resultSet.getInt("ruhm");
                nameTag.append("§7" + index + ". §e" + playername + " §7- §c" + ruhm + "\n");
                index++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return nameTag.toString();
    }

    public static String getTopSecretsNameTag() {
        int index = 1;
        StringBuilder nameTag = new StringBuilder();
        nameTag.append("§l§6>> §gBestenliste §7-§r §9Secrets §l§6<<\n");
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT DISTINCT athora_players.`uuid`, COUNT(athora_player_secrets.player_id) AS secrets" +
                " FROM athora_players, athora_player_secrets" +
                " WHERE athora_players.id = athora_player_secrets.player_id" +
                " GROUP BY player_id ORDER BY COUNT(athora_player_secrets.player_id) DESC LIMIT " + LEADERBOARD_ROWS_SECRETS + ";");
        try {
            while (sqlEntity.resultSet.next()) {
                String playername = AthoraPlayer.getPlayerName(UUID.fromString(sqlEntity.resultSet.getString("uuid")));
                int secrets = sqlEntity.resultSet.getInt("secrets");
                nameTag.append("§7" + index + ". §e" + playername + " §7- §9" + secrets + "§r\n");
                index++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return nameTag.toString();
    }

    public static String getTopPlaytimeNameTag() {
        String[] leaderboardData = new String[]{};
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT uuid FROM athora_players ORDER BY playtime DESC LIMIT " + LEADERBOARD_ROWS_PLAYTIME + ";");
        try {
            while (sqlEntity.resultSet.next()) {
                leaderboardData = Helper.append(leaderboardData, sqlEntity.resultSet.getString("uuid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int index = 1;
        StringBuilder nameTag = new StringBuilder();
        nameTag.append("§l§6>> §gBestenliste §7-§r §5Spielzeit §l§6<<\n");
        for (String row : leaderboardData) {
            String playername = AthoraPlayer.getPlayerName(UUID.fromString(row));
            String playtime = String.format("%dh", TimeUnit.MILLISECONDS.toHours(AthoraPlayer.getPlaytime(UUID.fromString(row))));
            if (Server.getInstance().getPlayer(UUID.fromString(row)).isPresent()) {
                playtime = PlaytimeManager.getTotalPlaytimeFormatted(Server.getInstance().getPlayer(UUID.fromString(row)).get());
            }
            nameTag.append("§7" + index + ". §e" + playername + " §7- §5" + playtime + "\n");
            index++;
        }
        sqlEntity.close();
        return nameTag.toString();
    }

    public static void fetchLeaderboardEntities(Level level) {
        Entity[] playtimeEntities = new Entity[]{};
        Entity[] secretsEntities = new Entity[]{};
        Entity[] ruhmEntities = new Entity[]{};
        Entity[] moneyEntities = new Entity[]{};
        Entity[] levelEntities = new Entity[]{};
        for (Entity entity : level.getEntities()) {
            if (entity.getScale() == ENTITY_SIZE_PLAYTIME) {
                playtimeEntities = Helper.append(playtimeEntities, entity);
            } else if (entity.getScale() == ENTITY_SIZE_SECRETS) {
                secretsEntities = Helper.append(secretsEntities, entity);
            } else if (entity.getScale() == ENTITY_SIZE_RUHM) {
                ruhmEntities = Helper.append(ruhmEntities, entity);
            } else if (entity.getScale() == ENTITY_SIZE_MONEY) {
                moneyEntities = Helper.append(moneyEntities, entity);
            } else if (entity.getScale() == ENTITY_SIZE_LEVEL) {
                levelEntities = Helper.append(levelEntities, entity);
            }
        }
        leaderboardEntities.clear();
        leaderboardEntities.put(LEADERBOARD_PLAYTIME, playtimeEntities);
        leaderboardEntities.put(LEADERBOARD_SECRETS, secretsEntities);
        leaderboardEntities.put(LEADERBOARD_RUHM, ruhmEntities);
        leaderboardEntities.put(LEADERBOARD_MONEY, moneyEntities);
        leaderboardEntities.put(LEADERBOARD_LEVEL, levelEntities);
    }

}
