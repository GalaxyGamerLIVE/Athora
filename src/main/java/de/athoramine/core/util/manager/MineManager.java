package de.athoramine.core.util.manager;

import cn.nukkit.Server;
import de.athoramine.core.api.AthoraPlayer;
import de.athoramine.core.main.Main;
import de.athoramine.core.util.Helper;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import net.llamagames.AreaProtection.AreaProtection;

import java.util.*;

public class MineManager {

    private static final Map<Block, Long> destroyedBlocks = new HashMap<>();
    public static final String[] mines = {"mine_1", "mine_2", "mine_3", "mine_4", "mine_5", "mine_6"};
    public static Config mineConfig;

    public static void setMineConfig(Config mineConfig) {
        MineManager.mineConfig = mineConfig;
    }

    public static Position getMineSpawnpoint(int mine, String direction) {
        String[] locationCoords = mineConfig.getString("mine_spawnpoints.mine_" + mine + "." + direction).split(" ");
        double x = Double.parseDouble(locationCoords[0]);
        double y = Double.parseDouble(locationCoords[1]);
        double z = Double.parseDouble(locationCoords[2]);
        return new Position(x, y, z, Server.getInstance().getLevelByName("lobby"));
    }

    public static double getMineSpawnYaw(int mine, String direction) {
        return mineConfig.getDouble("mine_spawnpoints.mine_" + mine + "." + direction + "Yaw");

    }

    public static double getMineSpawnPitch(int mine, String direction) {
        return mineConfig.getDouble("mine_spawnpoints.mine_" + mine + "." + direction + "Pitch");
    }

    public static int getMineRequirements(int mine) {
        return mineConfig.getInt("mine_requirements.mine_" + mine);
    }

    public static void generateOres(Level level) {
        for (String mine : mines) {
            if (mineConfig.getStringList("ore_spawn_locations." + mine).size() == 0) {
                level.getServer().getLogger().error("Found no ore spawns for mine: " + mine);
            } else {
                int coalOres = mineConfig.getInt("ore_spawn_rates." + mine + ".coal");
                int ironOres = mineConfig.getInt("ore_spawn_rates." + mine + ".iron");
                int goldOres = mineConfig.getInt("ore_spawn_rates." + mine + ".gold");
                int redstoneOres = mineConfig.getInt("ore_spawn_rates." + mine + ".redstone");
                int emeraldOres = mineConfig.getInt("ore_spawn_rates." + mine + ".emerald");
                int lapisOres = mineConfig.getInt("ore_spawn_rates." + mine + ".lapis");
                int diamondOres = mineConfig.getInt("ore_spawn_rates." + mine + ".diamond");
                int obsidianOres = mineConfig.getInt("ore_spawn_rates." + mine + ".obsidian");
                int quartzOres = mineConfig.getInt("ore_spawn_rates." + mine + ".quartz");
                int netheriteOres = mineConfig.getInt("ore_spawn_rates." + mine + ".netherite");
                generateOresFunction(level, mine, coalOres, ironOres, goldOres, redstoneOres, emeraldOres, lapisOres, diamondOres, obsidianOres, quartzOres, netheriteOres);
            }
        }

        level.save();
    }

    private static void generateOresFunction(Level level, String mine, int coal, int iron, int gold, int redstone, int emerald, int lapis, int diamond, int obsidian, int quartz, int netherite) {
        if (coal > 0) {
            for (int i = 0; i < coal; i++) {
                Block block = getNewOreSpawn(level, mine);
                level.setBlockIdAt((int) block.x, (int) block.y, (int) block.z, BlockID.COAL_ORE);
            }
        }
        if (iron > 0) {
            for (int i = 0; i < iron; i++) {
                Block block = getNewOreSpawn(level, mine);
                level.setBlockIdAt((int) block.x, (int) block.y, (int) block.z, BlockID.IRON_ORE);
            }
        }
        if (gold > 0) {
            for (int i = 0; i < gold; i++) {
                Block block = getNewOreSpawn(level, mine);
                level.setBlockIdAt((int) block.x, (int) block.y, (int) block.z, BlockID.GOLD_ORE);
            }
        }
        if (redstone > 0) {
            for (int i = 0; i < redstone; i++) {
                Block block = getNewOreSpawn(level, mine);
                level.setBlockIdAt((int) block.x, (int) block.y, (int) block.z, BlockID.REDSTONE_ORE);
            }
        }
        if (emerald > 0) {
            for (int i = 0; i < emerald; i++) {
                Block block = getNewOreSpawn(level, mine);
                level.setBlockIdAt((int) block.x, (int) block.y, (int) block.z, BlockID.EMERALD_ORE);
            }
        }
        if (lapis > 0) {
            for (int i = 0; i < lapis; i++) {
                Block block = getNewOreSpawn(level, mine);
                level.setBlockIdAt((int) block.x, (int) block.y, (int) block.z, BlockID.LAPIS_ORE);
            }
        }
        if (diamond > 0) {
            for (int i = 0; i < diamond; i++) {
                Block block = getNewOreSpawn(level, mine);
                level.setBlockIdAt((int) block.x, (int) block.y, (int) block.z, BlockID.DIAMOND_ORE);
            }
        }
        if (obsidian > 0) {
            for (int i = 0; i < obsidian; i++) {
                Block block = getNewOreSpawn(level, mine);
                level.setBlockIdAt((int) block.x, (int) block.y, (int) block.z, BlockID.OBSIDIAN);
            }
        }
        if (quartz > 0) {
            for (int i = 0; i < quartz; i++) {
                Block block = getNewOreSpawn(level, mine);
                level.setBlockIdAt((int) block.x, (int) block.y, (int) block.z, BlockID.QUARTZ_ORE);
            }
        }
        if (netherite > 0) {
            for (int i = 0; i < netherite; i++) {
                Block block = getNewOreSpawn(level, mine);
                level.setBlockIdAt((int) block.x, (int) block.y, (int) block.z, BlockID.ANCIENT_DERBRIS);
            }
        }
    }

