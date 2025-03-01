package worm.me.PlayerSystems.Tools;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import worm.me.server;

import java.util.List;

public class Tools {

    public void toolsGrantInitial(Player p){

        // Create tools
        ItemStack pickaxe = new ItemStack(Material.WOODEN_PICKAXE);
        ItemStack axe = new ItemStack(Material.WOODEN_AXE);
        ItemStack shovel = new ItemStack(Material.WOODEN_SHOVEL);
        ItemStack sword = new ItemStack(Material.WOODEN_SWORD);

        ItemMeta toolMeta = pickaxe.getItemMeta();

        toolMeta.setUnbreakable(true);

        // Make the tools unbreakable
        pickaxe.setItemMeta(toolMeta);
        axe.setItemMeta(toolMeta);
        shovel.setItemMeta(toolMeta);
        sword.setItemMeta(toolMeta);


        // Give tools to player
        p.getInventory().setItem(0, sword);
        p.getInventory().setItem(1, pickaxe);
        p.getInventory().setItem(2, axe);
        p.getInventory().setItem(3, shovel);
    }

    public ItemStack toolUpgrade(ItemStack tool){

        List<Material> toolList = server.tools.get(toolType(tool.getType()));

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

        List<Material> toolList = server.tools.get(toolType(tool.getType()));

        if (toolList.indexOf(tool.getType()) == 0) return tool; else {
            Material downgrade = toolList.get(toolList.indexOf(tool.getType()) - 1);
            ItemMeta toolMeta = tool.getItemMeta();
            assert toolMeta != null;

            if (downgrade.toString().contains("WOODEN")){
                toolMeta.setUnbreakable(true);
            }

            ItemStack newToolItem = new ItemStack(downgrade);
            newToolItem.setItemMeta(toolMeta);
            return newToolItem;
        }

    }

    public String toolType(Material tool){
        if (tool.toString().contains("PICKAXE")) return "pickaxe";
        if (tool.toString().contains("AXE")) return "axe";
        if (tool.toString().contains("SHOVEL")) return "shovel";
        if (tool.toString().contains("SWORD")) return "sword";
        return "none";
    }
}
