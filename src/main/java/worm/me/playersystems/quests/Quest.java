package worm.me.playersystems.quests;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Quest {
    MINE_STONE("Mine Cobblestone", Material.COBBLESTONE, "Mine 512 Cobblestone.", 512),
    COLLECT_IRON("I prefer Forge.", Material.BLAST_FURNACE, "Smelt 64 iron nuggets.", 64),
    GO_FISH("Go Fish!", Material.COD_BUCKET, "Catch 64 fish.", 64),
    BUILD_SNOWMEN("Do you wanna...", Material.CARVED_PUMPKIN, "Build (or deploy) 10 snowmen.", 10),
    KILL_MOBS("Monster Hunter", Material.ZOMBIE_HEAD, "Kill 100 hostile mobs.", 100),
    GROW_TREES("#TeamTrees", Material.OAK_SAPLING, "Grow 10 trees.", 10),
    EAT_GAPPLE("All That Glitters", Material.GOLDEN_APPLE, "Eat a golden apple.", 1),
    KILL_GOLEM("David and Goliath", Material.IRON_GOLEM_SPAWN_EGG, "Kill an iron golem boss.", 1),
    TRADE_WANDERING("The Silk Road", Material.GOLD_NUGGET, "Trade with a wandering trader.", 1);

    final String name;
    final Material icon;
    final String description;
    final int progressNeeded;


    Quest(String name, Material icon, String description, int progressNeeded) {
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.progressNeeded = progressNeeded;
    }

    public ItemStack getIcon() {
        return new ItemStack(icon);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getProgressNeeded() {
        return progressNeeded;
    }
}
