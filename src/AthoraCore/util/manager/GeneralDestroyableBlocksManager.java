package AthoraCore.util.manager;

import AthoraCore.util.Helper;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralDestroyableBlocksManager {

    public static final int CACTUS_BLOCK = 81;
    public static final int PUMPKIN_BLOCK = 86;
    public static final int MELON_BLOCK = 103;
    public static final int SAND_BLOCK = 12;
    public static final int GRAVEL_BLOCK = 13;
    public static final int SUGAR_CANE_BLOCK = 83;
    public static final int COCO_BEANS_BLOCK = 127;
    public static final int RED_MUSHROOM_BLOCK = 40;
    public static final int BROWN_MUSHROOM_BLOCK = 39;
    public static final int CLAY_BLOCK = 82;

    public static int[] destroyableBlocks = {CACTUS_BLOCK, PUMPKIN_BLOCK, MELON_BLOCK, SAND_BLOCK, GRAVEL_BLOCK, SUGAR_CANE_BLOCK, COCO_BEANS_BLOCK, RED_MUSHROOM_BLOCK, BROWN_MUSHROOM_BLOCK, CLAY_BLOCK};
    public static boolean isSetupModeActive = false;

    public static Map<Block, Long> destroyedBlocks = new HashMap<>();
    public static Map<Player, String> setupList = new HashMap<>();

    private static Config config;

    public static void setConfig(Config givenConfig) {
        config = givenConfig;
        //setup default values
        for (int destroyableBlock : destroyableBlocks) {
            String blockName = Block.get(destroyableBlock).getPersistenceName();
            blockName = blockName.substring(blockName.indexOf(":") + 1);
            if (!config.exists("settings." + blockName)) {
                Server.getInstance().getLogger().error("Found no settings for block '" + blockName + "' in generalBlocksConfig.yml");
                config.set("settings." + blockName + ".ruhm", 1.0);
                config.set("settings." + blockName + ".spawnInterval", 10000);
                if (destroyableBlock == SUGAR_CANE_BLOCK) {
                    config.set("settings." + blockName + ".min_height", 2);
                    config.set("settings." + blockName + ".max_height", 6);
                }
                if (destroyableBlock == CACTUS_BLOCK) {
                    config.set("settings." + blockName + ".min_height", 1);
                    config.set("settings." + blockName + ".max_height", 5);
                }
            }
            if (!config.exists("storage." + blockName)) {
                Server.getInstance().getLogger().error("Found no storage for block '" + blockName + "' in generalBlocksConfig.yml");
                List<String> defaultValue = new ArrayList<>();
                config.set("storage." + blockName + "", defaultValue);
            }
        }
        config.save();
    }

    public static void renderSetupView(Level level) {
        for (int destroyableBlock : destroyableBlocks) {
            String blockName = Block.get(destroyableBlock).getPersistenceName();
            blockName = blockName.substring(blockName.indexOf(":") + 1);
            List<String> locations = config.getStringList("storage." + blockName);
            for (int i = 0; i < locations.size(); i++) {
                String locationString = locations.get(i);
                String[] locationCoords = locationString.split(" ");
                double x = Double.parseDouble(locationCoords[0]);
                double y = Double.parseDouble(locationCoords[1]);
                double z = Double.parseDouble(locationCoords[2]);
                Vector3 blockPosition = new Vector3(x, y, z);
                placeSetupBlock(level, level.getBlock(blockPosition));
            }
        }
        level.save();
    }

    public static void renderDefaultView(Level level) {
        for (int destroyableBlock : destroyableBlocks) {
            String blockName = Block.get(destroyableBlock).getPersistenceName();
            blockName = blockName.substring(blockName.indexOf(":") + 1);
            List<String> locations = config.getStringList("storage." + blockName);
            for (int i = 0; i < locations.size(); i++) {
                String locationString = locations.get(i);
                String[] locationCoords = locationString.split(" ");
                double x = Double.parseDouble(locationCoords[0]);
                double y = Double.parseDouble(locationCoords[1]);
                double z = Double.parseDouble(locationCoords[2]);
                Vector3 blockPosition = new Vector3(x, y, z);
                placeBlock(level, blockPosition, destroyableBlock);
            }
        }
        level.save();
    }

    public static void placeSetupBlock(Level level, Block targetBlock) {
        boolean working = true;
        int height = 1;
        switch (targetBlock.getId()) {
            case CACTUS_BLOCK:
                level.setBlockIdAt((int) targetBlock.x, (int) targetBlock.y, (int) targetBlock.z, BlockID.MAGENTA_GLAZED_TERRACOTTA);
                level.setBlockDataAt((int) targetBlock.x, (int) targetBlock.y, (int) targetBlock.z, 5);
                do {
                    Block block = level.getBlock((int) targetBlock.x, (int) (targetBlock.y + height), (int) targetBlock.z);
                    if (block.getId() == CACTUS_BLOCK) {
                        level.setBlockIdAt((int) targetBlock.x, (int) targetBlock.y + height, (int) targetBlock.z, BlockID.AIR);
                        level.setBlockDataAt((int) targetBlock.x, (int) targetBlock.y + height, (int) targetBlock.z, 0);
                        height++;
                    } else {
                        working = false;
                    }
                } while (working);
                break;
            case SUGAR_CANE_BLOCK:
                level.setBlockIdAt((int) targetBlock.x, (int) targetBlock.y, (int) targetBlock.z, BlockID.MAGENTA_GLAZED_TERRACOTTA);
                level.setBlockDataAt((int) targetBlock.x, (int) targetBlock.y, (int) targetBlock.z, 5);
                do {
                    Block block = level.getBlock((int) targetBlock.x, (int) (targetBlock.y + height), (int) targetBlock.z);
                    if (block.getId() == SUGAR_CANE_BLOCK) {
                        level.setBlockIdAt((int) targetBlock.x, (int) targetBlock.y + height, (int) targetBlock.z, BlockID.AIR);
                        level.setBlockDataAt((int) targetBlock.x, (int) targetBlock.y + height, (int) targetBlock.z, 0);
                        height++;
                    } else {
                        working = false;
                    }
                } while (working);
                break;
            default:
                level.setBlockIdAt((int) targetBlock.x, (int) targetBlock.y, (int) targetBlock.z, BlockID.MAGENTA_GLAZED_TERRACOTTA);
                level.setBlockDataAt((int) targetBlock.x, (int) targetBlock.y, (int) targetBlock.z, 5);
        }
    }

    public static void placeBlock(Level level, Vector3 destination, int targetBlockID) {
        int randomHeight;
        switch (targetBlockID) {
            case CACTUS_BLOCK:
                level.setBlockIdAt((int) destination.x, (int) destination.y, (int) destination.z, CACTUS_BLOCK);
                level.setBlockDataAt((int) destination.x, (int) destination.y, (int) destination.z, 1);
                randomHeight = Helper.getRandomIntBetween(config.getInt("settings." + getBlockName(CACTUS_BLOCK) + ".min_height"), config.getInt("settings." + getBlockName(CACTUS_BLOCK) + ".max_height"));
                for (int i = 1; i < randomHeight + 1; i++) {
                    level.setBlockIdAt((int) destination.x, (int) destination.y + i, (int) destination.z, CACTUS_BLOCK);
                    level.setBlockDataAt((int) destination.x, (int) destination.y + i, (int) destination.z, 0);
                }
                break;
            case PUMPKIN_BLOCK:
                level.setBlockIdAt((int) destination.x, (int) destination.y, (int) destination.z, PUMPKIN_BLOCK);
                level.setBlockDataAt((int) destination.x, (int) destination.y, (int) destination.z, 1);
                break;
            case MELON_BLOCK:
                level.setBlockIdAt((int) destination.x, (int) destination.y, (int) destination.z, MELON_BLOCK);
                level.setBlockDataAt((int) destination.x, (int) destination.y, (int) destination.z, 0);
                break;
            case SAND_BLOCK:
                level.setBlockIdAt((int) destination.x, (int) destination.y, (int) destination.z, SAND_BLOCK);
                level.setBlockDataAt((int) destination.x, (int) destination.y, (int) destination.z, 0);
                break;
            case GRAVEL_BLOCK:
                level.setBlockIdAt((int) destination.x, (int) destination.y, (int) destination.z, GRAVEL_BLOCK);
                level.setBlockDataAt((int) destination.x, (int) destination.y, (int) destination.z, 0);
                break;
            case SUGAR_CANE_BLOCK:
                level.setBlockIdAt((int) destination.x, (int) destination.y, (int) destination.z, SUGAR_CANE_BLOCK);
                level.setBlockDataAt((int) destination.x, (int) destination.y, (int) destination.z, 0);
                randomHeight = Helper.getRandomIntBetween(config.getInt("settings." + getBlockName(SUGAR_CANE_BLOCK) + ".min_height"), config.getInt("settings." + getBlockName(SUGAR_CANE_BLOCK) + ".max_height"));
                for (int i = 1; i < randomHeight + 1; i++) {
                    level.setBlockIdAt((int) destination.x, (int) destination.y + i, (int) destination.z, SUGAR_CANE_BLOCK);
                    level.setBlockDataAt((int) destination.x, (int) destination.y + i, (int) destination.z, 0);
                }
                break;
            case COCO_BEANS_BLOCK:
                level.setBlockIdAt((int) destination.x, (int) destination.y, (int) destination.z, COCO_BEANS_BLOCK);
                level.setBlockDataAt((int) destination.x, (int) destination.y, (int) destination.z, 9);
                break;
            case RED_MUSHROOM_BLOCK:
                level.setBlockIdAt((int) destination.x, (int) destination.y, (int) destination.z, RED_MUSHROOM_BLOCK);
                level.setBlockDataAt((int) destination.x, (int) destination.y, (int) destination.z, 0);
                break;
            case BROWN_MUSHROOM_BLOCK:
                level.setBlockIdAt((int) destination.x, (int) destination.y, (int) destination.z, BROWN_MUSHROOM_BLOCK);
                level.setBlockDataAt((int) destination.x, (int) destination.y, (int) destination.z, 0);
                break;
            case CLAY_BLOCK:
                level.setBlockIdAt((int) destination.x, (int) destination.y, (int) destination.z, CLAY_BLOCK);
                level.setBlockDataAt((int) destination.x, (int) destination.y, (int) destination.z, 0);
                break;
            default:
                Server.getInstance().getLogger().error("GeneralDestroyableBlocks tryed to place a block with id: " + targetBlockID + " but the id is not registered!");
        }
    }

    public static boolean toggleSetupBlock(Player player, Block block) {
        List<String> locations = config.getStringList("storage." + setupList.get(player));
        String value = block.x + " " + block.y + " " + block.z;
        boolean created = false;
        removeLocationFromOtherStorageSections(setupList.get(player), block);
        if (getLocationIndexInStorage(block.getLocation()) == -1) {
            locations.add(value);
            created = true;
        } else {
            int index = -1;
            for (int i = 0; i < locations.size(); i++) {
                if (locations.get(i).equalsIgnoreCase(value)) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                block.getLevel().setBlockIdAt((int) block.x, (int) block.y, (int) block.z, BlockID.DIRT);
                block.getLevel().setBlockDataAt((int) block.x, (int) block.y, (int) block.z, 0);
                locations.remove(index);
            }
        }
        config.set("storage." + setupList.get(player), locations);
        config.save();
        return created;
    }

    public static void removeLocationFromOtherStorageSections(String blockSection, Block block) {
        String value = block.x + " " + block.y + " " + block.z;
        for (int destroyableBlock : destroyableBlocks) {
            String blockName = Block.get(destroyableBlock).getPersistenceName();
            blockName = blockName.substring(blockName.indexOf(":") + 1);
            if (!blockName.equals(blockSection)) {
                int index = -1;
                List<String> locations = config.getStringList("storage." + blockName);
                for (int i = 0; i < locations.size(); i++) {
                    String locationString = locations.get(i);
                    if (locationString.equalsIgnoreCase(value)) {
                        index = i;
                        break;
                    }
                }
                if (index != -1) {
                    locations.remove(index);
                    config.set("storage." + blockName, locations);
                    config.save();
                    break;
                }
            }
        }
    }

    public static boolean setPlayerToSetupList(Player player, String blockName) {
        if (!isBlockNameInDestroyableBlocks(blockName)) return false;
        if (setupList.containsKey(player)) {
            setupList.replace(player, blockName);
        } else {
            setupList.put(player, blockName);
            if (!isSetupModeActive) {
                isSetupModeActive = true;
                destroyedBlocks.clear();
                renderSetupView(player.getLevel());
            }
        }
        return true;
    }

    public static void removePlayerFromSetupList(Player player) {
        setupList.remove(player);
        if (isSetupModeActive && setupList.size() == 0) {
            isSetupModeActive = false;
            renderDefaultView(player.getLevel());
        }
    }

    public static int getLocationIndexInStorage(Location trackedLocation) {
        String trackedLocationString = trackedLocation.x + " " + trackedLocation.y + " " + trackedLocation.z;
        for (int destroyableBlock : destroyableBlocks) {
            String blockName = Block.get(destroyableBlock).getPersistenceName();
            blockName = blockName.substring(blockName.indexOf(":") + 1);
            for (int i = 0; i < config.getStringList("storage." + blockName).size(); i++) {
                if (config.getStringList("storage." + blockName).get(i).equalsIgnoreCase(trackedLocationString))
                    return i;
            }
        }
        return -1;
    }

    public static boolean isBlockNameInDestroyableBlocks(String name) {
        for (int blockID : destroyableBlocks) {
            String blockName = Block.get(blockID).getPersistenceName();
            blockName = blockName.substring(blockName.indexOf(":") + 1);
            if (blockName.equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public static String getBlockName(int id) {
        String blockName = Block.get(id).getPersistenceName();
        blockName = blockName.substring(blockName.indexOf(":") + 1);
        return blockName;
    }


}
