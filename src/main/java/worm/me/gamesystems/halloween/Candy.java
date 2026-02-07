package worm.me.gamesystems.halloween;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import worm.me.Server;
import worm.me.utils.Item;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public enum Candy {
    CANDY_CORN("Candy Corn", "Tastes fishy...", Material.TROPICAL_FISH, 0.5F, 200, 1, 16),
    CHOCOLATE_BAR("Nether Chocolate Bar", "", Material.NETHER_BRICK, 1.0F, 160, 2, 16),
    CARAMEL("Werthers", "Grandma's favorite candy.", Material.GLOW_BERRIES, 0.75F, 240, 1, 16),
    COOKIE("\"Scary Cookie\"", "Was there a razor blade baked into it? What makes it scary?", Material.COOKIE, -1, 400, 3, 8),
    CANDY_APPLE("Candy Apple", "It's not candy, but it is an apple.", Material.GOLDEN_APPLE, -1, 160, 3, 4),
    PEPPERMINT("Peppermint", "Is it Christmas already?", Material.RED_GLAZED_TERRACOTTA, 0.5F, 300, 2, 16),
    PIXIE_STIX("Pixie Stix", "Literally just a stick of sugar.", Material.BREEZE_ROD, 0.25F, 100, 5, 8),
    SKITTLES("Skittles", "Taste the rainbow... or the red I guess...", Material.NETHER_WART, 0.5F, 200, 2, 16),
    JAWBREAKER("Jawbreaker", "Hard as a rock.", Material.BLUE_EGG, 600, 1200, 3, 1);

    private final String displayName;
    private final String lore;
    private final Material material;
    private final float eatTime; // in seconds -- an eat time of -1 means default eat time
    private final int sugarRushDuration; // in seconds
    private final int speedLevel;
    private final int stackSize;


    Candy(String displayName, String lore, Material material, float eatTime, int sugarRushDuration, int speedLevel, int stackSize) {
        this.displayName = displayName;
        this.lore = lore;
        this.material = material;
        this.eatTime = eatTime;
        this.sugarRushDuration = sugarRushDuration;
        this.speedLevel = speedLevel;
        this.stackSize = stackSize;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLore() {
        return lore;
    }

    public Material getMaterial() {
        return material;
    }

    public float getEatTime() {
        return eatTime;
    }

    public int getSugarRushDuration() {
        return sugarRushDuration;
    }

    public int getSpeedLevel() {
        return speedLevel;
    }

    public int getStackSize() {
        return stackSize;
    }

    public static class CandyFactory {

        public ItemStack create(Candy candy) {

            float time = candy.getEatTime();
            if (time < 0) time = 1.61F;

            ItemStack item = new Item.ItemBuilder(
                    Item.create(candy.getMaterial(), candy.getDisplayName(), ChatColor.GRAY + candy.getLore()))
                    .addComponent(DataComponentTypes.MAX_STACK_SIZE, candy.getStackSize())
                    .addComponent(DataComponentTypes.CONSUMABLE, Consumable.consumable()
                            .consumeSeconds(time)
                            .addEffect(ConsumeEffect.applyStatusEffects(List.of(new PotionEffect(PotionEffectType.SPEED, candy.getSugarRushDuration(), candy.getSpeedLevel())), 1))
                            .build())
                    .addComponent(DataComponentTypes.FOOD, FoodProperties.food()
                            .nutrition(3)
                            .build())
                    .build();

            ItemMeta meta = item.getItemMeta();
                    meta.getPersistentDataContainer().set(new NamespacedKey(Server.getPlugin(), "candy"), PersistentDataType.STRING, candy.name());
                    item.setItemMeta(meta);

            return item;

        }
    }

    public static ItemStack selectCandy() {

        return new CandyFactory().create(Candy.values()[ThreadLocalRandom.current().nextInt(0, Candy.values().length - 1)]);

    }

    public static boolean isCandy(ItemStack item) {

        return item.getPersistentDataContainer().has(new NamespacedKey(Server.getPlugin(), "candy"), PersistentDataType.STRING);
    }

}
