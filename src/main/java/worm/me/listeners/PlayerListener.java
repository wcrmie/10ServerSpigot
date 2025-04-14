package worm.me.listeners;

import com.sk89q.worldguard.bukkit.event.inventory.UseItemEvent;
import net.coreprotect.CoreProtectAPI;
import net.coreprotect.api.result.ParseResult;
import net.kyori.adventure.util.TriState;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import worm.me.GuiConstructor;
import worm.me.Server;
import worm.me.gamesystems.blocks.ArtBlocks;
import worm.me.gamesystems.fishing.FishingTables;
import worm.me.playersystems.PlayerSpawning;
import worm.me.playersystems.abilities.Abilities;
import worm.me.playersystems.abilities.AbilityAttemptEvent;
import worm.me.playersystems.abilities.active.FeatherDash;
import worm.me.playersystems.abilities.passive.SlimeBoots;
import worm.me.playersystems.data.UnlockEvent;
import worm.me.playersystems.data.Vault;
import worm.me.playersystems.data.VoidPlayer;
import worm.me.playersystems.data.levels.GainExpEvent;
import worm.me.playersystems.data.levels.LevelUpEvent;
import worm.me.playersystems.data.levels.Levels;
import worm.me.playersystems.data.social.MessageHead;
import worm.me.playersystems.data.social.hats.HatsStorage;
import worm.me.playersystems.data.tools.Tools;
import worm.me.playersystems.data.tools.ToolsEvents;
import worm.me.playersystems.generator.Generator;
import worm.me.playersystems.info.Tips;
import worm.me.playersystems.quests.QuestGui;
import worm.me.playersystems.quests.QuestProgressEvent;
import worm.me.playersystems.quests.Quests;
import worm.me.playersystems.saddle.SaddleSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerListener implements Listener {

    private final Server plugin;

    public PlayerListener(Server plugin) {
        this.plugin = plugin;
    }

    // ----------------------------Player Gameplay Actions-----------------------------


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
    public void onInventoryClickEvent(InventoryClickEvent e) {

        ItemStack item = Objects.requireNonNullElse(e.getCurrentItem(), new ItemStack(Material.STRUCTURE_VOID));
        ItemStack cursor = Objects.requireNonNullElse(e.getCursor(), new ItemStack(Material.STRUCTURE_VOID));
        if (cursor.getItemMeta() instanceof PotionMeta potionMeta && item.getType().equals(Material.ARROW) && cursor.getType().equals(Material.POTION) && item.getAmount() >= 8) {
            ItemStack created = new ItemStack(Material.TIPPED_ARROW, 8);
            created.setItemMeta(potionMeta);
            item.setAmount(item.getAmount() - 8);
            e.getView().setCursor(created);
            e.setCancelled(true);

            Player p = (Player) e.getWhoClicked();
            p.playSound(p, Sound.ENTITY_PLAYER_SPLASH_HIGH_SPEED, 1, 2);
            p.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, 1, 0.5F);
        }


    }


    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {

        FishingTables handler = new FishingTables();
        handler.handleFishing(event);

        Levels.expHandler(event.getPlayer(), event);

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
            VoidPlayer.registerStats(shooter, 2);
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

    @EventHandler
    public void onPlayerPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();

        // Height limit for building
        if (p.getGameMode() == GameMode.CREATIVE) return;
        if (Objects.requireNonNull(e.getBlock().getLocation().getWorld()).getName().equals("ul_Void_nether")) return;
        if (e.getBlock().getLocation().getBlockY() > 280 || e.getBlock().getLocation().getBlockY() < 220){
            e.setCancelled(true);
            Server.pluginMessage(ChatColor.RED + "The build area is between y = 220 and y = 280.", p);
        }
    }

    @EventHandler
    public void onWaterPlace(PlayerBucketEmptyEvent e) {
        if (e.getBlock().getLocation().getBlockY() > 280 || e.getBlock().getLocation().getBlockY() < 220) {
            e.setCancelled(true);
            Server.pluginMessage(ChatColor.RED + "The build area is between y = 220 and y = 280.", e.getPlayer());
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
                                SlimeBoots slimeBoots = new SlimeBoots();
                                slimeBoots.bounce(player);
                                Bukkit.getServer().getPluginManager().callEvent(new AbilityAttemptEvent(7, player));

                                e.setCancelled(true);
                            }
                        }
                    }
                }
            }
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

        for (String s : worm.me.Server.tools.keySet()){
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

        for (String s : worm.me.Server.tools.keySet()){
            if (tool.contains(s)){
                e.setCancelled(true);
                break;
            }
        }

        if (tool.contains("bow") && e.getPlayer().isSneaking()) {
            e.setCancelled(true);
            if (e.getPlayer().isSneaking()) Bukkit.getServer().getPluginManager().callEvent(new AbilityAttemptEvent(10, e.getPlayer()));
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
    public void onSwapHands(PlayerSwapHandItemsEvent e){
        Player p = e.getPlayer();
        if (!p.getPassengers().isEmpty() && p.getPassengers().getFirst().getType() != EntityType.PLAYER){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onChangeHeldItem(PlayerItemHeldEvent e){
        Player p = e.getPlayer();
        if (!p.getPassengers().isEmpty() && p.getPassengers().getFirst().getType() != EntityType.PLAYER){
            e.setCancelled(true);
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
        event.setCancelled(true);


    }

    // --------------------------END MESSAGEHEAD--------------------------


    // ----------------------------Experience and Leveling-----------------------------

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
            worm.me.Server.pluginMessage(ChatColor.GREEN + "You have unlocked the " + Abilities.abilityGuiComponents.get(Integer.parseInt(e.getUnlock())).getFirst() + " " + e.getUnlockType(), p);
        } else {
            worm.me.Server.pluginMessage(ChatColor.GREEN + "You have unlocked the " + e.getUnlock() + " " + e.getUnlockType(), p);
        }
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent e){
        Player player = e.getPlayer();

        CoreProtectAPI api = worm.me.Server.getPlugin().getCoreProtect();


        if (e.getBlock().getType() == Material.PUMPKIN) {
            e.getBlock().getDrops().clear();
            e.getBlock().getDrops().add(new ItemStack(Material.CARVED_PUMPKIN));
            e.getBlock().getDrops().add(new ItemStack(Material.PUMPKIN_SEEDS));
        }

        if (Levels.getLevel(6, player) >= 12) {
            if (e.getBlock().getType() == Material.IRON_ORE || e.getBlock().getType() == Material.COAL_ORE) {
                int chance = ThreadLocalRandom.current().nextInt(0, 100);
                if (chance < 10) {
                    e.getBlock().getDrops().add(new ItemStack(Material.RAW_GOLD));
                    player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 0.1F);
                    Server.pluginMessage(ChatColor.GOLD + "Your Midas' Touch allowed you to uncover a chunk of raw gold!", player);
                }
            }
        }


        // If the block was placed by the player in the last 10 seconds, don't give exp

        if (api != null) {

            List<String[]> lookup = api.performLookup(5, null, null, null, null, null, 1, e.getBlock().getLocation());


            boolean exp = true;

            for (String[] value : lookup) {
                ParseResult result = api.parseResult(value);

                if (e.getBlock().getType() == Material.IRON_ORE) break;
                if (e.getBlock().getType() == Material.COAL_ORE) break;

                if (result.getType().equals(e.getBlock().getType()) && result.getActionString().equals("place") && result.getX() == e.getBlock().getX() && result.getY() == e.getBlock().getY() && result.getZ() == e.getBlock().getZ() && result.getPlayer() != null) {
                    exp = false;
                    break;
                }

            }

            if (exp) {
                Levels.expHandler(player, e);

                if (e.getBlock().getType().equals(Material.COBBLESTONE)) {

                    int rng = ThreadLocalRandom.current().nextInt(0, 100);
                    if (rng <= 1) {

                        e.getBlock().getLocation().getWorld().spawn(e.getBlock().getLocation(), Silverfish.class);
                        e.getBlock().getLocation().getWorld().spawnParticle(Particle.CLOUD, e.getBlock().getLocation().getX(), e.getBlock().getLocation().getY(), e.getBlock().getLocation().getZ(), 25, 0.5, 0.5, 0.5, 0.1);

                    }

                }
            }
        } else player.sendMessage(ChatColor.RED + "CoreProtect is not enabled on this server.");
    }

    @EventHandler
    public void onEntityKilled(EntityDeathEvent e){

        if (e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player p){
            Levels.expHandler(p, e);
            Player player = e.getEntity().getKiller();


            if (e.getEntity().getType() == EntityType.PLAYER) {

                PersistentDataContainer c = p.getPersistentDataContainer();

                long[] repsess = c.get(new NamespacedKey(worm.me.Server.getPlugin(), "repsess"), PersistentDataType.LONG_ARRAY);

                repsess[1]++;

                c.set(new NamespacedKey(worm.me.Server.getPlugin(), "repsess"), PersistentDataType.LONG_ARRAY, repsess);

            }
        }

        if (e.getEntity().getType() == EntityType.PLAYER) {
            e.getDrops().add(GuiConstructor.createGuiItem(Material.ROTTEN_FLESH, ChatColor.WHITE + "Player Flesh", ChatColor.GRAY + "A Player's flesh."));
        }

        e.getDrops().removeIf(item -> !Tools.toolType(item.getType()).equals("none"));

    }

    @EventHandler
    public void DeathMessage(PlayerDeathEvent e){

        Player player = e.getPlayer();

        for (Player p : Bukkit.getOnlinePlayers()){

            double distance = p.getLocation().distance(e.getEntity().getLocation());

            int color = distance == 0 ? 255 : (int) Math.round(255 - 255 * (distance / (player.getWorld().getWorldBorder().getSize() + 50)));
            if (color < 50) color = 50;
            String hexColor = String.format("#%02x%02x%02x", color, color, color);
            p.sendMessage(net.md_5.bungee.api.ChatColor.of(hexColor) + "[" + ChatColor.RED + "☠" + net.md_5.bungee.api.ChatColor.of(hexColor) + "] " + e.getDeathMessage());

        }
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
    public void onCraft(CraftItemEvent event) {
        if (!event.isCancelled()) Levels.addExp(1, Levels.skillTranslation("Crafting"), (Player) event.getWhoClicked());

    }

    // ----------------------------END Experience and Levelling-----------------------------


    // ----------------------------Ability Events-----------------------------

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e){

        Abilities ability = new Abilities();

        if (e.getFrom() == e.getTo()) return;

        if (!ability.isActive(e.getPlayer(), 1) && e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            e.getPlayer().setAllowFlight(false);
            e.getPlayer().setFlying(false);
        }
/*      FROGGRAB IS A HISTORICAL, ARCHIVED ABILITY
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
                e.getPlayer().setFlyingFallDamage(TriState.TRUE);
            }
        }
    }

    @EventHandler
    public void onAbilityAttempt(AbilityAttemptEvent e){

        Abilities ability = new Abilities();

        PersistentDataContainer container = e.getPlayer().getPersistentDataContainer();

        if (ability.isActive(e.getPlayer(), e.getAbilityID())){
            if (Objects.requireNonNullElse(container.get(Abilities.abilityCooldown, PersistentDataType.LONG_ARRAY), new long[3])[ability.getAbilitySlot(e.getPlayer(), e.getAbilityID())] <= System.currentTimeMillis()/1000L) {
                ability.handleAbility(e.getPlayer(), e.getAbilityID());
            } else {

                worm.me.Server.pluginMessage(ChatColor.RED + "Your " + Abilities.abilityGuiComponents.get(e.getAbilityID()).getFirst() + " is on cooldown for " + (container.get(Abilities.abilityCooldown, PersistentDataType.LONG_ARRAY)[ability.getAbilitySlot(e.getPlayer(), e.getAbilityID())] - (System.currentTimeMillis()/1000)) + " seconds.", e.getPlayer());
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
    public void onInteract(PlayerInteractEvent e){

        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {

            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.NOTE_BLOCK && e.getPlayer().isSneaking()) {
                Bukkit.getServer().getPluginManager().callEvent(new AbilityAttemptEvent(5, e.getPlayer()));
            }

            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.FEATHER && e.getPlayer().isSneaking()) {
                Bukkit.getServer().getPluginManager().callEvent(new AbilityAttemptEvent(8, e.getPlayer()));
            }

            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.TRIPWIRE_HOOK && e.getPlayer().isSneaking()) {
                Bukkit.getServer().getPluginManager().callEvent(new AbilityAttemptEvent(9, e.getPlayer()));
            }

            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.BOW) {
                Bukkit.getServer().getPluginManager().callEvent(new AbilityAttemptEvent(11, e.getPlayer()));
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

    // ----------------------------END Ability Events-----------------------------



    // ----------------------------Player Questing-----------------------------

    @EventHandler
    public void onQuestProgress(QuestProgressEvent e) {

        if (e.getProgress() == e.getMaxProgress()) {
            Quests quests = new Quests();
            quests.completeQuest(e.getQuestID(), e.getPlayer());
        }
    }

    // ----------------------------END Player Questing-----------------------------


    // ----------------------------END Player Gameplay Actions-----------------------------



    // ----------------------------Player Initializations----------------------------------

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

        // Set player variable
        Player player = event.getPlayer();

        player.setGravity(true);

        // Give the player a random item every 10 seconds
        worm.me.Server.getPlugin().generating.add(event.getPlayer());
        Generator generator = new Generator(plugin);
        generator.generateItem(player);

        Tips tips = new Tips();
        tips.beginTipSending(player);

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
        worm.me.Server.getPlugin().generating.remove(e.getPlayer());

        PersistentDataContainer container = p.getPersistentDataContainer();

        FeatherDash featherDash = new FeatherDash();
        featherDash.cancelDash(e.getPlayer().getUniqueId());

        container.set(new NamespacedKey(plugin, "repsess"), PersistentDataType.LONG_ARRAY, new long[]{System.currentTimeMillis()/1000L, container.get(new NamespacedKey(plugin, "repsess"), PersistentDataType.LONG_ARRAY)[1]});
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){

        /*
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

}
