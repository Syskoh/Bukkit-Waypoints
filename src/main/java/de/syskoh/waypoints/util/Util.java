package de.syskoh.waypoints.util;

import de.syskoh.waypoints.Waypoints;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;


/**
 * Various utilities
 */
public class Util {

    public static boolean isPlayerHoldingWaypointItem(Player player){
        PlayerInventory inv = player.getInventory();
        return inv.getItemInMainHand().equals(Waypoints.getInstance().getWaypointItem())
                || inv.getItemInOffHand().equals(Waypoints.getInstance().getWaypointItem());

    }
}
