package AthoraCore.listener;

import AthoraCore.main.Main;
import AthoraCore.util.manager.FarmingManager;
import AthoraCore.util.manager.GeneralDestroyableBlocksManager;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockGrowEvent;

public class SeedsGrow implements Listener {

    private final Main plugin;

    public SeedsGrow(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGrow(BlockGrowEvent event) {
        if (FarmingManager.fieldsContainsField(event.getBlock().getLevel().getBlock((int) event.getBlock().x, (int) (event.getBlock().y - 1), (int) event.getBlock().z), FarmingManager.getPlant(event.getBlock())) ||
                GeneralDestroyableBlocksManager.placedBlocks.containsKey(event.getBlock())) {
            event.setCancelled(true);
        }
    }


}
