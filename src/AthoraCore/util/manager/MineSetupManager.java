package AthoraCore.util.manager;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import net.llamagames.AreaProtection.AreaProtection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MineSetupManager {

    public static Map<Player, Boolean> mineSetupList = new HashMap<>();
    public static boolean isMineUnderSetup = false;

    public static boolean toggleSetupMode(Player player) {
        if (mineSetupList.containsKey(player)) {
            mineSetupList.remove(player);
            return false;
        } else {
            mineSetupList.put(player, true);
            if (!isMineUnderSetup) {
                isMineUnderSetup = true;
                renderSetupView(player.getLevel());
            }
            return true;
        }
    }

    public static void renderSetupView(Level level) {
        for (String mine : MineManager.mines) {
            List<String> locations = MineManager.mineConfig.getStringList("ore_spawn_locations." + mine);
            for (int i = 0; i < locations.size(); i++) {
                String locationString = locations.get(i);
                String[] locationCoords = locationString.split(" ");
                double x = Double.parseDouble(locationCoords[0]);
                double y = Double.parseDouble(locationCoords[1]);
                double z = Double.parseDouble(locationCoords[2]);
                Vector3 blockPosition = new Vector3(x, y, z);
                level.setBlockIdAt((int) blockPosition.x, (int) blockPosition.y, (int) blockPosition.z, BlockID.CONCRETE);
                level.setBlockDataAt((int) blockPosition.x, (int) blockPosition.y, (int) blockPosition.z, 5);
            }
        }
    }

    public static void renderDefaultView(Level level) {
        MineManager.resetMine(level);
        MineManager.generateOres(level);
    }

    public static boolean toggleSpawnBlock(Block block) {
        String mine = MineManager.getMineByAreaName(AreaProtection.getInstance().getAreaByPos(block.getLocation()).getName());
        List<String> locations = MineManager.mineConfig.getStringList("ore_spawn_locations." + mine);
        String value = block.x + " " + block.y + " " + block.z;
        boolean created = false;
        if (!mineContainsCoords(block)) {
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
            if (index != -1) locations.remove(index);
        }
        MineManager.mineConfig.set("ore_spawn_locations." + mine, locations);
        MineManager.mineConfig.save();
        return created;
    }

    public static boolean mineContainsCoords(Block block) {
        List<String> locations = MineManager.mineConfig.getStringList("ore_spawn_locations." + MineManager.getMineByAreaName(AreaProtection.getInstance().getAreaByPos(block.getLocation()).getName()));
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

}
