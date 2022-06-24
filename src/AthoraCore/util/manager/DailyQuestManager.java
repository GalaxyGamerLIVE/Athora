package AthoraCore.util.manager;

import AthoraCore.api.AthoraPlayer;
import AthoraCore.components.Quest;
import AthoraCore.components.QuestType;
import AthoraCore.database.DefaultDatabase;
import AthoraCore.database.GlobalDatabase;
import AthoraCore.util.Helper;
import AthoraCore.util.Vars;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.utils.DummyBossBar;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.TextFormat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class DailyQuestManager {

    public static void collectQuestItemsForActiveQuest(Player player) {
        Quest quest = getActiveQuest(player);
        int givenAmount = getActiveQuestGivenAmount(player);
        int restAmount = quest.getAmount() - givenAmount;
        int collectedAmount = 0;

        for (Map.Entry<Integer, Item> entry : player.getInventory().getContents().entrySet()) {
            if (entry.getValue().getId() == quest.getItemID() && entry.getValue().getDamage() == quest.getItemMeta()) {
                if (restAmount - entry.getValue().getCount() > 0) {
                    collectedAmount += entry.getValue().getCount();
                    restAmount -= entry.getValue().getCount();
                    player.getInventory().clear(entry.getKey());
                } else if (restAmount - entry.getValue().getCount() == 0) {
                    collectedAmount += entry.getValue().getCount();
                    player.getInventory().clear(entry.getKey());
                    break;
                } else if (restAmount - entry.getValue().getCount() < 0) {
                    int collectToZero = entry.getValue().getCount() - (entry.getValue().getCount() - restAmount);
                    collectedAmount += collectToZero;
                    player.getInventory().setItem(entry.getKey(), Item.get(quest.getItemID(), quest.getItemMeta(), entry.getValue().getCount() - collectToZero));
                    break;
                }
            }
        }

        if (collectedAmount > 0) {
            player.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du hast erfolgreich " + TextFormat.BLUE + collectedAmount + " " + getItemName(quest) + TextFormat.GREEN + " abgegeben!");
            updateQuestGivenAmount(player, givenAmount + collectedAmount);
        }
        if (restAmount - collectedAmount <= 0) {
            finishQuest(player);
            AthoraPlayer.setPurse(player, quest.getMoneyReward() + AthoraPlayer.getPurse(player));
            AthoraPlayer.setRuhm(player, quest.getRuhmReward() + AthoraPlayer.getRuhm(player));
            Helper.createFireworkParticle(player, DyeColor.ORANGE);
            player.sendMessage("§7<===§l§a Quest abgeschlossen!§7  ===>\n\n§6Du bekommst:\n\n§8 - §a" + quest.getMoneyReward() + "§2$\n§8 - §6" + quest.getRuhmReward() + " §gRuhm\n\n§8----------------------------");
        }
    }

    public static String getItemName(Quest quest) {
        if (quest.getItemType().equalsIgnoreCase("block")) {
            Block block = Block.get(quest.getItemID(), quest.getItemMeta());
            return block.getName();
        }
        Item item = Item.get(quest.getItemID(), quest.getItemMeta());
        return item.getName();
    }

    public static void loadBossBar(Player player) {
        if (hasActiveQuest(player)) {
            Quest quest = getActiveQuest(player);
            int givenAmount = getActiveQuestGivenAmount(player);
            String text = "§fBringe §g" + givenAmount + " von " + quest.getAmount() + " " + getItemName(quest) + "§f zu Finn";
            float progress = 0.01f;
            if (givenAmount > 0) {
                progress = (float) givenAmount / (float) quest.getAmount() * 100.0f;
            }
            if (BossBarManager.playerHasBossBar(player)) {
                DummyBossBar bossBar = BossBarManager.getPlayerBossBar(player);
                bossBar.setText(text);
                bossBar.setLength(progress);
            } else {
                DummyBossBar bossBar = (new DummyBossBar.Builder(player)).text(text).length(progress).build();
                player.createBossBar(bossBar);
                BossBarManager.addBossBar(player, bossBar);
            }
        } else if (BossBarManager.playerHasBossBar(player)) {
            BossBarManager.removeBossBar(player);
        }
    }

    public static int getActiveQuestGivenAmount(Player player) {
        ResultSet resultSet = DefaultDatabase.query("SELECT given_amount FROM athora_quest_tracker WHERE player_id = " + AthoraPlayer.getPlayerID(player) + " AND done = 0;");
        try {
            if (resultSet.next())
                return resultSet.getInt("given_amount");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Quest getActiveQuest(Player player) {
        ResultSet resultSet = DefaultDatabase.query("SELECT quest_id FROM athora_quest_tracker WHERE player_id = " + AthoraPlayer.getPlayerID(player) + " AND done = 0;");
        try {
            if (resultSet.next())
                return getQuest(resultSet.getInt("quest_id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateQuestGivenAmount(Player player, int amount) {
        DefaultDatabase.update("UPDATE athora_quest_tracker SET given_amount = " + amount + " WHERE player_id = " + AthoraPlayer.getPlayerID(player) + " AND done = 0;");
    }

    public static void finishQuest(Player player) {
        DefaultDatabase.update("UPDATE athora_quest_tracker SET done = 1 WHERE player_id = " + AthoraPlayer.getPlayerID(player) + ";");
    }

    public static void startQuest(Player player, Quest quest) {
        DefaultDatabase.update("INSERT INTO athora_quest_tracker (player_id, quest_id) VALUES(" + AthoraPlayer.getPlayerID(player) + ", " + quest.getQuestID() + ");");
    }

    public static boolean hasActiveQuest(Player player) {
        ResultSet resultSet = DefaultDatabase.query("SELECT id FROM athora_quest_tracker WHERE player_id = " + AthoraPlayer.getPlayerID(player) + " AND done = 0;");
        try {
            if (resultSet.next())
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Quest[] getCompletedQuests(Player player) {
        Quest[] quests = new Quest[]{};
        ResultSet resultSet = DefaultDatabase.query("SELECT quest_id FROM athora_quest_tracker WHERE player_id = " + AthoraPlayer.getPlayerID(player) + " AND done = 1;");
        try {
            while (resultSet.next())
                quests = Helper.append(quests, getQuest(resultSet.getInt("quest_id")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quests;
    }

    public static Quest[] getDailyQuests() {
        Quest[] quests = new Quest[]{};
        ResultSet resultSet = GlobalDatabase.query("SELECT quest_id FROM athora_daily_quest_pool;");
        try {
            while (resultSet.next())
                quests = Helper.append(quests, getQuest(resultSet.getInt("quest_id")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quests;
    }

    public static Quest getQuest(int id) {
        ResultSet resultSet = GlobalDatabase.query("SELECT * FROM athora_quests WHERE id = " + id + ";");
        try {
            if (resultSet.next())
                return new Quest(resultSet
                        .getInt("id"),
                        getQuestType(resultSet.getInt("type_id")), resultSet
                        .getString("item_type"), resultSet
                        .getInt("item_id"), resultSet
                        .getInt("item_meta"), resultSet
                        .getInt("amount"), resultSet
                        .getDouble("reward_money"), resultSet
                        .getDouble("reward_ruhm"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static QuestType getQuestType(int id) {
        ResultSet resultSet = GlobalDatabase.query("SELECT * FROM athora_quest_types WHERE id = " + id + ";");
        try {
            if (resultSet.next())
                return new QuestType(resultSet
                        .getInt("id"), resultSet
                        .getInt("required_level"), resultSet
                        .getString("name"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
