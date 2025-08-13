package worm.me.listeners;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.GuiConstructor;
import worm.me.Server;
import worm.me.gamesystems.recipes.RecipesGUI;
import worm.me.playersystems.abilities.AbilitiesGUI;
import worm.me.gamesystems.gear.Upgrade;
import worm.me.playersystems.generator.GeneratorEdit;
import worm.me.playersystems.data.*;
import worm.me.playersystems.data.levels.LevelsGUI;
//import worm.me.playersystems.social.legacyhats.Hats;
import worm.me.playersystems.info.GuideBook;
//import worm.me.playersystems.social.skins.SkinGui;
import worm.me.gamesystems.gear.tools.ToolsEvents;
import worm.me.playersystems.info.MainGUIMenu;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MainListener implements org.bukkit.event.Listener {

    private final worm.me.Server plugin;

    private final List<LivingEntity> tickList = new ArrayList<>();

    public MainListener(worm.me.Server plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEnchantTable(InventoryOpenEvent e){
        if (!e.getInventory().getType().equals(InventoryType.ENCHANTING)) return;
        e.setCancelled(true);
        Player p = (Player) e.getPlayer();
        GuideBook guideBook = new GuideBook();
        guideBook.guideBook(p);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){

        Inventory inventory = Objects.requireNonNullElse(e.getClickedInventory(), Bukkit.createInventory(e.getWhoClicked(), InventoryType.CHEST));
        InventoryView view = e.getView();
        String invName = view.getTitle();
        Player player = (Player) e.getWhoClicked();
        Material itemType = Objects.requireNonNullElse(e.getCurrentItem(), new ItemStack(Material.STRUCTURE_VOID)).getType();
        ItemStack cursor = Objects.requireNonNullElse(e.getCursor(), new ItemStack(Material.STRUCTURE_VOID));

        switch (itemType) {
            case CHAINMAIL_BOOTS -> {
                if (cursor.getType().equals(Upgrade.CHAIN_ARMOR.getUpgradeItem().getType()) && cursor.getAmount() >= Upgrade.CHAIN_ARMOR.getUpgradeItem().getAmount()){
                    e.setCancelled(true);
                    cursor.setAmount(cursor.getAmount() - Upgrade.CHAIN_ARMOR.getUpgradeItem().getAmount());

                    org.bukkit.inventory.meta.Damageable meta = (Damageable) e.getCurrentItem().getItemMeta();

                    meta.setDamage(0);

                    ItemStack armor = new ItemStack(Material.IRON_BOOTS);
                    armor.setItemMeta(meta);

                    inventory.setItem(e.getSlot(), armor);
                    player.playSound(player, Sound.BLOCK_ANVIL_USE, 1F, 2F);
                }
            }
            case CHAINMAIL_CHESTPLATE -> {
                if (cursor.getType().equals(Upgrade.CHAIN_ARMOR.getUpgradeItem().getType()) && cursor.getAmount() >= Upgrade.CHAIN_ARMOR.getUpgradeItem().getAmount()){
                    e.setCancelled(true);
                    cursor.setAmount(cursor.getAmount() - Upgrade.CHAIN_ARMOR.getUpgradeItem().getAmount());

                    org.bukkit.inventory.meta.Damageable meta = (Damageable) e.getCurrentItem().getItemMeta();

                    meta.setDamage(0);

                    ItemStack armor = new ItemStack(Material.IRON_CHESTPLATE);
                    armor.setItemMeta(meta);

                    inventory.setItem(e.getSlot(), armor);
                    player.playSound(player, Sound.BLOCK_ANVIL_USE, 1F, 2F);
                }
            }
            case CHAINMAIL_LEGGINGS -> {
                if (cursor.getType().equals(Upgrade.CHAIN_ARMOR.getUpgradeItem().getType()) && cursor.getAmount() >= Upgrade.CHAIN_ARMOR.getUpgradeItem().getAmount()) {
                    e.setCancelled(true);
                    cursor.setAmount(cursor.getAmount() - Upgrade.CHAIN_ARMOR.getUpgradeItem().getAmount());

                    org.bukkit.inventory.meta.Damageable meta = (Damageable) e.getCurrentItem().getItemMeta();

                    meta.setDamage(0);

                    ItemStack armor = new ItemStack(Material.IRON_LEGGINGS);
                    armor.setItemMeta(meta);

                    inventory.setItem(e.getSlot(), armor);
                    player.playSound(player, Sound.BLOCK_ANVIL_USE, 1F, 2F);
                }
            }
        }

        if (inventory.getType().equals(InventoryType.PLAYER)) {
            if (itemType.equals(Material.WOODEN_AXE) || itemType.equals(Material.WOODEN_PICKAXE) ||
                    itemType.equals(Material.WOODEN_SWORD) || itemType.equals(Material.WOODEN_SHOVEL)){
                if (cursor.getType() == Upgrade.WOOD_TOOL.getUpgradeItem().getType() && cursor.getAmount() >= Upgrade.WOOD_TOOL.getUpgradeItem().getAmount()){
                    e.setCancelled(true);
                    cursor.setAmount(cursor.getAmount() - Upgrade.WOOD_TOOL.getUpgradeItem().getAmount());
                    ToolsEvents toolsEvents = new ToolsEvents();
                    toolsEvents.toolUpgradeAttempt(player, e);
                    player.playSound(player, Sound.BLOCK_ANVIL_USE, 1F, 2F);
                }

            } else if (itemType.equals(Material.STONE_AXE) || itemType.equals(Material.STONE_PICKAXE) ||
                    itemType.equals(Material.STONE_SWORD) || itemType.equals(Material.STONE_SHOVEL)){
                if (cursor.getType() == Upgrade.STONE_TOOL.getUpgradeItem().getType() && cursor.getAmount() >= Upgrade.STONE_TOOL.getUpgradeItem().getAmount()){
                    e.setCancelled(true);
                    cursor.setAmount(cursor.getAmount() - Upgrade.STONE_TOOL.getUpgradeItem().getAmount());
                    ToolsEvents toolsEvents = new ToolsEvents();
                    toolsEvents.toolUpgradeAttempt(player, e);
                    player.playSound(player, Sound.BLOCK_ANVIL_USE, 1F, 2F);
                }
            }
        }

        if (!player.getPassengers().isEmpty() && player.getPassengers().getFirst().getType() != EntityType.PLAYER){
            e.setCancelled(true);
        }

        GeneratorEdit genEdit = new GeneratorEdit();

        if (e.getSlotType() == InventoryType.SlotType.ARMOR){
        }

        if (Objects.requireNonNullElse(e.getCurrentItem(), new ItemStack(Material.STRUCTURE_VOID)).isSimilar(GuiConstructor.createGuiItem(Material.BARRIER, ChatColor.RED + "Close Menu", " ", ChatColor.GRAY + "Close this menu."))) player.closeInventory();

        if (Objects.requireNonNullElse(e.getCurrentItem(), new ItemStack(Material.STRUCTURE_VOID)).getType() == Material.SPECTRAL_ARROW && e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GRAY + "Go Back")){
            GuiConstructor guiConstructor = new GuiConstructor();
            guiConstructor.goBack(e);
        }

        if (invName.equals(ChatColor.DARK_GRAY + "Main Menu")){
            MainGUIMenu mainGUIMenu = new MainGUIMenu();
            mainGUIMenu.eventHandler(e);
        }

        if (invName.equals(ChatColor.DARK_GRAY + "Your Active Abilities")){
            e.setCancelled(true);
            AbilitiesGUI abilitiesGUI = new AbilitiesGUI();
            abilitiesGUI.initializeAbilitySelection(player, e);;
        }

        if (invName.contains("Ability Slot")) {
            e.setCancelled(true);
            AbilitiesGUI abilitiesGUI = new AbilitiesGUI();
            abilitiesGUI.activateAbility(player, e.getSlot(), e);
        }

        if (invName.contains("Recipes Page ")) {
            e.setCancelled(true);
            RecipesGUI recipeGui = new RecipesGUI();

            recipeGui.eventHandlerMain(e);
        }

        if (invName.contains("Recipe of ")) {
            e.setCancelled(true);
            RecipesGUI recipeGui = new RecipesGUI();

            recipeGui.eventHandlerRecipe(e);
        }

        if (invName.equals(ChatColor.DARK_GRAY + "Skins")){
            e.setCancelled(true);
            //SkinGui.skinGuiDo(e, plugin);
        }

        if (invName.contains("Profile")){
            PlayerProfileGUI playerProfileGUI = new PlayerProfileGUI();
            playerProfileGUI.eventHandler(e);
        }

        if (invName.contains("Skill Levels")){
            LevelsGUI levels = new LevelsGUI();
            levels.eventHandler(e);
        }

//        if (invName.contains(ChatColor.DARK_GRAY + "Hats Page ")) {
//
//            Hats hat = new Hats();
//
//            hat.hatGuiEventHandler(e);
//
//        }

        // needs to be moved \/ \/ \/
        else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Select Generated Blocks")) {
            e.setCancelled(true);

            ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem == null || clickedItem.getType().isAir() || clickedItem.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;


            if (clickedItem.getType().equals(Material.COBBLESTONE)){
                genEdit.stoneItem(player);
            } else if (clickedItem.getType().equals(Material.DIRT)) {
                genEdit.dirtItem(player);
            } else if (clickedItem.getType().equals(Material.OAK_PLANKS)) {
                genEdit.woodItem(player);
            } else if (clickedItem.getType().equals(Material.COAL)) {
                genEdit.miscItem(player);
            }
        } else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Select Stone Block")){
            e.setCancelled(true);

            ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem == null || clickedItem.getType().isAir() || clickedItem.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;

            if (worm.me.Server.stones.containsKey(clickedItem.getType())){

                if (Unlocks.hasGenUnlocked(player, clickedItem.getType())){
                    PersistentDataContainer container = player.getPersistentDataContainer();
                    container.set(genEdit.stoneBlock, PersistentDataType.STRING, clickedItem.getType().toString());
                    player.sendMessage(ChatColor.GREEN + "Your stone block has been changed to " + clickedItem.getTranslationKey());
                }
            } else if (clickedItem.getType().equals(Material.ARROW)) genEdit.genOptions(player);
        } else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Select Wood Block")){
            e.setCancelled(true);

            ItemStack clickedItem = e.getCurrentItem();

            if (worm.me.Server.planks.containsKey(clickedItem.getType())){

                if (Unlocks.hasGenUnlocked(player, clickedItem.getType())){
                    PersistentDataContainer container = player.getPersistentDataContainer();
                    container.set(genEdit.woodBlock, PersistentDataType.STRING, clickedItem.getType().toString());
                    player.sendMessage(ChatColor.GREEN + "Your wood block has been changed to " + clickedItem.getTranslationKey());
                }

            } else if (clickedItem.getType().equals(Material.ARROW)) genEdit.genOptions(player);
        } else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Select Loose Block")){
            e.setCancelled(true);

            ItemStack clickedItem = e.getCurrentItem();

            if (worm.me.Server.dirts.containsKey(clickedItem.getType())){


                if (Unlocks.hasGenUnlocked(player, clickedItem.getType())) {
                    PersistentDataContainer container = player.getPersistentDataContainer();
                    container.set(genEdit.dirtBlock, PersistentDataType.STRING, clickedItem.getType().toString());
                    player.sendMessage(ChatColor.GREEN + "Your loose block has been changed to " + clickedItem.getTranslationKey());
                }
            } else if (clickedItem.getType().equals(Material.ARROW)) genEdit.genOptions(player);
        } else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Select Miscellaneous Item")){
            e.setCancelled(true);

            ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem == null || clickedItem.getType().isAir() || clickedItem.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;

            if (Server.miscs.containsKey(clickedItem.getType())){

                if (Unlocks.hasGenUnlocked(player, clickedItem.getType())){
                    PersistentDataContainer container = player.getPersistentDataContainer();
                    container.set(genEdit.miscItem, PersistentDataType.STRING, clickedItem.getType().toString());
                    player.sendMessage(ChatColor.GREEN + "Your misc. item has been changed to " + clickedItem.getTranslationKey());
                }
            } else if (clickedItem.getType().equals(Material.ARROW)) genEdit.genOptions(player);
        } else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Quests")){
            e.setCancelled(true);

        }

        if (e.isCancelled()) ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, ThreadLocalRandom.current().nextFloat(1.0F, 1.2F));

    }
}