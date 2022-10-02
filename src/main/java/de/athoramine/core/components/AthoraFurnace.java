package de.athoramine.core.components;

public class AthoraFurnace {

    private int id;
    private int playerID;
    private int upgrade;
    private String level;
    private int x;
    private int y;
    private int z;

    public AthoraFurnace(int id, int playerID, int upgrade, String level, int x, int y, int z) {
        this.id = id;
        this.playerID = playerID;
        this.upgrade = upgrade;
        this.level = level;
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(int upgrade) {
        this.upgrade = upgrade;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
