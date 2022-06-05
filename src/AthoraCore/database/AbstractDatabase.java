package AthoraCore.database;

import AthoraCore.util.configs.GeneralConfig;

abstract class AbstractDatabase {
    protected static final String GLOBAL_DATABASE = "athora_global";

    protected static final String DEFAULT_DATABASE = GeneralConfig.generalConfig.getString("database.default");

    protected static final String DEV_DATABASE = "athora";

    protected static final String PRODUCTION_DATABASE = "athora_production";

    protected static final String USERNAME = GeneralConfig.generalConfig.getString("database.user");

    protected static final String PASSWORD = GeneralConfig.generalConfig.getString("database.password");

    protected static String getUrl(String database) {
        return "jdbc:mysql://" + GeneralConfig.generalConfig
                .getString("database.host") + ":" + GeneralConfig.generalConfig
                .getInt("database.port") + "/" + database + "?serverTimezone=Europe/Berlin&autoReconnect=true";
    }
}
