package worm.me.playersystems.info;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import worm.me.gamesystems.world.border.WorldBorderSizeHandler;
import worm.me.gamesystems.recipes.RecipesGUI;
import worm.me.utils.GuiConstructor;
import worm.me.gamesystems.world.reset.WorldReset;
import worm.me.playersystems.abilities.AbilitiesGUI;
import worm.me.playersystems.generator.GeneratorEdit;
import worm.me.playersystems.data.PlayerProfileGUI;
//import worm.me.playersystems.social.legacyhats.Hats;
//import worm.me.playersystems.social.skins.SkinGui;
import worm.me.Server;
import worm.me.utils.Item;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

public class MainGUIMenu {

    public static void openMainMenu(Player p){
        // Open the main menu GUI

        Inventory gui = GuiConstructor.chestInventory(ChatColor.DARK_GRAY + "Main Menu", 36);

        // Add items to the GUI
        gui.setItem(11, Item.create(Material.BOOK, ChatColor.AQUA + "Tutorial Book", " ", ChatColor.GRAY + "Read this book to learn about the server!"));
        gui.setItem(12, Item.create(Material.LEATHER_CHESTPLATE, ChatColor.AQUA + "Skin Selection", " ", ChatColor.GRAY + "Open the skin selection menu."));

        // Set player head item to the player's head
        ItemStack skull = Item.create(Material.PLAYER_HEAD, ChatColor.AQUA + "Your Profile", " ", ChatColor.GRAY + "View your stats and progress.");
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(p);
        skull.setItemMeta(skullMeta);

        gui.setItem(13, skull);
        gui.setItem(14, Item.create(Material.GOLDEN_HELMET, ChatColor.AQUA + "Hats", " ", ChatColor.GRAY + "Open the hats menu."));
        gui.setItem(15, Item.create(Material.SCULK_SHRIEKER, ChatColor.AQUA + "Voices", " ", ChatColor.GRAY + "Open the voices menu."));
        gui.setItem(20, createResetInfoBook(p));
        gui.setItem(21, Item.create(Material.COBBLESTONE, ChatColor.AQUA + "Generators", " ", ChatColor.GRAY + "Open the generators menu."));
        gui.setItem(22, Item.create(Material.HEART_OF_THE_SEA, ChatColor.AQUA + "Abilities", " ", ChatColor.GRAY + "Open the abilities menu."));
        gui.setItem(23, Item.create(Material.CRAFTING_TABLE, ChatColor.AQUA + "Recipes", " ", ChatColor.GRAY + "Open the recipes menu."));

        gui.setItem(0, Item.create(Material.KNOWLEDGE_BOOK, ChatColor.BLUE + "\uD83D\uDEC8", " ", ChatColor.GRAY + "This is the main menu of the server;", ChatColor.GRAY + "from here you can access all the other menus.", ChatColor.GRAY + "If you're unsure where to start, try" , ChatColor.GRAY + "reading the tutorial book!"));

        gui.setItem(31, Item.create(Material.BARRIER, ChatColor.RED + "Close Menu", " ", ChatColor.GRAY + "Close this menu."));

        p.openInventory(gui);
    }

