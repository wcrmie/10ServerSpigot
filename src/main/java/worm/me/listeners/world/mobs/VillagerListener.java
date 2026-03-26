package worm.me.listeners.world.mobs;

import org.bukkit.*;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import worm.me.Server;
import worm.me.gamesystems.world.mob.traders.Trader;
import worm.me.gamesystems.world.mob.traders.VillagerProfession;
import worm.me.playersystems.attributes.LocatorBar;
import worm.me.utils.Item;

import java.util.Arrays;

import static worm.me.gamesystems.world.mob.traders.Trader.getGenetics;

public class VillagerListener implements Listener {

    @EventHandler
    public void onVillagerSpawn(EntitySpawnEvent event) {

        if (event.getEntity() instanceof Villager villy) {
            LocatorBar.attachVillager(villy);

            if (villy.getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.CURED)) {

                World world = villy.getWorld();
                Location loc = villy.getLocation();

                villy.remove();

                world.spawn(loc, Villager.class, v -> {
                    v.setProfession(Villager.Profession.NITWIT);
                });

                return;

            }

            villy.setProfession(Villager.Profession.NITWIT);

            if (!Arrays.equals(getGenetics(villy), new int[VillagerProfession.values().length])) villy.setProfession(Villager.Profession.NONE);

        }

    }

    @EventHandler
    public void onVillagerBreed(EntityBreedEvent event){

        if (event.getEntity() instanceof Villager child){

            Villager mother = (Villager) event.getMother();
            Villager father = (Villager) event.getFather();

            int[] motherGenetics = getGenetics(mother);
            int[] fatherGenetics = getGenetics(father);

            Trader.addGenetics(child, motherGenetics);
            Trader.addGenetics(child, fatherGenetics);

        }
    }

    @EventHandler
    public void onVillagerLevel(VillagerAcquireTradeEvent event) {

        // TODO: create custom event when trades are acquired, void trades for the same level.

        Villager villager = (Villager) event.getEntity();

        int level = villager.getVillagerLevel();

        MerchantRecipe trade = Trader.replaceTrade(villager, level);

        if (trade == null) {
            trade = new MerchantRecipe(new Item.ItemBuilder(new ItemStack(Material.BARRIER)).setName(ChatColor.RED + "ERROR -- PLS CONTACT ADMINS").build(), 0);
            trade.addIngredient(new ItemStack(Material.BARRIER));
        }

        event.setRecipe(Trader.replaceTrade(villager, level));

        Server.getPlugin().getLogger().info(villager.getRecipes().size() + " recipes");

    }

    @EventHandler
    public void onProfessionChange(VillagerCareerChangeEvent event) {
        Villager villager = event.getEntity();
        Villager.Profession currentProf = villager.getProfession();
        Villager.Profession newProf = event.getProfession();

        if (currentProf != Villager.Profession.NONE && currentProf != Villager.Profession.NITWIT) {
            event.setCancelled(true);
            return;
        }

        if (newProf == Villager.Profession.NONE || newProf == Villager.Profession.NITWIT) {
            return;
        }

        NamespacedKey professionKey = newProf.getKey();
        int[] genetics = getGenetics(villager);

        boolean hasGeneticMatch = false;
        for (VillagerProfession myEnum : VillagerProfession.values()) {
            if (myEnum.getBukkitProfession().getKey().equals(professionKey)) {
                int index = myEnum.ordinal();
                if (index < genetics.length && genetics[index] == 1) {
                    hasGeneticMatch = true;
                }
                break;
            }
        }

        if (!hasGeneticMatch) {
            event.setCancelled(true);
        }
    }

}
