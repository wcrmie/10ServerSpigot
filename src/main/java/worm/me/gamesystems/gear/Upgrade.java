package worm.me.gamesystems.gear;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Upgrade {

    WOOD_TOOL(new ItemStack(Material.COBBLESTONE, 16)),
    STONE_TOOL(new ItemStack(Material.IRON_INGOT, 2)),
    CHAIN_ARMOR(new ItemStack(Material.IRON_INGOT, 3));

    final ItemStack upgradeItem;

    Upgrade(ItemStack upgradeItem) {
        this.upgradeItem = upgradeItem;
    }

    public ItemStack getUpgradeItem() {
        return upgradeItem;
    }


}
