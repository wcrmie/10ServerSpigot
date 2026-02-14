package worm.me.listeners;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.menu.Menu;
import worm.me.menu.PlayerProfileMenu;
import worm.me.menu.abilities.AbilitiesSubMenu;
import worm.me.menu.generators.GensSubMenu;
import worm.me.menu.recipes.RecipeSpecificMenu;
import worm.me.utils.GuiConstructor;
import worm.me.Server;
import worm.me.menu.recipes.RecipeBrowserMenu;
import worm.me.menu.abilities.AbilitiesMainMenu;
import worm.me.gamesystems.items.gear.Upgrade;
import worm.me.menu.generators.GensMainMenu;
import worm.me.data.*;
import worm.me.menu.SkillsMenu;
//import worm.me.playersystems.social.legacyhats.Hats;
import worm.me.utils.info.GuideBook;
//import worm.me.playersystems.social.skins.SkinGui;
import worm.me.gamesystems.items.gear.tools.ToolsEvents;
import worm.me.menu.MainGUIMenu;
import worm.me.utils.Item;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MainListener implements Listener {
    

    @EventHandler
    public void onEnchantTable(InventoryOpenEvent e){
        if (!e.getInventory().getType().equals(InventoryType.ENCHANTING)) return;
        e.setCancelled(true);
        Player p = (Player) e.getPlayer();
        GuideBook.guideBook(p);
    }

    @EventHandler
    public void onTridentHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Trident trident)) return;

        if (!(trident.getShooter() instanceof Player)) return;

        ItemStack item = trident.getItemStack();

        if (!item.containsEnchantment(Enchantment.CHANNELING)) return;

        World world = trident.getWorld();

        if (!world.hasStorm()) return;

        Location strikeLoc = null;

        if (event.getHitEntity() != null) {
            strikeLoc = event.getHitEntity().getLocation();
        }

        if (event.getHitBlock() != null) {
            if (event.getHitBlock().getType() == Material.LIGHTNING_ROD) {
                strikeLoc = event.getHitBlock().getLocation().add(0.5, 1, 0.5);
            } else {
                strikeLoc = trident.getLocation();
            }
        }

        if (strikeLoc != null) {
            world.strikeLightning(strikeLoc);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){

        Inventory inventory = Objects.requireNonNullElse(event.getClickedInventory(), Bukkit.createInventory(event.getWhoClicked(), InventoryType.CHEST));

        Player player = (Player) event.getWhoClicked();

        Material itemType = Objects.requireNonNullElse(event.getCurrentItem(), new ItemStack(Material.STRUCTURE_VOID)).getType();
        ItemStack cursor = Objects.requireNonNullElse(event.getCursor(), new ItemStack(Material.STRUCTURE_VOID));

        Material heldItem = cursor.getType();

        switch (itemType) {
            case CHAINMAIL_BOOTS -> {
                if (heldItem.equals(Upgrade.CHAIN_ARMOR.getUpgradeItem().getType()) && cursor.getAmount() >= Upgrade.CHAIN_ARMOR.getUpgradeItem().getAmount()){
                    event.setCancelled(true);
                    cursor.setAmount(cursor.getAmount() - Upgrade.CHAIN_ARMOR.getUpgradeItem().getAmount());

                    Damageable meta = (Damageable) Objects.requireNonNull(event.getCurrentItem()).getItemMeta();

                    meta.setDamage(0);

                    ItemStack armor = new ItemStack(Material.IRON_BOOTS);
                    armor.setItemMeta(meta);

                    inventory.setItem(event.getSlot(), armor);
                    player.playSound(player, Sound.BLOCK_ANVIL_USE, 1F, 2F);
                }
            }
            case CHAINMAIL_CHESTPLATE -> {
                if (heldItem.equals(Upgrade.CHAIN_ARMOR.getUpgradeItem().getType()) && cursor.getAmount() >= Upgrade.CHAIN_ARMOR.getUpgradeItem().getAmount()){
                    event.setCancelled(true);
                    cursor.setAmount(cursor.getAmount() - Upgrade.CHAIN_ARMOR.getUpgradeItem().getAmount());

                    Damageable meta = (Damageable) Objects.requireNonNull(event.getCurrentItem()).getItemMeta();

                    meta.setDamage(0);

                    ItemStack armor = new ItemStack(Material.IRON_CHESTPLATE);
                    armor.setItemMeta(meta);

                    inventory.setItem(event.getSlot(), armor);
                    player.playSound(player, Sound.BLOCK_ANVIL_USE, 1F, 2F);
                }
            }
            case CHAINMAIL_LEGGINGS -> {
                if (heldItem.equals(Upgrade.CHAIN_ARMOR.getUpgradeItem().getType()) && cursor.getAmount() >= Upgrade.CHAIN_ARMOR.getUpgradeItem().getAmount()) {
                    event.setCancelled(true);
                    cursor.setAmount(cursor.getAmount() - Upgrade.CHAIN_ARMOR.getUpgradeItem().getAmount());

                    Damageable meta = (Damageable) Objects.requireNonNull(event.getCurrentItem()).getItemMeta();

                    meta.setDamage(0);

                    ItemStack armor = new ItemStack(Material.IRON_LEGGINGS);
                    armor.setItemMeta(meta);

                    inventory.setItem(event.getSlot(), armor);
                    player.playSound(player, Sound.BLOCK_ANVIL_USE, 1F, 2F);
                }
            }
        }

        if (inventory.getType().equals(InventoryType.PLAYER)) {
            if (itemType.equals(Material.WOODEN_AXE) || itemType.equals(Material.WOODEN_PICKAXE) ||
                    itemType.equals(Material.WOODEN_SWORD) || itemType.equals(Material.WOODEN_SHOVEL)){
                if (cursor.getType() == Upgrade.WOOD_TOOL.getUpgradeItem().getType() && cursor.getAmount() >= Upgrade.WOOD_TOOL.getUpgradeItem().getAmount()){
                    event.setCancelled(true);
                    cursor.setAmount(cursor.getAmount() - Upgrade.WOOD_TOOL.getUpgradeItem().getAmount());
                    ToolsEvents toolsEvents = new ToolsEvents();
                    toolsEvents.toolUpgradeAttempt(player, event);
                    player.playSound(player, Sound.BLOCK_ANVIL_USE, 1F, 2F);
                }

            } else if (itemType.equals(Material.STONE_AXE) || itemType.equals(Material.STONE_PICKAXE) ||
                    itemType.equals(Material.STONE_SWORD) || itemType.equals(Material.STONE_SHOVEL)){
                if (cursor.getType() == Upgrade.STONE_TOOL.getUpgradeItem().getType() && cursor.getAmount() >= Upgrade.STONE_TOOL.getUpgradeItem().getAmount()){
                    event.setCancelled(true);
                    cursor.setAmount(cursor.getAmount() - Upgrade.STONE_TOOL.getUpgradeItem().getAmount());
                    ToolsEvents toolsEvents = new ToolsEvents();
                    toolsEvents.toolUpgradeAttempt(player, event);
                    player.playSound(player, Sound.BLOCK_ANVIL_USE, 1F, 2F);
                }
            }
        }

        if (!player.getPassengers().isEmpty() && player.getPassengers().getFirst().getType() != EntityType.PLAYER){
            event.setCancelled(true);
        }

        if (inventory.getHolder() instanceof Menu menu) {

            event.setCancelled(true);

            switch (menu) {

                case PlayerProfileMenu profileGUI -> profileGUI.handleClick(event);

                case MainGUIMenu mainMenu -> mainMenu.handleClick(event);

                case SkillsMenu skillsMenu -> skillsMenu.handleClick(event);

                case RecipeBrowserMenu recipeBrowserMenu -> recipeBrowserMenu.handleClick(event);

                case RecipeSpecificMenu recipeSpecificMenu -> recipeSpecificMenu.handleClick(event);

                case GensMainMenu gensMainMenu -> gensMainMenu.handleClick(event);

                case GensSubMenu gensSubMenu -> gensSubMenu.handleClick(event);

                case AbilitiesMainMenu abilitiesMainMenu -> abilitiesMainMenu.handleClick(event);

                case AbilitiesSubMenu abilitiesSubMenu -> abilitiesSubMenu.handleClick(event);

                default -> Server.getPlugin().getLogger().info("bruh");
            }
        }

        if (Objects.requireNonNullElse(event.getCurrentItem(), new ItemStack(Material.STRUCTURE_VOID)).isSimilar(Item.create(Material.BARRIER, ChatColor.RED + "Close Menu", " ", ChatColor.GRAY + "Close this menu."))) player.closeInventory();

        if (Objects.requireNonNullElse(event.getCurrentItem(), new ItemStack(Material.STRUCTURE_VOID)).getType() == Material.SPECTRAL_ARROW && event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GRAY + "Go Back")){
            MainGUIMenu mainGUIMenu = new MainGUIMenu(player);
            mainGUIMenu.open();

        }

        if (event.isCancelled()) ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, ThreadLocalRandom.current().nextFloat(1.0F, 1.2F));

    }
}