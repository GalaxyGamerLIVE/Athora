package AthoraCore.util.manager;

import AthoraCore.util.Vars;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.utils.TextFormat;
import net.llamagames.AreaProtection.AreaProtection;

import java.util.Map;
import java.util.UUID;

public class ReloadLoop implements Runnable {

    @Override
    public void run() {
        Server.getInstance().broadcastMessage(Vars.PREFIX + TextFormat.RED + "§lServer Reload in 10 Sekunden!");
        Server.getInstance().getScheduler().scheduleDelayedTask(() -> {
            Server.getInstance().broadcastMessage(Vars.PREFIX + TextFormat.RED + "§lServer Reload in 5 Sekunden!");
        }, 100); // 1 sec -> 20 ticks
        Server.getInstance().getScheduler().scheduleDelayedTask(() -> {
            Server.getInstance().broadcastMessage(Vars.PREFIX + TextFormat.RED + "§lServer Reload in 4 Sekunden!");
        }, 120);
        Server.getInstance().getScheduler().scheduleDelayedTask(() -> {
            Server.getInstance().broadcastMessage(Vars.PREFIX + TextFormat.RED + "§lServer Reload in 3 Sekunden!");
        }, 140);
        Server.getInstance().getScheduler().scheduleDelayedTask(() -> {
            Server.getInstance().broadcastMessage(Vars.PREFIX + TextFormat.RED + "§lServer Reload in 2 Sekunden!");
        }, 160);
        Server.getInstance().getScheduler().scheduleDelayedTask(() -> {
            Server.getInstance().broadcastMessage(Vars.PREFIX + TextFormat.RED + "§lServer Reload in einer Sekunde!");
        }, 180);
        Server.getInstance().getScheduler().scheduleDelayedTask(() -> {
            Server.getInstance().reload();
        }, 200);
    }
}
