package worm.me.listeners.player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.event.inventory.UseItemEvent;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import net.kyori.adventure.util.TriState;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;
import worm.me.gamesystems.fishing.FishingTables;
import worm.me.gamesystems.halloween.Candy;
import worm.me.gamesystems.items.ExperienceBottle;
import worm.me.gamesystems.items.gear.armor.ArmorBreak;
import worm.me.gamesystems.items.gear.tools.LodestoneTeleporter;
import worm.me.gamesystems.items.gear.tools.ToolsEvents;
import worm.me.gamesystems.world.PlayArea;
import worm.me.gamesystems.world.blocks.ArtBlocks;
import worm.me.gamesystems.world.border.OutOfBorDer;
import worm.me.gamesystems.world.mob.traders.Trader;
import worm.me.gamesystems.world.mob.traders.CustomProfession;
import worm.me.playersystems.abilities.Abilities;
import worm.me.playersystems.abilities.Ability;
import worm.me.playersystems.abilities.AbilityAttemptEvent;
import worm.me.playersystems.abilities.passive.SlimeBoots;
import worm.me.playersystems.attributes.SizeModifier;
import worm.me.data.Vault;
import worm.me.data.VoidPlayer;
import worm.me.data.levels.Levels;
import worm.me.playersystems.saddle.SaddleSystem;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;



public class GameplayListener implements Listener {




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
    public void onConsumeEvent(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();

        if (item.getType().equals(Material.ENCHANTED_GOLDEN_APPLE)) {
            VoidPlayer.registerStats(player, 5);
        }
    }

