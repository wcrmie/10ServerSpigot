package worm.me.gamesystems.halloween;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import worm.me.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PumpkinPatch {

    private static final float chance = 25;

    public static float getChance() {
        return chance;
    }

    public static boolean tryGrowUndead(Location loc) {

        int chance = ThreadLocalRandom.current().nextInt(0, 100);

        if (chance <= getChance()) {

            loc.add(0.5,0.5,0.5);
            growUndead(loc);
            return true;

        }
        return false;
    }

    public static boolean isHalloweenUndead(Monster monster) {

        PersistentDataContainer data = monster.getPersistentDataContainer();

        return data.has(new NamespacedKey(Server.getPlugin(), "halloween_undead"), org.bukkit.persistence.PersistentDataType.INTEGER);

    }

    private static void growUndead(Location loc) {

        World world = loc.getWorld();

        Monster undead = world.spawn(loc, ScaryMob.create());

        ItemStack item = new ItemStack(Material.CARVED_PUMPKIN);

        if (ThreadLocalRandom.current().nextInt(0, 5) == 5) {
            item = new ItemStack(Material.JACK_O_LANTERN);
        }

        undead.getEquipment().setHelmet(item);
        undead.getEquipment().setItemInMainHand(new ItemStack(Material.WOODEN_SWORD));

        PersistentDataContainer data = undead.getPersistentDataContainer();

        data.set(new NamespacedKey(Server.getPlugin(), "halloween_undead"), org.bukkit.persistence.PersistentDataType.INTEGER, 1);

    }

    public static class ScaryMob {

        protected static Class<? extends Monster> create() {

            if (ThreadLocalRandom.current().nextInt(0, 100) < 50) {
                return Skeleton.class;
            }

            return Zombie.class;
        }

        public static List<ItemStack> getDrops() {

            List<ItemStack> items = new ArrayList<>();

            items.add(Candy.selectCandy());

            if (ThreadLocalRandom.current().nextInt(100) < 25) {
                items.add(Candy.selectCandy());
            }

            items.add(new ItemStack(Material.CARVED_PUMPKIN));
            items.add(new ItemStack(Material.PUMPKIN_SEEDS, ThreadLocalRandom.current().nextInt(1, 4)));

            return  items;
        }
    }
}
