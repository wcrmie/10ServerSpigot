package worm.me.gamesystems.recipes;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;
import worm.me.gamesystems.fishing.rods.BreezeRod;
import worm.me.gamesystems.items.ExperienceBottle;
import worm.me.utils.Item;

import java.util.List;

public enum ShapedRecipe {

    //    shapedRecipeMap.put(new ItemStack(Material.RAIL, 4), List.of(List.of(new ItemStack(Material.STICK), new ItemStack(Material.AIR), new ItemStack(Material.STICK), new ItemStack(Material.STICK), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.STICK),
//                new ItemStack(Material.STICK), new ItemStack(Material.AIR), new ItemStack(Material.STICK)), List.of(Item.create(Material.STONE, "CHEAP_RAILS"))));
//
//    // ID 1
//        shapedRecipeMap.put(new ItemStack(Material.POWERED_RAIL, 2), List.of(List.of(new ItemStack(Material.STICK), new ItemStack(Material.REDSTONE), new ItemStack(Material.STICK), new ItemStack(Material.STICK), new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.STICK),
//               new ItemStack(Material.STICK), new ItemStack(Material.AIR), new ItemStack(Material.STICK)), List.of(Item.create(Material.STONE, "CHEAP_POWERED_RAILS"))));
//
//    // ID 2
//        shapedRecipeMap.put(new ItemStack(Material.OAK_SAPLING, 1), List.of(List.of(new ItemStack(Material.WHEAT_SEEDS), new ItemStack(Material.STICK), new ItemStack(Material.WHEAT_SEEDS), new ItemStack(Material.STICK), new ItemStack(Material.DIRT), new ItemStack(Material.STICK),
//                new ItemStack(Material.WHEAT_SEEDS), new ItemStack(Material.WATER_BUCKET), new ItemStack(Material.WHEAT_SEEDS)), List.of(Item.create(Material.STONE, "OAK_SAPLING"))));
//        shapedRecipeMap.put(
//    createSlimeBoots(),
//                List.of(
//                        List.of(
//                        new ItemStack(Material.AIR),
//                                new ItemStack(Material.AIR),
//                                new ItemStack(Material.AIR),
//                                new ItemStack(Material.SLIME_BALL),
//                                new ItemStack(Material.AIR),
//                                new ItemStack(Material.SLIME_BALL),
//                                new ItemStack(Material.SLIME_BALL),
//                                new ItemStack(Material.AIR),
//                                new ItemStack(Material.SLIME_BALL)
//                        ),
//                                List.of(Item.create(Material.STONE, "SLIME_BOOTS"))
//            )
//            );
//
//
//    // ID 3, 4, and 5
//        shapedRecipeMap.put(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1), List.of(List.of(new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.AIR), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.IRON_NUGGET),
//                new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.IRON_NUGGET)), List.of(Item.create(Material.STONE, "CHAINMAIL_CHESTPLATE"))));
//
//        shapedRecipeMap.put(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1), List.of(List.of(new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.AIR), new ItemStack(Material.IRON_NUGGET),
//                new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.AIR), new ItemStack(Material.IRON_NUGGET)), List.of(Item.create(Material.STONE, "CHAINMAIL_LEGGINGS"))));
//
//        shapedRecipeMap.put(new ItemStack(Material.CHAINMAIL_BOOTS, 1), List.of(List.of(new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.AIR), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.AIR), new ItemStack(Material.IRON_NUGGET),
//                new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR)), List.of(Item.create(Material.STONE, "CHAINMAIL_BOOTS"))));
//
//    // ID 6, 7, 8
//    ItemStack emeraldChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
//    ItemStack emeraldLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
//    ItemStack emeraldBoots = new ItemStack(Material.LEATHER_BOOTS);
//
//    LeatherArmorMeta meta = (LeatherArmorMeta) emeraldChestplate.getItemMeta();
//        assert meta != null;
//        meta.setDisplayName("<green>Emerald Chestplate");
//        meta.setUnbreakable(true);
//        meta.setColor(Color.GREEN);
//        meta.setMaxStackSize(4);
//
//        emeraldChestplate.setItemMeta(meta);
//
//        shapedRecipeMap.put(emeraldChestplate, List.of(List.of(new ItemStack(Material.EMERALD), new ItemStack(Material.AIR), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD),
//                new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD)), List.of(Item.create(Material.STONE, "EMERALD_CHESTPLATE"))));
//
//        meta.setDisplayName("<green>Emerald Leggings");
//        meta.setMaxStackSize(4);
//        emeraldLeggings.setItemMeta(meta);
//
//        shapedRecipeMap.put(emeraldLeggings, List.of(List.of(new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.AIR), new ItemStack(Material.EMERALD),
//                new ItemStack(Material.EMERALD), new ItemStack(Material.AIR), new ItemStack(Material.EMERALD)), List.of(Item.create(Material.STONE, "EMERALD_LEGGINGS"))));
//
//        meta.setDisplayName("<green>Emerald Boots");
//        meta.setMaxStackSize(4);
//        emeraldBoots.setItemMeta(meta);
//
//        shapedRecipeMap.put(emeraldBoots, List.of(List.of(new ItemStack(Material.EMERALD), new ItemStack(Material.AIR), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.AIR), new ItemStack(Material.EMERALD),
//                new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR)), List.of(Item.create(Material.STONE, "EMERALD_BOOTS"))));
//
//    // ID 9
//        shapedRecipeMap.put(new ItemStack(Material.JUKEBOX), List.of(List.of(new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.OAK_PLANKS),
//                new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.IRON_NUGGET),new ItemStack(Material.OAK_PLANKS),
//                new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.OAK_PLANKS)), List.of(Item.create(Material.STONE, "JUKEBOX"))));
//
//        shapedRecipeMap.put(
//                (new ItemStack(Material.MINECART, 1)),
//            List.of(
//            List.of(
//            new ItemStack(Material.AIR),
//                                new ItemStack(Material.AIR),
//                                new ItemStack(Material.AIR),
//                                new ItemStack(Material.IRON_NUGGET),
//                                new ItemStack(Material.AIR),
//                                new ItemStack(Material.IRON_NUGGET),
//                                new ItemStack(Material.IRON_NUGGET),
//                                new ItemStack(Material.IRON_NUGGET),
//                                new ItemStack(Material.IRON_NUGGET)
//                        ),
//                                List.of(Item.create(Material.STONE, "MINECART"))
//            )
//            );
//
//        shapedRecipeMap.put(
//                (new ItemStack(Material.AMETHYST_SHARD, 1)),
//            List.of(
//            List.of(
//            new ItemStack(Material.SMALL_AMETHYST_BUD),
//                                new ItemStack(Material.SMALL_AMETHYST_BUD),
//                                new ItemStack(Material.AIR),
//                                new ItemStack(Material.SMALL_AMETHYST_BUD),
//                                new ItemStack(Material.SMALL_AMETHYST_BUD),
//                                new ItemStack(Material.AIR),
//                                new ItemStack(Material.AIR),
//                                new ItemStack(Material.AIR),
//                                new ItemStack(Material.AIR)
//                        ),
//                                List.of(Item.create(Material.STONE, "AMETHYST_SHARD"))
//            )
//            );
//
//        shapedRecipeMap.put(
//                (new ItemStack(Material.BUDDING_AMETHYST, 1)),
//            List.of(
//            List.of(
//            new ItemStack(Material.AMETHYST_SHARD),
//                                new ItemStack(Material.AMETHYST_BLOCK),
//                                new ItemStack(Material.AMETHYST_SHARD),
//                                new ItemStack(Material.AMETHYST_BLOCK),
//                                new ItemStack(Material.AMETHYST_BLOCK),
//                                new ItemStack(Material.AMETHYST_BLOCK),
//                                new ItemStack(Material.AMETHYST_SHARD),
//                                new ItemStack(Material.AMETHYST_BLOCK),
//                                new ItemStack(Material.AMETHYST_SHARD)
//                        ),
//                                List.of(Item.create(Material.STONE, "BUDDING_AMETHYST"))
//            )
//            );
//
//        shapedRecipeMap.put(
//                (new ItemStack(Material.HOPPER, 1)),
//            List.of(
//            List.of(
//            new ItemStack(Material.IRON_NUGGET),
//                                new ItemStack(Material.AIR),
//                                new ItemStack(Material.IRON_NUGGET),
//                                new ItemStack(Material.IRON_NUGGET),
//                                new ItemStack(Material.CHEST),
//                                new ItemStack(Material.IRON_NUGGET),
//                                new ItemStack(Material.AIR),
//                                new ItemStack(Material.IRON_NUGGET),
//                                new ItemStack(Material.AIR)
//                        ),
//                                List.of(Item.create(Material.STONE, "HOPPER"))
//            )
//            );
//
//        shapedRecipeMap.put(
//                (new ItemStack(Material.LODESTONE, 1)),
//            List.of(
//            List.of(
//            new ItemStack(Material.CHISELED_STONE_BRICKS),
//                                new ItemStack(Material.CHISELED_STONE_BRICKS),
//                                new ItemStack(Material.CHISELED_STONE_BRICKS),
//                                new ItemStack(Material.CHISELED_STONE_BRICKS),
//                                new ItemStack(Material.IRON_BLOCK),
//                                new ItemStack(Material.CHISELED_STONE_BRICKS),
//                                new ItemStack(Material.CHISELED_STONE_BRICKS),
//                                new ItemStack(Material.CHISELED_STONE_BRICKS),
//                                new ItemStack(Material.CHISELED_STONE_BRICKS)
//                        ),
//                                List.of(Item.create(Material.STONE, "LODESTONE"))
//            )
//            );
//
//        shapedRecipeMap.put(
//                (new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1)),
//            List.of(
//            List.of(
//            new ItemStack(Material.GOLD_BLOCK),
//                                new ItemStack(Material.GOLD_BLOCK),
//                                new ItemStack(Material.GOLD_BLOCK),
//                                new ItemStack(Material.GOLD_BLOCK),
//                                new ItemStack(Material.GOLDEN_APPLE),
//                                new ItemStack(Material.GOLD_BLOCK),
//                                new ItemStack(Material.GOLD_BLOCK),
//                                new ItemStack(Material.GOLD_BLOCK),
//                                new ItemStack(Material.GOLD_BLOCK)
//                        ),
//                                List.of(Item.create(Material.STONE, "ENCHANTED_GOLDEN_APPLE"))
//            )
//            );
//
//        shapedRecipeMap.put(
//                (new ItemStack(Material.GOLDEN_CARROT, 5)),
//            List.of(
//            List.of(
//            new ItemStack(Material.GOLD_NUGGET),
//                                new ItemStack(Material.CARROT),
//                                new ItemStack(Material.GOLD_NUGGET),
//                                new ItemStack(Material.CARROT),
//                                new ItemStack(Material.CARROT),
//                                new ItemStack(Material.CARROT),
//                                new ItemStack(Material.GOLD_NUGGET),
//                                new ItemStack(Material.CARROT),
//                                new ItemStack(Material.GOLD_NUGGET)
//                        ),
//                                List.of(Item.create(Material.STONE, "GOLDEN_CARROT"))
//            )
//            );
//
//        shapedRecipeMap.put(
//                (new ItemStack(Material.SPONGE, 4)),
//            List.of(
//            List.of(
//            new ItemStack(Material.KELP),
//                                new ItemStack(Material.KELP),
//                                new ItemStack(Material.KELP),
//                                new ItemStack(Material.KELP),
//                                new ItemStack(Material.SPONGE),
//                                new ItemStack(Material.KELP),
//                                new ItemStack(Material.KELP),
//                                new ItemStack(Material.KELP),
//                                new ItemStack(Material.KELP)
//                        ),
//                                List.of(Item.create(Material.STONE, "IM_SPUNCHPOP"))
//            )
//            );
//
//    ItemStack snowball = new ItemStack(Material.SNOWBALL);
//
//    ItemMeta snowmeta = snowball.getItemMeta();
//
//        snowmeta.setDisplayName(ChatColor.WHITE + "Portable Snowman");
//
//        snowmeta.getPersistentDataContainer().set(new NamespacedKey(Server.getPlugin(), "snowman"), PersistentDataType.STRING, "snowman");
//
//        snowball.setItemMeta(snowmeta);
//
//        shapedRecipeMap.put(snowball, List.of(List.of(new ItemStack(Material.AIR), new ItemStack(Material.CARVED_PUMPKIN), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.SNOW_BLOCK), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.SNOW_BLOCK), new ItemStack(Material.AIR)), List.of(Item.create(Material.STONE, "PORTABLE_SNOWMAN"))));
//
//        shapedRecipeMap.put(new ItemStack(Material.TNT), List.of(List.of(new ItemStack(Material.SAND), new ItemStack(Material.GUNPOWDER), new ItemStack(Material.SAND), new ItemStack(Material.GUNPOWDER), new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.GUNPOWDER),
//                new ItemStack(Material.SAND), new ItemStack(Material.GUNPOWDER), new ItemStack(Material.SAND)), List.of(Item.create(Material.STONE, "TNT"))));
//
//        shapedRecipeMap.put(Item.create(Material.WOODEN_AXE, null, "<gray>Upgrade this tool by clicking on it with", "<gray>16 Cobblestone"), List.of(List.of(new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.AIR),new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.STICK),new ItemStack(Material.AIR),new ItemStack(Material.AIR),new ItemStack(Material.STICK),new ItemStack(Material.AIR)), List.of(Item.create(Material.STONE, "WOODEN_AXE"))));
//        shapedRecipeMap.put(Item.create(Material.WOODEN_SHOVEL, null, "<gray>Upgrade this tool by clicking on it with", "<gray>16 Cobblestone"), List.of(List.of(new ItemStack(Material.AIR),new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.AIR),new ItemStack(Material.AIR),new ItemStack(Material.STICK),new ItemStack(Material.AIR),new ItemStack(Material.AIR),new ItemStack(Material.STICK),new ItemStack(Material.AIR)), List.of(Item.create(Material.STONE, "WOODEN_SHOVEL"))));
//
//        shapedRecipeMap.put(BreezeRod.createBreezeRod(), List.of(List.of(new ItemStack(Material.AIR), new ItemStack(Material.AIR),new ItemStack(Material.BREEZE_ROD),new ItemStack(Material.AIR),new ItemStack(Material.BREEZE_ROD),new ItemStack(Material.STRING),new ItemStack(Material.BREEZE_ROD),new ItemStack(Material.AIR),new ItemStack(Material.STRING)), List.of(Item.create(Material.STONE, "BREEZE_FISHING_ROD"))));
//
//        shapedRecipeMap.put(new ItemStack(Material.PISTON), List.of(List.of(new ItemStack(Material.OAK_PLANKS), new ItemStack(Material.OAK_PLANKS), new ItemStack(Material.OAK_PLANKS),
//                new ItemStack(Material.COBBLESTONE), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.COBBLESTONE),
//                new ItemStack(Material.COBBLESTONE), new ItemStack(Material.REDSTONE), new ItemStack(Material.COBBLESTONE)), List.of(Item.create(Material.STONE, "PISTON"))));
//
//        shapedRecipeMap.put(new Item.ItemBuilder(ExperienceBottle.emptyBottle).setAmount(3).build(), List.of(List.of(new ItemStack(Material.GLASS), new ItemStack(Material.AIR), new ItemStack(Material.GLASS),
//                new ItemStack(Material.AIR), new ItemStack(Material.GLASS), new ItemStack(Material.AIR),
//                new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR))
//            , List.of(Item.create(Material.STONE, "GLASS_BOTTLE"))));

