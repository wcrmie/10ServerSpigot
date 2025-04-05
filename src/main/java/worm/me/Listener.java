package worm.me;

import com.sk89q.worldguard.bukkit.event.inventory.UseItemEvent;
import net.coreprotect.CoreProtectAPI;
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
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import worm.me.gamesystems.fishing.FishingTables;
import worm.me.gamesystems.ArtBlocks;
import worm.me.gamesystems.recipes.RecipesGUI;
import worm.me.gamesystems.worldreset.WorldReset;
import worm.me.playersystems.*;
import worm.me.playersystems.abilities.AbilitiesGUI;
import worm.me.playersystems.abilities.Abilities;
import worm.me.playersystems.abilities.AbilityAttemptEvent;
import worm.me.playersystems.abilities.active.FeatherDash;
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

public class Listener implements org.bukkit.event.Listener {

    private final Server plugin;

    private final List<LivingEntity> tickList = new ArrayList<>();

    public Listener(Server plugin) {
        this.plugin = plugin;
    }

    private final FeatherDash featherDash = new FeatherDash();

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {

        FishingTables handler = new FishingTables();
        handler.handleFishing(event);

        Levels.expHandler(event.getPlayer(), event);

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (WorldReset.getInstance().isBlockPlacingDisabled()) {
            event.setCancelled(true);
            return;
        }

        plugin.getWorldBorderHandler().incrementBlockCount(event);
        if (event.getPlayer() != null) {
            VoidPlayer.registerStats(event.getPlayer(), 0);
        }
    }

