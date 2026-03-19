package worm.me.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import worm.me.gamesystems.world.border.ZoneGridManager;

public class GetBorderGrid implements CommandExecutor {

    private final ZoneGridManager zoneGridManager;

    public GetBorderGrid(ZoneGridManager zoneGridManager) {
        this.zoneGridManager = zoneGridManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        zoneGridManager.handleGetGrid(sender);
        return true;
    }
}