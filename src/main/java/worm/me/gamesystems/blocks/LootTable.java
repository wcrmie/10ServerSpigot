package worm.me.gamesystems.blocks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import worm.me.GuiConstructor;

import java.util.Map;

public enum LootTable {

    DIRT(Map.of(new ItemStack(Material.DIRT), 25, (GuiConstructor.createGuiItem(Material.QUARTZ, ChatColor.WHITE + "Salt")), 15, new ItemStack(Material.WHEAT_SEEDS), 10));

    private final Map<ItemStack, Integer> weights;

    LootTable(Map<ItemStack, Integer> weights) {
        this.weights = weights;
    }

    public Map<ItemStack, Integer> getWeights() {
        return weights;
    }
}
