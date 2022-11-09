package de.athoramine.core.util.configs;

import cn.nukkit.utils.Config;
import de.athoramine.core.database.GlobalDatabase;
import de.athoramine.core.database.SQLEntity;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class BankConfig {

    public static Config config;

    // TODO replace config
    public static void setConfig(Config config) {
        BankConfig.config = config;
    }

    public static Map<Integer, Integer> getMoneyLimitMapping() {
        Map<Integer, Integer> moneyLimitMapping = new HashMap<>();
        SQLEntity sqlEntity = GlobalDatabase.query("SELECT level, money_limit FROM athora_bank_storage_upgrades;");
        try {
            while (sqlEntity.resultSet.next()) {
                int level = sqlEntity.resultSet.getInt("level");
                int moneyLimit = sqlEntity.resultSet.getInt("money_limit");
                moneyLimitMapping.put(level, moneyLimit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return moneyLimitMapping;
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
