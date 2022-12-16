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
import net.llamagames.AreaProtection.utils.Area;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ForagingManager {

    public static String[] foragingAreas = {"Wald1-1", "Wald1-2", "wald2", "wald3-1", "wald3-2", "wald3-3", "wald3-4", "wald3-5", "wald3-6", "wald3-7", "wald4-1", "wald4-2", "wald4-3", "wald4-4", "wald5"};
    public static Map<Block[], Long> destroyedTrees = new HashMap<>();
    public static Config foragingConfig;

    public static void setForagingConfig(Config foragingConfig) {
        ForagingManager.foragingConfig = foragingConfig;
    }

    public static void resetForest(Level level) {
        Block[][] treesToRemove = new Block[][]{};
        for (Map.Entry<Block[], Long> entry : destroyedTrees.entrySet()) {
            for (Block treeBlock : entry.getKey()) {
                Vector3 blockLocation = treeBlock.getLocation();
                treeBlock.getLevel().setBlock(blockLocation, Block.get(treeBlock.getId()));
                treeBlock.getLevel().setBlockDataAt((int) blockLocation.x, (int) blockLocation.y, (int) blockLocation.z, treeBlock.getDamage());
            }
            treesToRemove = Helper.append(treesToRemove, entry.getKey());
        }
        level.save();
        for (Block[] blockToRemove : treesToRemove) {
            destroyedTrees.remove(blockToRemove);
        }
    }

    public static void respawnDestroyedTrees() {
        long currentTime = System.currentTimeMillis();
        long delayForRespawn = 120000; // 120 Seconds
        Block[][] treesToRemove = new Block[][]{};

        for (Map.Entry<Block[], Long> entry : destroyedTrees.entrySet()) {
            if ((currentTime - entry.getValue()) >= delayForRespawn) {
                for (Block treeBlock : entry.getKey()) {
                    Vector3 blockLocation = treeBlock.getLocation();
                    treeBlock.getLevel().setBlock(blockLocation, Block.get(treeBlock.getId()));
                    treeBlock.getLevel().setBlockDataAt((int) blockLocation.x, (int) blockLocation.y, (int) blockLocation.z, treeBlock.getDamage());
                }
                treesToRemove = Helper.append(treesToRemove, entry.getKey());
            }
        }
        for (Block[] blockToRemove : treesToRemove) {
            destroyedTrees.remove(blockToRemove);
        }
    }

    public static void destroyBlock(Player player, Block block) {
        Block[] destroyedTree = searchDestroyedTreesForBlock(block);
        if (destroyedTree != null) {
            destroyedTrees.replace(destroyedTree, System.currentTimeMillis());
        } else {
            Block[] tree = getTreeStructure(block);
            destroyedTrees.put(tree, System.currentTimeMillis());
        }
        if (block.getId() == BlockID.LOG || block.getId() == BlockID.LOG2) {
            double ruhmReward = foragingConfig.getDouble("ruhm_per_wood");
            double currentRuhm = AthoraPlayer.getRuhm(player);
            AthoraPlayer.setRuhm(player, currentRuhm + ruhmReward);
            player.sendActionBar(TextFormat.GOLD + "+ " + ruhmReward + " Ruhm");
            Helper.playSound("random.orb", player, 0.3f, 0.8f);
        }
    }

    public static boolean isBlockInDestroyedTrees(Block block) {
        return searchDestroyedTreesForBlock(block) != null;
    }

    public static Block[] searchDestroyedTreesForBlock(Block block) {
        for (Block[] tree : destroyedTrees.keySet()) {
            for (Block treeBlock : tree) {
                if (treeBlock.x == block.x && treeBlock.y == block.y && treeBlock.z == block.z) {
                    return tree;
                }
            }
        }
        return null;
    }

    public static Block[] getTreeStructure(Block block) {
        Block[] blocksToCheck = new Block[]{block};
        Block[] treeStructure = new Block[]{};
        boolean searchBlocks = true;
        do {
            Block[] newBlocksToCheck = new Block[]{};
            for (Block blockToCheck : blocksToCheck) {
                if (isBlockBreakable(blockToCheck)) {
                    boolean duplicate = false;
                    for (Block treeStructureBlock : treeStructure) {
                        if (blockToCheck.x == treeStructureBlock.x &&
                                blockToCheck.y == treeStructureBlock.y &&
                                blockToCheck.z == treeStructureBlock.z) {
                            duplicate = true;
                            break;
                        }
                    }
                    if (!duplicate) treeStructure = Helper.append(treeStructure, blockToCheck);
                    Block[] blocksAround = getBlocksAround(blockToCheck);
                    for (Block blockAround : blocksAround) {
                        if (isBlockBreakable(blockAround)) {
                            newBlocksToCheck = Helper.append(newBlocksToCheck, blockAround);
                        }
                    }
                }
            }
            Block[] checkedBlocksToCheck = new Block[]{};
            for (Block duplicateBlockCheck : newBlocksToCheck) {
                boolean duplicate = false;
                for (Block treeStructureBlock : treeStructure) {
                    if (duplicateBlockCheck.x == treeStructureBlock.x &&
                            duplicateBlockCheck.y == treeStructureBlock.y &&
                            duplicateBlockCheck.z == treeStructureBlock.z) {
                        duplicate = true;
                        break;
                    }
                }
                if (!duplicate) checkedBlocksToCheck = Helper.append(checkedBlocksToCheck, duplicateBlockCheck);
            }
            blocksToCheck = checkedBlocksToCheck;
            if (blocksToCheck.length == 0 || treeStructure.length >= 20) searchBlocks = false;
        } while (searchBlocks);
        return treeStructure;
    }

    public static Block[] getBlocksAround(Block block) {
        Level level = block.getLevel();
        Block[] blocksAround = new Block[]{};
        Vector3 position1 = new Vector3(block.x - 1, block.y, block.z);
        Vector3 position2 = new Vector3(block.x + 1, block.y, block.z);
        Vector3 position3 = new Vector3(block.x, block.y - 1, block.z);
        Vector3 position4 = new Vector3(block.x, block.y + 1, block.z);
        Vector3 position5 = new Vector3(block.x, block.y, block.z - 1);
        Vector3 position6 = new Vector3(block.x, block.y, block.z + 1);
        Vector3[] positions = new Vector3[]{position1, position2, position3, position4, position5, position6};
        for (Vector3 checkPosition : positions) {
            Block currentBlock = level.getBlock(checkPosition);
            if (isBlockBreakable(currentBlock)) blocksAround = Helper.append(blocksAround, currentBlock);
        }
        return blocksAround;
    }

    public static boolean isBlockBreakable(Block block) {
        if (AreaProtection.getInstance().getAreaByPos(block.getLocation()) == null) {
            return false;
        }
        if (block.getId() != BlockID.LOG && block.getId() != BlockID.LOG2 && block.getId() != BlockID.LEAVES && block.getId() != BlockID.LEAVES2) {
            return false;
        }
        Area area = AreaProtection.getInstance().getAreaByPos(block.getLocation());
        return Arrays.stream(foragingAreas).anyMatch(area.getName()::equalsIgnoreCase);
    }

}
