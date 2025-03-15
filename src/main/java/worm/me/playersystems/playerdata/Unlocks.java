package worm.me.playersystems.playerdata;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import worm.me.playersystems.abilities.Abilities;
import worm.me.playersystems.playerdata.levels.Levels;
import worm.me.Server;

import java.lang.constant.Constable;
import java.util.*;

import static worm.me.playersystems.playerdata.VoidPlayer.getUnlockedGens;
import static worm.me.playersystems.playerdata.VoidPlayer.setUnlockedGens;

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

    public void attemptUnlock(int type, Player p){

        // Allow maps with different key types
        Map<? extends Constable, int[]> reqsMap = unlockTypes.get(type);

        // Push keys to array to use indices
        Set<? extends Constable> mapAsList = unlockTypes.get(type).keySet();

        PersistentDataContainer pdc = p.getPersistentDataContainer();
        int[] playerLevels = pdc.get(Levels.Levels, PersistentDataType.INTEGER_ARRAY);

        // Get rid of anything the player has already unlocked
        if (type < 4){

            List<ItemStack> unlockedGens = getUnlockedGens(p);

            for (ItemStack itemStack : unlockedGens) {
                System.out.println("Unlocked gens: " + itemStack.getType());
            }

            Set<Material> totalGens = (Set<Material>) unlockTypes.get(type).keySet();

            System.out.println("Total gens: " + totalGens);

            System.out.println("Previously Unlocked gens: " + unlockedGens);

            for (ItemStack unlockedGen : unlockedGens) {
                totalGens.remove(unlockedGen.getType());
            }

            mapAsList = totalGens;

            System.out.println("Map as list: " + mapAsList);

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

            mapAsList = totalAbilities;
        }

        for (int i = 0; i < unlockTypes.get(type).size(); i++){
            System.out.println(i);
            for (int j = 0; j < 8; j++){
                int req = reqsMap.get(mapAsList.toArray()[i])[j];
                if (req != 0) {
                    if (playerLevels[j] < req) {
                        break;

                    // If the player has the required levels, unlock the thing
                    }
                } else {

                    if (j >= 6) {


                        // If the unlock is a generator, add it to the player's unlocked generators
                        if (type < 4) {

                            List<ItemStack> gens = getUnlockedGens(p);

                            gens.add(new ItemStack((Material) mapAsList.toArray()[i]));
                            setUnlockedGens(p, gens);

                            UnlockEvent unlockEvent = new UnlockEvent(p, mapAsList.toArray()[i].toString(), "Generator");
                            Bukkit.getServer().getPluginManager().callEvent(unlockEvent);

                            // If the unlock is an ability, add it to the player's unlocked abilities
                        } else if (type == 4) {
                            int[] unlockedAbilities = pdc.get(Abilities.abilitiesUnlocked, PersistentDataType.INTEGER_ARRAY);
                            unlockedAbilities[i] = 1;
                            pdc.set(Abilities.abilitiesUnlocked, PersistentDataType.INTEGER_ARRAY, unlockedAbilities);

                            UnlockEvent unlockEvent = new UnlockEvent(p, mapAsList.toArray()[i].toString(), "Ability");
                            Bukkit.getServer().getPluginManager().callEvent(unlockEvent);

                            // -----------------If unlock is recipe VVV
                        } else if (type == 5) {



                        }
                    }
                }
            }
        }
    }



}
