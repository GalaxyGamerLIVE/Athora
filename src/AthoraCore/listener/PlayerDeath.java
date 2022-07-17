package AthoraCore.listener;

import AthoraCore.api.AthoraPlayer;
import AthoraCore.main.Main;
import AthoraCore.util.Helper;
import AthoraCore.util.configs.BankConfig;
import AthoraCore.util.configs.GeneralConfig;
import AthoraCore.util.manager.ServerManager;
import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.utils.TextFormat;

public class PlayerDeath implements Listener {

    private final Main plugin;

    public PlayerDeath(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (!GeneralConfig.generalConfig.getStringList("no_keep_inventory_worlds").contains(event.getEntity().getLevelName())) {
            event.setKeepInventory(true);
            event.setKeepExperience(true);
        }
        if (!BankConfig.config.getStringList("keep_coins_on_death_worlds").contains(event.getEntity().getLevel().getName())) {
            double currentPurse = AthoraPlayer.getPurse(event.getEntity());
            if (currentPurse > 1) {
                double reduceMoneyAmount = Math.round((currentPurse / 100) * 50);
                AthoraPlayer.setPurse(event.getEntity(), (currentPurse - reduceMoneyAmount));
                event.getEntity().sendMessage(TextFormat.RED + "Du hast " + TextFormat.GOLD + reduceMoneyAmount + "$" + TextFormat.RED + " verloren! (Zahle dein Geld auf der Bank ein um nichts zu verlieren!");
            }
        }
    }

    @EventHandler
    public void onKill(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && !ServerManager.getCurrentServer().equalsIgnoreCase(ServerManager.PLOT_SERVER)) {
            Entity entity = event.getEntity();
            if (entity.getHealth() - event.getDamage() <= 0) {
                Player attacker = (Player) event.getDamager();
                double ruhmReward = 0.25;
                AthoraPlayer.setRuhm(attacker, AthoraPlayer.getRuhm(attacker) + ruhmReward);
                attacker.sendActionBar(TextFormat.GOLD + "+ " + ruhmReward + " Ruhm");
                Helper.playSound("random.orb", attacker, 0.3f, 0.8f);
            }
        }
    }

}
