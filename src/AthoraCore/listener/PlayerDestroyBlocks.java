package AthoraCore.listener;

import AthoraCore.main.Main;
import AthoraCore.util.*;
import AthoraCore.util.manager.*;
import cn.nukkit.Player;
import cn.nukkit.block.BlockID;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.block.BlockUpdateEvent;
import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;

public class PlayerDestroyBlocks implements Listener {

    private Main plugin;

    public PlayerDestroyBlocks(Main main) {
        this.plugin = main;
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        boolean isGeneralDestroyableBlock = GeneralDestroyableBlocksManager.getLocationIndexInStorage(event.getBlock().getLocation()) != -1;
        boolean isFarmingBlock = FarmingManager.fieldsContainsField(event.getBlock().getLevel().getBlock((int) event.getBlock().x, (int) (event.getBlock().y - 1), (int) event.getBlock().z), FarmingManager.getPlant(event.getBlock()));

        if (GeneralDestroyableBlocksManager.setupList.containsKey(player) || GeneralDestroyableBlocksManager.isSetupModeActive && isGeneralDestroyableBlock || !GeneralDestroyableBlocksManager.isSetupModeActive && GeneralDestroyableBlocksManager.placedBlocks.containsKey(event.getBlock())) {
            if (GeneralDestroyableBlocksManager.isSetupModeActive) {
                event.setCancelled(true);
                if (GeneralDestroyableBlocksManager.setupList.containsKey(player)) {
                    if (GeneralDestroyableBlocksManager.toggleSetupBlock(player, event.getBlock())) {
                        event.getBlock().getLevel().setBlockIdAt((int) event.getBlock().x, (int) event.getBlock().y, (int) event.getBlock().z, BlockID.MAGENTA_GLAZED_TERRACOTTA);
                        event.getBlock().getLevel().setBlockDataAt((int) event.getBlock().x, (int) event.getBlock().y, (int) event.getBlock().z, 5);
                    }
                } else {
                    player.sendMessage(Vars.PREFIX + TextFormat.RED + "Der Setup Mode ist momenten für diesen Block aktiviert und kann daher nicht abgebaut werden!");
                }
            } else {
                if (GeneralDestroyableBlocksManager.placedBlocks.containsKey(event.getBlock()) && GeneralDestroyableBlocksManager.placedBlocks.get(event.getBlock())) {
                    GeneralDestroyableBlocksManager.destroyBlock(player, event.getBlock());
                } else {
                    event.setCancelled(true);
                }
            }

        } else if (isFarmingBlock || !isFarmingBlock && FarmingManager.setupList.containsKey(player) || isFarmingBlock && !FarmingManager.setupList.containsKey(player) && FarmingManager.isFarmUnderSetup) {
            if (FarmingManager.isFarmUnderSetup) {
                event.setCancelled(true);
                if (FarmingManager.setupList.containsKey(player)) {
                    if (FarmingManager.togglePlantBlock(player, event.getBlock())) {
                        event.getBlock().getLevel().setBlockIdAt((int) event.getBlock().x, (int) event.getBlock().y, (int) event.getBlock().z, BlockID.CONCRETE);
                        event.getBlock().getLevel().setBlockDataAt((int) event.getBlock().x, (int) event.getBlock().y, (int) event.getBlock().z, FarmingManager.getSetupBlockMetaForPlant(FarmingManager.setupList.get(player)));
                    }
                } else {
                    player.sendMessage(Vars.PREFIX + TextFormat.RED + "Alle Farmen sind gerade im Setup Mode und können daher nicht benutzt werden!");
                }
            } else {
                FarmingManager.destroyBlock(player, event.getBlock().getLevel().getBlock((int) event.getBlock().x, (int) (event.getBlock().y - 1), (int) event.getBlock().z));
                if (event.getBlock().getDamage() >= 7) {
                    for (Item item : event.getDrops()) {
                        event.getBlock().getLevel().dropItem(event.getBlock().getLocation(), item);
                    }
                }
                event.setCancelled(true);
            }


        } else if (MineManager.isBlockBreakable(event.getBlock()) && !BuildManager.getState(player) || MineSetupManager.isMineUnderSetup && MineManager.isPositionInMine(event.getBlock().getLocation())) {
            if (MineSetupManager.isMineUnderSetup) {
                event.setCancelled(true);
                if (MineSetupManager.mineSetupList.containsKey(player) && MineSetupManager.mineSetupList.get(player)) {
                    if (MineSetupManager.toggleSpawnBlock(event.getBlock())) {
                        event.getBlock().getLevel().setBlockIdAt((int) event.getBlock().x, (int) event.getBlock().y, (int) event.getBlock().z, BlockID.CONCRETE);
                        event.getBlock().getLevel().setBlockDataAt((int) event.getBlock().x, (int) event.getBlock().y, (int) event.getBlock().z, 5);
                    } else {
                        event.getBlock().getLevel().setBlockIdAt((int) event.getBlock().x, (int) event.getBlock().y, (int) event.getBlock().z, BlockID.STONE);
                        event.getBlock().getLevel().setBlockDataAt((int) event.getBlock().x, (int) event.getBlock().y, (int) event.getBlock().z, 0);
                    }
                } else {
                    player.sendMessage(Vars.PREFIX + TextFormat.RED + "Die Mine ist momentan im Setup Modus und kann daher nicht benutzt werden!");
                }
            } else {
                plugin.getServer().getScheduler().scheduleDelayedTask(() -> {
                    MineManager.destroyBlock(event.getPlayer(), event.getBlock());
                }, 2);
            }


        } else if (ForagingManager.isBlockBreakable(event.getBlock()) && !BuildManager.getState(player)) {
            ForagingManager.destroyBlock(event.getPlayer(), event.getBlock());
//            event.setCancelled(true);


        } else if (!this.plugin.getServer().getMotd().equalsIgnoreCase("dev-server") && !this.plugin.getServer().getMotd().equalsIgnoreCase("plots1") && !BuildManager.getState(player)) {
            event.setCancelled(true);
        }


    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent event) {
        if (!this.plugin.getServer().getMotd().equalsIgnoreCase("dev-server") && !BuildManager.getState(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void blockUpdate(BlockUpdateEvent event) {
        if (GeneralDestroyableBlocksManager.placedBlocks.containsKey(event.getBlock())) {
            event.setCancelled(true);
        }
    }

}
