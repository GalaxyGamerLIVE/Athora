package de.athoramine.core.components;

import lombok.Value;

@Value
public class QuestType {
    int typeID;
    int requiredLevel;
    String name;
}
