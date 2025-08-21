package worm.me.commands.chat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import worm.me.playersystems.social.MessageHead;
import worm.me.Server;
import worm.me.utils.chat.Message;
import worm.me.utils.chat.MessageType;

public class SetVoiceCommand implements CommandExecutor {

    private Server plugin;

    public SetVoiceCommand(Server plugin) {
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
                    p.sendMessage(Message.create(MessageType.ERROR, String.format("Invalid Sound ID. Sound IDs range from 0-%d", plugin.sounds.size() - 1)));
                    return false;
                }
                MessageHead voiceHandler = new MessageHead(plugin);
                voiceHandler.setVoice(p, voiceVal);
                if (args.length > 1){
                    String pitch = args[1];
                    try {
                        float pitchVal = Float.parseFloat(pitch);
                        voiceHandler.setVoicePitch(p, pitchVal);
                        p.sendMessage(Message.create(MessageType.SUCCESS, String.format("Your voice has been changed to %s and your pitch has been changed to %f.", voiceHandler.VoiceTranslate(voiceVal), pitchVal)));
                        return true;
                    } catch (NumberFormatException exception){
                        p.sendMessage(Message.create(MessageType.ERROR, "The second argument must be a double. (anything between 0.0 and 2.0, i.e., 0.8 or 1.6)"));
                        return false;
                    }
                } else{
                    p.sendMessage(Message.create(MessageType.FAIL, "You must declare the pitch value."));
                    return false;
                }
            } catch (NumberFormatException exception){
                p.sendMessage(Message.create(MessageType.ERROR, "The first argument must be a whole number. (0, 1, 2, 3, 4)"));
                return false;
            }
        }
        return false;
    }
}
