package org.evlis.lunamatic.events;

import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.List;

public class EntitySpawn implements Listener {

    // Custom detection range in blocks (default is 16 for most mobs)
    private static final double CUSTOM_DETECTION_RANGE = 32.0;

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Monster) { // Check if the entity is a hostile mob
            Monster monster = (Monster) entity;
            Player nearestPlayer = findNearestPlayer(monster);
            if (nearestPlayer != null) {
                monster.setTarget(nearestPlayer);
            }
        }
    }

    private Player findNearestPlayer(Mob mob) {
        Location mobLocation = mob.getLocation();
        List<Player> players = mobLocation.getWorld().getPlayers(); // Get all players in the world

        Player nearestPlayer = null;
        double nearestDistanceSquared = CUSTOM_DETECTION_RANGE * CUSTOM_DETECTION_RANGE;

        for (Player player : players) {
            double distanceSquared = player.getLocation().distanceSquared(mobLocation);
            if (distanceSquared <= nearestDistanceSquared) {
                nearestPlayer = player;
                nearestDistanceSquared = distanceSquared; // Update the closest distance
            }
        }

        return nearestPlayer; // Returns null if no players are within the custom detection range
    }
}
