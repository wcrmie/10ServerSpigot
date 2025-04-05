package worm.me.playersystems.data;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.N;
import worm.me.playersystems.abilities.Abilities;
import worm.me.playersystems.data.levels.Levels;
import worm.me.Server;

import java.lang.constant.Constable;
import java.util.*;

public class Unlocks {

    // Allow maps with different key types to be stored in a list
    public static List<? extends Map<? extends Constable, int[]>> unlockTypes = Arrays.asList(
            Server.stones,
            Server.dirts,
            Server.planks,
            Server.miscs,
            Abilities.abilityRequirements
    );

    public static String[] requirementsTranslation(int[] requirements){


        List<String> translation = new ArrayList<>();
        translation.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Requirements: \n");
        translation.add(" ");

        for (int i = 0; i < 8; i++){

            if (requirements[i] != 0){
                translation.add(ChatColor.YELLOW + Levels.skills.get(i) + " " + requirements[i] + "\n" );
            }
        }
        return translation.toArray(new String[translation.size()]);
    }

    public static boolean hasAbilityUnlocked(Player p, int ability){
        PersistentDataContainer container = p.getPersistentDataContainer();
        int[] levels = Objects.requireNonNullElse(container.get(new NamespacedKey(Server.getPlugin(), "levels"), PersistentDataType.INTEGER_ARRAY), new int[8]);

        for (int i = 0; i < levels.length; i++){
            if (levels[i] < Abilities.abilityRequirements.get(ability)[i]){
                return false;
            }
        }
        return true;
    }

    public static boolean hasGenUnlocked(Player p, Material gen){
        PersistentDataContainer container = p.getPersistentDataContainer();
        int[] levels = Objects.requireNonNullElse(container.get(new NamespacedKey(Server.getPlugin(), "levels"), PersistentDataType.INTEGER_ARRAY), new int[8]);

        int[] reqs;

        if (Server.stones.containsKey(gen)) {
            reqs = Server.stones.get(gen);
        } else if (Server.planks.containsKey(gen)) {
            reqs = Server.planks.get(gen);
        } else if (Server.dirts.containsKey(gen)) {
            reqs = Server.dirts.get(gen);
        } else {
            reqs = Server.miscs.get(gen);
        }

        for (int i = 0; i < levels.length; i++){
            if (levels[i] < reqs[i]){
                return false;
            }
        }
        return true;
    }



}
