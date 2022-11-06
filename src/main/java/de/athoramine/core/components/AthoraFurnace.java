package de.athoramine.core.components;

import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

@Setter
@Value
public class AthoraFurnace {

    @NonFinal int id;
    @NonFinal int playerID;
    @NonFinal int upgrade;
    @NonFinal String level;
    @NonFinal int x;
    @NonFinal int y;
    @NonFinal int z;
}