    @EventHandler
    public void onFlow(BlockFromToEvent event) {

        if (event.getToBlock().getLocation().getBlockY() > 210) return;

        if (event.getBlock().getType() == Material.WATER || event.getBlock().getType() == Material.LAVA) {
            event.setCancelled(true);
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
            NamespacedKey customSpawnKey = new NamespacedKey(plugin, "custom_spawn");
            if (event.getPlayer().getPersistentDataContainer().has(customSpawnKey, PersistentDataType.BOOLEAN)) {
                if (event.getPlayer().getPersistentDataContainer().get(customSpawnKey, PersistentDataType.BOOLEAN) == true) {
                    PlayerSpawning handler = new PlayerSpawning(plugin);
                    handler.playerSpawn(event.getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLeftClick(PlayerAnimationEvent event){

        if (event.getAnimationType() == PlayerAnimationType.ARM_SWING) {

            SaddleSystem ss = new SaddleSystem(plugin);
            ss.leftClick(event);

        }
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEntityEvent event){
        SaddleSystem handler = new SaddleSystem(plugin);
        handler.rightClick(event);
    }


    // ----------------------------------------- INITIALIZATION EVENTS -----------------------------------------

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

        // Set player variable
        Player player = event.getPlayer();

        // Give the player a random item every 10 seconds
        Server.getPlugin().generating.add(event.getPlayer());
        Generator generator = new Generator(plugin);
        generator.generateItem(player);

        // Initialize player data
        VoidPlayer.initializeData(player);

        NamespacedKey customSpawnKey = new NamespacedKey(plugin, "custom_spawn");
        if (!player.getPersistentDataContainer().has(customSpawnKey, PersistentDataType.BOOLEAN)) {
            player.getPersistentDataContainer().set(customSpawnKey, PersistentDataType.BOOLEAN, true);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();
        Server.getPlugin().generating.remove(e.getPlayer());

        PersistentDataContainer container = p.getPersistentDataContainer();

        featherDash.cancelDash(e.getPlayer().getUniqueId());

        container.set(new NamespacedKey(plugin, "repsess"), PersistentDataType.LONG_ARRAY, new long[]{System.currentTimeMillis()/1000L, container.get(new NamespacedKey(plugin, "repsess"), PersistentDataType.LONG_ARRAY)[1]});
    }





    // --------------------------EXPERIENCE GAIN EVENTS--------------------------

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

                repsess[1]++;

                c.set(new NamespacedKey(Server.getPlugin(), "repsess"), PersistentDataType.LONG_ARRAY, repsess);
            }

        }

        // Change drop system in future
        if (e.getEntity().getType() == EntityType.ENDERMAN) {
            int emeraldCount = ThreadLocalRandom.current().nextInt(0, 3);
            e.getDrops().add(new ItemStack(Material.EMERALD, emeraldCount));
        }
        if (e.getEntity().getType() == EntityType.ENDERMITE) {
            int emeraldCount = ThreadLocalRandom.current().nextInt(0, 2);
            e.getDrops().add(new ItemStack(Material.SMALL_AMETHYST_BUD, emeraldCount));
        }
        if (e.getEntity().getType() == EntityType.SILVERFISH) {
            int emeraldCount = ThreadLocalRandom.current().nextInt(3, 6);
            e.getDrops().add(new ItemStack(Material.IRON_NUGGET, emeraldCount));
        }

        if (e.getEntity().getType() == EntityType.PLAYER) e.getDrops().add(GuiConstructor.createGuiItem(Material.ROTTEN_FLESH, ChatColor.WHITE + "Player Flesh", ChatColor.GRAY + "A Player's flesh."));

        e.getDrops().removeIf(item -> item.getType().toString().contains("sword") || item.getType().toString().contains("axe") ||
                item.getType().toString().contains("shovel") || item.getType().toString().contains("pickaxe"));
        
    }

    @EventHandler
    public void onFurnaceCollect(FurnaceExtractEvent e){

        Levels.expHandler(e.getPlayer(), e);

    }

    @EventHandler
    public void onHarvest(PlayerHarvestBlockEvent e){

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
    public void onQuestProgress(QuestProgressEvent e) {

        if (e.getProgress() == e.getMaxProgress()) {
            Quests quests = new Quests();
            quests.completeQuest(e.getQuestID(), e.getPlayer());
        }
    }



    // --------------------------MESSAGEHEAD--------------------------

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
        for (Player player : Bukkit.getOnlinePlayers()) {
            double distance = player.getLocation().distance(event.getPlayer().getLocation());

            if (distance <= 50) {
                int color = distance == 0 ? 255 : (int) Math.round(255 - distance * 5);
                String hexColor = String.format("#%02x%02x%02x", color, color, color);
                player.sendMessage(net.md_5.bungee.api.ChatColor.of(hexColor) + event.getPlayer().getName() + ": " + event.getMessage());
                event.setCancelled(true);
                return;
            } else if (player.hasPermission("10VS.MOD")) {
                player.sendMessage(p.getName() + ": " + m);
                event.setCancelled(true);
            }
        }


    }

    // --------------------------ABILITY EVENTS--------------------------

    @EventHandler
    public void onAbilityAttempt(AbilityAttemptEvent e){

        Abilities ability = new Abilities();

        PersistentDataContainer container = e.getPlayer().getPersistentDataContainer();

        if (ability.isActive(e.getPlayer(), e.getAbilityID())){
            if (Objects.requireNonNullElse(container.get(Abilities.abilityCooldown, PersistentDataType.LONG_ARRAY), new long[3])[ability.getAbilitySlot(e.getPlayer(), e.getAbilityID())] <= System.currentTimeMillis()/1000L) {
                ability.handleAbility(e.getPlayer(), e.getAbilityID());
            } else {

                Server.pluginMessage(ChatColor.RED + "Your " + Abilities.abilityGuiComponents.get(e.getAbilityID()).getFirst() + " is on cooldown for " + (container.get(Abilities.abilityCooldown, PersistentDataType.LONG_ARRAY)[ability.getAbilitySlot(e.getPlayer(), e.getAbilityID())] - (System.currentTimeMillis()/1000)) + " seconds.", e.getPlayer());
                e.setCancelled(true);
            }
        }
        else e.setCancelled(true);

    }


    @EventHandler
    public void onPlayerFlightEvent(PlayerToggleFlightEvent e){
        Player p = e.getPlayer();

        if (p.getGameMode().equals(GameMode.CREATIVE)) return;

        // Make the player double jump
        e.setCancelled(true);

        Bukkit.getServer().getPluginManager().callEvent(new AbilityAttemptEvent(1, p));

    }


    @EventHandler
    public void onPlayerMoveMoveEvent(PlayerMoveEvent e){

        Abilities ability = new Abilities();

        if (e.getFrom() == e.getTo()) return;

        if (!ability.isActive(e.getPlayer(), 1) && e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            e.getPlayer().setAllowFlight(false);
            e.getPlayer().setFlying(false);
        }

        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL && player.getAllowFlight()) {
            if (!player.isFlying() && player.getFallDistance() > 3) {
                if (player.isOnGround()) {
                    player.damage(player.getFallDistance() - 3);



                    if (player.getHealth()*2 > (player.getFallDistance() - 3)) {
                        Levels.addExp((Math.round(player.getFallDistance() - 3)/2), Levels.skillTranslation("Agility"), player);
                    }
                }
            }
        }
/*
        if (FrogGrab.isPlayerMovementRestricted(player.getUniqueId())) {
            Location from = e.getFrom();
            Location to = e.getTo();

            if (to != null) {
                if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
                    Location newLocation = new Location(
                            from.getWorld(),
                            from.getX(),
                            from.getY(),
                            from.getZ(),
                            to.getYaw(),
                            to.getPitch()
                    );

                    e.setTo(newLocation);
                }
            }
        }

 */

        if (Objects.requireNonNull(e.getTo()).getY() < 200) e.getPlayer().damage(1000000);

        if (ability.getAbilitySlot(e.getPlayer(), 1) != -1){
            if (Objects.requireNonNullElse(e.getPlayer().getPersistentDataContainer().get(Abilities.abilityCooldown, PersistentDataType.LONG_ARRAY), new long[3])[ability.getAbilitySlot(e.getPlayer(), 1)] <= System.currentTimeMillis()/1000L && ability.isActive(e.getPlayer(), 1)){
                e.getPlayer().setAllowFlight(true);
            }
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {

        if (event.getEntity() instanceof Player) return;

        if (event.getEntity() instanceof LivingEntity entity) tickList.add(entity);

        if (event.getEntity() instanceof Villager villy) {
            villy.setProfession(Villager.Profession.NITWIT);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {

        if (event.getEntity() instanceof Player) return;

        for (ItemStack itemStack : event.getDrops()) {
            if (!Tools.toolType(itemStack.getType()).equals("none")) {
                event.getDrops().remove(itemStack);
            }
        }

        LivingEntity entity = event.getEntity();

        tickList.remove(entity);

        if (event.getEntity() instanceof Bat) {
            event.getDrops().add(new ItemStack(Material.IRON_NUGGET, 1));
        }

    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e){
        if (e.getEntity() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.FALL){
            Player player = (Player) e.getEntity();
            Abilities ability = new Abilities();
            if (ability.isActive(player,7) && !player.isSneaking()){

                if (player.getInventory().getBoots() != null) {
                    ItemStack item = player.getInventory().getBoots();
                    if (player.getInventory().getBoots().hasItemMeta()) {
                        ItemMeta meta = item.getItemMeta();
                        if (meta != null) {
                            PersistentDataContainer data = meta.getPersistentDataContainer();
                            NamespacedKey key = new NamespacedKey(plugin, "slimeboots");
                            if (data.has(key, PersistentDataType.STRING)) {
                                String tagValue = data.get(key, PersistentDataType.STRING);

                                e.setCancelled(true);
                            }
                        }
                    }
                }
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

        if (e.getOriginalEvent() instanceof PlayerInteractEvent) {

            if (((PlayerInteractEvent) e.getOriginalEvent()).getAction().equals(Action.RIGHT_CLICK_AIR) || ((PlayerInteractEvent) e.getOriginalEvent()).getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (p.getInventory().getItemInMainHand().toString().toLowerCase().contains("sword")) {
                    if (p.isSneaking()){
                        Bukkit.getServer().getPluginManager().callEvent(new AbilityAttemptEvent(2, p));
                    }
                } else if (p.getInventory().getItemInMainHand().toString().toLowerCase().contains("_axe")) {
                    if (p.isSneaking()){
                        Bukkit.getServer().getPluginManager().callEvent(new AbilityAttemptEvent(3, p));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {

        Levels.addExp(1, Levels.skillTranslation("Crafting"), (Player) event.getWhoClicked());

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){

        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {

            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.NOTE_BLOCK && e.getPlayer().isSneaking()) {
                Bukkit.getServer().getPluginManager().callEvent(new AbilityAttemptEvent(5, e.getPlayer()));
            }

            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.FEATHER && e.getPlayer().isSneaking()) {
                Bukkit.getServer().getPluginManager().callEvent(new AbilityAttemptEvent(8, e.getPlayer()));
            }

            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.TINTED_GLASS && e.getPlayer().isSneaking()) {
                Player player = e.getPlayer();

                Location playerLoc = player.getLocation();
                Vector direction = playerLoc.getDirection();
                direction.setY(0);
                if (direction.length() > 0) {
                    direction.normalize();
                }

                Location finalLocation = playerLoc.clone();
                finalLocation.add(direction.multiply(10));

                Block finalFeetBlock = finalLocation.getBlock();
                Block finalHeadBlock = finalLocation.clone().add(0, 1, 0).getBlock();

                if (player.isOnGround() && finalFeetBlock.isPassable() && finalHeadBlock.isPassable()) {
                    Bukkit.getServer().getPluginManager().callEvent(new AbilityAttemptEvent(6, player));
                }
            }

            /*
            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.PINK_CONCRETE_POWDER && e.getPlayer().isSneaking()) {
                ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
                if (item.hasItemMeta()) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        PersistentDataContainer data = meta.getPersistentDataContainer();
                        NamespacedKey key = new NamespacedKey(plugin, "tongue");
                        if (data.has(key, PersistentDataType.STRING)) {
                            String tagValue = data.get(key, PersistentDataType.STRING);
                            Bukkit.getServer().getPluginManager().callEvent(new AbilityAttemptEvent(5, e.getPlayer()));
                        }
                    }
                }
            }
            */


        }

        if (e.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Player p = e.getPlayer();
        if (Objects.requireNonNullElse(e.getClickedBlock(), p.getLocation().getBlock()).getType().equals(Material.END_PORTAL_FRAME)){
            QuestGui questGui = new QuestGui();
            questGui.questGuiOpen(p);
        }
        if (Objects.requireNonNullElse(e.getClickedBlock(), p.getLocation().getBlock()).getType().toString().contains("WOOL")){
            ArtBlocks handler = new ArtBlocks(plugin);
            handler.rightClick(e);
        }
    }

    @EventHandler
    public void onPlayerDismount(EntityDismountEvent e){

        if (e.getEntity() instanceof Player p){
            if (e.getDismounted().getType() == EntityType.ARMOR_STAND){
                e.getDismounted().remove();
                Location loc = p.getLocation();
                loc.setY(p.getLocation().getY() + 1);
                p.teleport(loc);
            }
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
            if (!p.getPassengers().isEmpty() && p.getPassengers().getFirst().getType() != EntityType.PLAYER){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSnowballThrow(PlayerInteractEvent e){

        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.SNOWBALL) &&
                    e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, "snowman"), PersistentDataType.STRING, "no").equals("snowman")){
                e.setCancelled(true);

                e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);

                Snowball snowball = e.getPlayer().launchProjectile(Snowball.class);
                snowball.getPersistentDataContainer().set(new NamespacedKey(plugin, "snowman"), PersistentDataType.STRING, "snowman");

            }
        }

    }

    @EventHandler
    public void onSnowballHit(ProjectileHitEvent e){
        if (e.getEntity() instanceof Snowball snowball){
            if (snowball.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, "snowman"), PersistentDataType.STRING, "no").equals("snowman")){
                Snowman snowman = e.getEntity().getLocation().getWorld().spawn(e.getEntity().getLocation(), Snowman.class);
                snowman.setDerp(true);
            }
        }
    }

    @EventHandler
    public void onTreeGrow(StructureGrowEvent e){
        if (e.getSpecies() == TreeType.TREE){
            Location loc = e.getLocation();
            for (int x = loc.getBlockX() - 2; x < loc.getBlockX() + 2; x++){
                for (int z = loc.getBlockZ() - 2; z < loc.getBlockZ() + 2; z++){
                    for (int y = loc.getBlockY() - 2; y < loc.getBlockY(); y++){
                        Block block = loc.getWorld().getBlockAt(x, y, z);
                        if (block.getType() == Material.DIRT && block.getRelative(0, 1, 0).getType() == Material.AIR){
                            int RNG = ThreadLocalRandom.current().nextInt(0, 50);
                            if (RNG == 0) block.setType(Material.GRASS_BLOCK);
                        }
                    }

                }
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

    }

    @EventHandler
    public void onUnlock(UnlockEvent e){
        Player p = e.getPlayer();

        if (e.getUnlockType().equals("Ability")) {
            Server.pluginMessage(ChatColor.GREEN + "You have unlocked the " + Abilities.abilityGuiComponents.get(Integer.parseInt(e.getUnlock())).getFirst() + " " + e.getUnlockType(), p);
        } else {
            Server.pluginMessage(ChatColor.GREEN + "You have unlocked the " + e.getUnlock() + " " + e.getUnlockType(), p);
        }
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
    public void onInventoryClick(PlayerSwapHandItemsEvent e){
        Player p = e.getPlayer();
        if (!p.getPassengers().isEmpty() && p.getPassengers().getFirst().getType() != EntityType.PLAYER){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(PlayerItemHeldEvent e){
        Player p = e.getPlayer();
        if (!p.getPassengers().isEmpty() && p.getPassengers().getFirst().getType() != EntityType.PLAYER){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){

        if (Objects.requireNonNullElse(e.getClickedInventory(), Bukkit.createInventory(e.getWhoClicked(), InventoryType.CHEST)).getType() == InventoryType.PLAYER && (e.getAction().equals(InventoryAction.SWAP_WITH_CURSOR) || e.getAction().equals(InventoryAction.COLLECT_TO_CURSOR)) &&
                !Tools.toolType(Objects.requireNonNullElse(e.getCurrentItem(), new ItemStack(Material.STONE)).getType()).equals("none") && e.getWhoClicked().getInventory().firstEmpty() == -1) e.setCancelled(true);
        if (Objects.requireNonNullElse(e.getClickedInventory(), Bukkit.createInventory(e.getWhoClicked(), InventoryType.CHEST)).getType() != InventoryType.PLAYER && !Tools.toolType(e.getCursor().getType()).equals("none")) e.setCancelled(true);

        if (!Tools.toolType(Objects.requireNonNullElse(e.getCurrentItem(), new ItemStack(Material.STONE)).getType()).equals("none") && (e.getClickedInventory().getType() == InventoryType.PLAYER && e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) ||
                (e.getAction().equals(InventoryAction.HOTBAR_SWAP) && e.getClickedInventory().getType() != InventoryType.PLAYER) && !Tools.toolType(e.getWhoClicked().getInventory().getItem(e.getHotbarButton()).getType()).equals("none")) e.setCancelled(true);

        if (Objects.requireNonNullElse(e.getClickedInventory(), Bukkit.createInventory(e.getWhoClicked(), InventoryType.CHEST)).getType().equals(InventoryType.PLAYER)) {
            if (Objects.requireNonNull(e.getCurrentItem()).getType().equals(Material.WOODEN_AXE) || e.getCurrentItem().getType().equals(Material.WOODEN_PICKAXE) ||
                    e.getCurrentItem().getType().equals(Material.WOODEN_SWORD) || e.getCurrentItem().getType().equals(Material.WOODEN_SHOVEL) || e.getCurrentItem().getType().equals(Material.WOODEN_HOE)){
                if (Objects.requireNonNull(e.getCursor()).getType() == Upgrade.WOOD_TOOL.getUpgradeItem().getType() && e.getCursor().getAmount() >= Upgrade.WOOD_TOOL.getUpgradeItem().getAmount()){
                    e.setCancelled(true);
                    e.getCursor().setAmount(e.getCursor().getAmount() - Upgrade.WOOD_TOOL.getUpgradeItem().getAmount());
                    ToolsEvents toolsEvents = new ToolsEvents();
                    toolsEvents.toolUpgradeAttempt((Player) e.getWhoClicked(), e);
                }

            } else if (Objects.requireNonNull(e.getCurrentItem().getType()).equals(Material.STONE_AXE) || e.getCurrentItem().getType().equals(Material.STONE_PICKAXE) ||
                    e.getCurrentItem().getType().equals(Material.STONE_SWORD) || e.getCurrentItem().getType().equals(Material.STONE_SHOVEL) || e.getCurrentItem().getType().equals(Material.STONE_HOE)){
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

                if (Unlocks.hasGenUnlocked(p, clickedItem.getType())){
                    PersistentDataContainer container = p.getPersistentDataContainer();
                    container.set(genEdit.stoneBlock, PersistentDataType.STRING, clickedItem.getType().toString());
                    p.sendMessage(ChatColor.GREEN + "Your stone block has been changed to " + clickedItem.getTranslationKey());
                }
            } else if (clickedItem.getType().equals(Material.ARROW)) genEdit.genOptions(p);
        } else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Select Wood Block")){
            e.setCancelled(true);

            ItemStack clickedItem = e.getCurrentItem();

            if (Server.planks.containsKey(clickedItem.getType())){

                if (Unlocks.hasGenUnlocked(p, clickedItem.getType())){
                    PersistentDataContainer container = p.getPersistentDataContainer();
                    container.set(genEdit.woodBlock, PersistentDataType.STRING, clickedItem.getType().toString());
                    p.sendMessage(ChatColor.GREEN + "Your wood block has been changed to " + clickedItem.getTranslationKey());
                }

            } else if (clickedItem.getType().equals(Material.ARROW)) genEdit.genOptions(p);
        } else if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Select Loose Block")){
            e.setCancelled(true);

            ItemStack clickedItem = e.getCurrentItem();

            if (Server.dirts.containsKey(clickedItem.getType())){


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