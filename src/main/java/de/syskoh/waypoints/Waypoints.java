package de.syskoh.waypoints;

import de.syskoh.waypoints.commands.GiveWaypointManager;
import de.syskoh.waypoints.waypoints.WaypointManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * The main class for the waypoint plugin
 */
public final class Waypoints extends JavaPlugin {


    private static Waypoints instance;
    private ItemStack waypointItem;
    private final WaypointManager waypointManager = new WaypointManager();


    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(waypointManager, this);
        getCommand("gw").setExecutor(new GiveWaypointManager());
        prepareItem();


        // Loads the waypoints for all users incase the server did a reload
        waypointManager.reloadUser();

    }

    @Override
    public void onDisable() {
        // Removes all spawned armorstands incase some are still spawned
        waypointManager.removeAllArmorstandWaypoints();
    }

    /**
     * Generates the waypoint item and registers a crafting recipe for it
     */
    private void prepareItem() {
        waypointItem = new ItemStack(Material.COMPASS);
        ItemMeta meta = waypointItem.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Waypoint Manager");
        waypointItem.setItemMeta(meta);
        waypointItem.addUnsafeEnchantment(Enchantment.DURABILITY, 1);

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, "waypoints"), waypointItem);

        recipe.shape(
                " e ",
                "dcd",
                " d "
        );
        recipe.setIngredient('e', Material.ENDER_PEARL)
                .setIngredient('d', Material.DIAMOND)
                .setIngredient('c', Material.COMPASS);
        Bukkit.addRecipe(recipe);


    }

    public ItemStack getWaypointItem() {
        return waypointItem;
    }

    public static Waypoints getInstance() {
        return instance;
    }
}