    public static void resetMine(Level level) {
        for (String mine : mines) {
            List<String> locations = mineConfig.getStringList("ore_spawn_locations." + mine);
            for (int i = 0; i < locations.size(); i++) {
                String locationString = locations.get(i);
                String[] locationCoords = locationString.split(" ");
                double x = Double.parseDouble(locationCoords[0]);
                double y = Double.parseDouble(locationCoords[1]);
                double z = Double.parseDouble(locationCoords[2]);
                Vector3 blockPosition = new Vector3(x, y, z);
                if (level.getBlock(blockPosition).getId() != Block.STONE) {
                    level.setBlockIdAt((int) blockPosition.x, (int) blockPosition.y, (int) blockPosition.z, BlockID.STONE);
                    level.setBlockDataAt((int) blockPosition.x, (int) blockPosition.y, (int) blockPosition.z, 0);
                }
            }
        }
        level.save();
    }

    public static void destroyBlock(Player player, Block block) {
        Vector3 blockLocation = block.getLocation();
        if (block.getId() == BlockID.COBBLESTONE) {
            block.getLevel().setBlock(blockLocation, Block.get(Block.BEDROCK));
        } else if (block.getId() == BlockID.IRON_ORE || block.getId() == BlockID.COAL_ORE || block.getId() == BlockID.GOLD_ORE ||
                block.getId() == BlockID.REDSTONE_ORE || block.getId() == BlockID.GLOWING_REDSTONE_ORE || block.getId() == BlockID.EMERALD_ORE || block.getId() == BlockID.LAPIS_ORE ||
                block.getId() == BlockID.DIAMOND_ORE || block.getId() == BlockID.OBSIDIAN || block.getId() == BlockID.QUARTZ_ORE || block.getId() == BlockID.ANCIENT_DERBRIS) {
            Block newOreBlock = getNewOreSpawn(block.getLevel(), getMineByAreaName(AreaProtection.getInstance().getAreaByPos(block.getLocation()).getName()));
            Vector3 newOreLocation = newOreBlock.getLocation();
            if (block.getId() == BlockID.GLOWING_REDSTONE_ORE) {
                block.getLevel().setBlockIdAt((int) newOreLocation.x, (int) newOreLocation.y, (int) newOreLocation.z, BlockID.REDSTONE_ORE);
            } else {
                block.getLevel().setBlockIdAt((int) newOreLocation.x, (int) newOreLocation.y, (int) newOreLocation.z, block.getId());
            }
            block.getLevel().setBlock(blockLocation, Block.get(Block.COBBLESTONE));
//            block.getLevel().getServer().broadcastMessage("Placed new ore at: " + newOreLocation.x + " " + newOreLocation.y + " " + newOreLocation.z);
        } else {
            block.getLevel().setBlock(blockLocation, Block.get(Block.COBBLESTONE));
        }
        block.getLevel().save();

        double ruhmReward = getRuhmPerOre(block);
        if (ruhmReward > 0.00) {
            double currentRuhm = AthoraPlayer.getRuhm(player);
            AthoraPlayer.setRuhm(player, currentRuhm + ruhmReward);
            player.sendActionBar(TextFormat.GOLD + "+ " + ruhmReward + " Ruhm");
            Helper.playSound("random.orb", player, 0.3f, 0.8f);
        }

        Block newBlock = block.getLevel().getBlock((int) block.x, (int) block.y, (int) block.z);
        if (destroyedBlocks.containsKey(block)) {
            destroyedBlocks.replace(newBlock, System.currentTimeMillis());
        } else {
            destroyedBlocks.put(newBlock, System.currentTimeMillis());
        }
    }

