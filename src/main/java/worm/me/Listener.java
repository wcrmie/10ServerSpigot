package worm.me;

import com.sk89q.worldguard.bukkit.event.inventory.UseItemEvent;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.GameSystems.Fishing.FishingSystem;
import worm.me.GameSystems.ArtBlocks;
import worm.me.GameSystems.Recipes.RecipesGUI;
import worm.me.PlayerSystems.*;
import worm.me.PlayerSystems.Abilities.AbilitiesGUI;
import worm.me.PlayerSystems.Abilities.Abilities;
import worm.me.PlayerSystems.Abilities.Active.AgilityDash;
import worm.me.PlayerSystems.Abilities.Active.AgilityDoubleJump;
import worm.me.PlayerSystems.Abilities.Active.AgilityLeap;
import worm.me.PlayerSystems.Generator.Generator;
import worm.me.PlayerSystems.Generator.GeneratorEdit;
import worm.me.PlayerSystems.PlayerData.*;
import worm.me.PlayerSystems.PlayerData.Levels.GainExpEvent;
import worm.me.PlayerSystems.PlayerData.Levels.LevelUpEvent;
import worm.me.PlayerSystems.PlayerData.Levels.Levels;
import worm.me.PlayerSystems.PlayerData.Levels.LevelsGUI;
import worm.me.PlayerSystems.PlayerData.Social.Hats.Hats;
import worm.me.PlayerSystems.PlayerData.Social.Hats.HatsStorage;
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
        plugin.getWorldBorderHandler().incrementBlockCount(event);
        if (event.getPlayer() != null) {
            VoidPlayer.registerStats(event.getPlayer(), 0);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        plugin.getWorldBorderHandler().decrementBlockCount(event);
        if (event.getPlayer() != null) {
            VoidPlayer.registerStats(event.getPlayer(), 1);
        }
    }

    @EventHandler
    public void onArrowShot(EntityShootBowEvent e){
        if (e.getEntity() instanceof Player shooter) {
            e.getProjectile().getPersistentDataContainer().set(new NamespacedKey(plugin, "arrow"), PersistentDataType.STRING, "arrow");
            VoidPlayer.registerStats(shooter, 3);
        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent e){
        if (e.getEntity().getShooter() instanceof Player shooter && e.getHitEntity() != null && e.getEntity().getPersistentDataContainer().get(new NamespacedKey(plugin, "arrow"), PersistentDataType.STRING) != null){
            Server.pluginMessage(ChatColor.GRAY + "" + VoidPlayer.getStat(shooter, 2), shooter);
            VoidPlayer.registerStats(shooter, 2);
        }
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
    public void onPlayerLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();

        PersistentDataContainer container = p.getPersistentDataContainer();

        container.set(new NamespacedKey(plugin, "repsess"), PersistentDataType.LONG_ARRAY, new long[]{System.currentTimeMillis()/1000L, container.get(new NamespacedKey(plugin, "repsess"), PersistentDataType.LONG_ARRAY)[1]});
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent e){
        Player player = e.getPlayer();

        CoreProtectAPI api = Server.getPlugin().getCoreProtect();

        // If the block was placed by the player in the last 10 seconds, don't give exp

        if (api != null) {

            List<String[]> lookup = api.performLookup(10, null, null, null, null, null, 1, e.getBlock().getLocation());


            boolean exp = true;

                for (String[] value : lookup) {
                    CoreProtectAPI.ParseResult result = api.parseResult(value);

                    if (result.getType().equals(e.getBlock().getType()) && result.getActionString().equals("place") && result.getX() == e.getBlock().getX() && result.getY() == e.getBlock().getY() && result.getZ() == e.getBlock().getZ()) {
                        exp = false;
                        break;
                    }

            }


            if (exp) Levels.expHandler(player, e);
        } else player.sendMessage(ChatColor.RED + "CoreProtect is not enabled on this server.");
    }

    @EventHandler
    public void onEntityKilled(EntityDeathEvent e){
        if (e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player p){
            Levels.expHandler(p, e);

            if (e.getEntity().getType() == EntityType.PLAYER) {

                PersistentDataContainer c = p.getPersistentDataContainer();

                long[] repsess = c.get(new NamespacedKey(Server.getPlugin(), "repsess"), PersistentDataType.LONG_ARRAY);

                repsess[1] = repsess[1] + 1;

                c.set(new NamespacedKey(Server.getPlugin(), "repsess"), PersistentDataType.LONG_ARRAY, repsess);
            }

        }
    }

    @EventHandler
    public void onFurnaceCollect(FurnaceExtractEvent e){

        Levels.expHandler(e.getPlayer(), e);


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
    public void onPlayerFlightEvent(PlayerToggleFlightEvent e){
        Player p = e.getPlayer();

        if (p.getGameMode().equals(GameMode.CREATIVE)) return;

        // Make the player double jump
        e.setCancelled(true);
        AgilityDoubleJump doubleJump = new AgilityDoubleJump();
        doubleJump.doubleJump(p);
    }


    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e){


        if (e.getFrom() == e.getTo()) return;

        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL && player.getAllowFlight()) {
            if (!player.isFlying() && player.getFallDistance() > 3) {
                if (player.isOnGround()) {
                    player.damage(player.getFallDistance() - 3);
                }
            }
        }

        if (Objects.requireNonNull(e.getTo()).getY() < 200) e.getPlayer().damage(1000000);

        if (e.getPlayer().getPersistentDataContainer().get(Abilities.moveCooldown, PersistentDataType.LONG) != null){
            if (e.getPlayer().getPersistentDataContainer().get(Abilities.moveCooldown, PersistentDataType.LONG) <= System.currentTimeMillis()/1000L){
                e.getPlayer().setAllowFlight(true);
            }
        }
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
        if (Levels.getExp(Levels.skills.get(e.getSkillType()), e.getPlayer()) + e.getExpValue() > Levels.expRequired(Levels.getLevel(e.getSkillType(), e.getPlayer()) + 1)){
            Levels.levelUp(e.getSkillType(), e.getPlayer());
        }
    }

    @EventHandler
    public void onLevelUp(LevelUpEvent e){
        Player p = e.getPlayer();
        Unlocks unlocks = new Unlocks();

        for (int i = 0; i < 5; i++){
            unlocks.attemptUnlock(i, p);
        }


    }

    @EventHandler
    public void onUnlock(UnlockEvent e){
        Player p = e.getPlayer();

        Server.pluginMessage(ChatColor.GREEN + "You have unlocked a(n) " + e.getUnlock() + " " + e.getUnlockType(), p);
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

            Server.pluginMessage(ChatColor.RED + "Hat storage closed", p);

            HatsStorage hatsStorage = new HatsStorage();
            hatsStorage.eventHandler(e);


        }

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){

        if (Objects.requireNonNullElse(e.getCurrentItem(), new ItemStack(Material.AIR)).getType().isAir()) return;

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
            AbilitiesGUI abilitiesGUI = new AbilitiesGUI();
            abilitiesGUI.initializeAbilitySelection(p, e);
            e.setCancelled(true);
        }

        if (invName.contains("Ability Slot")) {
            AbilitiesGUI abilitiesGUI = new AbilitiesGUI();
            abilitiesGUI.activateAbility(p, e.getSlot(), e);
            e.setCancelled(true);
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

            Server.pluginMessage("Attempted hat storage click", p);

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

            if (Server.stones.containsKey(clickedItem.getType())){

                    boolean unlocked = false;

                    for (ItemStack item : VoidPlayer.getUnlockedGens(p)){
                        if (item.getType() == clickedItem.getType()){
                            unlocked = true;
                            break;
                        }
                    }
                    if (unlocked){
                        PersistentDataContainer container = p.getPersistentDataContainer();
                        container.set(genEdit.stoneBlock, PersistentDataType.STRING, clickedItem.getType().toString());
                        p.sendMessage(ChatColor.GREEN + "Your stone block has been changed to " + clickedItem.getTranslationKey());
                    }
            } else if (clickedItem.getType().equals(Material.ARROW)) genEdit.genOptions(p);
        } else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Select Wood Block")){
            e.setCancelled(true);

            ItemStack clickedItem = e.getCurrentItem();

            if (Server.planks.containsKey(clickedItem.getType())){

                boolean unlocked = false;

                for (ItemStack item : VoidPlayer.getUnlockedGens(p)){
                    if (item.getType() == clickedItem.getType()){
                        unlocked = true;
                        break;
                    }
                }
                if (unlocked){
                    PersistentDataContainer container = p.getPersistentDataContainer();
                    container.set(genEdit.woodBlock, PersistentDataType.STRING, clickedItem.getType().toString());
                    p.sendMessage(ChatColor.GREEN + "Your wood block has been changed to " + clickedItem.getTranslationKey());
                }
            } else if (clickedItem.getType().equals(Material.ARROW)) genEdit.genOptions(p);
        } else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Select Loose Block")){
            e.setCancelled(true);

            ItemStack clickedItem = e.getCurrentItem();

            if (Server.dirts.containsKey(clickedItem.getType())){

                boolean unlocked = false;

                for (ItemStack item : VoidPlayer.getUnlockedGens(p)){
                    if (item.getType() == clickedItem.getType()){
                        unlocked = true;
                        break;
                    }
                }
                if (unlocked){
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

                boolean unlocked = false;

                for (ItemStack item : VoidPlayer.getUnlockedGens(p)){
                    if (item.getType() == clickedItem.getType()){
                        unlocked = true;
                        break;
                    }
                }
                if (unlocked){
                    PersistentDataContainer container = p.getPersistentDataContainer();
                    container.set(genEdit.miscItem, PersistentDataType.STRING, clickedItem.getType().toString());
                    p.sendMessage(ChatColor.GREEN + "Your misc. item has been changed to " + clickedItem.getTranslationKey());
                }
            } else if (clickedItem.getType().equals(Material.ARROW)) genEdit.genOptions(p);
        } else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Quests")){
            e.setCancelled(true);

        }

        if (e.isCancelled()) ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.UI_BUTTON_CLICK, 0.5F, 1.2F);

    }
}
