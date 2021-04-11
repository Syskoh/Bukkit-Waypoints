package de.syskoh.waypoints.waypoints;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;


/**
 * Represents a single waypoint
 */

public class Waypoint {

    private Location location;
    private String label;
    private ItemStack stack;

    public Waypoint(String label, Location location, ItemStack stack) {
        this.label = label;
        this.location = location;
        this.stack = stack;
    }

    public Location getLocation() {
        return location;
    }

    public String getLabel() {
        return label;
    }

    public ItemStack getStack() {
        return stack;
    }
}
