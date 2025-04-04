package worm.me.gamesystems.recipes;

import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;
import worm.me.GuiConstructor;
import worm.me.Server;

import java.util.*;

public class ShapedRecipes {

    public static Map<ItemStack, List<List<ItemStack>>> shapedRecipeMap = new HashMap<>();

    public void addRecipes(){

        // ID 0
        shapedRecipeMap.put(new ItemStack(Material.RAIL, 4), List.of(List.of(new ItemStack(Material.STICK), new ItemStack(Material.AIR), new ItemStack(Material.STICK), new ItemStack(Material.STICK), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.STICK),
                new ItemStack(Material.STICK), new ItemStack(Material.AIR), new ItemStack(Material.STICK)), List.of(GuiConstructor.createGuiItem(Material.STONE, "CHEAP_RAILS"))));

        // ID 1
        shapedRecipeMap.put(new ItemStack(Material.POWERED_RAIL, 2), List.of(List.of(new ItemStack(Material.STICK), new ItemStack(Material.REDSTONE), new ItemStack(Material.STICK), new ItemStack(Material.STICK), new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.STICK),
               new ItemStack(Material.STICK), new ItemStack(Material.AIR), new ItemStack(Material.STICK)), List.of(GuiConstructor.createGuiItem(Material.STONE, "CHEAP_POWERED_RAILS"))));

        // ID 2
        shapedRecipeMap.put(new ItemStack(Material.OAK_SAPLING, 1), List.of(List.of(new ItemStack(Material.WHEAT_SEEDS), new ItemStack(Material.STICK), new ItemStack(Material.WHEAT_SEEDS), new ItemStack(Material.STICK), new ItemStack(Material.DIRT), new ItemStack(Material.STICK),
                new ItemStack(Material.WHEAT_SEEDS), new ItemStack(Material.WATER_BUCKET), new ItemStack(Material.WHEAT_SEEDS)), List.of(GuiConstructor.createGuiItem(Material.STONE, "OAK_SAPLING"))));
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
                                new ItemStack(Material.SLIME_BLOCK),
                                new ItemStack(Material.AIR),
                                new ItemStack(Material.SLIME_BLOCK)
                        ),
                        List.of(GuiConstructor.createGuiItem(Material.STONE, "SLIME_BOOTS"))
                )
        );


        // ID 3, 4, and 5
        shapedRecipeMap.put(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1), List.of(List.of(new ItemStack(Material.IRON_INGOT), new ItemStack(Material.AIR), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_NUGGET),
                new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.IRON_INGOT)), List.of(GuiConstructor.createGuiItem(Material.STONE, "CHAINMAIL_CHESTPLATE"))));

        shapedRecipeMap.put(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1), List.of(List.of(new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.AIR), new ItemStack(Material.IRON_NUGGET),
                new ItemStack(Material.IRON_INGOT), new ItemStack(Material.AIR), new ItemStack(Material.IRON_INGOT)), List.of(GuiConstructor.createGuiItem(Material.STONE, "CHAINMAIL_LEGGINGS"))));

        shapedRecipeMap.put(new ItemStack(Material.CHAINMAIL_BOOTS, 1), List.of(List.of(new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.AIR), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.AIR), new ItemStack(Material.IRON_INGOT),
                new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR)), List.of(GuiConstructor.createGuiItem(Material.STONE, "CHAINMAIL_BOOTS"))));

        // ID 6, 7, 8
        ItemStack emeraldChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack emeraldLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack emeraldBoots = new ItemStack(Material.LEATHER_BOOTS);

        LeatherArmorMeta meta = (LeatherArmorMeta) emeraldChestplate.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GREEN + "Emerald Chestplate");
        meta.setUnbreakable(true);
        meta.setColor(Color.GREEN);

        emeraldChestplate.setItemMeta(meta);

        shapedRecipeMap.put(emeraldChestplate, List.of(List.of(new ItemStack(Material.EMERALD), new ItemStack(Material.AIR), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD),
                new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD)), List.of(GuiConstructor.createGuiItem(Material.STONE, "EMERALD_CHESTPLATE"))));

        meta.setDisplayName(ChatColor.GREEN + "Emerald Leggings");

        emeraldLeggings.setItemMeta(meta);

        shapedRecipeMap.put(emeraldLeggings, List.of(List.of(new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.AIR), new ItemStack(Material.EMERALD),
                new ItemStack(Material.EMERALD), new ItemStack(Material.AIR), new ItemStack(Material.EMERALD)), List.of(GuiConstructor.createGuiItem(Material.STONE, "EMERALD_LEGGINGS"))));

        meta.setDisplayName(ChatColor.GREEN + "Emerald Boots");

        emeraldBoots.setItemMeta(meta);

        shapedRecipeMap.put(emeraldBoots, List.of(List.of(new ItemStack(Material.EMERALD), new ItemStack(Material.AIR), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.AIR), new ItemStack(Material.EMERALD),
                new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR)), List.of(GuiConstructor.createGuiItem(Material.STONE, "EMERALD_BOOTS"))));

        // ID 9
        shapedRecipeMap.put(new ItemStack(Material.JUKEBOX), List.of(List.of(new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.OAK_PLANKS),
                new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.IRON_INGOT),new ItemStack(Material.OAK_PLANKS),
                new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.OAK_PLANKS),new ItemStack(Material.OAK_PLANKS)), List.of(GuiConstructor.createGuiItem(Material.STONE, "JUKEBOX"))));

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
                        List.of(GuiConstructor.createGuiItem(Material.STONE, "MINECART"))
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
                        List.of(GuiConstructor.createGuiItem(Material.STONE, "AMETHYST_SHARD"))
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
                        List.of(GuiConstructor.createGuiItem(Material.STONE, "BUDDING_AMETHYST"))
                )
        );

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

                System.out.println(Arrays.toString(shape.split(",")));

                for (int i = 0; i < 9; i++) {
                    if (values.getFirst().get(i).getType() != Material.AIR){

                        char c = (char) (i + 64);

                        if (values.getFirst().get(i).getType() == Material.OAK_PLANKS) {
                            shapedRecipe.setIngredient(c, new RecipeChoice.MaterialChoice(Tag.PLANKS));
                        } else {
                            shapedRecipe.setIngredient(c, new RecipeChoice.ExactChoice(values.getFirst().get(i)));
                        }
                    }

                }

                Bukkit.addRecipe(shapedRecipe);

            }
        }
    }

    public void removeRecipes(){

        for (ItemStack i : shapedRecipeMap.keySet()) {
            Bukkit.removeRecipe(new NamespacedKey(Server.getPlugin(), shapedRecipeMap.get(i).getFirst().toString()));
        }

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

        Bukkit.removeRecipe(new NamespacedKey("minecraft", "iron_chestplate"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "iron_leggings"));
        Bukkit.removeRecipe(new NamespacedKey("minecraft", "iron_boots"));


    }

    private ItemStack createSlimeBoots() {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        ItemMeta meta = boots.getItemMeta();

        // Set name and color
        meta.setDisplayName(ChatColor.GREEN + "Slime Boots");
        ((LeatherArmorMeta)meta).setColor(Color.LIME);

        // Add NBT tag
        meta.getPersistentDataContainer().set(
                new NamespacedKey(Server.getPlugin(), "slimeboots"),
                PersistentDataType.STRING,
                "true"
        );

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "You must have the slime boot ability to use this");
        meta.setLore(lore);

        boots.setItemMeta(meta);
        return boots;
    }
}
