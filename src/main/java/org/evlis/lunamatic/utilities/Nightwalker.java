package org.evlis.lunamatic.utilities;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class Nightwalker {
    public void SummonVex (Location location) {
        location.getWorld().spawnEntity(location, EntityType.VEX);
    }
}
