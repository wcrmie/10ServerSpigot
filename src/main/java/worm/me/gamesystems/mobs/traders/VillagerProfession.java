package worm.me.gamesystems.mobs.traders;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.BlockItemDataProperties;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import worm.me.gamesystems.items.ExperienceBottle;
import worm.me.utils.Item;

import java.util.List;
import java.util.Map;

public enum VillagerProfession {
    ARMORER("Armorer",
            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.GOLD_INGOT), 3, 10)
                    .newTrade(new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.GOLD_NUGGET, 6), 3, 10)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.COAL, 4), 5, 50)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 3), new ItemStack(Material.RAW_IRON, 6), 5, 20)
                    .newTrade(new Item.ItemBuilder(new ItemStack(Material.GOLDEN_BOOTS)).addEnchantment(Enchantment.MENDING, 1).build(), new ItemStack(Material.GOLD_NUGGET, 5), 1, 5)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.GOLD_INGOT, 2), 3, 10)
                    .newTrade(new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.GOLD_NUGGET, 12), 3, 10)
                    .newTrade(new Item.ItemBuilder(new ItemStack(Material.GOLDEN_LEGGINGS)).addEnchantment(Enchantment.MENDING, 1).build(), new ItemStack(Material.GOLD_NUGGET, 8), 1, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 4), new ItemStack(Material.SMOOTH_STONE, 12), 5, 20)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 2), new ItemStack(Material.ARMOR_STAND, 1), 5, 15)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new Item.ItemBuilder(new ItemStack(Material.GOLDEN_CHESTPLATE)).addEnchantment(Enchantment.MENDING, 1).build(), new ItemStack(Material.GOLD_INGOT), 1, 5)
                    .newTrade(new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.GOLD_INGOT, 3), 3, 10)
                    .newTrade(new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.GOLD_INGOT, 3), 3, 7)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 4), new ItemStack(Material.IRON_INGOT, 1), 5, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 1), new ItemStack(Material.COPPER_INGOT, 4), 10, 15)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new Item.ItemBuilder(new ItemStack(Material.IRON_BOOTS)).addEnchantment(Enchantment.PROTECTION, 1).build(), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 6), new ItemStack(Material.EMERALD), 3, 10)
                    .newTrade(new ItemStack(Material.BELL), new ItemStack(Material.GOLD_NUGGET, 2), 3, 10)
                    .newTrade(new ItemStack(Material.IRON_NUGGET, 2), new ItemStack(Material.GOLD_NUGGET, 1), 5, 5)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new Item.ItemBuilder(new ItemStack(Material.IRON_LEGGINGS)).addEnchantment(Enchantment.PROTECTION, 1).build(), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new Item.ItemBuilder(new ItemStack(Material.IRON_CHESTPLATE)).addEnchantment(Enchantment.PROTECTION, 1).build(), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.BLAST_FURNACE), new ItemStack(Material.GOLD_INGOT, 3), 3, 5)
                    .newTrade(new ItemStack(Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE), new ItemStack(Material.EMERALD, 5), 3, 10)
                    .build()),

    BUTCHER("Butcher",
            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.MUTTON, 2), 5, 25)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.CHICKEN, 3), 5, 25)
                    .newTrade(new Item.ItemBuilder(new ItemStack(Material.RABBIT_STEW, 2)).addComponent(DataComponentTypes.MAX_STACK_SIZE, 16).build(), new ItemStack(Material.GOLD_NUGGET, 3), 5, 25)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.COAL, 3), 5, 15)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.RABBIT), 5, 25)
                    .newTrade(new Item.ItemBuilder(Item.create(Material.STICK, ChatColor.WHITE + "Slim Jim")).addComponent(DataComponentTypes.CONSUMABLE, Consumable.consumable().hasConsumeParticles(true).consumeSeconds(3).build()).addComponent(DataComponentTypes.FOOD, FoodProperties.food().nutrition(5).saturation(2).build()).build(), new ItemStack(Material.GOLD_NUGGET, 8), 1, 5)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.BEEF), 5, 20)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.PORKCHOP, 2), 5, 20)
                    .newTrade(new ItemStack(Material.LEATHER), new ItemStack(Material.GOLD_NUGGET, 3), 3, 10)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), Item.create(Material.QUARTZ, ChatColor.WHITE + "Salt"), 5, 7)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.SWEET_BERRIES, 4), 5, 10)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.DRIED_KELP, 5), 5, 10)
                    .newTrade(new ItemStack(Material.COOKED_MUTTON, 8), new ItemStack(Material.GOLD_NUGGET, 5), 5, 10)
                    .newTrade(new ItemStack(Material.COOKED_CHICKEN, 8), new ItemStack(Material.GOLD_NUGGET, 5), 5, 10)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.RABBIT_FOOT), new ItemStack(Material.GOLD_INGOT, 3), 3, 5)
                    .newTrade(new Item.ItemBuilder(new ItemStack(Material.COOKED_BEEF, 8)).addComponent(DataComponentTypes.FOOD, FoodProperties.food().nutrition(10).saturation(14).build()).setName(ChatColor.WHITE + "Filet Mignon").build(), new ItemStack(Material.EMERALD, 1), 5, 10)
                    .build()),

    CARTOGRAPHER("Cartographer",
            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 3), new ItemStack(Material.PAPER, 12), 5, 50)
                    .newTrade(new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.COMPASS), 2, 10)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.COAL, 4), 5, 50)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 3), new ItemStack(Material.RAW_IRON, 6), 5, 20)
                    .newTrade(new ItemStack(Material.GOLDEN_BOOTS), new ItemStack(Material.GOLD_NUGGET, 5), 1, 5)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.GOLD_INGOT, 2), 3, 10)
                    .newTrade(new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.GOLD_NUGGET, 12), 3, 10)
                    .newTrade(new ItemStack(Material.GOLDEN_LEGGINGS), new ItemStack(Material.GOLD_NUGGET, 8), 1, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 4), new ItemStack(Material.SMOOTH_STONE, 12), 5, 20)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 2), new ItemStack(Material.ARMOR_STAND, 1), 5, 15)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.GOLDEN_CHESTPLATE), new ItemStack(Material.GOLD_INGOT), 1, 5)
                    .newTrade(new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.GOLD_INGOT, 3), 3, 10)
                    .newTrade(new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.GOLD_INGOT, 3), 3, 7)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 4), new ItemStack(Material.IRON_INGOT, 1), 5, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 1), new ItemStack(Material.COPPER_INGOT, 4), 10, 15)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.EMERALD), 5, 10)
                    .newTrade(new ItemStack(Material.BELL), new ItemStack(Material.GOLD_NUGGET, 2), 3, 10)
                    .newTrade(new ItemStack(Material.IRON_NUGGET, 2), new ItemStack(Material.GOLD_NUGGET, 1), 5, 5)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.BLAST_FURNACE), new ItemStack(Material.GOLD_INGOT, 3), 3, 5)
                    .newTrade(new ItemStack(Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE), new ItemStack(Material.EMERALD, 1), 1, 10)
                    .build()),
    CLERIC("Cleric",
            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.ROTTEN_FLESH, 12), 3, 10)
                    .newTrade(new ItemStack(Material.REDSTONE, 8), new ItemStack(Material.GOLD_NUGGET), 3, 10)
                    .newTrade(new Item.ItemBuilder(ExperienceBottle.emptyBottle).setAmount(6).build(), new ItemStack(Material.GOLD_NUGGET), 5, 20)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new Item.ItemBuilder(ExperienceBottle.fullBottle).setAmount(3).build(), new ItemStack(Material.AMETHYST_SHARD), 5, 10)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 1), new Item.ItemBuilder(ExperienceBottle.fullBottle).setAmount(3).build(), 5, 20)
                    .newTrade(new ItemStack(Material.LAPIS_LAZULI, 4), new ItemStack(Material.GOLD_NUGGET, 1), 5, 10)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 1), new ItemStack(Material.ARMADILLO_SCUTE, 4), 5, 15)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.BLAZE_ROD), new Item.ItemBuilder(ExperienceBottle.fullBottle).setAmount(16).build(), 3, 10)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 6), new ItemStack(Material.ENDER_PEARL), 3, 10)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.GUNPOWDER, 16), 5, 7)
                    .newTrade(new ItemStack(Material.GHAST_TEAR), new ItemStack(Material.GOLD_NUGGET, 6), 5, 5)
                    .newTrade(new ItemStack(Material.IRON_NUGGET), new Item.ItemBuilder(ExperienceBottle.fullBottle).setAmount(3).build(), 9, 5)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.TURTLE_HELMET), new ItemStack(Material.GOLD_INGOT, 3), 1, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.NETHER_WART, 6), 5, 10)
                    .newTrade(new ItemStack(Material.IRON_NUGGET, 3), ExperienceBottle.fullBottle, 5, 10)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.DRAGON_BREATH), new ItemStack(Material.GOLD_NUGGET, 6), 3, 5)
                    .newTrade(new ItemStack(Material.FLOW_POTTERY_SHERD), new ItemStack(Material.EMERALD, 1), 1, 10)
                    .newTrade(new ItemStack(Material.OMINOUS_TRIAL_KEY), new ItemStack(Material.EMERALD, 1), 1, 10)
                    .newTrade(new ItemStack(Material.OMINOUS_BOTTLE), new ItemStack(Material.EMERALD, 16), 1, 5)
                    .build()),
    FARMER("Farmer",
            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.WHEAT, 8), 5, 50)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.POTATO, 18), 5, 20)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.CARROT, 18), 5, 20)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.BEETROOT, 5), 5, 20)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.BREAD, 4), new ItemStack(Material.GOLD_NUGGET, 1), 5, 10)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.CARVED_PUMPKIN, 5), 5, 20)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.MELON_SLICE, 16), 5, 15)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.SWEET_BERRIES), new ItemStack(Material.GOLD_INGOT), 1, 5)
                    .newTrade(new ItemStack(Material.MELON_SEEDS), new ItemStack(Material.GOLD_INGOT, 1), 3, 10)
                    .newTrade(new ItemStack(Material.BEETROOT_SEEDS), new ItemStack(Material.GOLD_INGOT, 3), 1, 7)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.EGG, 4), 5, 10)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.HONEYCOMB, 3), 5, 15)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.BONE, 6), 5, 10)
                    .newTrade(new ItemStack(Material.BAKED_POTATO, 6), new ItemStack(Material.GOLD_NUGGET, 1), 5, 5)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.BLAST_FURNACE), new ItemStack(Material.GOLD_INGOT, 3), 3, 5)
                    .newTrade(new ItemStack(Material.GOLDEN_CARROT, 8), new ItemStack(Material.EMERALD, 1), 2, 10)
                    .build()),
    FISHERMAN("Fisherman",
            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.COD, 6), 5, 50)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.SALMON, 4), 5, 20)
                    .newTrade(new ItemStack(Material.KELP), new ItemStack(Material.GOLD_NUGGET, 5), 1, 15)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.PUFFERFISH, 2), 5, 10)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.TROPICAL_FISH), 5, 10)
                    .newTrade(new ItemStack(Material.BRAIN_CORAL_BLOCK, 8), new ItemStack(Material.GOLD_NUGGET), 3, 8)
                    .newTrade(new ItemStack(Material.BUBBLE_CORAL_BLOCK, 8), new ItemStack(Material.GOLD_NUGGET), 3, 8)
                    .newTrade(new ItemStack(Material.FIRE_CORAL_BLOCK, 8), new ItemStack(Material.GOLD_NUGGET), 3, 8)
                    .newTrade(new ItemStack(Material.HORN_CORAL_BLOCK, 8), new ItemStack(Material.GOLD_NUGGET), 3, 8)
                    .newTrade(new ItemStack(Material.TUBE_CORAL_BLOCK, 8), new ItemStack(Material.GOLD_NUGGET), 3, 8)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new Item.ItemBuilder(ExperienceBottle.fullBottle).setAmount(8).build(), new ItemStack(Material.GOLD_INGOT), 3, 20)
                    .newTrade(new ItemStack(Material.NAUTILUS_SHELL), new ItemStack(Material.GOLD_NUGGET, 5), 3, 10)
                    .newTrade(new ItemStack(Material.SEA_PICKLE, 8), new ItemStack(Material.GOLD_NUGGET), 3, 7)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.STRING, 4), 3, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 5), new ItemStack(Material.SADDLE), 5, 10)
                    .newTrade(new ItemStack(Material.BELL), new ItemStack(Material.GOLD_NUGGET, 1), 3, 10)
                    .newTrade(new ItemStack(Material.LILY_PAD, 8), new ItemStack(Material.GOLD_NUGGET, 1), 5, 5)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.GOLD_INGOT, 3), new ItemStack(Material.AXOLOTL_BUCKET), 1, 5)
                    .newTrade(new ItemStack(Material.GOLD_INGOT, 3), new ItemStack(Material.TADPOLE_BUCKET, 5), 1, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.OAK_BOAT, 1), 3, 5)
                    .newTrade(new ItemStack(Material.HEART_OF_THE_SEA), new ItemStack(Material.EMERALD, 16), 1, 50)
                    .build()),
    FLETCHER("Fletcher",
            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.STICK, 24), 5, 10)
                    .newTrade(new ItemStack(Material.ARROW, 24), new ItemStack(Material.GOLD_NUGGET), 3, 10)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.FLINT, 12), 5, 50)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.STRING, 6), 5, 10)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.FEATHER, 8), 5, 10)
                    .newTrade(new ItemStack(Material.BOW), new ItemStack(Material.GOLD_NUGGET, 1), 1, 5)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.CROSSBOW), new ItemStack(Material.GOLD_NUGGET, 3), 1, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.TRIPWIRE_HOOK, 2), 3, 10)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new Item.PotionBuilder(new ItemStack(Material.TIPPED_ARROW, 8)).addEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE, 1, 1)).build(), new ItemStack(Material.GOLD_NUGGET, 3), 1, 5)
                    .newTrade(new Item.PotionBuilder(new ItemStack(Material.TIPPED_ARROW, 8)).addEffect(new PotionEffect(PotionEffectType.POISON, 15, 1)).build(), new ItemStack(Material.GOLD_NUGGET, 3), 1, 5)
                    .newTrade(new Item.PotionBuilder(new ItemStack(Material.TIPPED_ARROW, 8)).addEffect(new PotionEffect(PotionEffectType.NAUSEA, 15, 1)).build(), new ItemStack(Material.GOLD_NUGGET, 3), 1, 5)
                    .newTrade(new Item.PotionBuilder(new ItemStack(Material.TIPPED_ARROW, 8)).addEffect(new PotionEffect(PotionEffectType.SLOWNESS, 15, 1)).build(), new ItemStack(Material.GOLD_NUGGET, 3), 1, 5)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.GUNPOWDER, 10), 5, 5)
                    .newTrade(new Item.ItemBuilder(new ItemStack(Material.BOW)).addEnchantment(Enchantment.INFINITY, 1).build(), new ItemStack(Material.EMERALD, 12), 1, 10)
                    .newTrade(new Item.ItemBuilder(new ItemStack(Material.CROSSBOW)).addEnchantment(Enchantment.MULTISHOT, 1).build(), new ItemStack(Material.EMERALD, 10), 1, 10)
                    .build()),
    LEATHERWORKER("Leatherworker",
            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.BUNDLE), new ItemStack(Material.GOLD_NUGGET, 3), 3, 10)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.LEATHER, 2), 3, 10)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.COAL, 4), 5, 50)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 3), new ItemStack(Material.RAW_IRON, 6), 5, 20)
                    .newTrade(new ItemStack(Material.GOLDEN_BOOTS), new ItemStack(Material.GOLD_NUGGET, 5), 1, 5)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.GOLD_INGOT, 2), 3, 10)
                    .newTrade(new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.GOLD_NUGGET, 12), 3, 10)
                    .newTrade(new ItemStack(Material.GOLDEN_LEGGINGS), new ItemStack(Material.GOLD_NUGGET, 8), 1, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 4), new ItemStack(Material.SMOOTH_STONE, 12), 5, 20)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 2), new ItemStack(Material.ARMOR_STAND, 1), 5, 15)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.GOLDEN_CHESTPLATE), new ItemStack(Material.GOLD_INGOT), 1, 5)
                    .newTrade(new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.GOLD_INGOT, 3), 3, 10)
                    .newTrade(new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.GOLD_INGOT, 3), 3, 7)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 4), new ItemStack(Material.IRON_INGOT, 1), 5, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 1), new ItemStack(Material.COPPER_INGOT, 4), 10, 15)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.EMERALD), 5, 10)
                    .newTrade(new ItemStack(Material.BELL), new ItemStack(Material.GOLD_NUGGET, 2), 3, 10)
                    .newTrade(new ItemStack(Material.IRON_NUGGET, 2), new ItemStack(Material.GOLD_NUGGET, 1), 5, 5)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.BLAST_FURNACE), new ItemStack(Material.GOLD_INGOT, 3), 3, 5)
                    .newTrade(new ItemStack(Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE), new ItemStack(Material.EMERALD, 1), 1, 10)
                    .build()),
    LIBRARIAN("Librarian",
            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.GOLD_INGOT), 3, 10)
                    .newTrade(new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.GOLD_NUGGET, 6), 3, 10)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.COAL, 4), 5, 50)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 3), new ItemStack(Material.RAW_IRON, 6), 5, 20)
                    .newTrade(new ItemStack(Material.GOLDEN_BOOTS), new ItemStack(Material.GOLD_NUGGET, 5), 1, 5)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.GOLD_INGOT, 2), 3, 10)
                    .newTrade(new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.GOLD_NUGGET, 12), 3, 10)
                    .newTrade(new ItemStack(Material.GOLDEN_LEGGINGS), new ItemStack(Material.GOLD_NUGGET, 8), 1, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 4), new ItemStack(Material.SMOOTH_STONE, 12), 5, 20)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 2), new ItemStack(Material.ARMOR_STAND, 1), 5, 15)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.GOLDEN_CHESTPLATE), new ItemStack(Material.GOLD_INGOT), 1, 5)
                    .newTrade(new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.GOLD_INGOT, 3), 3, 10)
                    .newTrade(new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.GOLD_INGOT, 3), 3, 7)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 4), new ItemStack(Material.IRON_INGOT, 1), 5, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 1), new ItemStack(Material.COPPER_INGOT, 4), 10, 15)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.EMERALD), 5, 10)
                    .newTrade(new ItemStack(Material.BELL), new ItemStack(Material.GOLD_NUGGET, 2), 3, 10)
                    .newTrade(new ItemStack(Material.IRON_NUGGET, 2), new ItemStack(Material.GOLD_NUGGET, 1), 5, 5)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.BLAST_FURNACE), new ItemStack(Material.GOLD_INGOT, 3), 3, 5)
                    .newTrade(new ItemStack(Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE), new ItemStack(Material.EMERALD, 1), 1, 10)
                    .build()),
    MASON("Mason",
            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.GOLD_INGOT), 3, 10)
                    .newTrade(new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.GOLD_NUGGET, 6), 3, 10)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.COAL, 4), 5, 50)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 3), new ItemStack(Material.RAW_IRON, 6), 5, 20)
                    .newTrade(new ItemStack(Material.GOLDEN_BOOTS), new ItemStack(Material.GOLD_NUGGET, 5), 1, 5)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.GOLD_INGOT, 2), 3, 10)
                    .newTrade(new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.GOLD_NUGGET, 12), 3, 10)
                    .newTrade(new ItemStack(Material.GOLDEN_LEGGINGS), new ItemStack(Material.GOLD_NUGGET, 8), 1, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 4), new ItemStack(Material.SMOOTH_STONE, 12), 5, 20)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 2), new ItemStack(Material.ARMOR_STAND, 1), 5, 15)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.GOLDEN_CHESTPLATE), new ItemStack(Material.GOLD_INGOT), 1, 5)
                    .newTrade(new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.GOLD_INGOT, 3), 3, 10)
                    .newTrade(new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.GOLD_INGOT, 3), 3, 7)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 4), new ItemStack(Material.IRON_INGOT, 1), 5, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 1), new ItemStack(Material.COPPER_INGOT, 4), 10, 15)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.EMERALD), 5, 10)
                    .newTrade(new ItemStack(Material.BELL), new ItemStack(Material.GOLD_NUGGET, 2), 3, 10)
                    .newTrade(new ItemStack(Material.IRON_NUGGET, 2), new ItemStack(Material.GOLD_NUGGET, 1), 5, 5)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.BLAST_FURNACE), new ItemStack(Material.GOLD_INGOT, 3), 3, 5)
                    .newTrade(new ItemStack(Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE), new ItemStack(Material.EMERALD, 1), 1, 10)
                    .build()),
    SHEPHERD("Shepherd",
            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.GOLD_INGOT), 3, 10)
                    .newTrade(new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.GOLD_NUGGET, 6), 3, 10)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.COAL, 4), 5, 50)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 3), new ItemStack(Material.RAW_IRON, 6), 5, 20)
                    .newTrade(new ItemStack(Material.GOLDEN_BOOTS), new ItemStack(Material.GOLD_NUGGET, 5), 1, 5)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.GOLD_INGOT, 2), 3, 10)
                    .newTrade(new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.GOLD_NUGGET, 12), 3, 10)
                    .newTrade(new ItemStack(Material.GOLDEN_LEGGINGS), new ItemStack(Material.GOLD_NUGGET, 8), 1, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 4), new ItemStack(Material.SMOOTH_STONE, 12), 5, 20)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 2), new ItemStack(Material.ARMOR_STAND, 1), 5, 15)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.GOLDEN_CHESTPLATE), new ItemStack(Material.GOLD_INGOT), 1, 5)
                    .newTrade(new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.GOLD_INGOT, 3), 3, 10)
                    .newTrade(new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.GOLD_INGOT, 3), 3, 7)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 4), new ItemStack(Material.IRON_INGOT, 1), 5, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 1), new ItemStack(Material.COPPER_INGOT, 4), 10, 15)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.EMERALD), 5, 10)
                    .newTrade(new ItemStack(Material.BELL), new ItemStack(Material.GOLD_NUGGET, 2), 3, 10)
                    .newTrade(new ItemStack(Material.IRON_NUGGET, 2), new ItemStack(Material.GOLD_NUGGET, 1), 5, 5)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.BLAST_FURNACE), new ItemStack(Material.GOLD_INGOT, 3), 3, 5)
                    .newTrade(new ItemStack(Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE), new ItemStack(Material.EMERALD, 1), 1, 10)
                    .build()),
    TOOLSMITH("Toolsmith",
            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.GOLD_INGOT), 3, 10)
                    .newTrade(new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.GOLD_NUGGET, 6), 3, 10)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.COAL, 4), 5, 50)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 3), new ItemStack(Material.RAW_IRON, 6), 5, 20)
                    .newTrade(new ItemStack(Material.GOLDEN_BOOTS), new ItemStack(Material.GOLD_NUGGET, 5), 1, 5)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.GOLD_INGOT, 2), 3, 10)
                    .newTrade(new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.GOLD_NUGGET, 12), 3, 10)
                    .newTrade(new ItemStack(Material.GOLDEN_LEGGINGS), new ItemStack(Material.GOLD_NUGGET, 8), 1, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 4), new ItemStack(Material.SMOOTH_STONE, 12), 5, 20)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 2), new ItemStack(Material.ARMOR_STAND, 1), 5, 15)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.GOLDEN_CHESTPLATE), new ItemStack(Material.GOLD_INGOT), 1, 5)
                    .newTrade(new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.GOLD_INGOT, 3), 3, 10)
                    .newTrade(new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.GOLD_INGOT, 3), 3, 7)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 4), new ItemStack(Material.IRON_INGOT, 1), 5, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 1), new ItemStack(Material.COPPER_INGOT, 4), 10, 15)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.EMERALD), 5, 10)
                    .newTrade(new ItemStack(Material.BELL), new ItemStack(Material.GOLD_NUGGET, 2), 3, 10)
                    .newTrade(new ItemStack(Material.IRON_NUGGET, 2), new ItemStack(Material.GOLD_NUGGET, 1), 5, 5)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.BLAST_FURNACE), new ItemStack(Material.GOLD_INGOT, 3), 3, 5)
                    .newTrade(new ItemStack(Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE), new ItemStack(Material.EMERALD, 1), 1, 10)
                    .build()),
    WEAPONSMITH("Weaponsmith",
            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.STONE_SWORD), new ItemStack(Material.GOLD_NUGGET), 3, 10)
                    .newTrade(new ItemStack(Material.STONE_AXE), new ItemStack(Material.GOLD_NUGGET), 3, 10)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.COAL, 4), 5, 50)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 3), new ItemStack(Material.RAW_IRON, 6), 5, 20)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.IRON_SWORD), new ItemStack(Material.GOLD_NUGGET, 5), 3, 10)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 4), new ItemStack(Material.SMOOTH_STONE, 12), 5, 20)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 1), new ItemStack(Material.FLINT, 16), 5, 15)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.GOLDEN_CHESTPLATE), new ItemStack(Material.GOLD_INGOT), 1, 5)
                    .newTrade(new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.GOLD_INGOT, 3), 3, 10)
                    .newTrade(new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.GOLD_INGOT, 3), 3, 7)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 4), new ItemStack(Material.IRON_INGOT, 1), 5, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 1), new ItemStack(Material.COPPER_INGOT, 4), 10, 15)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.EMERALD), 5, 10)
                    .newTrade(new ItemStack(Material.BELL), new ItemStack(Material.GOLD_NUGGET, 2), 3, 10)
                    .newTrade(new ItemStack(Material.IRON_NUGGET, 2), new ItemStack(Material.GOLD_NUGGET, 1), 5, 5)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.BLAST_FURNACE), new ItemStack(Material.GOLD_INGOT, 3), 3, 5)
                    .newTrade(new ItemStack(Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE), new ItemStack(Material.EMERALD, 1), 1, 10)
                    .build());

    private final String professionName;
    private final Map<MerchantRecipe, Integer> firstTrades;
    private final Map<MerchantRecipe, Integer> secondTrades;
    private final Map<MerchantRecipe, Integer> thirdTrades;
    private final Map<MerchantRecipe, Integer> fourthTrades;
    private final Map<MerchantRecipe, Integer> fifthTrades;

    VillagerProfession(String professionName, Map<MerchantRecipe, Integer> firstTrades, Map<MerchantRecipe, Integer> secondTrades, Map<MerchantRecipe, Integer> thirdTrades, Map<MerchantRecipe, Integer> fourthTrades, Map<MerchantRecipe, Integer> fifthTrades) {
        this.professionName = professionName;
        this.firstTrades = firstTrades;
        this.secondTrades = secondTrades;
        this.thirdTrades = thirdTrades;
        this.fourthTrades = fourthTrades;
        this.fifthTrades = fifthTrades;

    }

    public String getName() {
        return professionName;
    }

    public Map<MerchantRecipe, Integer> getFirstTrades() {
        return firstTrades;
    }

    public Map<MerchantRecipe, Integer> getSecondTrades() {
        return secondTrades;
    }

    public Map<MerchantRecipe, Integer> getThirdTrades() {
        return thirdTrades;
    }

    public Map<MerchantRecipe, Integer> getFourthTrades() {
        return fourthTrades;
    }

    public Map<MerchantRecipe, Integer> getFifthTrades() {
        return fifthTrades;
    }
}