    // TODO: put all above recipes into enums

    WOODEN_PICKAXE(
            "WOODEN_PICKAXE",
            Item.create(
                    Material.WOODEN_PICKAXE,
                    null,
                    "<gray>Upgrade this tool by clicking on it with", "<gray>16 Cobblestone"
            ),
            List.of(
                    new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.OAK_PLANKS),
                    new ItemStack(Material.AIR),new ItemStack(Material.STICK),new ItemStack(Material.AIR),
                    new ItemStack(Material.AIR),new ItemStack(Material.STICK),new ItemStack(Material.AIR)
            )
    ),
    WOODEN_SWORD(
            "WOODEN_SWORD",
            Item.create(
                    Material.WOODEN_SWORD,
                    null,
                    "<gray>Upgrade this tool by clicking on it with", "<gray>16 Cobblestone"
            ),
            List.of(
                    new ItemStack(Material.AIR),new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.AIR),
                    new ItemStack(Material.AIR),new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.AIR),
                    new ItemStack(Material.AIR),new ItemStack(Material.STICK),new ItemStack(Material.AIR)
            )
    ),
    WOODEN_AXE(
            "WOODEN_AXE",
            Item.create(
                    Material.WOODEN_AXE,
                    null,
                    "<gray>Upgrade this tool by clicking on it with", "<gray>16 Cobblestone"
            ),
            List.of(
                    new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.AIR),
                    new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.STICK),new ItemStack(Material.AIR),
                    new ItemStack(Material.AIR),new ItemStack(Material.STICK),new ItemStack(Material.AIR)
            )

    );

    private final String key;
    private final ItemStack result;
    private final List<ItemStack> ingredients;

    ShapedRecipe(String key, ItemStack result, List<ItemStack> ingredients) {
        this.key = key;
        this.result = result;
        this.ingredients = ingredients;
    }

    public String getKey() {
        return key;
    }

    public ItemStack getResult() {
        return result;
    }

    public List<ItemStack> getIngredients() {
        return ingredients;
    }




}
