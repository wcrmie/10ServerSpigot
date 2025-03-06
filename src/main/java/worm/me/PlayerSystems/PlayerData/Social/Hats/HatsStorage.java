package worm.me.PlayerSystems.PlayerData.Social.Hats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import worm.me.Server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class HatsStorage {

    public void addHat(int page, Player p) {

        Inventory hatInv = Bukkit.createInventory(null, 54, ChatColor.RED + "Hat Storage " + page);

        List<ItemStack> hats = getHats(page);

        for (int i = 0; i < hats.size(); i++) {
            hatInv.setItem(i, hats.get(i));
        }

        p.openInventory(hatInv);


    }

    public ItemStack getHat(int ID) {
        for (int i = 0; i < 50; i++){
            if (ID - 54 * i < 54) {
                List<ItemStack> hats = getHats(i + 1);
                return hats.get(ID - 54 * i);
            }
        } return new ItemStack(Material.AIR);
    }

    public List<ItemStack> getHats(int page) {
        FileConfiguration hatsStorage = Server.getPlugin().getHatsStorage();

        String encodedData = hatsStorage.getString("page " + page);

        ArrayList<ItemStack> items = new ArrayList<>();

        if (encodedData != null){
            byte[] rawData = Base64.getDecoder().decode(encodedData);

            try {
                ByteArrayInputStream io = new ByteArrayInputStream(rawData);
                BukkitObjectInputStream is = new BukkitObjectInputStream(io);

                int size = is.readInt();

                for (int i = 0; i < size; i++) {

                    items.add((ItemStack) is.readObject());

                }

                is.close();

                return items;

            } catch (IOException | ClassNotFoundException e) {
                Bukkit.getLogger().info(e.getMessage());
            }
        }
        return items;
    }

    public void storeHats(List<ItemStack> hats, int page) {

        if (hats.isEmpty()){
            return;
        } else {

            try {
                ByteArrayOutputStream io = new ByteArrayOutputStream();
                BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);

                os.writeInt(hats.size());

                for (ItemStack item : hats) {
                    os.writeObject(item);
                }

                os.flush();

                byte[] rawData = io.toByteArray();

                String encodedData = Base64.getEncoder().encodeToString(rawData);

                FileConfiguration hatsStorage = Server.getPlugin().getHatsStorage();

                hatsStorage.set("page " + page, encodedData);

                hatsStorage.save(new File(Server.getPlugin().getDataFolder(), "hatStorage.yml"));

                os.close();

            } catch (IOException e) {
                Bukkit.getLogger().info(e.getMessage());
            }
        }
    }

    public void eventHandler(InventoryCloseEvent e) {

        Server.pluginMessage(ChatColor.RED + "Attempting save", (Player) e.getPlayer());

        List<ItemStack> hats = new ArrayList<>();

        Arrays.stream(e.getInventory().getContents()).toList().forEach(itemStack -> {
            if (itemStack != null) {
                hats.add(itemStack);
            } else {
                hats.add(new ItemStack(Material.AIR));
            }
        });

        Server.pluginMessage(ChatColor.RED + "Hats: " + hats, (Player) e.getPlayer());

        int page = Integer.parseInt(e.getView().getTitle().split("Hat Storage ")[1]);

        Server.pluginMessage(ChatColor.RED + "Page: " + page, (Player) e.getPlayer());

        storeHats(hats, page);

    }

}
