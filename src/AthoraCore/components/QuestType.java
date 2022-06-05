package AthoraCore.components;

public class QuestType {
    private final int typeID;

    private final int requiredLevel;

    private final String name;

    public QuestType(int typeID, int requiredLevel, String name) {
        this.typeID = typeID;
        this.requiredLevel = requiredLevel;
        this.name = name;
    }

    public int getTypeID() {
        return this.typeID;
    }

    public int getRequiredLevel() {
        return this.requiredLevel;
    }

    public String getName() {
        return this.name;
    }
}
