package worm.me.gamesystems.fishing.rods;

import worm.me.gamesystems.fishing.FishingTables.FishingConfiguration;

public enum RodType {
    DEFAULT("default", new FishingConfiguration(40,40,20, null)),
    BREEZE_ROD("", new FishingConfiguration(40,40,20, null)),
    HOT_ROD("", new FishingConfiguration(20,50,30, 30));

    RodType(String rodType, FishingConfiguration config) {
        

    }



}
