package worm.me;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.event.inventory.UseItemEvent;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.GameSystems.FishingSystem;
import worm.me.GameSystems.ArtBlocks;
import worm.me.PlayerSystems.*;
import worm.me.PlayerSystems.Abilities.AbilitiesGUI;
import worm.me.PlayerSystems.Abilities.Abilities;
import worm.me.PlayerSystems.Abilities.Active.AgilityDash;
import worm.me.PlayerSystems.Abilities.Active.AgilityDoubleJump;
import worm.me.PlayerSystems.Abilities.Active.AgilityLeap;
import worm.me.PlayerSystems.Generator.Generator;
import worm.me.PlayerSystems.Generator.GeneratorEdit;
import worm.me.PlayerSystems.PlayerData.Levels.GainExpEvent;
import worm.me.PlayerSystems.PlayerData.Levels.LevelUpEvent;
import worm.me.PlayerSystems.PlayerData.Levels.Levels;
import worm.me.PlayerSystems.PlayerData.Vault;
import worm.me.PlayerSystems.PlayerData.VoidPlayer;
import worm.me.PlayerSystems.Quests.QuestGui;
import worm.me.PlayerSystems.PlayerData.Social.MessageHead;
import worm.me.PlayerSystems.PlayerData.Social.Skin.SkinGui;
import org.bukkit.event.player.PlayerJoinEvent;
import worm.me.PlayerSystems.PlayerData.Tools.ToolsEvents;

import java.util.*;

public class Listener implements org.bukkit.event.Listener {

    private final Server plugin;

    private final Map<Player, Boolean> doubleJumpMap = new HashMap<>();

    public Listener(Server plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        FishingSystem handler = new FishingSystem(plugin);
        handler.handleFishing(event);
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        plugin.getWorldBorderHandler().incrementBlockCount();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        plugin.getWorldBorderHandler().decrementBlockCount();
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){

        if (!event.isBedSpawn()) {
            PlayerSpawning handler = new PlayerSpawning(plugin);
            handler.playerSpawn(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerLeftClick(PlayerAnimationEvent event){

        if (event.getAnimationType() != PlayerAnimationType.ARM_SWING) return;

        SaddleRide handler = new SaddleRide(plugin);
        handler.leftClick(event);
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEntityEvent event){

        SaddleRide handler = new SaddleRide(plugin);
        handler.rightClick(event);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

        // Set player variable
        Player player = event.getPlayer();

        // Give the player a random item every 10 seconds
        Generator generator = new Generator(plugin);
        generator.generateItem(player);

        // Initialize player data
        VoidPlayer.initializeData(player);
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent e){
        Player player = e.getPlayer();
        Levels level = new Levels();

        // Gives the player exp for mining certain blocks
        if (plugin.miningBlocks.containsKey(e.getBlock().getType())) {
            level.addExp(plugin.miningBlocks.get(e.getBlock().getType()), Levels.skillTranslation("Mining"), player);
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
        Abilities ability = new Abilities();
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
    /*
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        if (e.getEntity().getType() == EntityType.ARMOR_STAND) {
            PlayerSpawning handler = new PlayerSpawning(plugin);
            ArmorStand ar = (ArmorStand) e.getEntity() ;
            handler.ArmorStandSpawn(ar);
        }
    }
    */
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
            ArtBlocks handler = new ArtBlocks(plugin);
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

        for (String s : Server.tools.keySet()){
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

        for (String s : Server.tools.keySet()){
            if (tool.contains(s)){
                e.setCancelled(true);
                break;
            }
        }
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e){
        if (e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if (p.getItemOnCursor().getType().toString().contains("PICKAXE") || p.getItemOnCursor().getType().toString().contains("AXE") || p.getItemOnCursor().getType().toString().contains("SHOVEL") || p.getItemOnCursor().getType().toString().contains("SWORD")){
                e.setCancelled(true);

            }
        }
    }

    @EventHandler
    public void onExpGain(GainExpEvent e){
        if (Levels.getExp(Levels.skills.get(e.getSkillType()), e.getPlayer()) > Levels.expRequired(e.getSkillType() + 1)){
            Levels.levelUp(Levels.skills.get(e.getSkillType()), e.getPlayer());
        }
    }

    @EventHandler
    public void onLevelUp(LevelUpEvent e){
        Player p = e.getPlayer();

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

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){

        GuiConstructor guiConstructor = new GuiConstructor();

        Player p = (Player) e.getWhoClicked();

        String tool = e.getCursor().getType().toString().toLowerCase();
        for (String s : Server.tools.keySet()){
            if (tool.contains(s)){
                if (e.getClickedInventory().getType() != InventoryType.PLAYER) e.setCancelled(true);
                break;
            }
        }

        GeneratorEdit genEdit = new GeneratorEdit();

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

            if (plugin.stones.containsKey(clickedItem.getType()) && VoidPlayer.getUnlockedGens(p).contains(clickedItem)) {
                PersistentDataContainer container = p.getPersistentDataContainer();
                container.set(genEdit.stoneBlock, PersistentDataType.STRING, clickedItem.getType().toString());
                p.sendMessage(ChatColor.GREEN + "Your stone block has been changed to " + clickedItem.getTranslationKey());
            } else if (clickedItem.getType().equals(Material.ARROW)) genEdit.genOptions(p);
        } else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Select Wood Block")){
            e.setCancelled(true);

            ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem == null || clickedItem.getType().isAir() || clickedItem.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;

            if (plugin.planks.containsKey(clickedItem.getType()) && VoidPlayer.getUnlockedGens(p).contains(clickedItem)) {
                PersistentDataContainer container = p.getPersistentDataContainer();
                container.set(genEdit.woodBlock, PersistentDataType.STRING, clickedItem.getType().toString());
                p.sendMessage(ChatColor.GREEN + "Your wood block has been changed to " + clickedItem.getTranslationKey());
            } else if (clickedItem.getType().equals(Material.ARROW)) genEdit.genOptions(p);
        } else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Select Loose Block")){
            e.setCancelled(true);

            ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem == null || clickedItem.getType().isAir() || clickedItem.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;

            if (plugin.dirts.containsKey(clickedItem.getType()) && VoidPlayer.getUnlockedGens(p).contains(clickedItem)) {
                PersistentDataContainer container = p.getPersistentDataContainer();
                container.set(genEdit.dirtBlock, PersistentDataType.STRING, clickedItem.getType().toString());
                p.sendMessage(ChatColor.GREEN + "Your loose block has been changed to " + clickedItem.getTranslationKey());
            } else if (clickedItem.getType().equals(Material.ARROW)) genEdit.genOptions(p);
        } else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Select Miscellaneous Item")){
            e.setCancelled(true);

            ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem == null || clickedItem.getType().isAir() || clickedItem.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;

            if (plugin.miscs.containsKey(clickedItem.getType()) && VoidPlayer.getUnlockedGens(p).contains(clickedItem)) {
                PersistentDataContainer container = p.getPersistentDataContainer();
                container.set(genEdit.miscItem, PersistentDataType.STRING, clickedItem.getType().toString());
                p.sendMessage(ChatColor.GREEN + "Your misc. item has been changed to " + clickedItem.getTranslationKey());
            } else if (clickedItem.getType().equals(Material.ARROW)) genEdit.genOptions(p);
        } else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Quests")){
            e.setCancelled(true);

        }
    }
}
