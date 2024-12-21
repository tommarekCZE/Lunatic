package org.evlis.lunamatic.triggers;

import io.papermc.paper.world.MoonPhase;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.evlis.lunamatic.utilities.PlayerMessage;
import org.evlis.lunamatic.utilities.TranslationManager;

public class NightEffects {
    public static void ApplyMoonlight(Player player, MoonPhase moonPhase, Integer timeTilDawn) {
        TranslationManager translationManager = TranslationManager.getInstance();
        if (moonPhase == MoonPhase.FULL_MOON) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, timeTilDawn, 0));
            PlayerMessage.Send(player, translationManager.getTranslation("lucky_feel"), NamedTextColor.DARK_GREEN);
        } else if (moonPhase == MoonPhase.NEW_MOON) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, timeTilDawn, 0));
            PlayerMessage.Send(player, translationManager.getTranslation("wary_feel"), NamedTextColor.DARK_PURPLE);
        }

    }
}
