package de.athoramine.core.util.manager;

import de.athoramine.core.api.AthoraPlayer;
import de.athoramine.core.util.Helper;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import net.llamagames.AreaProtection.AreaProtection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FarmingManager {

    public static final String PLANT_BEETROOT = "beetroot";
    public static final String PLANT_CARROT = "carrot";
    public static final String PLANT_WHEAT = "wheat";
    public static final String PLANT_POTATO = "potato";

    public static final String[] areas = {"feld_1", "feld_2", "feld_3", "feld_4"};
    public static final String[] plants = {PLANT_BEETROOT, PLANT_CARROT, PLANT_WHEAT, PLANT_POTATO};
    public static Map<Player, String> setupList = new HashMap<>();
    public static Map<Block, Long> destroyedBlocks = new HashMap<>();
    public static boolean isFarmUnderSetup = false;

    public static Config config;

    public static void respawnDestroyedBlocks() {
        if (destroyedBlocks.size() > 0) {
            long currentTime = System.currentTimeMillis();
            long delayForRespawn = config.getInt("delay_per_growth_step");
            Block[] blocksToRemove = new Block[]{};
            Block[] blocksToUpdate = new Block[]{};
            for (Map.Entry<Block, Long> entry : destroyedBlocks.entrySet()) {
                if ((currentTime - entry.getValue()) >= delayForRespawn) {
                    Vector3 blockLocation = entry.getKey().getLocation();
                    Block targetFarmBlock = entry.getKey().getLevel().getBlock((int) blockLocation.x, (int) (blockLocation.y + 1), (int) blockLocation.z);
                    if (targetFarmBlock.getDamage() >= 6) {
                        targetFarmBlock.getLevel().setBlockDataAt((int) targetFarmBlock.x, (int) targetFarmBlock.y, (int) targetFarmBlock.z, 7);
                        blocksToRemove = Helper.append(blocksToRemove, entry.getKey());
                    } else {
                        targetFarmBlock.getLevel().setBlockDataAt((int) targetFarmBlock.x, (int) targetFarmBlock.y, (int) targetFarmBlock.z, targetFarmBlock.getDamage() + 1);
                        blocksToUpdate = Helper.append(blocksToUpdate, entry.getKey());
                    }
                }
            }
            for (Block blockToUpdate : blocksToUpdate) {
                destroyedBlocks.replace(blockToUpdate, System.currentTimeMillis());
            }
            for (Block blockToRemove : blocksToRemove) {
                destroyedBlocks.remove(blockToRemove);
            }
        }
    }

    public static void destroyBlock(Player player, Block block) {
        if (!destroyedBlocks.containsKey(block)) {
            destroyedBlocks.put(block, System.currentTimeMillis());
            block.getLevel().setBlockDataAt((int) block.x, (int) (block.y + 1), (int) block.z, 0);
            double ruhmReward = getRuhmPerPlant(getPlant(block.getLevel().getBlock((int) block.x, (int) (block.y + 1), (int) block.z)));
            AthoraPlayer.setRuhm(player, AthoraPlayer.getRuhm(player) + ruhmReward);
            player.sendActionBar(TextFormat.GOLD + "+ " + ruhmReward + " Ruhm");
            Helper.playSound("random.orb", player, 0.3f, 0.8f);
        }
    }

    public static double getRuhmPerPlant(String plant) {
        return config.getDouble("ruhm_per_plant." + plant);
    }

    public static void renderSetupView(Level level) {
        for (String plant : plants) {
            List<String> locations = config.getStringList("fields." + plant);
            for (int i = 0; i < locations.size(); i++) {
                String locationString = locations.get(i);
                String[] locationCoords = locationString.split(" ");
                double x = Double.parseDouble(locationCoords[0]);
                double y = Double.parseDouble(locationCoords[1]);
                double z = Double.parseDouble(locationCoords[2]);
                Vector3 blockPosition = new Vector3(x, y, z);
                level.setBlockIdAt((int) blockPosition.x, (int) blockPosition.y, (int) blockPosition.z, BlockID.CONCRETE);
                level.setBlockDataAt((int) blockPosition.x, (int) blockPosition.y, (int) blockPosition.z, getSetupBlockMetaForPlant(plant));
                level.setBlockIdAt((int) blockPosition.x, (int) blockPosition.y + 1, (int) blockPosition.z, BlockID.AIR);
                level.setBlockDataAt((int) blockPosition.x, (int) blockPosition.y + 1, (int) blockPosition.z, 0);
            }
        }
    }

    public static void renderDefaultView(Level level) {
        resetFarms(level);
    }

    public static void resetFarms(Level level) {
        for (String plant : plants) {
            List<String> locations = config.getStringList("fields." + plant);
            for (int i = 0; i < locations.size(); i++) {
                String locationString = locations.get(i);
                String[] locationCoords = locationString.split(" ");
                double x = Double.parseDouble(locationCoords[0]);
                double y = Double.parseDouble(locationCoords[1]);
                double z = Double.parseDouble(locationCoords[2]);
                Vector3 blockPosition = new Vector3(x, y, z);
                level.setBlockIdAt((int) blockPosition.x, (int) blockPosition.y, (int) blockPosition.z, BlockID.FARMLAND);
                level.setBlockDataAt((int) blockPosition.x, (int) blockPosition.y, (int) blockPosition.z, 7);
                level.setBlockIdAt((int) blockPosition.x, (int) blockPosition.y + 1, (int) blockPosition.z, getBlockIDForPlant(plant));
                level.setBlockDataAt((int) blockPosition.x, (int) blockPosition.y + 1, (int) blockPosition.z, 7);
            }
        }
        level.save();
    }

    public static int getSetupBlockMetaForPlant(String plant) {
        int meta = 0;
        switch (plant) {
            case PLANT_BEETROOT:
                meta = 14;
                break;
            case PLANT_CARROT:
                meta = 1;
                break;
            case PLANT_POTATO:
                meta = 4;
                break;
            case PLANT_WHEAT:
                meta = 5;
                break;
        }
        return meta;
    }

    public static int getBlockIDForPlant(String plant) {
        int id = BlockID.AIR;
        switch (plant) {
            case PLANT_BEETROOT:
                id = BlockID.BEETROOT_BLOCK;
                break;
            case PLANT_CARROT:
                id = BlockID.CARROT_BLOCK;
                break;
            case PLANT_POTATO:
                id = BlockID.POTATO_BLOCK;
                break;
            case PLANT_WHEAT:
                id = BlockID.WHEAT_BLOCK;
                break;
        }
        return id;
    }

    public static boolean togglePlantBlock(Player player, Block block) {
        List<String> locations = config.getStringList("fields." + setupList.get(player));
        String value = block.x + " " + block.y + " " + block.z;
        boolean created = false;
        removeCoordsFromOtherFields(setupList.get(player), block);
        if (!fieldsContainsField(block, setupList.get(player))) {
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
        config.set("fields." + setupList.get(player), locations);
        config.save();
        return created;
    }

    public static void removeCoordsFromOtherFields(String currentField, Block block) {
        String value = block.x + " " + block.y + " " + block.z;
        for (String plant : plants) {
            if (!plant.equals(currentField)) {
                int index = -1;
                List<String> locations = config.getStringList("fields." + plant);
                for (int i = 0; i < locations.size(); i++) {
                    String locationString = locations.get(i);
                    if (locationString.equalsIgnoreCase(value)) {
                        index = i;
                        break;
                    }
                }
                if (index != -1) {
                    locations.remove(index);
                    config.set("fields." + plant, locations);
                    config.save();
                    break;
                }
            }
        }
    }

    public static boolean fieldsContainsField(Block block, String plant) {
        List<String> locations = config.getStringList("fields." + plant);
        for (String locationString : locations) {
            String[] locationCoords = locationString.split(" ");
            double x = Double.parseDouble(locationCoords[0]);
            double y = Double.parseDouble(locationCoords[1]);
            double z = Double.parseDouble(locationCoords[2]);
            if (x == block.x && y == block.y && z == block.z) {
                return true;
            }
        }
        return false;
    }

    public static boolean setPlayerToSetupList(Player player, String plant) {
        if (!plantExist(plant)) return false;
        if (setupList.containsKey(player)) {
            setupList.replace(player, plant);
        } else {
            setupList.put(player, plant);
            if (!isFarmUnderSetup) {
                isFarmUnderSetup = true;
                destroyedBlocks.clear();
                renderSetupView(player.getServer().getDefaultLevel());
            }
        }
        return true;
    }

    public static void removePlayerFromSetupList(Player player) {
        setupList.remove(player);
        if (isFarmUnderSetup && setupList.size() == 0) {
            isFarmUnderSetup = false;
            renderDefaultView(player.getServer().getDefaultLevel());
        }
    }

    public static boolean plantExist(String plant) {
        for (String plantsPlant : plants) if (plant.equalsIgnoreCase(plantsPlant)) return true;
        return false;
    }

    public static boolean isBlockBreakable(Block block) {
        if (!isBlockInArea(block)) return false;
        return false;
    }

    public static String getPlant(Block block) {
        switch (block.getId()) {
            case BlockID.BEETROOT_BLOCK:
                return PLANT_BEETROOT;
            case BlockID.CARROT_BLOCK:
                return PLANT_CARROT;
            case BlockID.POTATO_BLOCK:
                return PLANT_POTATO;
            case BlockID.WHEAT_BLOCK:
                return PLANT_WHEAT;
        }
        return null;
    }

    public static boolean isBlockInArea(Block block) {
        if (AreaProtection.getInstance().getAreaByPos(block.getLocation()) == null) return false;
        String blockArea = AreaProtection.getInstance().getAreaByPos(block.getLocation()).getName();
        for (String area : areas) if (blockArea.equalsIgnoreCase(area)) return true;
        return false;
    }

}
