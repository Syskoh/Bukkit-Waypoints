package de.syskoh.waypoints.commands;

import de.syskoh.waypoints.Waypoints;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Debug command to give the waypoint item
 */

public class GiveWaypointManager implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;

            if(!p.isOp()){
                return true;
            }
            p.getInventory().addItem(Waypoints.getInstance().getWaypointItem());
        }
        return true;
    }
}
