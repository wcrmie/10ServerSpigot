package worm.me.gamesystems.items.gear.tools;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import worm.me.gamesystems.items.gear.EnchantsDowngrade;

import java.util.concurrent.ThreadLocalRandom;

public class ToolsEvents {

    public void toolBreak(Player p, PlayerItemDamageEvent e){

        Tools tools = new Tools();

        EnchantsDowngrade enchantsDowngrade = new EnchantsDowngrade();

        ItemStack brokenTool = enchantsDowngrade.downgradeEnchants(e.getItem());

        if (brokenTool != e.getItem()) p.playSound(p, Sound.ENTITY_ENDER_EYE_DEATH, 1F, 0.5F);

        if ((brokenTool.getType().name().startsWith("GOLDEN_") || brokenTool.getType().name().startsWith("COPPER_") )&& brokenTool.getEnchantments().isEmpty()) {
            int RNG = ThreadLocalRandom.current().nextInt(0, 10);
            if (RNG == 0) {
                p.getInventory().setItem(p.getInventory().getHeldItemSlot(), null);
                p.playSound(p, Sound.ENTITY_ITEM_BREAK, 1F, 1F);
                return;
            }
        }

        Damageable meta = (Damageable) brokenTool.getItemMeta();

        assert meta != null;
        meta.setDamage(0);

        brokenTool.setItemMeta(meta);

        int i = p.getInventory().getHeldItemSlot();

        p.getInventory().setItem(i, tools.toolDowngrade(brokenTool));

        p.playSound(p, Sound.ENTITY_ITEM_BREAK, 1F, 1F);

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
