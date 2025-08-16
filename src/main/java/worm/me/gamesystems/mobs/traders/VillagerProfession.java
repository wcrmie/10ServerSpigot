package worm.me.gamesystems.mobs.traders;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.Map;

public enum VillagerProfession {
    ARMORER("Armorer",
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

    BUTCHER("Butcher",
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
