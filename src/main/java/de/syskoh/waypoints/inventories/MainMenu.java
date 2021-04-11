package de.syskoh.waypoints.inventories;

import de.syskoh.waypoints.Waypoints;
import de.syskoh.waypoints.waypoints.Waypoint;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Locale;

public class MainMenu implements Listener {

    private Inventory mainMenu;
    private Inventory woolColor;
    private ItemStack createItemStack;
    private ItemStack deleteItemStack;

    private HashMap<Player, String> waypointNames = new HashMap<>();


    public MainMenu(){
        prepareItems();
        prepareMenu();
    }

    /**
     * Checks if a player is shift-right clicking with a waypoint item and opens the main menu
     * @param event
     */
    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();

        if (!event.getInventory().equals(mainMenu) && !event.getInventory().equals(woolColor)) {
            return;
        }
        event.setCancelled(true);

        if (event.getCurrentItem() == null) {
            return;
        }

        if (event.getCurrentItem().equals(createItemStack)) {
            Waypoints.getInstance().getMenuManager().setPlayerInvState(p, InvState.NAMING_WAYPOINT);
            p.closeInventory();
            p.sendMessage(ChatColor.GREEN + "Bitte gib einen Namen für deinen Waypoint ein.");
            p.sendMessage(ChatColor.GREEN + "Schreibe " + ChatColor.RED + "cancel" + ChatColor.GREEN + " um den Vorgang abzubrechen.");
        }

        if (Waypoints.getInstance().getMenuManager().getPlayerInvState(p).equals(InvState.COLORING_WAYPOINT)) {
            if (event.getCurrentItem().getType().name().endsWith("_WOOL")) {
                p.closeInventory();
                Waypoints.getInstance().getWaypointManager().getUserWaypoints(p).addUserWaypoint(waypointNames.get(p), p.getEyeLocation(), event.getCurrentItem().getType());
                p.sendMessage(ChatColor.GREEN + "Waypoint erstellt!");
                Waypoints.getInstance().getMenuManager().resetPlayerInvState(p);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        assert Waypoints.getInstance().getMenuManager().getPlayerInvState(p) != null;
        assert !Waypoints.getInstance().getMenuManager().getPlayerInvState(p).equals(InvState.NAMING_WAYPOINT);

        e.setCancelled(true);

        if(e.getMessage().trim().toLowerCase(Locale.ROOT).equals("cancel")){
            Waypoints.getInstance().getMenuManager().resetPlayerInvState(p);
            p.sendMessage(ChatColor.RED + "Vorgang abgebrochen!");
            return;
        }

        waypointNames.put(p, ChatColor.translateAlternateColorCodes('&', e.getMessage()));
        p.sendMessage(ChatColor.GOLD + "Bitte eine Farbe für den Waypoint wählen!");
        Waypoints.getInstance().getMenuManager().setPlayerInvState(p, InvState.COLORING_WAYPOINT);
        Bukkit.getScheduler().runTask(Waypoints.getInstance(), () -> {
            p.openInventory(woolColor);
        });
    }


    public void prepareItems() {

        // Create Waypoint Item
        this.createItemStack = new ItemStack(Material.LIME_WOOL);
        ItemMeta createStackItemMeta = createItemStack.getItemMeta();
        createStackItemMeta.setDisplayName(ChatColor.GREEN + "Erstellen");
        createItemStack.setItemMeta(createStackItemMeta);

        // Delete Waypoint Item
        this.deleteItemStack = new ItemStack(Material.RED_WOOL);
        ItemMeta deleteStackItemMeta = deleteItemStack.getItemMeta();
        deleteStackItemMeta.setDisplayName(ChatColor.RED + "Löschen");
        deleteItemStack.setItemMeta(deleteStackItemMeta);
    }

    public void prepareMenu() {
        mainMenu = Bukkit.createInventory(null, 9, ChatColor.DARK_GREEN + "Hauptmenü");
        mainMenu.setItem(0, createItemStack);
        mainMenu.setItem(8, deleteItemStack);

        woolColor = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Bitte Farbe wählen");

        for (Material m : Material.values()) {
            if (m.name().endsWith("_WOOL") && !m.name().contains("LEGACY")) {
                woolColor.addItem(new ItemStack(m));
            }
        }
    }

    public Inventory getInventory() {
        return mainMenu;
    }
}
