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

    String noPermission = Waypoints.getInstance().getConfig().getString("NoPermission");

    public GiveWaypointManager() {
        Waypoints.getInstance().getConfig().addDefault("NoPermission", "§cYou do not have permission to use this command");
        Waypoints.getInstance().getConfig().options().copyDefaults(true);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;

            if(!p.hasPermission("waypoints.give")){
                p.sendMessage(noPermission);
                return true;
            }

            p.getInventory().addItem(Waypoints.getInstance().getWaypointItem());
        }
        return true;
    }
}
