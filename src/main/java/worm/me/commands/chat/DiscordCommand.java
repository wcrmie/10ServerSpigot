package worm.me.commands.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DiscordCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        sender.sendMessage(Component.text("Join us on Discord: https://discord.gg/wHYpar7wP6").clickEvent(ClickEvent.openUrl("https://discord.gg/wHYpar7wP6")));

        if (sender instanceof Player player) player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1.5F);


        return true;
    }
}
