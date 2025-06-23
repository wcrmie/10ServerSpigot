package worm.me.commands.discord;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import worm.me.Server;
import worm.me.discord.bot.DiscordLink;

public class DiscordLinkCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        if (commandSender instanceof Player p) {

            PersistentDataContainer container = p.getPersistentDataContainer();

            if (container.getOrDefault(new NamespacedKey(Server.getPlugin(), "linked"), PersistentDataType.STRING, "").isEmpty()) {

                if (DiscordLink.linking.containsValue(p)) {
                    p.sendMessage("§cYou already have a linking code.");
                    return true;
                }

                DiscordLink discordLink = new DiscordLink();
                discordLink.beginLinking(p);
                return true;

            } else {
                p.sendMessage("§cYou are already linked to Discord.");
                return true;
            }


        }

        return false;
    }
}