    @EventHandler
    public void onArrowShot(EntityShootBowEvent e){
        if (e.getEntity() instanceof Player shooter) {
            e.getProjectile().getPersistentDataContainer().set(new NamespacedKey(Server.getPlugin(), "arrow"), PersistentDataType.STRING, "arrow");
            VoidPlayer.registerStats(shooter, 3);

            if (e.getBow().containsEnchantment(Enchantment.INFINITY)) {

                int RNG = ThreadLocalRandom.current().nextInt(0, 1);
                if (RNG == 0) {

                    e.setConsumeItem(true);

                }

            }

        }
    }


    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e){

        Player player = e.getPlayer();

        if (e.getTo().getY() < 190) {
            player.damage(1000000);
        } else {
            boolean allowed = Server.zoneGridManager.isInAllowedGrid(e.getTo());
            if (!allowed) {
                OutOfBorDer.apply(player);
            }
        }

        if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getY() != e.getTo().getY() || e.getFrom().getZ() != e.getTo().getZ()) {
            if (LodestoneTeleporter.isTeleporting(player)) {
                LodestoneTeleporter.cancelTeleport(player);
            }
        }

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;

        boolean isOnCooldown = Abilities.isOnCooldown(player, 0);

        if (isOnCooldown) {
            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
            }
        } else {
            if (!player.getAllowFlight()) {
                if( !isSlot1Empty(player)) {
                    player.setAllowFlight(true);
                    player.setFlyingFallDamage(TriState.TRUE);
                }
            }
        }

    }

    public static boolean isSlot1Empty(Player player) {
        List<Integer> active = Abilities.getActiveAbilities(player);
        int abilityId = active.getFirst();
        return abilityId == Ability.EMPTY.getId();
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent e){
        if (e.getEntity().getShooter() instanceof Player shooter && e.getHitEntity() != null && e.getEntity().getPersistentDataContainer().get(new NamespacedKey(Server.getPlugin(), "arrow"), PersistentDataType.STRING) != null){
            VoidPlayer.registerStats(shooter, 2);
        }
    }

    @EventHandler
    public void onPlayerUseItemEvent(UseItemEvent e) {
        Player p = e.getCause().getFirstPlayer();
        if (e.getItemStack().getType().equals(Material.GLASS_BOTTLE) && p.isSneaking()) {
            e.setCancelled(true);
            ExperienceBottle.fillBottle(p);
        }
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEntityEvent e){

        Player player = e.getPlayer();

        SaddleSystem sshandler = new SaddleSystem(Server.getPlugin());
        sshandler.rightClick(e);


        if (player.getInventory().getItemInMainHand().getType().equals(Material.KNOWLEDGE_BOOK) || player.getInventory().getItemInOffHand().getType().equals(Material.KNOWLEDGE_BOOK)) {

            // dont delete the item
            e.setCancelled(true);

            if (e.getRightClicked() instanceof Villager villager && (villager.getProfession().equals(Villager.Profession.NITWIT) || villager.getProfession().equals(Villager.Profession.NONE))) {

                ItemStack tome = player.getInventory().getItemInMainHand();

                if (!player.getInventory().getItemInMainHand().getType().equals(Material.KNOWLEDGE_BOOK)){
                    tome = player.getInventory().getItemInOffHand();
                }



                PersistentDataContainer pdc = tome.getItemMeta().getPersistentDataContainer();
                int[] genetics = pdc.getOrDefault(new NamespacedKey(Server.getPlugin(), "genetics"), PersistentDataType.INTEGER_ARRAY, new int[CustomProfession.values().length]);

                player.getInventory().setItem(e.getHand(), new ItemStack(Material.AIR));

                villager.getWorld().spawn(villager.getLocation(), Villager.class, villager1 -> {

                   Trader.addGenetics(villager1, Trader.getGenetics(villager));
                   Trader.addGenetics(villager1, genetics);
                   villager1.setProfession(Villager.Profession.NONE);
                   if (!villager.isAdult()) {
                       villager1.setBaby();
                   }

                });

                villager.remove();

            }
        }
    }

    @EventHandler
    public void onPlayerPlace(BlockPlaceEvent e){
        Player player = e.getPlayer();

        if (Candy.isCandy(e.getItemInHand())){
            Server.getPlugin().getLogger().info("candy");
            e.setCancelled(true);
        }

        // Height limit for building
        if (player.getGameMode() == GameMode.CREATIVE) return;
        if (Objects.requireNonNull(e.getBlock().getLocation().getWorld()).getName().equals("ul_Void_nether")) return;
        if (e.getBlock().getLocation().getBlockY() > 280 || e.getBlock().getLocation().getBlockY() < PlayArea.getMinHeight() - 1) {
            e.setCancelled(true);
            player.sendMessage(Message.create(MessageType.FAIL, "The build area is between y = 200 and y = 280."));
        }
    }

    @EventHandler
    public void onWaterPlace(PlayerBucketEmptyEvent e) {
        if (e.getBlock().getLocation().getBlockY() > 280 || e.getBlock().getLocation().getBlockY() < PlayArea.getMinHeight() - 1) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(Message.create(MessageType.FAIL, "The build area is between y = 200 and y = 280."));
        }
    }

    @EventHandler
    public void onPlayerSwing(PlayerArmSwingEvent event) {

        Player player = event.getPlayer();

        if (player.isSneaking() && !player.getPassengers().isEmpty()) {

            Entity passenger = player.getPassengers().getFirst();

            SaddleSystem ss = new SaddleSystem(Server.getPlugin());
            ss.throwEntity(passenger, player);

        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e){

        if (e.getDamageSource() instanceof Player attacker && SizeModifier.getSize(attacker) <= 1) {
            e.setDamage(SizeModifier.calculateDamage(attacker, e.getDamage()));
        }

        if (e.getEntity() instanceof Player player) {
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (!player.isSneaking()) {
                    if (player.getInventory().getBoots() != null) {
                        ItemStack item = player.getInventory().getBoots();
                        if (player.getInventory().getBoots().hasItemMeta()) {
                            ItemMeta meta = item.getItemMeta();
                            if (meta != null) {
                                PersistentDataContainer data = meta.getPersistentDataContainer();
                                NamespacedKey key = new NamespacedKey(Server.getPlugin(), "slimeboots");
                                if (data.has(key, PersistentDataType.STRING)) {
                                    SlimeBoots.bounce(player);
                                    e.setCancelled(true);
                                }
                            }
                        }
                    }
                }
            }

            switch (player.getInventory().getHelmet().getType()) {

                case BELL: {

                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BELL_USE, 1F, 2F);
                    break;

                }

                case NOTE_BLOCK: {

                    player.getWorld().playSound(player.getLocation(), Objects.requireNonNull(Instrument.values()[ThreadLocalRandom.current().nextInt(0, 15)].getSound()), 1F, ThreadLocalRandom.current().nextFloat(1.0f,2.0f));
                    break;

                }

                case HONEY_BLOCK: {
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_HONEY_BLOCK_PLACE, 1F, 0.5F);
                    break;
                }

                case CHEST: {

                    int RNG = ThreadLocalRandom.current().nextInt(0, 1);

                    if (RNG == 0) player.getWorld().playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1F, 2F);

                    if (RNG == 1) player.getWorld().playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1F, 2F);

                    break;

                }

                case SLIME_BLOCK: {

                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SLIME_JUMP, 1F, 2F);

                    break;

                }

                case ZOMBIE_HEAD: {

                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_HURT, 1F, 1.3F);
                    break;

                }

                case SKELETON_SKULL: {

                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SKELETON_HURT, 1F, 1.3F);
                    break;

                }

                case CREEPER_HEAD: {


                    int RNG = ThreadLocalRandom.current().nextInt(0, 25);

                    if (RNG == 25) {

                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1F, 1.3F);

                    } else player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CREEPER_HURT, 1F, 1.3F);

                    break;

                }

                case PIGLIN_HEAD: {

                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PIGLIN_HURT, 1F, 1.3F);

                    break;

                }

                case BEE_NEST: {

                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BEE_HURT, 1F, 1.3F);

                    break;

                }

                case DECORATED_POT: {

                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_DECORATED_POT_SHATTER, 1F, 1.3F);
                    break;

                }
                
            }

            ProtectedRegion region = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld())).getRegion("spawn");
            Location loc = player.getLocation();

            // If the player is in spawn, don't damage them
            if (region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDismount(EntityDismountEvent e){

        if (e.getEntity() instanceof Player p){

            PersistentDataContainer pdc = p.getPersistentDataContainer();
            pdc.set(new NamespacedKey(Server.getPlugin(), "recentDismount"), PersistentDataType.LONG, System.currentTimeMillis() + 1000L);

            if (e.getDismounted().getType() == EntityType.ARMOR_STAND){
                e.getDismounted().remove();
                Location loc = p.getLocation();
                loc.setY(p.getLocation().getY() + 1);
                p.teleport(loc);
            }
        }
    }

    @EventHandler
    public void onBreakItem(PlayerItemBreakEvent e){

        if (e.getBrokenItem().getType().toString().replaceAll("BOOTS", "█").replaceAll("LEGGINGS", "█").replaceAll("CHESTPLATE", "█").contains("█")) {
            ArmorBreak armorBreak = new ArmorBreak();
            armorBreak.handleArmorBreak(e);
        }

    }

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent e) {

        if (((Damageable) e.getItem().getItemMeta()).getDamage() + e.getDamage() >= e.getItem().getType().getMaxDurability()) {

            String tool = e.getItem().getType().toString().toLowerCase();

            for (String s : worm.me.Server.tools.keySet()) {
                if (tool.contains(s)){
                    e.setCancelled(true);
                    ToolsEvents toolsEvents = new ToolsEvents();
                    toolsEvents.toolBreak(e.getPlayer(), e);
                    break;
                }
            }

        }

    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e){
        String tool = e.getItemDrop().getItemStack().toString().toLowerCase();

        if (tool.contains("bow") && e.getPlayer().isSneaking()) {
            e.setCancelled(true);
            if (e.getPlayer().isSneaking()) Bukkit.getServer().getPluginManager().callEvent(new AbilityAttemptEvent(10, e.getPlayer()));
        }
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e){
        if (e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if (!p.getPassengers().isEmpty() && p.getPassengers().getFirst().getType() != EntityType.PLAYER){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSnowballThrow(PlayerInteractEvent e){

        //FishThrow fthandler = new FishThrow(plugin);
        //fthandler.handleBucketThrow(e);

        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.SNOWBALL) &&
                    e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().getOrDefault(new NamespacedKey(Server.getPlugin(), "snowman"), PersistentDataType.STRING, "no").equals("snowman")){
                e.setCancelled(true);

                e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);

                Snowball snowball = e.getPlayer().launchProjectile(Snowball.class);
                snowball.getPersistentDataContainer().set(new NamespacedKey(Server.getPlugin(), "snowman"), PersistentDataType.STRING, "snowman");

            }

            Player p = (Player) e.getPlayer();
            if (e.getHand() != org.bukkit.inventory.EquipmentSlot.HAND) {
                if (
                                Objects.requireNonNullElse(e.getClickedBlock(), p.getLocation().getBlock()).getType().toString().contains("WOOL") ||
                                Objects.requireNonNullElse(e.getClickedBlock(), p.getLocation().getBlock()).getType().toString().contains("CONCRETE") ||
                                Objects.requireNonNullElse(e.getClickedBlock(), p.getLocation().getBlock()).getType().toString().contains("CARPET"))
                {
                    ArtBlocks handler = new ArtBlocks(Server.getPlugin());
                    handler.rightClick(e);
                }
            }

        }



        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        Material material = item.getType();

        if (material == Material.WOODEN_PICKAXE || material == Material.WOODEN_AXE || material == Material.WOODEN_SHOVEL) {
            if (item.containsEnchantment(Enchantment.EFFICIENCY)) {
                int level = item.getEnchantmentLevel(Enchantment.EFFICIENCY);
                if (level > 2) {
                    item.removeEnchantment(Enchantment.EFFICIENCY);
                    item.addUnsafeEnchantment(Enchantment.EFFICIENCY, 2);
                }
            }
        }

        if (material == Material.WOODEN_SWORD) {
            if (item.containsEnchantment(Enchantment.SHARPNESS)) {
                int level = item.getEnchantmentLevel(Enchantment.SHARPNESS);
                if (level > 2) {
                    item.removeEnchantment(Enchantment.SHARPNESS);
                    item.addUnsafeEnchantment(Enchantment.SHARPNESS, 2);
                }
            }
        }

    }


    @EventHandler
    public void onSnowballHit(ProjectileHitEvent e){
        if (e.getEntity() instanceof Snowball snowball){
            if (snowball.getPersistentDataContainer().getOrDefault(new NamespacedKey(Server.getPlugin(), "snowman"), PersistentDataType.STRING, "no").equals("snowman")){
                Snowman snowman = e.getEntity().getLocation().getWorld().spawn(e.getEntity().getLocation(), Snowman.class);
                snowman.setDerp(true);
            }
        }
    }

    @EventHandler
    public void onSnowballThrow(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();

        if (projectile instanceof Snowball snowball) {
            if (snowball.getShooter() instanceof Player player) {
                player.setCooldown(Material.SNOWBALL, 1);
            }
        }
    }

    @EventHandler
    public void onSwapHands(PlayerSwapHandItemsEvent e){
        Player p = e.getPlayer();
        if (!p.getPassengers().isEmpty() && p.getPassengers().getFirst().getType() != EntityType.PLAYER){
            e.setCancelled(true);
        }

        try {
            LodestoneTeleporter.cancelTeleport(p);
        } catch (Exception ignored) {
        }

    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {

        if (event.isSneaking()) {
            Player player = event.getPlayer();
            Location playerLoc = player.getLocation();
            BlockFace blockFace = Objects.requireNonNullElse(player.getTargetBlockFace(1), BlockFace.UP);
            if (blockFace.equals(BlockFace.UP) || blockFace.equals(BlockFace.DOWN)) {
                return;
            }




        }
    }

    @EventHandler
    public void onChangeHeldItem(PlayerItemHeldEvent e){
        Player p = e.getPlayer();
        if (!p.getPassengers().isEmpty() && p.getPassengers().getFirst().getType() != EntityType.PLAYER){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onAnvilCombine(PrepareAnvilEvent event) {

        ItemStack result = event.getResult();

        if (result.getItemMeta().isUnbreakable()) {
            event.setResult(null);
            return;
        }

        Enchantment[] enchantments = result.getEnchantments().keySet().toArray(new Enchantment[0]);

        for (Enchantment ench : enchantments) {
            if ((ench.equals(Enchantment.POWER) || ench.equals(Enchantment.PROTECTION) || ench.equals(Enchantment.FEATHER_FALLING)) && result.getEnchantments().get(ench) > 3) {
                event.getResult().removeEnchantment(ench);
                event.getResult().addUnsafeEnchantment(ench, 3);
            }

            if ((ench.equals(Enchantment.SHARPNESS) && result.getEnchantments().get(ench) > 4)) {
                event.getResult().removeEnchantment(ench);
                event.getResult().addUnsafeEnchantment(ench, 4);
            }

            if (ench.equals(Enchantment.UNBREAKING) && result.getEnchantments().get(ench) > 1) {
                if (result.getType() == Material.BOW) {
                    result.removeEnchantment(ench);
                    result.addUnsafeEnchantment(ench, 1);
                }
            }

            if (result.getType().name().startsWith("WOODEN_") && ench.equals(Enchantment.EFFICIENCY) && result.getEnchantments().get(ench) > 2 ){
                event.getResult().removeEnchantment(ench);
                event.getResult().addUnsafeEnchantment(ench, 2);
            }

            if (result.getType().name().startsWith("WOODEN_") && ench.equals(Enchantment.SHARPNESS) && result.getEnchantments().get(ench) > 2 ){
                event.getResult().removeEnchantment(ench);
                event.getResult().addUnsafeEnchantment(ench, 2);
            }

            if (result.getType().name().startsWith("STONE_") && ench.equals(Enchantment.EFFICIENCY) && result.getEnchantments().get(ench) > 4 ){
                event.getResult().removeEnchantment(ench);
                event.getResult().addUnsafeEnchantment(ench, 4);
            }

        }
    }

}
