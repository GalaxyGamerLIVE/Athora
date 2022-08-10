package de.athoramine.core.util;

import de.athoramine.core.util.manager.*;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.utils.TextFormat;
import net.llamagames.AreaProtection.AreaProtection;

import java.util.Map;
import java.util.UUID;

public class GameLoop implements Runnable {

    @Override
    public void run() {
        MineManager.respawnDestroyedBlocks();
        ForagingManager.respawnDestroyedTrees();
        FarmingManager.respawnDestroyedBlocks();
        GeneralDestroyableBlocksManager.respawnDestroyedBlocks();
        if (MineSetupManager.mineSetupList.isEmpty() && MineSetupManager.isMineUnderSetup) {
            MineSetupManager.isMineUnderSetup = false;
            MineSetupManager.renderDefaultView(Server.getInstance().getDefaultLevel());
        }
        if (FarmingManager.setupList.isEmpty() && FarmingManager.isFarmUnderSetup) {
            FarmingManager.isFarmUnderSetup = false;
            FarmingManager.renderDefaultView(Server.getInstance().getDefaultLevel());
        }
        if (GeneralDestroyableBlocksManager.setupList.isEmpty() && GeneralDestroyableBlocksManager.isSetupModeActive) {
            GeneralDestroyableBlocksManager.isSetupModeActive = false;
            GeneralDestroyableBlocksManager.renderDefaultView(Server.getInstance().getDefaultLevel());
        }
        try {
            Map<UUID, Player> players = Server.getInstance().getOnlinePlayers();
            if (!players.isEmpty()) {
                for (Player player : players.values()) {
                    ScoreboardManager.loadScoreboard(player);
                    NameTagManager.updateNameTag(player);
                    DailyQuestManager.loadBossBar(player);
                    if (SalaryManager.reachedMinSalaryTime(player) && !SalaryManager.isSalaryFinished(player)) {
                        SalaryManager.untrackPlayer(player);
                        SalaryManager.setPlayerToSalaryFinished(player);
                        player.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du kannst jetzt dein Gehalt mit " + TextFormat.BLUE + "/gehalt" + TextFormat.GREEN + " abholen!");
                    }
                    boolean debug = false;
                    if (debug) {
                        if (AreaProtection.getInstance().getAreaByPos(player.getPosition()) == null) {
                            player.sendActionBar(TextFormat.GREEN + "Area: " + TextFormat.RED + "/");
                        } else {
                            player.sendActionBar(TextFormat.GREEN + "Area: " + TextFormat.RED + AreaProtection.getInstance().getAreaByPos(player.getPosition()).getName());
                        }
                        Block blockLookingAt = player.getTargetBlock(50);
                        if (blockLookingAt != null) {
//                        player.sendPopup(TextFormat.GREEN + "Block: " + TextFormat.BLUE + blockLookingAt.x + " " + blockLookingAt.y + " " + blockLookingAt.z);
                            player.sendPopup(TextFormat.GREEN + "Block: " + blockLookingAt.getId() + " " + blockLookingAt.getDamage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
