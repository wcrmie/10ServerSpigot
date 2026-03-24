package worm.me.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import worm.me.gamesystems.world.WorldTimer;
import worm.me.gamesystems.world.border.ZoneGridManager;
import worm.me.menu.recipes.RecipeBrowserMenu;
import worm.me.utils.menu.GuiConstructor;
import worm.me.menu.abilities.AbilitiesMainMenu;
import worm.me.menu.generators.GensMainMenu;
//import worm.me.playersystems.social.legacyhats.Hats;
//import worm.me.playersystems.social.skins.SkinGui;
import worm.me.Server;
import worm.me.utils.Item;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;
import worm.me.utils.info.GuideBook;

import java.util.ArrayList;
import java.util.List;

public class MainGUIMenu extends Menu implements InventoryHolder {

    private final Inventory inventory;


    public MainGUIMenu(Player viewer) {
        super(viewer);
        this.inventory = initialize();

    }

    @Override
    public Inventory initialize() {

        Inventory inventory = GuiConstructor.chestInventory(ChatColor.DARK_GRAY + "Main Menu", 36, this);

        // Add items to the GUI
        inventory.setItem(11, Item.create(Material.BOOK, ChatColor.AQUA + "Tutorial Book", " ", ChatColor.GRAY + "Read this book to learn about the server!"));
        inventory.setItem(12, Item.create(Material.LEATHER_CHESTPLATE, ChatColor.AQUA + "Skin Selection", " ", ChatColor.GRAY + "Open the skin selection menu."));

        // Set player head item to the player's head
        ItemStack skull = Item.create(Material.PLAYER_HEAD, ChatColor.AQUA + "Your Profile", " ", ChatColor.GRAY + "View your stats and progress.");
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(getViewer());
        skull.setItemMeta(skullMeta);

        inventory.setItem(13, skull);
        inventory.setItem(14, Item.create(Material.GOLDEN_HELMET, ChatColor.AQUA + "Hats", " ", ChatColor.GRAY + "Open the hats menu."));
        inventory.setItem(15, Item.create(Material.SCULK_SHRIEKER, ChatColor.AQUA + "Voices", " ", ChatColor.GRAY + "Open the voices menu."));
        inventory.setItem(20, createResetInfoBook());
        inventory.setItem(21, Item.create(Material.COBBLESTONE, ChatColor.AQUA + "Generators", " ", ChatColor.GRAY + "Open the generators menu."));
        inventory.setItem(22, Item.create(Material.HEART_OF_THE_SEA, ChatColor.AQUA + "Abilities", " ", ChatColor.GRAY + "Open the abilities menu."));
        inventory.setItem(23, Item.create(Material.CRAFTING_TABLE, ChatColor.AQUA + "Recipes", " ", ChatColor.GRAY + "Open the recipes menu."));

        inventory.setItem(0, Item.create(Material.KNOWLEDGE_BOOK, ChatColor.BLUE + "\uD83D\uDEC8", " ", ChatColor.GRAY + "This is the main menu of the server;", ChatColor.GRAY + "from here you can access all the other menus.", ChatColor.GRAY + "If you're unsure where to start, try" , ChatColor.GRAY + "reading the tutorial book!"));

        inventory.setItem(31, Item.create(Material.BARRIER, ChatColor.RED + "Close Menu", " ", ChatColor.GRAY + "Close this menu."));

        return inventory;
    }

    @Override
    public void open(){

        getViewer().openInventory(inventory);

    }

    @Override
    public void handleClick(InventoryClickEvent e) {
        e.setCancelled(true);

        Player player = (Player) e.getWhoClicked();

        switch (e.getSlot()) {

            case 11 -> {
                GuideBook.guideBook(player);
            }
            case 12 -> {
                player.sendMessage(Message.create(MessageType.FAIL, "This feature is no longer available."));
            }
            case 13 -> {
                PlayerProfileMenu playerProfileMenu = new PlayerProfileMenu(player, player);
                playerProfileMenu.open();
            }
            case 14 -> {
                player.sendMessage(Message.create(MessageType.FAIL, "Hats are no longer in this menu. To put something on your head, use /hat."));
            }
            case 15 -> {
                player.sendMessage(Message.create(MessageType.FAIL, "This feature is not yet available."));
            }
            case 20 -> {
                ItemStack clickedItem = e.getCurrentItem();
                if (clickedItem != null && clickedItem.hasItemMeta()) {
                    ItemMeta meta = clickedItem.getItemMeta();
                    if (meta.getPersistentDataContainer().has(new NamespacedKey(Server.getPlugin(), "reset_info_book"), PersistentDataType.STRING)) {
                        e.getInventory().setItem(e.getSlot(), createResetInfoBook());
                    }
                }
            }
            case 21 -> {
                GensMainMenu gensMainMenu = new GensMainMenu(player);
                gensMainMenu.open();
            }
            case 22 -> {
                AbilitiesMainMenu abilitiesGUI = new AbilitiesMainMenu(player);
                abilitiesGUI.open();
            }
            case 23 -> {
                RecipeBrowserMenu recipeBrowserMenu = new RecipeBrowserMenu(player, 1);
                recipeBrowserMenu.open();
            }
            case 31 -> {
                player.closeInventory();
            }


        }
    }

    public static ItemStack createResetInfoBook() {
        ZoneGridManager gridManager = Server.getZoneGridManager();
        int[][] grid = gridManager.getGrid();

        WorldTimer timer = WorldTimer.getInstance();

        long expansionSecs = timer.secondsUntilNextExpansion();
        long resetSecs = timer.secondsUntilReset();

        String timeTillExpansion = (expansionSecs < 0)
                ? ChatColor.RED + "Grid is fully open!"
                : formatTime(expansionSecs);

        String worldResetTime = (resetSecs < 0)
                ? ChatColor.RED + "Not yet active"
                : formatTime(resetSecs);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "World Reset Time:");
        lore.add(ChatColor.WHITE + worldResetTime);
        lore.add(" ");
        lore.add(ChatColor.GRAY + "Time Till Expansion:");
        lore.add(ChatColor.WHITE + timeTillExpansion);
        lore.add(" ");
        lore.add(ChatColor.DARK_GRAY + "Grid Status:");

        int center = grid.length / 2;
        for (int row = 0; row < grid.length; row++) {
            StringBuilder line = new StringBuilder();
            for (int col = 0; col < grid[row].length; col++) {
                if (row == center && col == center) {
                    line.append(ChatColor.AQUA).append("■ ");
                } else if (grid[row][col] == 1) {
                    line.append(ChatColor.GREEN).append("■ ");
                } else {
                    line.append(ChatColor.RED).append("■ ");
                }
            }
            lore.add(line.toString());
        }

        ItemStack book = Item.create(
                Material.WRITTEN_BOOK,
                ChatColor.AQUA + "" + "Border and World Reset Info",
                lore.toArray(new String[0])
        );
        ItemMeta meta = book.getItemMeta();
        meta.getPersistentDataContainer().set(
                new NamespacedKey(Server.getPlugin(), "reset_info_book"),
                PersistentDataType.STRING,
                "true"
        );
        book.setItemMeta(meta);
        return book;
    }

    private static String formatTime(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
