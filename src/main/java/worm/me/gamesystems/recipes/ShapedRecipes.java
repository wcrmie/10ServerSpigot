package worm.me.gamesystems.recipes;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;
import worm.me.Server;
import worm.me.gamesystems.fishing.rods.BreezeRod;
import worm.me.gamesystems.items.ExperienceBottle;
import worm.me.utils.Item;

import java.util.*;

public class ShapedRecipes {

    public static Map<ItemStack, List<List<ItemStack>>> shapedRecipeMap = new LinkedHashMap<>();

    public void addRecipes(){

        // ID 0
        shapedRecipeMap.put(new ItemStack(Material.RAIL, 4), List.of(List.of(new ItemStack(Material.STICK), new ItemStack(Material.AIR), new ItemStack(Material.STICK), new ItemStack(Material.STICK), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.STICK),
                new ItemStack(Material.STICK), new ItemStack(Material.AIR), new ItemStack(Material.STICK)), List.of(Item.create(Material.STONE, "CHEAP_RAILS"))));

        // ID 1
        shapedRecipeMap.put(new ItemStack(Material.POWERED_RAIL, 2), List.of(List.of(new ItemStack(Material.STICK), new ItemStack(Material.REDSTONE), new ItemStack(Material.STICK), new ItemStack(Material.STICK), new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.STICK),
               new ItemStack(Material.STICK), new ItemStack(Material.AIR), new ItemStack(Material.STICK)), List.of(Item.create(Material.STONE, "CHEAP_POWERED_RAILS"))));

        // ID 2
        shapedRecipeMap.put(new ItemStack(Material.OAK_SAPLING, 1), List.of(List.of(new ItemStack(Material.WHEAT_SEEDS), new ItemStack(Material.STICK), new ItemStack(Material.WHEAT_SEEDS), new ItemStack(Material.STICK), new ItemStack(Material.DIRT), new ItemStack(Material.STICK),
                new ItemStack(Material.WHEAT_SEEDS), new ItemStack(Material.WATER_BUCKET), new ItemStack(Material.WHEAT_SEEDS)), List.of(Item.create(Material.STONE, "OAK_SAPLING"))));
        shapedRecipeMap.put(
                createSlimeBoots(),
                List.of(
                        List.of(
                                new ItemStack(Material.AIR),
                                new ItemStack(Material.AIR),
                                new ItemStack(Material.AIR),
                                new ItemStack(Material.SLIME_BALL),
                                new ItemStack(Material.AIR),
                                new ItemStack(Material.SLIME_BALL),
                                new ItemStack(Material.SLIME_BALL),
                                new ItemStack(Material.AIR),
                                new ItemStack(Material.SLIME_BALL)
                        ),
                        List.of(Item.create(Material.STONE, "SLIME_BOOTS"))
                )
        );


        // ID 3, 4, and 5


        shapedRecipeMap.put(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1), List.of(List.of(new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.AIR), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.IRON_NUGGET),
                new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.IRON_NUGGET)), List.of(Item.create(Material.STONE, "CHAINMAIL_CHESTPLATE"))));

        shapedRecipeMap.put(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1), List.of(List.of(new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.AIR), new ItemStack(Material.IRON_NUGGET),
                new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.AIR), new ItemStack(Material.IRON_NUGGET)), List.of(Item.create(Material.STONE, "CHAINMAIL_LEGGINGS"))));

        shapedRecipeMap.put(new ItemStack(Material.CHAINMAIL_BOOTS, 1), List.of(List.of(new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.AIR), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.AIR), new ItemStack(Material.IRON_NUGGET),
                new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR)), List.of(Item.create(Material.STONE, "CHAINMAIL_BOOTS"))));

                // ID 6, 7, 8
        ItemStack emeraldChestplate = new Item.ItemBuilder(Item.create(Material.LEATHER_CHESTPLATE, "<green>Emerald Chestplate")).setUnbreakable(true).build();
        ItemStack emeraldLeggings = new Item.ItemBuilder(Item.create(Material.LEATHER_LEGGINGS, "<green>Emerald Leggings")).setUnbreakable(true).build();
        ItemStack emeraldBoots = new Item.ItemBuilder(Item.create(Material.LEATHER_BOOTS, "<green>Emerald Boots")).setUnbreakable(true).build();

        LeatherArmorMeta meta = (LeatherArmorMeta) emeraldChestplate.getItemMeta();
        assert meta != null;

        meta.setColor(Color.GREEN);
        meta.setMaxStackSize(4);

        emeraldChestplate.setItemMeta(meta);

        shapedRecipeMap.put(emeraldChestplate, List.of(List.of(new ItemStack(Material.EMERALD), new ItemStack(Material.AIR), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD),
                new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD)), List.of(Item.create(Material.STONE, "EMERALD_CHESTPLATE"))));

        emeraldLeggings.setItemMeta(meta);

        shapedRecipeMap.put(emeraldLeggings, List.of(List.of(new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.AIR), new ItemStack(Material.EMERALD),
                new ItemStack(Material.EMERALD), new ItemStack(Material.AIR), new ItemStack(Material.EMERALD)), List.of(Item.create(Material.STONE, "EMERALD_LEGGINGS"))));

        emeraldBoots.setItemMeta(meta);

        shapedRecipeMap.put(emeraldBoots, List.of(List.of(new ItemStack(Material.EMERALD), new ItemStack(Material.AIR), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.AIR), new ItemStack(Material.EMERALD),
                new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR)), List.of(Item.create(Material.STONE, "EMERALD_BOOTS"))));

        // ID 9
        shapedRecipeMap.put(new ItemStack(Material.JUKEBOX), List.of(List.of(new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.OAK_PLANKS),
                new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.IRON_INGOT),new ItemStack(Material.OAK_PLANKS),
                new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.OAK_PLANKS)), List.of(Item.create(Material.STONE, "JUKEBOX"))));

        shapedRecipeMap.put(
                (new ItemStack(Material.MINECART, 1)),
                List.of(
                        List.of(
                                new ItemStack(Material.AIR),
                                new ItemStack(Material.AIR),
                                new ItemStack(Material.AIR),
                                new ItemStack(Material.IRON_NUGGET),
                                new ItemStack(Material.AIR),
                                new ItemStack(Material.IRON_NUGGET),
                                new ItemStack(Material.IRON_NUGGET),
                                new ItemStack(Material.IRON_NUGGET),
                                new ItemStack(Material.IRON_NUGGET)
                        ),
                        List.of(Item.create(Material.STONE, "MINECART"))
                )
        );

        shapedRecipeMap.put(
                (new ItemStack(Material.AMETHYST_SHARD, 1)),
                List.of(
                        List.of(
                                new ItemStack(Material.SMALL_AMETHYST_BUD),
                                new ItemStack(Material.SMALL_AMETHYST_BUD),
                                new ItemStack(Material.AIR),
                                new ItemStack(Material.SMALL_AMETHYST_BUD),
                                new ItemStack(Material.SMALL_AMETHYST_BUD),
                                new ItemStack(Material.AIR),
                                new ItemStack(Material.AIR),
                                new ItemStack(Material.AIR),
                                new ItemStack(Material.AIR)
                        ),
                        List.of(Item.create(Material.STONE, "AMETHYST_SHARD"))
                )
        );

        shapedRecipeMap.put(
                (new ItemStack(Material.BUDDING_AMETHYST, 1)),
                List.of(
                        List.of(
                                new ItemStack(Material.AMETHYST_SHARD),
                                new ItemStack(Material.AMETHYST_BLOCK),
                                new ItemStack(Material.AMETHYST_SHARD),
                                new ItemStack(Material.AMETHYST_BLOCK),
                                new ItemStack(Material.AMETHYST_BLOCK),
                                new ItemStack(Material.AMETHYST_BLOCK),
                                new ItemStack(Material.AMETHYST_SHARD),
                                new ItemStack(Material.AMETHYST_BLOCK),
                                new ItemStack(Material.AMETHYST_SHARD)
                        ),
                        List.of(Item.create(Material.STONE, "BUDDING_AMETHYST"))
                )
        );

        shapedRecipeMap.put(
                (new ItemStack(Material.HOPPER, 1)),
                List.of(
                        List.of(
                                new ItemStack(Material.IRON_NUGGET),
                                new ItemStack(Material.AIR),
                                new ItemStack(Material.IRON_NUGGET),
                                new ItemStack(Material.IRON_NUGGET),
                                new ItemStack(Material.CHEST),
                                new ItemStack(Material.IRON_NUGGET),
                                new ItemStack(Material.AIR),
                                new ItemStack(Material.IRON_NUGGET),
                                new ItemStack(Material.AIR)
                        ),
                        List.of(Item.create(Material.STONE, "HOPPER"))
                )
        );

        shapedRecipeMap.put(
                (new ItemStack(Material.LODESTONE, 1)),
                List.of(
                        List.of(
                                new ItemStack(Material.CHISELED_STONE_BRICKS),
                                new ItemStack(Material.CHISELED_STONE_BRICKS),
                                new ItemStack(Material.CHISELED_STONE_BRICKS),
                                new ItemStack(Material.CHISELED_STONE_BRICKS),
                                new ItemStack(Material.IRON_BLOCK),
                                new ItemStack(Material.CHISELED_STONE_BRICKS),
                                new ItemStack(Material.CHISELED_STONE_BRICKS),
                                new ItemStack(Material.CHISELED_STONE_BRICKS),
                                new ItemStack(Material.CHISELED_STONE_BRICKS)
                        ),
                        List.of(Item.create(Material.STONE, "LODESTONE"))
                )
        );

        shapedRecipeMap.put(
                (new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1)),
                List.of(
                        List.of(
                                new ItemStack(Material.GOLD_BLOCK),
                                new ItemStack(Material.GOLD_BLOCK),
                                new ItemStack(Material.GOLD_BLOCK),
                                new ItemStack(Material.GOLD_BLOCK),
                                new ItemStack(Material.GOLDEN_APPLE),
                                new ItemStack(Material.GOLD_BLOCK),
                                new ItemStack(Material.GOLD_BLOCK),
                                new ItemStack(Material.GOLD_BLOCK),
                                new ItemStack(Material.GOLD_BLOCK)
                        ),
                        List.of(Item.create(Material.STONE, "ENCHANTED_GOLDEN_APPLE"))
                )
        );

        shapedRecipeMap.put(
                (new ItemStack(Material.GOLDEN_CARROT, 5)),
                List.of(
                        List.of(
                                new ItemStack(Material.GOLD_NUGGET),
                                new ItemStack(Material.CARROT),
                                new ItemStack(Material.GOLD_NUGGET),
                                new ItemStack(Material.CARROT),
                                new ItemStack(Material.CARROT),
                                new ItemStack(Material.CARROT),
                                new ItemStack(Material.GOLD_NUGGET),
                                new ItemStack(Material.CARROT),
                                new ItemStack(Material.GOLD_NUGGET)
                        ),
                        List.of(Item.create(Material.STONE, "GOLDEN_CARROT"))
                )
        );

        shapedRecipeMap.put(
                (new ItemStack(Material.SPONGE, 4)),
                List.of(
                        List.of(
                                new ItemStack(Material.KELP),
                                new ItemStack(Material.KELP),
                                new ItemStack(Material.KELP),
                                new ItemStack(Material.KELP),
                                new ItemStack(Material.SPONGE),
                                new ItemStack(Material.KELP),
                                new ItemStack(Material.KELP),
                                new ItemStack(Material.KELP),
                                new ItemStack(Material.KELP)
                        ),
                        List.of(Item.create(Material.STONE, "IM_SPUNCHPOP"))
                )
        );

        ItemStack snowball = new ItemStack(Material.SNOWBALL);

        ItemMeta snowmeta = snowball.getItemMeta();

        snowmeta.setDisplayName(ChatColor.WHITE + "Portable Snowman");

        snowmeta.getPersistentDataContainer().set(new NamespacedKey(Server.getPlugin(), "snowman"), PersistentDataType.STRING, "snowman");

        snowball.setItemMeta(snowmeta);

        shapedRecipeMap.put(snowball, List.of(List.of(new ItemStack(Material.AIR), new ItemStack(Material.CARVED_PUMPKIN), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.SNOW_BLOCK), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.SNOW_BLOCK), new ItemStack(Material.AIR)), List.of(Item.create(Material.STONE, "PORTABLE_SNOWMAN"))));

        shapedRecipeMap.put(new ItemStack(Material.TNT), List.of(List.of(new ItemStack(Material.SAND), new ItemStack(Material.GUNPOWDER), new ItemStack(Material.SAND), new ItemStack(Material.GUNPOWDER), new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.GUNPOWDER),
                new ItemStack(Material.SAND), new ItemStack(Material.GUNPOWDER), new ItemStack(Material.SAND)), List.of(Item.create(Material.STONE, "TNT"))));

        shapedRecipeMap.put(Item.create(Material.WOODEN_PICKAXE, null, "<gray>Upgrade this tool by clicking on it with", "<gray>16 Cobblestone"), List.of(List.of(new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.AIR),new ItemStack(Material.STICK),new ItemStack(Material.AIR),new ItemStack(Material.AIR),new ItemStack(Material.STICK),new ItemStack(Material.AIR)), List.of(Item.create(Material.STONE, "WOODEN_PICKAXE"))));
        shapedRecipeMap.put(Item.create(Material.WOODEN_AXE, null, "<gray>Upgrade this tool by clicking on it with", "<gray>16 Cobblestone"), List.of(List.of(new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.AIR),new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.STICK),new ItemStack(Material.AIR),new ItemStack(Material.AIR),new ItemStack(Material.STICK),new ItemStack(Material.AIR)), List.of(Item.create(Material.STONE, "WOODEN_AXE"))));
        shapedRecipeMap.put(Item.create(Material.WOODEN_SWORD, null, "<gray>Upgrade this tool by clicking on it with", "<gray>16 Cobblestone"), List.of(List.of(new ItemStack(Material.AIR),new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.AIR),new ItemStack(Material.AIR),new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.AIR),new ItemStack(Material.AIR),new ItemStack(Material.STICK),new ItemStack(Material.AIR)), List.of(Item.create(Material.STONE, "WOODEN_SWORD"))));
        shapedRecipeMap.put(Item.create(Material.WOODEN_SHOVEL, null, "<gray>Upgrade this tool by clicking on it with", "<gray>16 Cobblestone"), List.of(List.of(new ItemStack(Material.AIR),new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.AIR),new ItemStack(Material.AIR),new ItemStack(Material.STICK),new ItemStack(Material.AIR),new ItemStack(Material.AIR),new ItemStack(Material.STICK),new ItemStack(Material.AIR)), List.of(Item.create(Material.STONE, "WOODEN_SHOVEL"))));

        shapedRecipeMap.put(BreezeRod.createBreezeRod(), List.of(List.of(new ItemStack(Material.AIR), new ItemStack(Material.AIR),new ItemStack(Material.BREEZE_ROD),new ItemStack(Material.AIR),new ItemStack(Material.BREEZE_ROD),new ItemStack(Material.STRING),new ItemStack(Material.BREEZE_ROD),new ItemStack(Material.AIR),new ItemStack(Material.STRING)), List.of(Item.create(Material.STONE, "BREEZE_FISHING_ROD"))));

        shapedRecipeMap.put(new ItemStack(Material.PISTON), List.of(List.of(new ItemStack(Material.OAK_PLANKS), new ItemStack(Material.OAK_PLANKS), new ItemStack(Material.OAK_PLANKS),
                new ItemStack(Material.COBBLESTONE), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.COBBLESTONE),
                new ItemStack(Material.COBBLESTONE), new ItemStack(Material.REDSTONE), new ItemStack(Material.COBBLESTONE)), List.of(Item.create(Material.STONE, "PISTON"))));

        shapedRecipeMap.put(new Item.ItemBuilder(ExperienceBottle.emptyBottle).setAmount(3).build(), List.of(List.of(new ItemStack(Material.GLASS), new ItemStack(Material.AIR), new ItemStack(Material.GLASS),
                new ItemStack(Material.AIR), new ItemStack(Material.GLASS), new ItemStack(Material.AIR),
                new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR))
                , List.of(Item.create(Material.STONE, "GLASS_BOTTLE"))));


        shapedRecipes(shapedRecipeMap);

    }

    public void shapedRecipes(Map<ItemStack, List<List<ItemStack>>> recipe) {

        Set<ItemStack> keys = recipe.keySet();

        for (ItemStack key : keys) {

            List<List<ItemStack>> values = recipe.get(key);

            if (Bukkit.getRecipe(new NamespacedKey(Server.getPlugin(), values.get(1).getFirst().getItemMeta().getDisplayName())) == null){
                ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(Server.getPlugin(), values.get(1).getFirst().getItemMeta().getDisplayName()), key);

                String shape = "";

                for (int i = 0; i < 9; i++) {
                    if (values.getFirst().get(i).getType() == Material.AIR){
                        shape =  shape + " ";
                    } else {

                        char c = (char) (i + 64);

                        shape = shape + c;
                    }

                    if ((i + 1) % 3 == 0){
                        shape = shape + ",";
                    }
                }

                shapedRecipe.shape(shape.split(","));

                for (int i = 0; i < 9; i++) {
                    if (values.getFirst().get(i).getType() != Material.AIR){

                        char c = (char) (i + 64);

                        if (values.getFirst().get(i).getType() == Material.OAK_PLANKS) {
                            shapedRecipe.setIngredient(c, new RecipeChoice.MaterialChoice(Tag.PLANKS));
                        } else if (values.getFirst().get(i).getType() == Material.DIRT){
                            shapedRecipe.setIngredient(c, new RecipeChoice.MaterialChoice(Material.DIRT));
                        } else {
                            shapedRecipe.setIngredient(c, new RecipeChoice.ExactChoice(values.getFirst().get(i)));
                        }
                    }

                }

                Bukkit.addRecipe(shapedRecipe);

                Bukkit.removeRecipe(new NamespacedKey(Server.getPlugin(), values.get(1).getFirst().getItemMeta().getDisplayName()));

                Bukkit.addRecipe(shapedRecipe);

            }
        }
    }

    public void removeRecipes(){

        for (ItemStack i : shapedRecipeMap.keySet()) {
            Bukkit.removeRecipe(new NamespacedKey(Server.getPlugin(), shapedRecipeMap.get(i).getFirst().toString()));
        }


        Bukkit.removeRecipe(new NamespacedKey("minecraft", "golden_carrot"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "tnt"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "piston"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "glass_bottle"));

        Bukkit.removeRecipe(new NamespacedKey("minecraft", "hopper"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "minecart"));

        Bukkit.removeRecipe(new NamespacedKey("minecraft", "wooden_pickaxe"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "wooden_axe"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "wooden_shovel"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "wooden_sword"));

        Bukkit.removeRecipe(new NamespacedKey("minecraft", "stone_pickaxe"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "stone_axe"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "stone_shovel"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "stone_sword"));

        Bukkit.removeRecipe(new NamespacedKey("minecraft", "iron_pickaxe"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "iron_axe"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "iron_shovel"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "iron_sword"));

        Bukkit.removeRecipe(new NamespacedKey("minecraft", "diamond_pickaxe"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "diamond_axe"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "diamond_shovel"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "diamond_sword"));

        Bukkit.removeRecipe(new NamespacedKey("minecraft", "golden_pickaxe"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "golden_axe"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "golden_shovel"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "golden_sword"));


        Bukkit.removeRecipe(new NamespacedKey("minecraft", "leather_helmet"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "iron_helmet"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "gold_helmet"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "diamond_helmet"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "copper_helmet"));

        Bukkit.removeRecipe(new NamespacedKey("minecraft", "iron_chestplate"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "iron_leggings"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "iron_boots"));

        Bukkit.removeRecipe(new NamespacedKey("minecraft", "lodestone"));


    }

    private ItemStack createSlimeBoots() {
        ItemStack boots = new Item.ItemBuilder(Item.create(Material.LEATHER_BOOTS, "<green>Slime Boots", "<yellow>All fall damage is nullified while wearing these.", "<gray>[SNEAK]</gray> to cancel a bounce.")).build();
        ItemMeta meta = boots.getItemMeta();

        ((LeatherArmorMeta)meta).setColor(Color.LIME);

        meta.addEnchant(Enchantment.UNBREAKING, 2, true);

        // Add NBT tag
        meta.getPersistentDataContainer().set(
                new NamespacedKey(Server.getPlugin(), "slimeboots"),
                PersistentDataType.STRING,
                "true"
        );

        boots.setItemMeta(meta);
        return boots;
    }
}
