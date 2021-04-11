package de.syskoh.waypoints.waypoints;

import de.syskoh.waypoints.Waypoints;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Manages all UserWaypoints, listens to specific events and renders the waypoints
 */

public class WaypointManager implements Listener {


    /**
     * A list of all loaded userwaypoints
     */
    LinkedList<UserWaypoints> userWaypoints = new LinkedList<>();

    /**
     * A HashMap of all spawned Armorstands assigned with the Name of the player who spawned them
     */
    HashMap<ArmorStand, String> waypointArmorStands = new HashMap<>();


    /**
     * Loads the UserWaypoints from the config file
     * @param event PlayerJoinEvent
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        userWaypoints.add(new UserWaypoints(event.getPlayer()));
    }


    /**
     * Saves the user's waypoints and destroys them
     * @param event PlayerQuitEvent
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        for (UserWaypoints waypoint : userWaypoints) {
            if (waypoint.getPlayer().equals(event.getPlayer())) {
                waypoint.saveWaypoints();
                userWaypoints.remove(waypoint);
            }
        }
    }

    /**
     * Fires if a player starts or stops sneaking
     * @param e PLayerToggleSneakEvent
     */

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        PlayerInventory inv = p.getInventory();


        // if the player stopped sneaking, remove all armorstands from the player
        if (!e.isSneaking()) {
            LinkedList<ArmorStand> armorStandToRemove = new LinkedList<>();
            for (ArmorStand armorStand : waypointArmorStands.keySet()) {
                if (waypointArmorStands.get(armorStand).equals(p.getName())) {
                    armorStandToRemove.add(armorStand);
                }
            }
            for (ArmorStand toRemove : armorStandToRemove) {
                waypointArmorStands.remove(toRemove);
                toRemove.remove();
            }


        } else {

            // If the player is not holding the WaypointItem in either hand, return
            if (
                    !inv.getItemInMainHand().equals(Waypoints.getInstance().getWaypointItem())
                            & !inv.getItemInOffHand().equals(Waypoints.getInstance().getWaypointItem())) {

                return;
            }



            UserWaypoints uw = getUserWaypoints(p);
            assert uw != null;

            // Spawns an armorstand in the direction of the waypoint at a distance of 5 blocks
            for (Waypoint waypoint : uw.getWaypoints()) {
                Vector vec = waypoint.getLocation().toVector().subtract(p.getEyeLocation().toVector()).normalize().multiply(5);

                //p.getEyeLocation().add(vec);
                ArmorStand armorStandDisplayLabel = (ArmorStand) p.getWorld().spawnEntity(p.getLocation().add(vec), EntityType.ARMOR_STAND);
                //displayName.setAI(false);
                armorStandDisplayLabel.setInvisible(true);
                armorStandDisplayLabel.setCustomName(waypoint.getLabel());
                armorStandDisplayLabel.setCustomNameVisible(true);
                armorStandDisplayLabel.setGravity(false);
                armorStandDisplayLabel.setInvulnerable(true);

                //armorStandDisplayLabel.teleport(p.getLocation().add(vec));


                ArmorStand armorStandDisplayItem = (ArmorStand) p.getWorld().spawnEntity(p.getLocation().add(vec.multiply(1.2)).add(0,1.55, 0), EntityType.ARMOR_STAND);
                armorStandDisplayItem.setInvisible(true);
                armorStandDisplayItem.setGravity(false);
                armorStandDisplayItem.setSmall(true);
                armorStandDisplayItem.getEquipment().setHelmet(waypoint.getStack());
                armorStandDisplayItem.setInvulnerable(true);

                waypointArmorStands.put(armorStandDisplayLabel, p.getName());
                waypointArmorStands.put(armorStandDisplayItem, p.getName());

            }
        }
    }

    public void removeAllArmorstandWaypoints(){
        for (ArmorStand armorStand : waypointArmorStands.keySet()) {
            armorStand.remove();
        }
        waypointArmorStands.clear();
    }


    public UserWaypoints getUserWaypoints(Player player) {
        for (UserWaypoints waypoint : userWaypoints) {
            if (waypoint.getPlayer().equals(player)) {
                return waypoint;
            }
        }
        return null;
    }


    public void reloadUser() {
        userWaypoints.clear();
        for (Player p : Bukkit.getOnlinePlayers()) {
            userWaypoints.add(new UserWaypoints(p));
        }
    }
}
