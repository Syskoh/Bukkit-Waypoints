package de.syskoh.waypoints.inventories;

import de.syskoh.waypoints.Waypoints;
import de.syskoh.waypoints.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;

public class MenuManager implements Listener {

    private HashMap<Player, InvState> playerInvStateHashMap = new HashMap<>();

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        if (!p.hasPermission("waypoints.menu") || !Util.isPlayerHoldingWaypointItem(p) || !p.isSneaking()) {
            return;
        }

        p.openInventory(Waypoints.getInstance().getMainMenu().getInventory());
        setPlayerInvState(p, InvState.MAIN_MENU);
    }


    public void resetPlayerInvState(Player p){
        playerInvStateHashMap.remove(p);
    }

    public void setPlayerInvState(Player p, InvState invState){
        playerInvStateHashMap.put(p, invState);
    }

    public InvState getPlayerInvState(Player p) {
        return playerInvStateHashMap.get(p);
    }
}
