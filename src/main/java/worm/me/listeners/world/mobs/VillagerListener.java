package worm.me.listeners.world.mobs;

import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import worm.me.gamesystems.world.mobs.traders.Trader;
import worm.me.gamesystems.world.mobs.traders.VillagerProfession;

import java.util.Arrays;

public class VillagerListener implements Listener {

    @EventHandler
    public void onVillagerSpawn(EntitySpawnEvent event) {

        if (event.getEntity() instanceof Villager villy) {
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

        Villager villager = (Villager) event.getEntity();

        int level = villager.getVillagerLevel();

        event.setCancelled(true);

        Trader.replaceTrade(villager, event.getRecipe(), level);


    }


}
