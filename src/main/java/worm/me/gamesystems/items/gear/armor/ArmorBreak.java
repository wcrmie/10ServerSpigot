package worm.me.gamesystems.items.gear.armor;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import worm.me.gamesystems.items.gear.EnchantsDowngrade;

public class ArmorBreak {

    public ItemStack breakArmor(ItemStack item) {

        Material material = item.getType();

        ItemMeta meta = item.getItemMeta();

        if (material.toString().contains("IRON")) {
            material = Material.valueOf(material.toString().replace("IRON", "CHAINMAIL"));
        } else return null;

        ItemStack newItem = new ItemStack(material, 1);

        Damageable metaDamageable = (Damageable) meta;
        metaDamageable.setDamage(0); // Reset the damage to 0

        newItem.setItemMeta(metaDamageable);

        EnchantsDowngrade enchantsDowngrade = new EnchantsDowngrade();

        return enchantsDowngrade.downgradeEnchants(newItem);

    }

    public void handleArmorBreak(PlayerItemBreakEvent event) {
        ItemStack brokenItem = event.getBrokenItem();
        ItemStack newItem = breakArmor(brokenItem);

        if (newItem != null) {
            if (event.getPlayer().getInventory().getItem(EquipmentSlot.CHEST).equals(brokenItem)) {
                event.getPlayer().getInventory().setChestplate(newItem);
            } else if (event.getPlayer().getInventory().getItem(EquipmentSlot.LEGS).equals(brokenItem)) {
                event.getPlayer().getInventory().setLeggings(newItem);
            } else if (event.getPlayer().getInventory().getItem(EquipmentSlot.FEET).equals(brokenItem)) {
                event.getPlayer().getInventory().setBoots(newItem);
            }
        }
    }

}
