package worm.me.PlayerSystems.Quests;

import worm.me.server;

import java.util.HashMap;
import java.util.List;

public class QuestInitialize {

    private final server plugin;

    public QuestInitialize(server plugin) {
        this.plugin = plugin;
    }

    public static final HashMap<Integer, List<String>> dailyQuestAttributes = new HashMap<>();

    public void questInitialize(){

        // Daily Quests
        List<String> minecobble = List.of("Mine Cobblestone", "Break 500 cobblestone blocks.", "500", Material.COBBLESTONE.toString());
        dailyQuestAttributes.put(0, minecobble);
        List<String> collectiron = List.of("Yeah... I use forge actually.", "Collect 32 iron ingots out of furnaces.", "32", Material.IRON_INGOT.toString());
        dailyQuestAttributes.put(1, collectiron);
        List<String> fish64 = List.of("Go Fish!", "Catch 64 fish.", "64", Material.COD.toString());
        dailyQuestAttributes.put(2, fish64);
        List<String> buildsnowmen = List.of("Do you wanna build a snowman?", "Build 10 snowmen.", "10", Material.CARVED_PUMPKIN.toString());
        dailyQuestAttributes.put(3, buildsnowmen);
        List<String> killmobs100 = List.of("Revenge...", "Kill 100 mobs.", "100", Material.SKELETON_SKULL.toString());
        dailyQuestAttributes.put(4, killmobs100);
        List<String> fishsaddle = List.of("How Hungry...", "Catch a saddle while fishing.", "1", Material.SADDLE.toString());
        dailyQuestAttributes.put(5, fishsaddle);
        List<String> growtrees25 = List.of("Green Thumb", "Grow 25 trees.", "25", Material.OAK_SAPLING.toString());
        dailyQuestAttributes.put(6, growtrees25);
        List<String> eatgapple = List.of("The Garden of Eden", "Eat a golden apple.", "1");
        dailyQuestAttributes.put(7, eatgapple);
        List<String> donthurt10 = List.of("Peaceful", "Don't take damage for 10 minutes. (OUTSIDE OF SPAWN)", "600");
        dailyQuestAttributes.put(8, donthurt10);

    }
}
