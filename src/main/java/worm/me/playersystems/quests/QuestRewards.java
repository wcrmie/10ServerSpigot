package worm.me.playersystems.quests;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum QuestRewards {

    MINE_STONE_REWARD(new int[]{5, 250}, new ItemStack[]{new ItemStack(Material.IRON_INGOT, 5)}, null, null),
    COLLECT_IRON_REWARD(new int[]{4,250, 5, 100}, new ItemStack[]{new ItemStack(Material.GOLD_NUGGET, 5)}, null, null),
    GO_FISH_REWARD(new int[]{3, 250}, new ItemStack[]{new ItemStack(Material.IRON_INGOT, 5)}, null, null),
    BUILD_SNOWMEN_REWARD(new int[]{2, 250}, new ItemStack[]{new ItemStack(Material.IRON_INGOT, 2), new ItemStack(Material.GOLD_NUGGET, 3)}, null, null), // IDEA FOR PORTABLE SNOWMAN ITEM
    KILL_MOBS_REWARD(new int[]{0, 250, 6, 50}, new ItemStack[]{new ItemStack(Material.IRON_INGOT, 5)}, null, null),
    FISH_SADDLE_REWARD(new int[]{3, 250}, new ItemStack[]{new ItemStack(Material.IRON_INGOT, 3)}, new int[]{0}, null),
    GROW_TREES_REWARD(new int[]{1, 100, 7, 250}, new ItemStack[]{new ItemStack(Material.GOLD_NUGGET, 4), new ItemStack(Material.BONE, 8)}, null, null),
    EAT_GAPPLE_REWARD(new int[]{1, 50, 2, 50, 7, 50}, new ItemStack[]{new ItemStack(Material.GOLD_NUGGET, 5)}, null, null),



    ;

    int[] exp;
    ItemStack[] items;
    int[] hats;
    String[] recipes;

    QuestRewards(int[] exp, ItemStack[] items, int[] hats, String[] recipes){
        this.exp = exp;
        this.items = items;
    }

    public int[] getExp(){
        return exp;
    }

    public ItemStack[] getItems(){
        return items;
    }

    public int[] getHats(){
        return hats;
    }

    public String[] getRecipes(){
        return recipes;
    }


}
