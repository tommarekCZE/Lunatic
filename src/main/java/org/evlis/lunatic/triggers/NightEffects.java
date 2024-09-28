package org.evlis.lunatic.triggers;

import io.papermc.paper.world.MoonPhase;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.evlis.lunatic.utilities.PlayerMessage;

public class NightEffects {

    public static void ApplyMoonlight(Player player, MoonPhase moonPhase, Integer timeTilDawn) {
        if (moonPhase == MoonPhase.FULL_MOON) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, timeTilDawn, 1));
            PlayerMessage.Send(player, "You feel lucky!", NamedTextColor.DARK_GREEN);
        } else if (moonPhase == MoonPhase.NEW_MOON) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, timeTilDawn, 1));
            PlayerMessage.Send(player, "You feel wary...", NamedTextColor.DARK_PURPLE);
        }

    }
}
