package AthoraCore.listener;

import AthoraCore.main.Main;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    private final Main plugin;

    public PlayerMove(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

//        String[] mines = {"mine1", "mine2", "mine3", "mine4", "mine5", "mine6"};
//        if (AreaProtection.getInstance().getAreaByPos(player.getPosition()) != null && Arrays.stream(mines).anyMatch(AreaProtection.getInstance().getAreaByPos(player.getPosition()).getName()::equalsIgnoreCase)) {
//            int playerLevel = AthoraPlayer.getLevel(player);
//            player.sendMessage(String.valueOf(AreaProtection.getInstance().getAreaByPos(player.getPosition()).getName().split("mine")));
////            if (playerLevel < MineManager.getMineRequirements(Integer.parseInt(AreaProtection.getInstance().getAreaByPos(player.getPosition()).getName().split("mine")[0]))) {
////                player.knockBack(player, 0, 0, -8);
////                player.sendTitle(TextFormat.RED + "Ab Level " + TextFormat.BLUE + 5);
////            }
//        }

//        if (AreaProtection.getInstance().getAreaByPos(player.getPosition()) != null && AreaProtection.getInstance().getAreaByPos(player.getPosition()).getName().equalsIgnoreCase("mine2")) {
//            player.knockBack(player, 0, 0, -8);
//            player.sendTitle(TextFormat.RED + "Ab Level " + TextFormat.BLUE + 5);
//        }

    }

}
