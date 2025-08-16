package worm.me.gamesystems.mobs.traders;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
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
                    .newTrade(new Item.ItemBuilder(new ItemStack(Material.GOLDEN_BOOTS)).addEnchantment(Enchantment.UNBREAKING, 2).build(), new ItemStack(Material.GOLD_NUGGET, 5), 1, 5)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.GOLD_INGOT, 2), 3, 10)
                    .newTrade(new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.GOLD_NUGGET, 12), 3, 10)
                    .newTrade(new Item.ItemBuilder(new ItemStack(Material.GOLDEN_LEGGINGS)).addEnchantment(Enchantment.UNBREAKING, 2).build(), new ItemStack(Material.GOLD_NUGGET, 8), 1, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 4), new ItemStack(Material.SMOOTH_STONE, 12), 5, 20)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 2), new ItemStack(Material.ARMOR_STAND, 1), 5, 15)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new Item.ItemBuilder(new ItemStack(Material.GOLDEN_CHESTPLATE)).addEnchantment(Enchantment.UNBREAKING, 2).build(), new ItemStack(Material.GOLD_INGOT), 1, 5)
                    .newTrade(new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.GOLD_INGOT, 3), 3, 10)
                    .newTrade(new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.GOLD_INGOT, 3), 3, 7)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 4), new ItemStack(Material.IRON_INGOT, 1), 5, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET, 1), new ItemStack(Material.COPPER_INGOT, 4), 10, 15)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new Item.ItemBuilder(new ItemStack(Material.IRON_BOOTS)).addEnchantment(Enchantment.PROTECTION, 1).build(), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.EMERALD), 5, 10)
                    .newTrade(new ItemStack(Material.BELL), new ItemStack(Material.GOLD_NUGGET, 2), 3, 10)
                    .newTrade(new ItemStack(Material.IRON_NUGGET, 2), new ItemStack(Material.GOLD_NUGGET, 1), 5, 5)
                    .build(),

            new Trader.TradeBuilder()
                    .newTrade(new Item.ItemBuilder(new ItemStack(Material.IRON_LEGGINGS)).addEnchantment(Enchantment.PROTECTION, 1).build(), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new Item.ItemBuilder(new ItemStack(Material.IRON_CHESTPLATE)).addEnchantment(Enchantment.PROTECTION, 1).build(), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.BLAST_FURNACE), new ItemStack(Material.GOLD_INGOT, 3), 3, 5)
                    .newTrade(new ItemStack(Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE), new ItemStack(Material.EMERALD, 1), 1, 10)
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
                    .newTrade(new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.GOLD_INGOT, 5), 1, 5)
                    .newTrade(new ItemStack(Material.BLAST_FURNACE), new ItemStack(Material.GOLD_INGOT, 3), 3, 5)
                    .newTrade(new Item.ItemBuilder(new ItemStack(Material.COOKED_BEEF, 8)).addComponent(DataComponentTypes.FOOD, FoodProperties.food().nutrition(10).saturation(14).build()).setName(ChatColor.WHITE + "Ribeye Steak").build(), new ItemStack(Material.EMERALD, 1), 1, 10)
                    .build()),

    CARTOGRAPHER("Cartographer",
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
    CLERIC("Cleric",
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
    FARMER("Farmer",
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
    FISHERMAN("Fisherman",
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
    FLETCHER("Fletcher",
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
    LEATHERWORKER("Leatherworker",
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
        return null;
    }

    public Map<MerchantRecipe, Integer> getSecondTrades() {
        return null;
    }

    public Map<MerchantRecipe, Integer> getThirdTrades() {
        return null;
    }

    public Map<MerchantRecipe, Integer> getFourthTrades() {
        return null;
    }

    public Map<MerchantRecipe, Integer> getFifthTrades() {
        return null;
    }
}
