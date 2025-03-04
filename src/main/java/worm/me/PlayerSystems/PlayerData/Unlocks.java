package worm.me.PlayerSystems.PlayerData;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.PlayerSystems.Abilities.Abilities;
import worm.me.PlayerSystems.PlayerData.Levels.Levels;
import worm.me.Server;

import java.lang.constant.Constable;
import java.util.*;

import static worm.me.PlayerSystems.PlayerData.VoidPlayer.getUnlockedGens;
import static worm.me.PlayerSystems.PlayerData.VoidPlayer.setUnlockedGens;

public class Unlocks {

    // Allow maps with different key types to be stored in a list
    public static List<? extends Map<? extends Constable, int[]>> unlockTypes = Arrays.asList(
            Server.stones,
            Server.dirts,
            Server.planks,
            Server.miscs,
            Abilities.abilityRequirements
    );

    public static String requirementsTranslation(int[] requirements){

        String translation = (ChatColor.GOLD + "" + ChatColor.BOLD + "Requirements: \n");

        for (int i = 0; i < requirements.length; i++){
            if (requirements[i] != 0){
                translation += (ChatColor.GOLD + Levels.skills.get(i) + requirements[i] + "\n" );
            }
        }
        return translation;
    }

    public void attemptUnlock(int type, Player p){

        // Allow maps with different key types
        Map<? extends Constable, int[]> reqsMap = unlockTypes.get(type);

        // Push keys to array to use indices
        Object[] mapAsList = unlockTypes.get(type).keySet().toArray();

        PersistentDataContainer pdc = p.getPersistentDataContainer();
        int[] playerLevels = pdc.get(Levels.Levels, PersistentDataType.INTEGER_ARRAY);

        // Get rid of anything the player has already unlocked
        if (type < 4){

            List<ItemStack> unlockedGens = VoidPlayer.getUnlockedGens(p);
            Set<ItemStack> totalGens = (Set<ItemStack>) unlockTypes.get(type).keySet();

            unlockedGens.forEach(totalGens::remove);

            mapAsList = totalGens.toArray();
        } else if (type == 4){
            List<Integer> skippedAbilities = new ArrayList<>();
            Set<Integer> totalAbilities = (Set<Integer>) unlockTypes.get(type).keySet();
            int[] unlockedAbilities = pdc.get(Abilities.abilitiesUnlocked, PersistentDataType.INTEGER_ARRAY);
            for (int i = 0; i < unlockedAbilities.length; i++){
                int j = unlockedAbilities[i];
                if (j == 1){
                    skippedAbilities.add(i);
                }
            }

            skippedAbilities.forEach(totalAbilities::remove);

            mapAsList = totalAbilities.toArray();
        }

        for (int i = 0; i < unlockTypes.get(type).size(); i++){
            for (int j = 0; j < 8; j++){
                int req = reqsMap.get(mapAsList[i])[j];
                if (req != 0) {
                    if (playerLevels[j] < req) {
                        break;

                    // If the player has the required levels, unlock the thing
                    } else if (j == 7) {

                        // If the unlock is a generator, add it to the player's unlocked generators
                        if (type < 4) {
                            List<ItemStack> gens = getUnlockedGens(p);
                            gens.add((ItemStack)mapAsList[i]);

                            setUnlockedGens(p, gens);

                        // If the unlock is an ability, add it to the player's unlocked abilities
                        } else if (type == 4) {
                            int[] unlockedAbilities = pdc.get(Abilities.abilitiesUnlocked, PersistentDataType.INTEGER_ARRAY);
                            unlockedAbilities[i] = 1;
                            pdc.set(Abilities.abilitiesUnlocked, PersistentDataType.INTEGER_ARRAY, unlockedAbilities);
                        }
                    }
                }
            }
        }
    }



}
