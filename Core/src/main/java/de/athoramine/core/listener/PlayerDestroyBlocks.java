package de.athoramine.core.listener;

import de.athoramine.core.api.AthoraPlayer;
import de.athoramine.core.forms.LobbyItem.LobbyItemMainForm;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.block.BlockUpdateEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.TextFormat;
import de.athoramine.core.main.Main;
import de.athoramine.core.util.Helper;
import de.athoramine.core.util.Vars;
import de.athoramine.core.util.manager.BuildManager;
import de.athoramine.core.util.manager.FarmingManager;
import de.athoramine.core.util.manager.ForagingManager;
import de.athoramine.core.util.manager.GeneralDestroyableBlocksManager;
import de.athoramine.core.util.manager.LobbyItemManager;
import de.athoramine.core.util.manager.MineManager;
import de.athoramine.core.util.manager.MineSetupManager;
import de.athoramine.core.util.manager.SecretsManager;
import de.athoramine.core.util.manager.WorldManager;

import java.util.concurrent.CompletableFuture;

public class PlayerDestroyBlocks implements Listener {

    private Main plugin;

    public PlayerDestroyBlocks(Main main) {
        this.plugin = main;
    }

    @EventHandler
    public void interact(PlayerInteractEvent event) {
        CompletableFuture.runAsync(() -> {
            Player player = event.getPlayer();
            if (!WorldManager.isInWorld(player, WorldManager.PLOT) && !BuildManager.getState(player)) {
                if (player.getInventory().getItemInHand().getId() == ItemID.BUCKET || player.getInventory().getItemInHand().getId() == ItemID.FLINT_AND_STEEL) {
                    event.setCancelled(true);
                    player.sendMessage(Vars.PREFIX + TextFormat.RED + "Du kannst dieses Item nur auf dem Plot Server benutzten!");
                }
                if (event.getBlock().getId() == BlockID.LOG || event.getBlock().getId() == BlockID.LOG2) {
                    event.setCancelled(true);
                }
                if (event.getBlock().getId() == BlockID.TRAPDOOR || event.getBlock().getId() == BlockID.SPRUCE_TRAPDOOR) {
                    event.setCancelled(true);
                }
                int id = player.getInventory().getItemInHand().getId();
                if (id >= 290 && id <= 294 || id == 747) { // hoes
                    event.setCancelled(true);
                }
                if (id == 269 || id == 273 || id == 256 || id == 284 || id == 277 || id == 744) { // shovels
                    event.setCancelled(true);
                }
            }
            if (player.getInventory().getItemInHand().getId() == 368) { // enderpearl
                event.setCancelled(true);
                player.sendMessage(Vars.PREFIX + TextFormat.RED + "Dieses Item ist deaktiviert!");
            }
            if (SecretsManager.setupAddList.containsKey(player)) {
                event.setCancelled(true);
                if (event.getBlock().getId() == BlockID.SIGN_POST || event.getBlock().getId() == BlockID.WALL_SIGN) {
                    int coinsReward = SecretsManager.setupAddList.get(player)[0];
                    int ruhmReward = SecretsManager.setupAddList.get(player)[1];
                    Block block = event.getBlock();
                    SecretsManager.addSecret(block.getLevel().getName(), (int) block.x, (int) block.y, (int) block.z, coinsReward, ruhmReward);
                    player.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du hast erfolgreich ein Secret hinzugefügt");
                    SecretsManager.setupAddList.remove(player);
                } else {
                    player.sendMessage(Vars.PREFIX + TextFormat.RED + "Du musst auf ein Schild klicken um es als Secret hinzuzufügen!");
                }
            } else if (SecretsManager.setupRemoveList.containsKey(player)) {
                event.setCancelled(true);
                if (SecretsManager.secrets.containsKey(event.getBlock())) {
                    Block block = event.getBlock();
                    SecretsManager.removeSecret(block.getLevel().getName(), (int) block.x, (int) block.y, (int) block.z);
                    player.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du hast erfolgreich ein Secret entfernt!");
                    SecretsManager.setupRemoveList.remove(player);
                } else {
                    player.sendMessage(Vars.PREFIX + TextFormat.RED + "Du musst auf ein Secret klicken um es entfernen!");
                }
            } else if (SecretsManager.secrets.containsKey(event.getBlock())) {
                if (!SecretsManager.hasPlayerFoundSecret(player, event.getBlock())) {
                    SecretsManager.playerFindSecret(player, event.getBlock());
                    int coinsReward = SecretsManager.secrets.get(event.getBlock())[0];
                    int ruhmReward = SecretsManager.secrets.get(event.getBlock())[1];
                    AthoraPlayer.setPurse(player, AthoraPlayer.getPurse(player) + coinsReward);
                    AthoraPlayer.setRuhm(player, AthoraPlayer.getRuhm(player) + ruhmReward);
                    player.getLevel().getServer().broadcastMessage(TextFormat.GREEN + "Der Spieler §l§9" + player.getName() + TextFormat.RESET + TextFormat.GREEN + " hat ein Secret gefunden!");
                    player.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du bist der " + TextFormat.BLUE + SecretsManager.getSecretDiscovers(event.getBlock()) + TextFormat.GREEN + " der dieses Secret entdeckt hat! Du bekommst " + TextFormat.GOLD + coinsReward + "$" + TextFormat.GREEN + " und " + TextFormat.GOLD + ruhmReward + TextFormat.GREEN + " Ruhm!");
//                player.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du hast ein neues Secret gefunden und bekommst " + TextFormat.GOLD + coinsReward + "$ " + TextFormat.GREEN + "und " + TextFormat.GOLD + ruhmReward + " Ruhm" + TextFormat.GREEN + "!");
//                player.sendMessage(Vars.PREFIX + TextFormat.GREEN + "Du bist der " + TextFormat.BLUE + SecretsManager.getSecretDiscovers(event.getBlock()) + TextFormat.GREEN + " der dieses Secret entdeckt hat!");
                    Helper.createFireworkParticle(player, DyeColor.CYAN);
                } else {
                    player.sendMessage(Vars.PREFIX + TextFormat.RED + "Du hast das Secret bereits gefunden!");
                }
            }
            if (LobbyItemManager.isLobbyItem(player.getInventory().getItemInHand()) && !LobbyItemManager.openedForms.containsKey(player.getUniqueId())) {
                LobbyItemManager.openedForms.put(player.getUniqueId(), player);
                new LobbyItemMainForm(player);
                event.setCancelled(true);
            }
        });
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent event) {
        CompletableFuture.runAsync(() -> {
            Player player = event.getPlayer();
            if (SecretsManager.setupAddList.containsKey(player) || SecretsManager.setupRemoveList.containsKey(player) || SecretsManager.secrets.containsKey(event.getBlock())) {
                event.setCancelled(true);
            } else if (!WorldManager.isInWorld(player, WorldManager.PLOT)) {
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
                        //TODO find the cause why its not working anymore
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


                } else if (!WorldManager.isInWorld(player, WorldManager.DEV) && !WorldManager.isInWorld(player, WorldManager.PLOT) && !BuildManager.getState(player)) {
                    event.setCancelled(true);
                }
            }
        });

    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent event) {
        if (!WorldManager.isInWorld(event.getPlayer(), WorldManager.DEV) && !BuildManager.getState(event.getPlayer()) && !WorldManager.isInWorld(event.getPlayer(), WorldManager.PLOT)) {
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
