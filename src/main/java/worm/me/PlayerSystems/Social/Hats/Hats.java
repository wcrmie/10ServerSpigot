package worm.me.PlayerSystems.Social.Hats;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Hats {

    public void equipDefaultHat(Player p){
        if (p.getInventory().getHelmet().getType().isAir()){
            HatsList hatsList = new HatsList();
            p.getInventory().setHelmet(HatsList.hats.getFirst());
        }
    }


}
