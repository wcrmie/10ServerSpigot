package worm.me.data;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import worm.me.Server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Vault {

    public static void storeItems(List<ItemStack> items, Player p){
        PersistentDataContainer pdc = p.getPersistentDataContainer();

        if (items.isEmpty()){
            pdc.set(new NamespacedKey(Server.getPlugin(), "vault"), PersistentDataType.STRING, "");
        } else {

            try {
                ByteArrayOutputStream io = new ByteArrayOutputStream();
                BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);

                os.writeInt(items.size());

                for (ItemStack item : items) {
                    os.writeObject(item);
                }

                os.flush();

                byte[] rawData = io.toByteArray();

                String encodedData = Base64.getEncoder().encodeToString(rawData);

                pdc.set(new NamespacedKey(Server.getPlugin(), "vault"), PersistentDataType.STRING, encodedData);

                os.close();

            } catch (IOException e) {
                Bukkit.getLogger().info(e.getMessage());
            }
        }
    }

    public static ArrayList<ItemStack> getItems(Player p){
        PersistentDataContainer pdc = p.getPersistentDataContainer();

        String encodedData = pdc.get(new NamespacedKey(Server.getPlugin(), "vault"), PersistentDataType.STRING);

        ArrayList<ItemStack> items = new ArrayList<>();

        if (!encodedData.isEmpty()){
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

    public static void openVault(Player p){
        // Open the vault GUI

        PersistentDataContainer container = p.getPersistentDataContainer();

        Inventory vault = Bukkit.createInventory(p, 9, ChatColor.DARK_PURPLE + "Vault");

        if (!container.has(new NamespacedKey(Server.getPlugin(), "vault"), PersistentDataType.STRING)) {
            container.set(new NamespacedKey(Server.getPlugin(), "vault"), PersistentDataType.STRING, "");
        }

        ArrayList<ItemStack> items = getItems(p);

        items.forEach(vault::addItem);

        p.openInventory(vault);

    }

}
