package de.athoramine.core.components;

public class Quest {
    private final int questID;

    private final QuestType questType;

    private final String itemType;

    private final int itemID;

    private final int itemMeta;

    private final int amount;

    private final double moneyReward;

    private final double ruhmReward;

    public Quest(int questID, QuestType questType, String itemType, int itemID, int itemMeta, int amount, double moneyReward, double ruhmReward) {
        this.questID = questID;
        this.questType = questType;
        this.itemType = itemType;
        this.itemID = itemID;
        this.itemMeta = itemMeta;
        this.amount = amount;
        this.moneyReward = moneyReward;
        this.ruhmReward = ruhmReward;
    }

    public int getQuestID() {
        return this.questID;
    }

    public String getItemType() {
        return this.itemType;
    }

    public int getItemID() {
        return this.itemID;
    }

    public int getItemMeta() {
        return this.itemMeta;
    }

    public int getAmount() {
        return this.amount;
    }

    public double getMoneyReward() {
        return this.moneyReward;
    }

    public double getRuhmReward() {
        return this.ruhmReward;
    }

    public QuestType getQuestType() {
        return this.questType;
    }
}
