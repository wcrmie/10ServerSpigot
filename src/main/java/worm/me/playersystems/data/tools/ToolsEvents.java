package worm.me.playersystems.data.tools;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import worm.me.Server;
import worm.me.playersystems.data.VoidPlayer;

import java.util.List;

public class ToolsEvents {

    public void toolBreak(Player p, PlayerItemBreakEvent e){

        Tools tools = new Tools();

        ItemStack clickedItem = e.getBrokenItem();

        Damageable meta = (Damageable) clickedItem.getItemMeta();

        assert meta != null;
        meta.setDamage(0);

        clickedItem.setItemMeta(meta);

        int slot = p.getInventory().getHeldItemSlot();

        p.getInventory().setItem(slot, tools.toolDowngrade(clickedItem));

    }

    public void toolUpgradeAttempt(Player player, InventoryClickEvent event){

        Tools tools = new Tools();

        ItemStack clickedItem = event.getCurrentItem();

        Damageable meta = (Damageable) clickedItem.getItemMeta();

        meta.setDamage(0);

        clickedItem.setItemMeta(meta);

        player.getInventory().setItem(event.getSlot(), tools.toolUpgrade(clickedItem));


    }

}
