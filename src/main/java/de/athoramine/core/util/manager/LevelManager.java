package de.athoramine.core.util.manager;

import cn.nukkit.utils.Config;

public class LevelManager {

    public static Config levelConfig;

    public static void setLevelConfig(Config levelConfig) {
        LevelManager.levelConfig = levelConfig;
    }

    public static int getLevelRewardLevel(int level) {
        boolean searching = true;
        do {
            if (levelConfig.exists("level_rewards.level_" + level)) {
                searching = false;
            } else {
                if (level == 0) return 0;
                level--;
            }
        } while (searching);
        return level;
    }

    public static int getRuhmRequirement(int level) {
        if (levelConfig.exists("level_upgrades.level_" + level)) {
            return levelConfig.getInt("level_upgrades.level_" + level + ".ruhm");
        } else {
            int lastLevelInConfig = (levelConfig.getSection("level_upgrades").size());
            int levelDifference = level - lastLevelInConfig;
            int ruhmPerLevelAfterLastLevelInConfig = levelConfig.getInt("level_upgrade_end_loop.ruhm") * levelDifference;
            return levelConfig.getInt("level_upgrades.level_" + lastLevelInConfig + ".ruhm") + ruhmPerLevelAfterLastLevelInConfig;
        }
    }

    public static int getMoneyRequirement(int level) {
        if (levelConfig.exists("level_upgrades.level_" + level)) {
            return levelConfig.getInt("level_upgrades.level_" + level + ".money");
        } else {
            return levelConfig.getInt("level_upgrade_end_loop.money");
        }
    }

    public static String getRoleName(int level) {
        for (int i = 0; i < levelConfig.getSection("level_roles").size(); i++) {
            if (i == levelConfig.getSection("level_roles").size() - 1) {
                return levelConfig.getString("level_roles.role_" + (i + 1) + ".name");
            }
            if (level < levelConfig.getInt("level_roles.role_" + (i + 2) + ".min_level")) {
                return levelConfig.getString("level_roles.role_" + (i + 1) + ".name");
            }
        }
        return "NO_RANK";
    }

}
