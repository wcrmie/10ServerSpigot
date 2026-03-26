package worm.me.commands.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class RulesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Component rulesMessage = MiniMessage.miniMessage().deserialize(

                """
                        <red><b>RULES</b>\
                        
                        
                        <red>1. No hacking, exploiting, scripting, or using any program to get an unfair advantage over other players\
                        
                        <red>2. No racism, bigotry, or slurs of any kind\
                        
                        <red>3. No undue weirdness; don't harass other players or be overly sexual.\
                        
                        <red>4. No spamming.\
                        
                        <red>5. No advertisement of external servers or services.\
                        
                        <red>6. Do not ruin the player experience. This includes barricading spawn, flooding, and being an indiscriminate nuisance.\
                        
                        <red>7. Use common sense.\
                        
                        
                        <yellow>If you see someone breaking the rules, make sure to use <red><b>/report</b></red>."""

        );


        sender.sendMessage(rulesMessage);
        return true;
    }
}
