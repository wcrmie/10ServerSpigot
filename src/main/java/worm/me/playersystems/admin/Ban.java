package worm.me.playersystems.admin;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Ban {

    public boolean permBan(OfflinePlayer p, String reason, String banner) {

        if (!p.isBanned()) {

            Bukkit.getBanList(BanList.Type.NAME).addBan(p.getName(), reason, null, banner);
            if (p.isOnline()) {
                Player onlinePlayer = p.getPlayer();
                onlinePlayer.kickPlayer("You have been banned: " + Objects.requireNonNullElse(reason, "No reason provided"));
                return true;
            }
        }
        return false;
    }

}
