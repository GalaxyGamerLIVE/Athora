package AthoraCore.util.manager;

import AthoraCore.api.AthoraPlayer;
import AthoraCore.main.Main;
import cn.nukkit.Player;
import cn.nukkit.Server;
import de.theamychan.scoreboard.api.ScoreboardAPI;
import de.theamychan.scoreboard.network.DisplaySlot;
import de.theamychan.scoreboard.network.Scoreboard;
import de.theamychan.scoreboard.network.ScoreboardDisplay;
import tim03we.futureplots.FuturePlots;
import tim03we.futureplots.utils.Plot;

public class ScoreboardManager {

    public static boolean plotsEnabled = false;

    public static void loadScoreboard(Player player) {
        if (player.isOnline()) {

            if (Main.scoreboards.containsKey(player)) {
                Main.scoreboards.get(player).hideFor(player);
                Main.scoreboards.remove(player);
            }

            Scoreboard scoreboard = ScoreboardAPI.createScoreboard();
            ScoreboardDisplay scoreboardDisplay = scoreboard.addDisplay(DisplaySlot.SIDEBAR, "dumy", " §l§gAthora§6-Mine§g.de ");

            scoreboardDisplay.addLine("    §8§o " + Server.getInstance().getOnlinePlayers().size() + "/" + Server.getInstance().getMaxPlayers() + " " + Server.getInstance().getMotd() + " ", 0);

            if (plotsEnabled) {
                Plot plot = FuturePlots.getInstance().getPlotByPosition(player.getPosition());
                if (plot != null) {
                    scoreboardDisplay.addLine("   §o§8" + plot.getX() + ":" + plot.getZ() + " " + FuturePlots.provider.getOwner(plot) + "", 1);
                }
            }

            scoreboardDisplay.addLine("  §o§7-§gDein Vermögen§7-", 2);
            scoreboardDisplay.addLine("     §2Geld:§a " + String.format("%.2f", AthoraPlayer.getPurse(player)) + "§2$", 3);
            scoreboardDisplay.addLine("     §6Ruhm: §g" + String.format("%.2f", AthoraPlayer.getRuhm(player)), 4);
            scoreboardDisplay.addLine("    §o§7-§gDein Profil§7-", 5);
            scoreboardDisplay.addLine("       §6Level: §g" + AthoraPlayer.getLevel(player), 6);
            scoreboardDisplay.addLine("    §6Spielzeit: §g" + PlaytimeManager.getTotalPlaytimeFormatted(player), 7);



            Main.scoreboards.put(player, scoreboard);
            Main.scoreboards.get(player).showFor(player);
        }
    }

}