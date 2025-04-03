package worm.me.playersystems.data.tools;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Upgrade {

    WOOD_TOOL(new ItemStack(Material.COBBLESTONE, 16)),
    STONE_TOOL(new ItemStack(Material.IRON_INGOT, 8)),
    CHAIN_ARMOR(new ItemStack(Material.IRON_INGOT, 8));

    final ItemStack upgradeItem;

    Upgrade(ItemStack upgradeItem) {
        this.upgradeItem = upgradeItem;
    }

    public ItemStack getUpgradeItem() {
        return upgradeItem;
    }


}
