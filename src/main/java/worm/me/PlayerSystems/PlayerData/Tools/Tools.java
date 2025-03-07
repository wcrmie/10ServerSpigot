package worm.me.PlayerSystems.PlayerData.Tools;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import worm.me.PlayerSystems.PlayerData.VoidPlayer;
import worm.me.Server;

import java.util.ArrayList;
import java.util.List;

public class Tools {

    public static void toolsGrantInitial(Player p){

        // Create tools
        List<ItemStack> tools = new ArrayList<>();

        tools.add(new ItemStack(Material.WOODEN_PICKAXE));
        tools.add(new ItemStack(Material.WOODEN_AXE));
        tools.add(new ItemStack(Material.WOODEN_SHOVEL));
        tools.add(new ItemStack(Material.WOODEN_SWORD));

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

            if (downgrade.toString().contains("WOODEN")){
                toolMeta.setUnbreakable(true);
            }

            ItemStack newToolItem = new ItemStack(downgrade);
            newToolItem.setItemMeta(toolMeta);
            newToolItem.setDurability((short) 0);
            return newToolItem;
        }

    }

    public static String toolType(Material tool){
        if (tool.toString().contains("PICKAXE")) return "pickaxe";
        if (tool.toString().contains("AXE")) return "axe";
        if (tool.toString().contains("SHOVEL")) return "shovel";
        if (tool.toString().contains("SWORD")) return "sword";
        return "none";
    }
}
