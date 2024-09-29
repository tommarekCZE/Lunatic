package org.evlis.lunamatic;

import org.bukkit.Difficulty;

import java.util.Map;

public class GlobalVars {
    // Test flag, makes every night a bloodmoon if true
    public static Boolean debug = true;
    // is there a blood moon today?
    public static Boolean bloodMoonToday = false;
    // is there a blood moon RIGHT NOW?
    public static Boolean bloodMoonNow = false;
    // how far should monsters engage the player from during a blood moon?
    public static final double bloodmoonDetectionRange = 32.0;
    public static final Map<Difficulty, Integer> difficultyArmorMap = Map.of(
            Difficulty.PEACEFUL, 0,
            Difficulty.EASY, 2,
            Difficulty.NORMAL, 3,
            Difficulty.HARD, 4
    );
    public static final Map<Difficulty, Integer> difficultyPotionMap = Map.of(
            Difficulty.PEACEFUL, 0,
            Difficulty.EASY, 0,
            Difficulty.NORMAL, 1,
            Difficulty.HARD, 2
    );
}
