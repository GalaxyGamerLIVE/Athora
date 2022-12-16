package de.athoramine.core.util.manager;

import cn.nukkit.scoreboard.data.DisplaySlot;
import cn.nukkit.scoreboard.scoreboard.Scoreboard;
import de.athoramine.core.api.AthoraPlayer;
import cn.nukkit.Player;
import cn.nukkit.Server;
import de.athoramine.core.main.Main;
import tim03we.futureplots.FuturePlots;
import tim03we.futureplots.utils.Plot;

public class ScoreboardManager {

    public static void loadScoreboard(Player player) {
        if (player.isOnline()) {

            if (Main.scoreboards.containsKey(player)) {
                Main.scoreboards.get(player).removeViewer(player, DisplaySlot.SIDEBAR);
                Main.scoreboards.remove(player);
            }

            Scoreboard scoreboard = new Scoreboard("dummy", " §l§gAthora§6-Mine§g.de ");

            scoreboard.addLine("    §8§o " + Server.getInstance().getOnlinePlayers().size() + "/" + Server.getInstance().getMaxPlayers() + " " + player.getLevelName() + " ", 0);

            if (WorldManager.isInWorld(player, WorldManager.PLOT)) {
                Plot plot = FuturePlots.getInstance().getPlotByPosition(player.getPosition());
                if (plot != null) {
                    scoreboard.addLine("   §o§8" + plot.getX() + ":" + plot.getZ() + " " + FuturePlots.provider.getOwner(plot) + "", 1);
                }
            }

            scoreboard.addLine("  §o§7-§gDein Vermögen§7-", 2);
            scoreboard.addLine("     §2Geld:§a " + String.format("%.2f", AthoraPlayer.getPurse(player)) + "§2$", 3);
            scoreboard.addLine("     §6Ruhm: §g" + String.format("%.2f", AthoraPlayer.getRuhm(player)), 4);
            scoreboard.addLine("    §o§7-§gDein Profil§7-", 5);
            scoreboard.addLine("       §6Level: §g" + AthoraPlayer.getLevel(player), 6);
            scoreboard.addLine("    §6Spielzeit: §g" + PlaytimeManager.getTotalPlaytimeFormatted(player), 7);

            scoreboard.addViewer(player, DisplaySlot.SIDEBAR);

            Main.scoreboards.put(player, scoreboard);
        }
    }

}