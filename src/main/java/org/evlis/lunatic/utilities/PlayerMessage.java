package org.evlis.lunatic.utilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerMessage {
    // Send a BOLD message to all players in a list
    public static void Send(List<Player> players, String msg, NamedTextColor color) {
        Component message = Component.text(msg)
                .color(color)
                .decoration(TextDecoration.BOLD, true);
        for (Player p : players) {
            p.sendMessage(message);
        }
    }
    // Send a BOLD message to a specific player
    public static void Send(Player player, String msg, NamedTextColor color) {
        Component message = Component.text(msg)
                .color(color)
                .decoration(TextDecoration.BOLD, true);
        player.sendMessage(message);
    }
}
