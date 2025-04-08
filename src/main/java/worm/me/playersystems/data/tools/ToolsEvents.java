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

        List<ItemStack> playerTools = VoidPlayer.getTools(p);

        ItemStack clickedItem = e.getBrokenItem();

        Damageable meta = (Damageable) clickedItem.getItemMeta();

        assert meta != null;
        meta.setDamage(0);

        clickedItem.setItemMeta(meta);

        playerTools.remove(clickedItem);

        playerTools.add(tools.toolDowngrade(clickedItem));

        int slot = p.getInventory().getHeldItemSlot();

        p.getInventory().setItem(slot, tools.toolDowngrade(clickedItem));

        VoidPlayer.setTools(playerTools, p);
    }

    public void toolUpgradeAttempt(Player player, InventoryClickEvent event){

        Tools tools = new Tools();

        List<ItemStack> playerTools = VoidPlayer.getTools(player);

        ItemStack clickedItem = event.getCurrentItem();

        Damageable meta = (Damageable) clickedItem.getItemMeta();

        meta.setDamage(0);

        clickedItem.setItemMeta(meta);

        playerTools.remove(clickedItem);

        playerTools.add(tools.toolUpgrade(clickedItem));

        player.getInventory().setItem(event.getSlot(), tools.toolUpgrade(clickedItem));

        VoidPlayer.setTools(playerTools, player);


    }

}
