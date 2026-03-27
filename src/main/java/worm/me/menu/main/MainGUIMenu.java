package worm.me.menu.main;

import org.bukkit.Location;
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
import worm.me.gamesystems.world.PlayArea;
import worm.me.gamesystems.world.border.ZoneGridManager;
import worm.me.menu.Menu;
import worm.me.menu.PlayerProfileMenu;
import worm.me.menu.recipes.RecipeBrowserMenu;
import worm.me.utils.menu.GuiConstructor;
import worm.me.menu.abilities.AbilitiesMainMenu;
import worm.me.menu.generators.GensMainMenu;
import worm.me.Server;
import worm.me.utils.Item;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;
import worm.me.utils.info.GuideBook;
import worm.me.utils.menu.MenuItem;

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
        Inventory inventory = GuiConstructor.chestInventory("<dark_gray>Main Menu", 36, this);

        ItemStack skull = Item.create(Material.PLAYER_HEAD, "<aqua>Your Profile", " ", "<gray>View your stats and progress.");
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(getViewer());
        skull.setItemMeta(skullMeta);
        inventory.setItem(13, skull);

        inventory.setItem(15, Item.create(Material.SCULK_SHRIEKER, "<aqua>Voices", " ", "<gray>Open the voices menu."));

        inventory.setItem(20, createResetInfoBook(getViewer()));
        inventory.setItem(21, Item.create(Material.COBBLESTONE, "<aqua>Generators", " ", "<gray>Open the generators menu."));
        inventory.setItem(22, Item.create(Material.HEART_OF_THE_SEA, "<aqua>Abilities", " ", "<gray>Open the abilities menu."));
        inventory.setItem(23, Item.create(Material.CRAFTING_TABLE, "<aqua>Recipes", " ", "<gray>Open the recipes menu."));

        inventory.setItem(0, Item.create(Material.KNOWLEDGE_BOOK, "<blue>\uD83D\uDEC8", " ",
                "<gray>This is the main menu of the server;",
                "<gray>from here you can access all the other menus.",
                "<gray>If you're unsure where to start, try",
                "<gray>reading the tutorial book!"));

        inventory.setItem(31, MenuItem.CLOSE.getItem());

        return inventory;
    }

    @Override
    public void open() {
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
                        e.getInventory().setItem(e.getSlot(), createResetInfoBook(player));
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

    public static ItemStack createResetInfoBook(Player player) {
        ZoneGridManager gridManager = Server.getZoneGridManager();
        int[][] grid = gridManager.getGrid();
        WorldTimer timer = WorldTimer.getInstance();
        long expansionSecs = timer.secondsUntilNextExpansion();
        long resetSecs = timer.secondsUntilReset();

        String timeTillExpansion = (expansionSecs < 0)
                ? "<red>Grid is fully open!"
                : formatTime(expansionSecs);
        String worldResetTime = (resetSecs < 0)
                ? "<red>Not yet active"
                : formatTime(resetSecs);

        int playerRow = -1;
        int playerCol = -1;
        Location playerLoc = player.getLocation();
        if (playerLoc.getWorld() == PlayArea.getPlayWorld()) {
            int start = -(gridManager.getGridSize() * gridManager.getCellSize()) / 2;
            int pCol = (playerLoc.getBlockX() - start) / gridManager.getCellSize();
            int pRow = (playerLoc.getBlockZ() - start) / gridManager.getCellSize();
            if (pRow >= 0 && pRow < grid.length && pCol >= 0 && pCol < grid[0].length) {
                playerRow = pRow;
                playerCol = pCol;
            }
        }

        List<String> lore = new ArrayList<>();
        lore.add("<gray>World Reset Time:");
        lore.add("<white>" +worldResetTime);
        lore.add(" ");
        lore.add("<gray>Time Till Expansion:");
        lore.add("<white>" +timeTillExpansion);
        lore.add(" ");
        lore.add("<dark_gray>Grid Status:");

        lore.add("<black>W" +"<yellow>      N");

        for (int row = 0; row < grid.length; row++) {
            StringBuilder line = new StringBuilder();

            if (row == grid.length / 2) {
                line.append("<yellow>").append("W ");
            } else {
                line.append("<black>").append("W ");
            }

            for (int col = 0; col < grid[row].length; col++) {
                if (row == playerRow && col == playerCol) {
                    line.append("<blue>").append("■ ");
                } else if (grid[row][col] == 1) {
                    line.append("<green>").append("■ ");
                } else {
                    line.append("<red>").append("■ ");
                }
            }

            if (row == grid.length / 2) {
                line.append("<yellow>").append("E");
            }

            lore.add(line.toString());
        }

        lore.add("<black>W" +"<yellow>      S");

        lore.add(" ");

        ItemStack book = Item.create(
                Material.WRITTEN_BOOK,
                "<aqua>Border and World Reset Info",
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