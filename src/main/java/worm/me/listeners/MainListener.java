package worm.me.listeners;

import com.sk89q.worldguard.bukkit.event.inventory.UseItemEvent;
import net.coreprotect.CoreProtectAPI;
import net.kyori.adventure.util.TriState;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import worm.me.GuiConstructor;
import worm.me.Server;
import worm.me.gamesystems.fishing.FishingTables;
import worm.me.gamesystems.ArtBlocks;
import worm.me.gamesystems.recipes.RecipesGUI;
import worm.me.gamesystems.worldreset.WorldReset;
import worm.me.playersystems.*;
import worm.me.playersystems.abilities.AbilitiesGUI;
import worm.me.playersystems.abilities.Abilities;
import worm.me.playersystems.abilities.AbilityAttemptEvent;
import worm.me.playersystems.abilities.active.FeatherDash;
import worm.me.playersystems.abilities.active.SpeedStep;
import worm.me.playersystems.abilities.passive.SlimeBoots;
import worm.me.playersystems.data.tools.Tools;
import worm.me.playersystems.data.tools.Upgrade;
import worm.me.playersystems.generator.Generator;
import worm.me.playersystems.generator.GeneratorEdit;
import worm.me.playersystems.data.*;
import worm.me.playersystems.data.levels.GainExpEvent;
import worm.me.playersystems.data.levels.LevelUpEvent;
import worm.me.playersystems.data.levels.Levels;
import worm.me.playersystems.data.levels.LevelsGUI;
import worm.me.playersystems.data.social.hats.Hats;
import worm.me.playersystems.data.social.hats.HatsStorage;
import worm.me.playersystems.quests.QuestGui;
import worm.me.playersystems.data.social.MessageHead;
import worm.me.playersystems.data.social.skins.SkinGui;
import org.bukkit.event.player.PlayerJoinEvent;
import worm.me.playersystems.data.tools.ToolsEvents;
import worm.me.playersystems.quests.QuestProgressEvent;
import worm.me.playersystems.quests.Quests;
import worm.me.playersystems.saddle.SaddleSystem;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MainListener implements org.bukkit.event.Listener {

    private final worm.me.Server plugin;

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
    public void onInventoryOpen(InventoryOpenEvent e){
        if (e.getInventory().getType().equals(InventoryType.ENDER_CHEST)){
            e.setCancelled(true);
            Vault.openVault((Player) e.getPlayer());
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){

        Player p = (Player) e.getPlayer();

        if (e.getView().getTitle().equals(ChatColor.DARK_PURPLE + "Vault")){

            ArrayList<ItemStack> prunedItems = new ArrayList<>();

            Arrays.stream(p.getOpenInventory().getTopInventory().getContents())
                    .filter(Objects::nonNull).forEach(prunedItems::add);

            Vault.storeItems(prunedItems, p);
        }

        if (e.getView().getTitle().contains(ChatColor.RED + "Hat Storage ")){

            HatsStorage hatsStorage = new HatsStorage();
            hatsStorage.eventHandler(e);


        }

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){

        if (!e.getView().getType().equals(InventoryType.PLAYER) && e.getAction().equals(InventoryAction.HOTBAR_SWAP)) e.setCancelled(true);

        if (Objects.requireNonNullElse(e.getClickedInventory(), Bukkit.createInventory(e.getWhoClicked(), InventoryType.CHEST)).getType() == InventoryType.PLAYER && (e.getAction().equals(InventoryAction.SWAP_WITH_CURSOR) || e.getAction().equals(InventoryAction.COLLECT_TO_CURSOR)) &&
                !Tools.toolType(Objects.requireNonNullElse(e.getCurrentItem(), new ItemStack(Material.STONE)).getType()).equals("none") && e.getWhoClicked().getInventory().firstEmpty() == -1) e.setCancelled(true);
        if (Objects.requireNonNullElse(e.getClickedInventory(), Bukkit.createInventory(e.getWhoClicked(), InventoryType.CHEST)).getType() != InventoryType.PLAYER && !Tools.toolType(e.getCursor().getType()).equals("none")) e.setCancelled(true);

        if (!Tools.toolType(Objects.requireNonNullElse(e.getCurrentItem(), new ItemStack(Material.STONE)).getType()).equals("none") && (e.getClickedInventory().getType() == InventoryType.PLAYER && e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) ||
                (e.getAction().equals(InventoryAction.HOTBAR_SWAP) && e.getClickedInventory().getType() != InventoryType.PLAYER) && !Tools.toolType(e.getWhoClicked().getInventory().getItem(e.getHotbarButton()).getType()).equals("none")) e.setCancelled(true);

        if (Objects.requireNonNullElse(e.getClickedInventory(), Bukkit.createInventory(e.getWhoClicked(), InventoryType.CHEST)).getType().equals(InventoryType.PLAYER)) {
            if (Objects.requireNonNull(e.getCurrentItem()).getType().equals(Material.WOODEN_AXE) || e.getCurrentItem().getType().equals(Material.WOODEN_PICKAXE) ||
                    e.getCurrentItem().getType().equals(Material.WOODEN_SWORD) || e.getCurrentItem().getType().equals(Material.WOODEN_SHOVEL)){
                if (Objects.requireNonNull(e.getCursor()).getType() == Upgrade.WOOD_TOOL.getUpgradeItem().getType() && e.getCursor().getAmount() >= Upgrade.WOOD_TOOL.getUpgradeItem().getAmount()){
                    e.setCancelled(true);
                    e.getCursor().setAmount(e.getCursor().getAmount() - Upgrade.WOOD_TOOL.getUpgradeItem().getAmount());
                    ToolsEvents toolsEvents = new ToolsEvents();
                    toolsEvents.toolUpgradeAttempt((Player) e.getWhoClicked(), e);
                }

            } else if (Objects.requireNonNull(e.getCurrentItem().getType()).equals(Material.STONE_AXE) || e.getCurrentItem().getType().equals(Material.STONE_PICKAXE) ||
                    e.getCurrentItem().getType().equals(Material.STONE_SWORD) || e.getCurrentItem().getType().equals(Material.STONE_SHOVEL)){
                if (Objects.requireNonNull(e.getCursor()).getType() == Upgrade.STONE_TOOL.getUpgradeItem().getType() && e.getCursor().getAmount() >= Upgrade.STONE_TOOL.getUpgradeItem().getAmount()){
                    e.setCancelled(true);
                    e.getCursor().setAmount(e.getCursor().getAmount() - Upgrade.STONE_TOOL.getUpgradeItem().getAmount());
                    ToolsEvents toolsEvents = new ToolsEvents();
                    toolsEvents.toolUpgradeAttempt((Player) e.getWhoClicked(), e);
                }

            }
        }


        if (Objects.requireNonNullElse(e.getCurrentItem(), new ItemStack(Material.AIR)).getType().isAir()) return;

        Player p = (Player) e.getWhoClicked();

        if (!p.getPassengers().isEmpty() && p.getPassengers().getFirst().getType() != EntityType.PLAYER){
            e.setCancelled(true);
        }

        String tool = e.getCursor().getType().toString().toLowerCase();
        for (String s : worm.me.Server.tools.keySet()){
            if (tool.contains(s)){
                if (e.getClickedInventory().getType() != InventoryType.PLAYER) e.setCancelled(true);
                break;
            }
        }

        GeneratorEdit genEdit = new GeneratorEdit();

        InventoryView inv = e.getView();
        String invName = inv.getTitle();

        if (e.getSlotType() == InventoryType.SlotType.ARMOR){
        }

        if (e.getCurrentItem().isSimilar(GuiConstructor.createGuiItem(Material.BARRIER, ChatColor.RED + "Close Menu", " ", ChatColor.GRAY + "Close this menu."))) p.closeInventory();

        if (e.getCurrentItem().getType() == Material.SPECTRAL_ARROW && e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GRAY + "Go Back")){
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
            abilitiesGUI.initializeAbilitySelection(p, e);;
        }

        if (invName.contains("Ability Slot")) {
            e.setCancelled(true);
            AbilitiesGUI abilitiesGUI = new AbilitiesGUI();
            abilitiesGUI.activateAbility(p, e.getSlot(), e);
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
            SkinGui.skinGuiDo(e, plugin);
        }

        if (invName.contains("Profile")){
            PlayerProfileGUI playerProfileGUI = new PlayerProfileGUI();
            playerProfileGUI.eventHandler(e);
        }

        if (invName.contains("Skill Levels")){
            LevelsGUI levels = new LevelsGUI();
            levels.eventHandler(e);
        }

        if (invName.contains(ChatColor.DARK_GRAY + "Hats Page ")) {

            Hats hat = new Hats();

            hat.hatGuiEventHandler(e);

        }

        // needs to be moved \/ \/ \/
        else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Select Generated Blocks")) {
            e.setCancelled(true);

            ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem == null || clickedItem.getType().isAir() || clickedItem.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;


            if (clickedItem.getType().equals(Material.COBBLESTONE)){
                genEdit.stoneItem(p);
            } else if (clickedItem.getType().equals(Material.DIRT)) {
                genEdit.dirtItem(p);
            } else if (clickedItem.getType().equals(Material.OAK_PLANKS)) {
                genEdit.woodItem(p);
            } else if (clickedItem.getType().equals(Material.COAL)) {
                genEdit.miscItem(p);
            }
        } else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Select Stone Block")){
            e.setCancelled(true);

            ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem == null || clickedItem.getType().isAir() || clickedItem.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;

            if (worm.me.Server.stones.containsKey(clickedItem.getType())){

                if (Unlocks.hasGenUnlocked(p, clickedItem.getType())){
                    PersistentDataContainer container = p.getPersistentDataContainer();
                    container.set(genEdit.stoneBlock, PersistentDataType.STRING, clickedItem.getType().toString());
                    p.sendMessage(ChatColor.GREEN + "Your stone block has been changed to " + clickedItem.getTranslationKey());
                }
            } else if (clickedItem.getType().equals(Material.ARROW)) genEdit.genOptions(p);
        } else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Select Wood Block")){
            e.setCancelled(true);

            ItemStack clickedItem = e.getCurrentItem();

            if (worm.me.Server.planks.containsKey(clickedItem.getType())){

                if (Unlocks.hasGenUnlocked(p, clickedItem.getType())){
                    PersistentDataContainer container = p.getPersistentDataContainer();
                    container.set(genEdit.woodBlock, PersistentDataType.STRING, clickedItem.getType().toString());
                    p.sendMessage(ChatColor.GREEN + "Your wood block has been changed to " + clickedItem.getTranslationKey());
                }

            } else if (clickedItem.getType().equals(Material.ARROW)) genEdit.genOptions(p);
        } else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Select Loose Block")){
            e.setCancelled(true);

            ItemStack clickedItem = e.getCurrentItem();

            if (worm.me.Server.dirts.containsKey(clickedItem.getType())){


                if (Unlocks.hasGenUnlocked(p, clickedItem.getType())) {
                    PersistentDataContainer container = p.getPersistentDataContainer();
                    container.set(genEdit.dirtBlock, PersistentDataType.STRING, clickedItem.getType().toString());
                    p.sendMessage(ChatColor.GREEN + "Your loose block has been changed to " + clickedItem.getTranslationKey());
                }
            } else if (clickedItem.getType().equals(Material.ARROW)) genEdit.genOptions(p);
        } else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Select Miscellaneous Item")){
            e.setCancelled(true);

            ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem == null || clickedItem.getType().isAir() || clickedItem.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;

            if (Server.miscs.containsKey(clickedItem.getType())){

                if (Unlocks.hasGenUnlocked(p, clickedItem.getType())){
                    PersistentDataContainer container = p.getPersistentDataContainer();
                    container.set(genEdit.miscItem, PersistentDataType.STRING, clickedItem.getType().toString());
                    p.sendMessage(ChatColor.GREEN + "Your misc. item has been changed to " + clickedItem.getTranslationKey());
                }
            } else if (clickedItem.getType().equals(Material.ARROW)) genEdit.genOptions(p);
        } else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Quests")){
            e.setCancelled(true);

        }

        if (e.isCancelled()) ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, ThreadLocalRandom.current().nextFloat(1.0F, 1.2F));

    }
}