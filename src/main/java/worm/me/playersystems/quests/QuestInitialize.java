package worm.me.playersystems.quests;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class QuestInitialize {

    public static final List<List<String>> dailyQuestAttributes = new ArrayList<>();

    public void questInitialize(){

        // Daily Quests
        List<String> minecobble = List.of("Mine Cobblestone", "Break 500 cobblestone blocks.", "500", Material.COBBLESTONE.toString());
        dailyQuestAttributes.add(minecobble);
        List<String> collectiron = List.of("Yeah... I use forge actually.", "Collect 32 iron ingots out of furnaces.", "32", Material.FURNACE.toString());
        dailyQuestAttributes.add(1, collectiron);
        List<String> fish64 = List.of("Go Fish!", "Catch 64 fish.", "64", Material.COD.toString());
        dailyQuestAttributes.add(2, fish64);
        List<String> buildsnowmen = List.of("Do you wanna build a snowman?", "Build 10 snowmen.", "10", Material.CARVED_PUMPKIN.toString());
        dailyQuestAttributes.add(3, buildsnowmen);
        List<String> killmobs100 = List.of("Revenge...", "Kill 100 mobs.", "100", Material.SKELETON_SKULL.toString());
        dailyQuestAttributes.add(4, killmobs100);
        List<String> fishsaddle = List.of("How Hungry...", "Catch a saddle while fishing.", "1", Material.SADDLE.toString());
        dailyQuestAttributes.add(5, fishsaddle);
        List<String> growtrees25 = List.of("Green Thumb", "Grow 25 trees.", "25", Material.OAK_SAPLING.toString());
        dailyQuestAttributes.add(6, growtrees25);
        List<String> eatgapple = List.of("The Garden of Eden", "Eat a golden apple.", "1", Material.GOLDEN_APPLE.toString());
        dailyQuestAttributes.add(7, eatgapple);

    }
}
