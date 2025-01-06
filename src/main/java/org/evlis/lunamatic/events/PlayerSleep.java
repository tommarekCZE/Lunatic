package org.evlis.lunamatic.events;

import io.papermc.paper.world.MoonPhase;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.evlis.lunamatic.GlobalVars;
import org.evlis.lunamatic.utilities.PlayerMessage;
import org.jetbrains.annotations.NotNull;
import org.evlis.lunamatic.utilities.LangManager;

public class PlayerSleep implements Listener {
    @EventHandler
    public void onPlayerSleep(PlayerBedEnterEvent event) {
        LangManager langManager = LangManager.getInstance();
        Player player = event.getPlayer();
        World world = player.getWorld();
        @NotNull MoonPhase moonPhase = world.getMoonPhase();

        if (GlobalVars.bloodMoonNow) {
            PlayerMessage.Send(player, langManager.getTranslation("blood_moon_sleep"), NamedTextColor.RED);
            event.setCancelled(true);
        }
    }
}
