package worm.me;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.event.inventory.UseItemEvent;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import net.skinsrestorer.api.exception.DataRequestException;
import net.skinsrestorer.api.exception.MineSkinException;
import net.skinsrestorer.api.property.InputDataResult;
import net.skinsrestorer.api.storage.PlayerStorage;
import net.skinsrestorer.api.storage.SkinStorage;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import worm.me.GameSystems.FishingSystem;
import worm.me.GameSystems.artBlocks;
import worm.me.PlayerSystems.*;
import worm.me.PlayerSystems.Abilities.AbilitiesGUI;
import worm.me.PlayerSystems.Abilities.AbilitiesMain;
import worm.me.PlayerSystems.Abilities.Active.AgilityDash;
import worm.me.PlayerSystems.Abilities.Active.AgilityDoubleJump;
import worm.me.PlayerSystems.Abilities.Active.AgilityLeap;
import worm.me.PlayerSystems.Quests.QuestAssign;
import worm.me.PlayerSystems.Quests.QuestGui;
import worm.me.PlayerSystems.Social.MessageHead;
import worm.me.PlayerSystems.Social.PlayerReputation;
import worm.me.PlayerSystems.Social.Skin.SkinGui;
import org.bukkit.event.player.PlayerJoinEvent;
import worm.me.PlayerSystems.Tools.ToolsEvents;
import worm.me.WorldBorder.worldBorderSizeHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class Listener implements org.bukkit.event.Listener {

    private final server plugin;

    private final Map<Player, Boolean> doubleJumpMap = new HashMap<>();

    public Listener(server plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        FishingSystem handler = new FishingSystem(plugin);
        handler.handleFishing(event);
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        worldBorderSizeHandler handler = new worldBorderSizeHandler(plugin);
        handler.incrementBlockCount();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        worldBorderSizeHandler handler = new worldBorderSizeHandler(plugin);
        handler.decrementBlockCount();
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        PlayerSpawning handler = new PlayerSpawning(plugin);
        handler.playerSpawn(event.getPlayer());
    }

    @EventHandler
    public void onPlayerLeftClick(EntityDamageByEntityEvent event){

        saddleRide handler = new saddleRide(plugin);
        handler.leftClick(event);
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEntityEvent event){

        saddleRide handler = new saddleRide(plugin);
        handler.rightClick(event);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

        // Set player variable
        Player player = event.getPlayer();
        MessageHead messageHead = new MessageHead(plugin);
        player.getPersistentDataContainer().set(messageHead.armorStandExists, PersistentDataType.BOOLEAN, false);

        // Give the player a random item every 10 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    if (hasEmptySlot(player)) player.getInventory().addItem(randomItem(player));
                } else cancel();
            }
        }.runTaskTimer(plugin, 200, 200);

        // Initialize player's levels, if none.
        PersistentDataContainer c = player.getPersistentDataContainer();
        Level level = new Level(plugin);
        if (!c.has(level.mining)){
            level.initLevel(player);
        }

        // Change player's skin
        SkinStorage storage = plugin.skinsRestorer.getSkinStorage();
        try {

            int RNG = ThreadLocalRandom.current().nextInt(1, 4);

            Optional<InputDataResult> result = storage.findOrCreateSkinData("worrm");

            if (RNG == 1){
                result = storage.findOrCreateSkinData("goobier");
            } else if (RNG == 2){
                result = storage.findOrCreateSkinData("snowmaam");
            } else if (RNG == 3) {
                result = storage.findOrCreateSkinData("worrm");
            }


            if (result.isEmpty()){
                player.sendMessage("Skin could not be loaded.");
                return;
            }

            PlayerStorage playerStorage = plugin.skinsRestorer.getPlayerStorage();

            playerStorage.setSkinIdOfPlayer(player.getUniqueId(), result.get().getIdentifier());

            plugin.skinsRestorer.getSkinApplier(Player.class).applySkin(player);
        } catch (DataRequestException | MineSkinException e) {
            throw new RuntimeException(e);
        }

        // Hide the player's nametag
        if (!player.isOp()) {
            Scoreboard score = Bukkit.getScoreboardManager().getMainScoreboard();
            Team nHide = score.getTeam("hiddenName");
            if (nHide == null) {
                nHide = score.registerNewTeam("hiddenName");
                nHide.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OWN_TEAM);
            }
            nHide.addEntry(player.getName());
        }

        // Assign quests
        QuestAssign questAssign = new QuestAssign(plugin);
        questAssign.dailyCheck(player);


    }


    // Check if the player has an empty slot
    public boolean hasEmptySlot(Player player) {
        Inventory inventory = player.getInventory();
        for (ItemStack item : inventory.getContents()) {
            if (item == null || item.getType() == Material.AIR) {
                return true;
            }
        }
        return false;
    }

    // Declares randomItem variable
    private Material randomItem;

    // Allows the setting of a random item.
    private void setRandomItem(Material m){
        randomItem = m;
    }

    // Generates the random item the player is given
    public ItemStack randomItem(Player p){
        PersistentDataContainer container = p.getPersistentDataContainer();
        GeneratorEdit genEdit = new GeneratorEdit();
        int RNG = ThreadLocalRandom.current().nextInt(0,100);
        if (1 <= RNG && RNG <= 45) {
            setRandomItem(plugin.stones.get(Objects.requireNonNullElse(container.get(genEdit.stoneBlock, PersistentDataType.INTEGER), 0)));
        } else if (45 < RNG && RNG <= 60) {
            setRandomItem(plugin.dirts.get(Objects.requireNonNullElse(container.get(genEdit.dirtBlock, PersistentDataType.INTEGER), 0)));
        } else if (60 < RNG && RNG <= 75) {
            setRandomItem(plugin.planks.get(Objects.requireNonNullElse(container.get(genEdit.woodBlock, PersistentDataType.INTEGER), 0)));
        } else if (75 < RNG && RNG <= 90) {
            setRandomItem(plugin.miscs.get(Objects.requireNonNullElse(container.get(genEdit.miscItem, PersistentDataType.INTEGER), 0)));
        } else {
            setRandomItem(Material.IRON_ORE);
        }
        return new ItemStack(randomItem, 1);
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent e){
        Player player = e.getPlayer();
        Level level = new Level(plugin);

        // Gives the player exp for mining certain blocks
        if (plugin.miningBlocks.containsKey(e.getBlock().getType())) {
            level.addExp(plugin.miningBlocks.get(e.getBlock().getType()), level.mining, player);
        }
    }

    @EventHandler
    public void onPlayerPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();

        // Height limit for building
        if (p.getGameMode() == GameMode.CREATIVE) return;
        if (Objects.requireNonNull(e.getBlock().getLocation().getWorld()).getName().equals("ul_Void_nether")) return;
        if (e.getBlock().getLocation().getBlockY() > 280 || e.getBlock().getLocation().getBlockY() < 220){
            e.setCancelled(true);
            p.sendMessage(ChatColor.RED + "The build area is between y = 220 and y = 280.");
        }
    }

    @EventHandler
    public void onChat(PlayerChatEvent event){

        // For messages above head
        MessageHead messageHead = new MessageHead(plugin);

        // initialize variables
        Player p = event.getPlayer();
        String m = event.getMessage();

        // message character limit

        PersistentDataContainer c = p.getPersistentDataContainer();

        if (m.length() > c.getOrDefault(messageHead.msgLength, PersistentDataType.INTEGER, 192)){
            p.sendMessage(ChatColor.RED + String.format("Your messages must be %d characters or less.", c.getOrDefault(messageHead.msgLength, PersistentDataType.INTEGER, 192)));
            event.setCancelled(true);
            return;
        }

        // queue messages to be sent above head
        messageHead.messageQueue(p, m);
        messageHead.messageDisplay(p, m);

        // Only ops can see chat logged (later will include moderators and such)
        event.getRecipients().clear();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp()) event.getRecipients().add(player);
        }
    }


    @EventHandler
    public void onPlayerCrouchEvent(PlayerToggleSneakEvent e){
        Player p = e.getPlayer();
        Location loc = p.getLocation();

        if (p.isFlying()) return;

        // If the block below the player is not air
        loc.setY(loc.getY() - 1);
        if (!loc.getBlock().getType().isAir()) return;

        // Check cooldown
        AbilitiesMain ability = new AbilitiesMain();
        PersistentDataContainer container = p.getPersistentDataContainer();
        long cooldown = Objects.requireNonNullElse(container.get(ability.moveCooldown, PersistentDataType.LONG), 0L);
        if (cooldown <= System.currentTimeMillis()/1000L){

            // Make the player double jump
            AgilityDoubleJump doubleJump = new AgilityDoubleJump();
            doubleJump.doubleJump(p);
        }
    }


    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e){

        // Kill the player if they walk into a deathbox (so they don't fall for a whole minute)
        if (e.getFrom() == e.getTo()) return;
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(e.getPlayer().getWorld()));
        ProtectedRegion deathBox = regions.getRegion("deathbox");
        if (deathBox.contains(e.getTo().getBlockX(), e.getTo().getBlockY(), e.getTo().getBlockZ())) e.getPlayer().damage(1000000);
    }

    @EventHandler
    public void onEnchantTable(InventoryOpenEvent e){
        if (!e.getInventory().getType().equals(InventoryType.ENCHANTING)) return;
        e.setCancelled(true);
        Player p = (Player) e.getPlayer();
        GuideBook guideBook = new GuideBook();
        guideBook.guideBook(p);
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerRightClick(UseItemEvent e){
        Player p = e.getCause().getFirstPlayer();

        if (p.getInventory().getItemInMainHand().toString().toLowerCase().contains("sword")) {
            if (p.isSneaking()){
                AgilityDash dash = new AgilityDash();
                dash.dash(p);
            }
        } else if (p.getInventory().getItemInMainHand().toString().toLowerCase().contains("_axe")) {
            if (p.isSneaking()){
                AgilityLeap leap = new AgilityLeap();
                leap.leap(p);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){

        if (e.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Player p = e.getPlayer();
        if (Objects.requireNonNullElse(e.getClickedBlock(), p.getLocation().getBlock()).getType().equals(Material.END_PORTAL_FRAME)){
            QuestGui questGui = new QuestGui(this.plugin);
            questGui.questGuiOpen(p);
        }
        if (Objects.requireNonNullElse(e.getClickedBlock(), p.getLocation().getBlock()).getType().toString().contains("WOOL")){
            artBlocks handler = new artBlocks(plugin);
            handler.rightClick(e);
        }
    }

    @EventHandler
    public void onPlayerDismount(EntityDismountEvent e){
        Player p = (Player) e.getEntity();
        if (e.getDismounted().getType() == EntityType.ARMOR_STAND){
            e.getDismounted().remove();
            Location loc = p.getLocation();
            loc.setY(p.getLocation().getY() + 1);
            p.teleport(loc);
        }
    }

    @EventHandler
    public void onToolBreak(PlayerItemBreakEvent e){

        String tool = e.getBrokenItem().getType().toString().toLowerCase();

        for (String s : server.tools.keySet()){
            if (tool.contains(s)){
                ToolsEvents toolsEvents = new ToolsEvents();
                toolsEvents.toolBreak(e.getPlayer(), e);
                break;
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e){
        String tool = e.getItemDrop().getItemStack().toString().toLowerCase();

        for (String s : server.tools.keySet()){
            if (tool.contains(s)){
                e.setCancelled(true);
                break;
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){

        String tool = e.getCursor().getType().toString().toLowerCase();
        for (String s : server.tools.keySet()){
            if (tool.contains(s)){
                if (e.getClickedInventory().getType() != InventoryType.PLAYER) e.setCancelled(true);
                break;
            }
        }

        GeneratorEdit genEdit = new GeneratorEdit();
        Player p = (Player) e.getWhoClicked();
        InventoryView inv = e.getView();
        String invName = inv.getTitle();

        if (invName.equals(ChatColor.DARK_GRAY + "Your Active Abilities")){
            AbilitiesGUI abilitiesGUI = new AbilitiesGUI();
            abilitiesGUI.initializeAbilitySelection(p, e);
            e.setCancelled(true);
        }

        if (invName.contains("Ability Slot")) {
            AbilitiesGUI abilitiesGUI = new AbilitiesGUI();
            abilitiesGUI.activateAbility(p, e.getSlot(), e);
            abilitiesGUI.abilitiesActive(p);
            e.setCancelled(true);
        }

        if (invName.equals(ChatColor.DARK_GRAY + "Skins")){
            e.setCancelled(true);
            SkinGui.skinGuiDo(e, plugin);
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

            if (plugin.stones.contains(clickedItem.getType())) {
                PersistentDataContainer container = p.getPersistentDataContainer();
                container.set(genEdit.stoneBlock, PersistentDataType.INTEGER, plugin.stones.indexOf(clickedItem.getType()));
                p.sendMessage(ChatColor.GREEN + "Your stone block has been changed to " + clickedItem.getTranslationKey());
            } else if (clickedItem.getType().equals(Material.ARROW)) genEdit.genOptions(p);
        } else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Select Wood Block")){
            e.setCancelled(true);

            ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem == null || clickedItem.getType().isAir() || clickedItem.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;

            if (plugin.planks.contains(clickedItem.getType())) {
                PersistentDataContainer container = p.getPersistentDataContainer();
                container.set(genEdit.woodBlock, PersistentDataType.INTEGER, plugin.planks.indexOf(clickedItem.getType()));
                p.sendMessage(ChatColor.GREEN + "Your wood block has been changed to " + clickedItem.getTranslationKey());
            } else if (clickedItem.getType().equals(Material.ARROW)) genEdit.genOptions(p);
        } else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Select Loose Block")){
            e.setCancelled(true);

            ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem == null || clickedItem.getType().isAir() || clickedItem.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;

            if (plugin.dirts.contains(clickedItem.getType())) {
                PersistentDataContainer container = p.getPersistentDataContainer();
                container.set(genEdit.dirtBlock, PersistentDataType.INTEGER, plugin.dirts.indexOf(clickedItem.getType()));
                p.sendMessage(ChatColor.GREEN + "Your loose block has been changed to " + clickedItem.getTranslationKey());
            } else if (clickedItem.getType().equals(Material.ARROW)) genEdit.genOptions(p);
        } else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Select Miscellaneous Item")){
            e.setCancelled(true);

            ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem == null || clickedItem.getType().isAir() || clickedItem.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;

            if (plugin.miscs.contains(clickedItem.getType())) {
                PersistentDataContainer container = p.getPersistentDataContainer();
                container.set(genEdit.miscItem, PersistentDataType.INTEGER, plugin.miscs.indexOf(clickedItem.getType()));
                p.sendMessage(ChatColor.GREEN + "Your misc. item has been changed to " + clickedItem.getTranslationKey());
            } else if (clickedItem.getType().equals(Material.ARROW)) genEdit.genOptions(p);
        } else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Quests")){
            e.setCancelled(true);

        }
    }
}
