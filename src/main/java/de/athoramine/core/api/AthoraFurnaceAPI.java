package de.athoramine.core.api;

import de.athoramine.core.components.AthoraFurnace;
import de.athoramine.core.database.DefaultDatabase;
import de.athoramine.core.database.SQLEntity;
import de.athoramine.core.util.Helper;

import java.sql.SQLException;

public class AthoraFurnaceAPI {

    public static AthoraFurnace[] getAllFurnaces() {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT * FROM athora_furnaces WHERE athora_furnaces.deleted = 'no';");
        try {
            AthoraFurnace[] athoraFurnaces = new AthoraFurnace[]{};
            while (sqlEntity.resultSet.next()) {
                AthoraFurnace athoraFurnace = new AthoraFurnace(
                        sqlEntity.resultSet.getInt("id"),
                        sqlEntity.resultSet.getInt("player_id"),
                        sqlEntity.resultSet.getInt("upgrade"),
                        sqlEntity.resultSet.getString("level"),
                        sqlEntity.resultSet.getInt("x"),
                        sqlEntity.resultSet.getInt("y"),
                        sqlEntity.resultSet.getInt("z")
                );
                athoraFurnaces = Helper.append(athoraFurnaces, athoraFurnace);
            }
            sqlEntity.close();
            return athoraFurnaces;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return new AthoraFurnace[]{};
    }

    /**
     * @param furnace furnace
     */
    public static void addFurnace(AthoraFurnace furnace) {
        DefaultDatabase.update("INSERT INTO athora_furnaces (player_id, upgrade, level, x, y, z) VALUES(" + furnace.getPlayerID() + ", " + furnace.getUpgrade() + ", '" + furnace.getLevel() + "', " + furnace.getX() + ", " + furnace.getY() + ", " + furnace.getZ() + ");");
    }

    /**
     * @param level level
     * @param x     x
     * @param y     y
     * @param z     z
     * @return AthoraFurnace|null return null if no furnace was found
     */
    public static AthoraFurnace getFurnace(String level, int x, int y, int z) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT * FROM athora_furnaces WHERE athora_furnaces.level = '" + level + "' AND x = " + x + " AND y = " + y + " AND z = " + z + " AND athora_furnaces.deleted = 'no';");
        try {
            if (sqlEntity.resultSet.next()) {
                AthoraFurnace furnace = new AthoraFurnace(
                        sqlEntity.resultSet.getInt("id"),
                        sqlEntity.resultSet.getInt("player_id"),
                        sqlEntity.resultSet.getInt("upgrade"),
                        sqlEntity.resultSet.getString("level"),
                        sqlEntity.resultSet.getInt("x"),
                        sqlEntity.resultSet.getInt("y"),
                        sqlEntity.resultSet.getInt("z")
                );
                sqlEntity.close();
                return furnace;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return null;
    }

    /**
     * @param id furnace id
     * @return AthoraFurnace|null return null if no furnace was found
     */
    public static AthoraFurnace getFurnace(int id) {
        SQLEntity sqlEntity = DefaultDatabase.query("SELECT * FROM athora_furnaces WHERE athora_furnaces.id = " + id + " AND athora_furnaces.deleted = 'no';");
        try {
            if (sqlEntity.resultSet.next()) {
                AthoraFurnace furnace = new AthoraFurnace(
                        sqlEntity.resultSet.getInt("id"),
                        sqlEntity.resultSet.getInt("player_id"),
                        sqlEntity.resultSet.getInt("upgrade"),
                        sqlEntity.resultSet.getString("level"),
                        sqlEntity.resultSet.getInt("x"),
                        sqlEntity.resultSet.getInt("y"),
                        sqlEntity.resultSet.getInt("z")
                );
                sqlEntity.close();
                return furnace;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlEntity.close();
        return null;
    }

    /**
     * @param id furnace id
     */
    public static void deleteFurnace(int id) {
        DefaultDatabase.update("UPDATE athora_furnaces SET deleted = 'yes' WHERE id = " + id + ";");
    }

    /**
     * @param newFurnace new Furnace
     */
    public static void updateFurnace(AthoraFurnace newFurnace) {
        DefaultDatabase.update("UPDATE athora_furnaces SET id = " + newFurnace.getId() +
                ", player_id = " + newFurnace.getPlayerID() +
                ", upgrade = " + newFurnace.getUpgrade() +
                ", level = '" + newFurnace.getLevel() + "'" +
                ", x = " + newFurnace.getX() +
                ", y = " + newFurnace.getY() +
                ", z = " + newFurnace.getZ() +
                " WHERE deleted = 'no' AND id = " + newFurnace.getId() + ";");
    }

}
