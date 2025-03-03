package worm.me.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import worm.me.PlayerSystems.Social.MessageHead;
import worm.me.server;

public class setVoiceCommand implements CommandExecutor {

    private server plugin;

    public setVoiceCommand(server plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player p = ((Player) sender).getPlayer();
            assert p != null;
            String voice = args[0];
            try {
                int voiceVal = Integer.parseInt(voice);
                if (voiceVal >= plugin.sounds.size()){
                    plugin.pluginMessage(String.format("Invalid Sound ID. Sound IDs range from 0-%d", plugin.sounds.size() - 1), p);
                    return false;
                }
                MessageHead voiceHandler = new MessageHead(plugin);
                voiceHandler.setVoice(p, voiceVal);
                if (args.length > 1){
                    String pitch = args[1];
                    try {
                        float pitchVal = Float.parseFloat(pitch);
                        voiceHandler.setVoicePitch(p, pitchVal);
                        plugin.pluginMessage(String.format("Your voice has been changed to %s and your pitch has been changed to %f.", voiceHandler.VoiceTranslate(voiceVal), pitchVal), p);
                        return true;
                    } catch (NumberFormatException exception){
                        plugin.pluginMessage(ChatColor.RED + "The second argument must be a double. (anything between 0.0 and 2.0, i.e., 0.8 or 1.6)", p);
                        return false;
                    }
                } else{
                    p.sendMessage("You must declare the pitch value.");
                    return false;
                }
            } catch (NumberFormatException exception){
                p.sendMessage(ChatColor.RED + "The first argument must be a whole number. (0, 1, 2, 3, 4)");
                return false;
            }
        }
        return false;
    }
}
