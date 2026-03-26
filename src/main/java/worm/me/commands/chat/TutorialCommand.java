package worm.me.commands.chat;

import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import worm.me.gamesystems.chat.TutorialTip;

public class TutorialCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (sender instanceof Player player) {

            int page;
            try {
                page = Integer.parseInt(args[0]);
            } catch (Exception e) {
                page = 1;
            }

            TutorialTip.values()[page - 1].send(player);

            if (page < TutorialTip.values().length) {
                player.sendMessage(MiniMessage.miniMessage().deserialize(

                        "<gray>[NEXT PAGE]</gray>"

                ).clickEvent(ClickEvent.runCommand("/tutorial " + (page + 1))));
            }



            return true;

        }

        return false;
    }
}
