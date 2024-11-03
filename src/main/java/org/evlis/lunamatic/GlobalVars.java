package org.evlis.lunamatic;

import org.bukkit.Difficulty;

import java.util.List;
import java.util.Map;

public class GlobalVars {
    // Test flag, makes every night a bloodmoon if true
    public static Boolean debug = false;
    // enabled moons:
    public static Boolean fullMoonEnabled = true;
    public static Boolean harvestMoonEnabled = true;
    public static Boolean newMoonEnabled = true;
    public static Boolean bloodMoonEnabled = true;
    // Dice sides for blood & harvest
    public static Integer bloodMoonDieSides = 2;
    public static Integer harvestMoonDieSides = 2;
    // is there a blood moon today?
    public static Boolean bloodMoonToday = false;
    // is there a harvest moon today?
    public static Boolean harvestMoonToday = false;
    // is there a blood moon RIGHT NOW?
    public static Boolean bloodMoonNow = false;
    // is there a harvest moon RIGHT NOW?
    public static Boolean harvestMoonNow = false;
    // how far should monsters engage the player from during a blood moon?
    public static final double bloodmoonDetectionRange = 32.0;
    // worlds to exclude entirely from moon effects
    public static List<String> disabledWorlds = List.of();
    // map of how many armor pieces to apply
    public static final Map<Difficulty, Integer> difficultyArmorMap = Map.of(
            Difficulty.PEACEFUL, 0,
            Difficulty.EASY, 2,
            Difficulty.NORMAL, 3,
            Difficulty.HARD, 4
    );
    // map of what level potion effect mobs should get
    public static final Map<Difficulty, Integer> difficultyPotionMap = Map.of(
            Difficulty.PEACEFUL, 0,
            Difficulty.EASY, 0,
            Difficulty.NORMAL, 1,
            Difficulty.HARD, 2
    );
}
