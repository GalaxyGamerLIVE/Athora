package de.athoramine.core.database;

import de.athoramine.core.util.configs.GeneralConfig;

abstract class AbstractDatabase {
    protected static final String GLOBAL_DATABASE = "athora_global";

    protected static final String DEFAULT_DATABASE = GeneralConfig.generalConfig.getString("de.athoramine.core.database.default");

    protected static final String DEV_DATABASE = "athora";

    protected static final String PRODUCTION_DATABASE = "athora_production";

    protected static final String USERNAME = GeneralConfig.generalConfig.getString("de.athoramine.core.database.user");

    protected static final String PASSWORD = GeneralConfig.generalConfig.getString("de.athoramine.core.database.password");

    protected static String getUrl(String database) {
        return "jdbc:mysql://" + GeneralConfig.generalConfig
                .getString("de.athoramine.core.database.host") + ":" + GeneralConfig.generalConfig
                .getInt("de.athoramine.core.database.port") + "/" + database + "?serverTimezone=Europe/Berlin&autoReconnect=true";
    }
}
