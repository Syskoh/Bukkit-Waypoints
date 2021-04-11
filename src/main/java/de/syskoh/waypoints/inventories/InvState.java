package de.syskoh.waypoints.inventories;

/**
 * Inverntory states for managing waypoints
 * See resources/InvState.png
 */

public enum InvState {

    MAIN_MENU,

    //Create Waypoint
    NAMING_WAYPOINT,
    COLORING_WAYPOINT,


    //Delete Waypoint
    CHOOSING_DELETE,
    CONFIRM_DELETE
}