    public void eventHandler(InventoryClickEvent e) {
        e.setCancelled(true);

        Player p = (Player) e.getWhoClicked();

        // Rest of your existing click handlers
        if (e.getSlot() == 11) {
            GuideBook.guideBook(p);
        } else if (e.getSlot() == 12) {
            p.sendMessage(Message.create(MessageType.FAIL, "This feature is no longer available."));
        } else if (e.getSlot() == 13) {
            PlayerProfileGUI playerProfileGUI = new PlayerProfileGUI();
            playerProfileGUI.openProfile(p, p);
        } else if (e.getSlot() == 14) {
            p.sendMessage(Message.create(MessageType.FAIL, "Hats are no longer in this menu. To put something on your head, use /hat."));
        } else if (e.getSlot() == 15) {
            p.sendMessage(Message.create(MessageType.FAIL, "This feature is not yet available."));
        } else if (e.getSlot() == 20) {
            ItemStack clickedItem = e.getCurrentItem();
            if (clickedItem != null && clickedItem.hasItemMeta()) {
                ItemMeta meta = clickedItem.getItemMeta();
                if (meta.getPersistentDataContainer().has(new NamespacedKey(Server.getPlugin(), "reset_info_book"), PersistentDataType.STRING)) {
                    e.getInventory().setItem(e.getSlot(), createResetInfoBook(p));
                }
            }
        } else if (e.getSlot() == 21) {
            GeneratorEdit generatorEdit = new GeneratorEdit();
            generatorEdit.genOptions(p);
        } else if (e.getSlot() == 22) {
            AbilitiesGUI abilitiesGUI = new AbilitiesGUI();
            abilitiesGUI.abilitiesActive(p);
        } else if (e.getSlot() == 23) {
            RecipesGUI recipesGUI = new RecipesGUI();
            recipesGUI.openMainGUI(p, 1);
        } else if (e.getSlot() == 31) {
            p.closeInventory();
        }
    }

    public static ItemStack createResetInfoBook(Player p) {
        WorldBorderSizeHandler borderHandler = Server.getPlugin().getWorldBorderHandler();
        WorldReset worldReset = WorldReset.getInstance();

        long currentBlocks = borderHandler.getBlockCount();
        double currentSize = borderHandler.getWorldBorderSize();

        double percentageToReset = (double) ((long) worldReset.resetScore() / worldReset.scoreNeededForReset()) *100;

        long blocksForNextIncrease = (long) (borderHandler.calculateWorldBlockSize() *
                (borderHandler.getExpansionPercentage() / 100.0));
        long blocksRemainingToNext = Math.max(0, blocksForNextIncrease - (currentBlocks % blocksForNextIncrease));
        double percentageToNext = blocksForNextIncrease == 0 ? 0 :
                (double) (currentBlocks % blocksForNextIncrease) / blocksForNextIncrease * 100;

        String timerStatus = worldReset.projectedTimeTillReset() > 0 ?
                ChatColor.GRAY + "Time until reset: " + ChatColor.WHITE + formatTime((long) (worldReset.projectedTimeTillReset())) :
                ChatColor.GRAY + "No timer active";

        ItemStack book = Item.create(
                Material.WRITTEN_BOOK,
                ChatColor.AQUA + "Reset Info",
                " ",
                ChatColor.DARK_AQUA + "" + ChatColor.UNDERLINE + "World Reset",
                timerStatus,
                ChatColor.GRAY + "Progress: " + ChatColor.WHITE + (long) worldReset.resetScore() + "/" + (long) worldReset.scoreNeededForReset(),
                ChatColor.GRAY + "Completed: " + ChatColor.WHITE + String.format("%.1f", percentageToReset) + "%",
                " ",
                ChatColor.DARK_AQUA + "" + ChatColor.UNDERLINE + "Border Size",
                ChatColor.GRAY + "Current: " + ChatColor.WHITE + String.format("%.1f", currentSize),
                ChatColor.GRAY + "Next at: " + ChatColor.WHITE + (blocksForNextIncrease - blocksRemainingToNext) + "/" + blocksForNextIncrease,
                ChatColor.GRAY + "Blocks needed for Next: " + ChatColor.WHITE + (blocksRemainingToNext),
                ChatColor.GRAY + "Progress: " + ChatColor.WHITE + String.format("%.1f", percentageToNext) + "%"
        );

        ItemMeta meta = book.getItemMeta();
        meta.getPersistentDataContainer().set(
                new NamespacedKey(Server.getPlugin(), "reset_info_book"),
                PersistentDataType.STRING, "true"
        );
        book.setItemMeta(meta);

        return book;
    }

    private static long calculateTotalBlocksForReset(WorldBorderSizeHandler borderHandler) {
        double expansionPercentage = borderHandler.getExpansionPercentage() / 100.0;
        return (long) ((201 * 201 * 60) * expansionPercentage);
    }

    private static String formatTime(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

}
