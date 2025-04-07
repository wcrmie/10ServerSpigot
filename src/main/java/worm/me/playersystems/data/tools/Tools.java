package worm.me.playersystems.data.tools;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import worm.me.GuiConstructor;
import worm.me.playersystems.data.VoidPlayer;
import worm.me.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tools {

    public static Map<Player, List<ItemStack>> playerTools = new HashMap<>();

    public static void toolsGrantInitial(Player p){

        // Create tools
        List<ItemStack> tools = new ArrayList<>();

        tools.add(GuiConstructor.createGuiItem(Material.WOODEN_PICKAXE, "", ChatColor.DARK_GRAY + "Upgrade this tool by clicking on it with", ChatColor.DARK_GRAY + "16 Cobblestone"));
        tools.add(GuiConstructor.createGuiItem(Material.WOODEN_AXE, "", ChatColor.DARK_GRAY + "Upgrade this tool by clicking on it with", ChatColor.DARK_GRAY + "16 Cobblestone"));
        tools.add(GuiConstructor.createGuiItem(Material.WOODEN_SWORD, "", ChatColor.DARK_GRAY + "Upgrade this tool by clicking on it with", ChatColor.DARK_GRAY + "16 Cobblestone"));
        tools.add(GuiConstructor.createGuiItem(Material.WOODEN_SHOVEL, "", ChatColor.DARK_GRAY + "Upgrade this tool by clicking on it with", ChatColor.DARK_GRAY + "16 Cobblestone"));

        ItemMeta toolMeta = tools.getFirst().getItemMeta();

        toolMeta.setUnbreakable(true);

        for (int i = 0; i < tools.size(); i++){
            tools.get(i).setItemMeta(toolMeta);
            p.getInventory().setItem(i, tools.get(i));
        }

        VoidPlayer.setTools(tools, p);
    }

    public ItemStack toolUpgrade(ItemStack tool){

        List<Material> toolList = Server.tools.get(toolType(tool.getType()));

        if (toolList.indexOf(tool.getType()) == toolList.size() - 1) return tool; else {

            Material newTool = toolList.get(toolList.indexOf(tool.getType()) + 1);

            ItemMeta toolMeta = tool.getItemMeta();

            assert toolMeta != null;
            toolMeta.setUnbreakable(false);

            toolMeta.setLore(List.of(" "));

            if (newTool.toString().contains("STONE")){

                toolMeta.setLore(List.of(ChatColor.DARK_GRAY + "Upgrade this tool by clicking on it with", ChatColor.DARK_GRAY + "4 Iron Ingots"));
            }

            ItemStack newToolItem = new ItemStack(newTool);
            newToolItem.setItemMeta(toolMeta);
            return newToolItem;
        }

    }

    public ItemStack toolDowngrade(ItemStack tool){

        List<Material> toolList = Server.tools.get(toolType(tool.getType()));

        if (toolList.indexOf(tool.getType()) == 0) return tool; else {
            Material downgrade = toolList.get(toolList.indexOf(tool.getType()) - 1);
            ItemMeta toolMeta = tool.getItemMeta();
            assert toolMeta != null;

            if (toolMeta.getLore() != null) {
                toolMeta.getLore().clear();
            } else {
                toolMeta.setLore(List.of(" "));
            }

            if (downgrade.toString().contains("WOODEN")){
                toolMeta.setUnbreakable(true);

                toolMeta.setLore(List.of(ChatColor.DARK_GRAY + "Upgrade this tool by clicking on it with", ChatColor.DARK_GRAY + "16 Cobblestone"));

            } else if (downgrade.toString().contains("STONE")) {

                toolMeta.setLore(List.of(ChatColor.DARK_GRAY + "Upgrade this tool by clicking on it with", ChatColor.DARK_GRAY + "4 Iron Ingots"));

            }

            ItemStack newToolItem = new ItemStack(downgrade);

            newToolItem.setItemMeta(toolMeta);

            org.bukkit.inventory.meta.Damageable damageable = (Damageable) newToolItem.getItemMeta();

            damageable.setDamage(0);

            newToolItem.setItemMeta(damageable);

            return newToolItem;
        }

    }

    public static String toolType(Material tool){
        if (tool.toString().contains("_PICKAXE")) return "pickaxe";
        if (tool.toString().contains("_AXE")) return "axe";
        if (tool.toString().contains("_SHOVEL")) return "shovel";
        if (tool.toString().contains("_SWORD")) return "sword";
        return "none";
    }
}
