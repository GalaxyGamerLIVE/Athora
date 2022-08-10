package de.athoramine.core.util.configs;

import cn.nukkit.utils.Config;

public class BankConfig {

    public static Config config;

    public static void setConfig(Config config) {
        BankConfig.config = config;
    }

    public static int getMoneyLimitByBankLevel(int level) {
        return BankConfig.config.getInt("lagerplatz_upgrades.upgrade_" + level + ".money_limit");
    }

    public static int getRequiredRankByBankLevel(int level) {
        return BankConfig.config.getInt("lagerplatz_upgrades.upgrade_" + level + ".required_rank");
    }

    public static int getUpgradesCostsByBankLevel(int level) {
        return BankConfig.config.getInt("lagerplatz_upgrades.upgrade_" + level + ".costs");
    }

    public static int getSalaryAmountBySalaryLevel(int level) {
        return BankConfig.config.getInt("salary_upgrades.upgrade_" + level + ".salary_amount");
    }

    public static int getRequiredRankBySalaryLevel(int level) {
        return BankConfig.config.getInt("salary_upgrades.upgrade_" + level + ".required_rank");
    }

    public static int getUpgradesCostsBySalaryLevel(int level) {
        return BankConfig.config.getInt("salary_upgrades.upgrade_" + level + ".costs");
    }

}
