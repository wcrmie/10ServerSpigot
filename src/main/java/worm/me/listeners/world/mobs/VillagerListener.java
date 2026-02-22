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
import worm.me.utils.Item;

import java.util.Arrays;

public class VillagerListener implements Listener {

    @EventHandler
    public void onVillagerSpawn(EntitySpawnEvent event) {

        if (event.getEntity() instanceof Villager villy) {

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

            if (!Arrays.equals(Trader.getGenetics(villy), new int[VillagerProfession.values().length])) villy.setProfession(Villager.Profession.NONE);

        }

    }

    @EventHandler
    public void onVillagerBreed(EntityBreedEvent event){

        if (event.getEntity() instanceof Villager child){

            Villager mother = (Villager) event.getMother();
            Villager father = (Villager) event.getFather();

            int[] motherGenetics = Trader.getGenetics(mother);
            int[] fatherGenetics = Trader.getGenetics(father);

            Trader.addGenetics(child, motherGenetics);
            Trader.addGenetics(child, fatherGenetics);

        }
    }

    @EventHandler
    public void onVillagerEmploy(VillagerCareerChangeEvent event) {

        Villager villager = event.getEntity();

        if (event.getProfession() == Villager.Profession.NONE) {
            event.setCancelled(true);
            return;
        }

        if (Trader.getGenetics(villager)[VillagerProfession.valueOf(event.getProfession().toString()).ordinal()] != 1) event.setCancelled(true);

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


}
