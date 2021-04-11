package de.syskoh.waypoints;

import de.syskoh.waypoints.commands.GiveWaypointManager;
import de.syskoh.waypoints.inventories.MainMenu;
import de.syskoh.waypoints.inventories.MenuManager;
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
    private WaypointManager waypointManager;
    private MenuManager menuManager;
    private MainMenu mainMenu;


    @Override
    public void onEnable() {
        instance = this;
        prepareItem();

        waypointManager = new WaypointManager();
        menuManager = new MenuManager();
        mainMenu = new MainMenu();

        getServer().getPluginManager().registerEvents(waypointManager, this);
        getServer().getPluginManager().registerEvents(menuManager, this);
        getServer().getPluginManager().registerEvents(mainMenu, this);

        getCommand("gw").setExecutor(new GiveWaypointManager());

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

    public MenuManager getMenuManager() {
        return menuManager;
    }

    public WaypointManager getWaypointManager() {
        return waypointManager;
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public ItemStack getWaypointItem() {
        return waypointItem;
    }

    public static Waypoints getInstance() {
        return instance;
    }
}