    public static double getRuhmPerOre(Block block) {
        double ruhmReward = 0;
        switch (block.getId()) {
            case BlockID.STONE:
            case BlockID.COBBLESTONE:
                ruhmReward = mineConfig.getDouble("ruhm_per_ore.stone");
                break;
            case BlockID.COAL_ORE:
                ruhmReward = mineConfig.getDouble("ruhm_per_ore.coal");
                break;
            case BlockID.IRON_ORE:
                ruhmReward = mineConfig.getDouble("ruhm_per_ore.iron");
                break;
            case BlockID.GOLD_ORE:
                ruhmReward = mineConfig.getDouble("ruhm_per_ore.gold");
                break;
            case BlockID.GLOWING_REDSTONE_ORE:
            case BlockID.REDSTONE_ORE:
                ruhmReward = mineConfig.getDouble("ruhm_per_ore.redstone");
                break;
            case BlockID.EMERALD_ORE:
                ruhmReward = mineConfig.getDouble("ruhm_per_ore.emerald");
                break;
            case BlockID.LAPIS_ORE:
                ruhmReward = mineConfig.getDouble("ruhm_per_ore.lapis");
                break;
            case BlockID.DIAMOND_ORE:
                ruhmReward = mineConfig.getDouble("ruhm_per_ore.diamond");
                break;
            case BlockID.OBSIDIAN:
                ruhmReward = mineConfig.getDouble("ruhm_per_ore.obsidian");
                break;
            case BlockID.QUARTZ_ORE:
                ruhmReward = mineConfig.getDouble("ruhm_per_ore.quartz");
                break;
            case BlockID.ANCIENT_DERBRIS:
                ruhmReward = mineConfig.getDouble("ruhm_per_ore.netherite");
                break;
        }
        return ruhmReward;
    }

    public static Block getNewOreSpawn(Level level, String mine) {
        List<String> locations = mineConfig.getStringList("ore_spawn_locations." + mine);
        Block block = null;
        boolean foundNewLocation = false;
        while (!foundNewLocation) {
            if (locations.size() == 0) {
                foundNewLocation = true;
            } else {
                String locationString = locations.get(new Random().nextInt(locations.size()));
                String[] locationCoords = locationString.split(" ");
                double x = Double.parseDouble(locationCoords[0]);
                double y = Double.parseDouble(locationCoords[1]);
                double z = Double.parseDouble(locationCoords[2]);
                Vector3 blockPosition = new Vector3(x, y, z);
                if (level.getBlock(blockPosition).getId() == Block.STONE) {
                    block = level.getBlock(blockPosition);
                    foundNewLocation = true;
                }
            }
        }
        return block;
    }

    public static void respawnDestroyedBlocks() {
        long currentTime = System.currentTimeMillis();
        long delayForRespawn = 15000; // 15 Seconds
        Block[] blocksToRemove = new Block[]{};
        for (Map.Entry<Block, Long> entry : destroyedBlocks.entrySet()) {
            if ((currentTime - entry.getValue()) >= delayForRespawn) {
                Vector3 blockLocation = entry.getKey().getLocation();
                entry.getKey().getLevel().setBlock(blockLocation, Block.get(Block.STONE));
                blocksToRemove = Helper.append(blocksToRemove, entry.getKey());
            }
        }
        for (Block blockToRemove : blocksToRemove) {
            destroyedBlocks.remove(blockToRemove);
        }
    }

    public static String getMineByAreaName(String area) {
        switch (area) {
            case "mine1":
                return "mine_1";
            case "mine2":
                return "mine_2";
            case "mine3":
                return "mine_3";
            case "mine4":
                return "mine_4";
            case "mine5":
                return "mine_5";
            case "mine6":
                return "mine_6";
            default:
                Main.getInstance().getLogger().error("Found no mine by area name!");
                return null;
        }
    }

    public static boolean isBlockBreakable(Block block) {
        Position blockPosition = new Position(block.getLocation().x, block.getLocation().y, block.getLocation().z, block.getLocation().level);
        return AreaProtection.getInstance().getAreaByPos(blockPosition) != null && isPositionInMine(blockPosition) && isBlockIDAllowed(block.getId());
//        return AreaProtection.getInstance().getAreaByPos(blockPosition) != null && AreaProtection.getInstance().getAreaByPos(blockPosition).getName().equalsIgnoreCase("mine1") && isBlockIDAllowed(block.getId());
    }

    public static boolean isPositionInMine(Position position) {
        String[] mines = {"mine1", "mine2", "mine3", "mine4", "mine5", "mine6"};
        return Arrays.asList(mines).contains(AreaProtection.getInstance().getAreaByPos(position).getName());
    }

    public static boolean isBlockIDAllowed(int blockID) {
        return blockID == BlockID.STONE || blockID == BlockID.COBBLESTONE || blockID == BlockID.COAL_ORE || blockID == BlockID.IRON_ORE || blockID == BlockID.GOLD_ORE || blockID == BlockID.REDSTONE_ORE || blockID == BlockID.GLOWING_REDSTONE_ORE || blockID == BlockID.EMERALD_ORE || blockID == BlockID.LAPIS_ORE || blockID == BlockID.DIAMOND_ORE || blockID == BlockID.OBSIDIAN || blockID == BlockID.QUARTZ_ORE || blockID == BlockID.ANCIENT_DERBRIS;
    }

}




