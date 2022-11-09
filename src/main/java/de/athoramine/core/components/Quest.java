package de.athoramine.core.components;

import lombok.Value;

@Value
public class Quest {
    int questID;
    QuestType questType;
    String itemType;
    int itemID;
    int itemMeta;
    int amount;
    double moneyReward;
    double ruhmReward;
}
