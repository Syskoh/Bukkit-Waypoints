package de.syskoh.waypoints.waypoints;

import de.syskoh.waypoints.Waypoints;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;


/**
 * Represents a list of waypoints for a specific user
 */
public class UserWaypoints {

    Waypoints w = Waypoints.getInstance();
    Player player;
    LinkedList<Waypoint> waypoints = new LinkedList<>();


    /**
     * A list of Waypoints for the specified player
     * @param player the player for which the waypoints should get loaded
     */
    public UserWaypoints(Player player) {
        this.player = player;
        loadWaypoints();
    }

    /**
     * Loads all player waypoints and stores them into getWaypoints()
     * Foramt: "Waypoint.(UUID).(waypoint-label).(Data)"
     */

    public void loadWaypoints() {
        String prefix = "Waypoint." + player.getUniqueId().toString();
        int x;
        int y;
        int z;
        String world;
        String material;


        // Check if the user doesn't have waypoints
        if (w.getConfig().getConfigurationSection(prefix) == null) {
            return;
        }

        // Get all user waypoints
        for (String label : w.getConfig().getConfigurationSection(prefix).getKeys(false)) {

            // Assign them to the representing varioables
            String path = prefix + "." + label + ".";
            x = w.getConfig().getInt(path + "X");
            y = w.getConfig().getInt(path + "Y");
            z = w.getConfig().getInt(path + "Z");
            world = w.getConfig().getString(path + "World");
            material = w.getConfig().getString(path + "Material");

            // System.out.println(path);
            // System.out.println(x + " " + y + " " + z + " " + world);

            //create the waypoint object and store it into the waypoints linked list
            waypoints.add(new Waypoint(
                    label,
                    new Location(w.getServer().getWorld(world),
                            x,
                            y,
                            z),
                    new ItemStack(Material.valueOf(material))));

        }
    }


    /**
     * Saves the waypoints to the config file
     * Foramt: "Waypoint.(UUID).(waypoint-label).(Data)"
     */
    public void saveWaypoints() {
        for (Waypoint wa : waypoints) {
            String prefix = "Waypoint." + player.getUniqueId().toString() + "." + wa.getLabel() + ".";
            set(prefix + "X", wa.getLocation().getBlockX());
            set(prefix + "Y", wa.getLocation().getBlockY());
            set(prefix + "Z", wa.getLocation().getBlockZ());
            set(prefix + "World", wa.getLocation().getWorld().getName());
            set(prefix + "Material", wa.getStack().getType().name());
        }
    }

    /**
     * Returns a list of waypoints
     * @return Waypoints
     */
    public LinkedList<Waypoint> getWaypoints() {
        return waypoints;
    }

    /**
     * Sets a config variable and saves it
     * @param path the config path
     * @param o    the value of the config path
     */
    public void set(String path, Object o) {
        w.getConfig().set(path, o);
        w.saveConfig();
    }


    /**
     * Adds a new waypoint to the collection
     * @param label The name of the waypoint
     * @param loc The Location of the waypoint
     * @param material Which Material should spawn with the waypoint
     */
    public void addUserWaypoint(String label, Location loc, Material material){
        waypoints.add(new Waypoint(label, loc, new ItemStack(material)));
        saveWaypoints();
    }

    /**
     * @return The player this class represents
     */
    public Player getPlayer() {
        return player;
    }


    /**
     * removes a waypoint
     * @param wp The waypoint to remove
     */
    public void removeUserWaypoint(Waypoint wp) {
        String prefix = "Waypoint." + player.getUniqueId().toString() + "." + wp.getLabel();
        Waypoints.getInstance().getConfig().set(prefix, null);
        Waypoints.getInstance().saveConfig();
        waypoints.remove(wp);
        saveWaypoints();
    }
}
