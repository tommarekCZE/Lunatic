package org.evlis.lunamatic.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import io.papermc.paper.world.MoonPhase;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.evlis.lunamatic.GlobalVars;
import org.evlis.lunamatic.utilities.ResetFlags;
import org.jetbrains.annotations.NotNull;
import org.evlis.lunamatic.utilities.LangManager;
import org.evlis.lunamatic.Lunamatic;
import org.evlis.lunamatic.utilities.PlayerMessage;

import static org.evlis.lunamatic.Lunamatic.REQUIRED_LANG_VER;

@CommandAlias("luma")
public class LumaCommand extends BaseCommand {
    private final Plugin plugin; // stores the reference to your main plugin

    public LumaCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    private LangManager getLangManager() {
        return LangManager.getInstance(); // Always fetch the latest instance
    }

    @Default
    public void defCommand(CommandSender sender) {
        // Display GlobalVars status
        sender.sendMessage(getLangManager().getTranslation("cmd_running") + plugin.getPluginMeta().getVersion());
    }

    @Subcommand("reload")
    @CommandPermission("luma.command.reload")
    @Description("Reloads the plugin configuration")
    public void onReload(CommandSender sender) {
        // Display GlobalVars status
        try {
            plugin.reloadConfig();
            Lunamatic.getInstance().loadGlobalConfig();

            LangManager.initialize(plugin.getDataFolder(),GlobalVars.lang);

            getLangManager().loadTranslations();

            if (!getLangManager().doesTranslationExist(GlobalVars.lang)) {
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[Lunamatic] " + ChatColor.RESET + ChatColor.RED + GlobalVars.lang + " language does NOT exists! Disabling plugin.");
                sender.sendMessage("Error occurred while loading language! Check console.");
                Bukkit.getPluginManager().disablePlugin(plugin);
                return;
            }

            if (Integer.parseInt(getLangManager().getTranslation("lang_ver")) != REQUIRED_LANG_VER) {
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[Lunamatic] " + ChatColor.RESET + ChatColor.RED + "Unsupported language version! Disabling plugin. Expected lang ver: "+REQUIRED_LANG_VER);
                sender.sendMessage("Error occurred while loading language! Check console.");
                Bukkit.getPluginManager().disablePlugin(plugin);
                return;
            }

            plugin.getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[Lunamatic] " + ChatColor.RESET + ChatColor.GREEN + getLangManager().getTranslation("lang_load_success"));
            sender.sendMessage(getLangManager().getTranslation("cmd_reload_success"));
            if (sender instanceof Player) {
                PlayerMessage.Send((Player) sender,getLangManager().getTranslation("cmd_reload_warn"), NamedTextColor.YELLOW);
            } else {
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[Lunamatic] " + ChatColor.RESET + ChatColor.YELLOW + GlobalVars.lang + getLangManager().getTranslation("cmd_reload_warn"));
            }

        } catch (Exception e) {
            sender.sendMessage(getLangManager().getTranslation("cmd_reload_fail") + e.getMessage());
        }
    }

    @Subcommand("status")
    @CommandPermission("luma.command.status")
    @Description("Displays the status of plugin variables")
    public void onStatus(Player player) {
        // this command is player only!!!
        // get the current world & moon state:
        World world = player.getWorld();
        @NotNull MoonPhase moonPhase = world.getMoonPhase();
        // Display GlobalVars status
        player.sendMessage(getLangManager().getTranslation("cmd_lang") + GlobalVars.lang);
        player.sendMessage(getLangManager().getTranslation("cmd_blood_moon_enabled") + GlobalVars.bloodMoonEnabled);
        player.sendMessage(getLangManager().getTranslation("cmd_blood_moon_now") + GlobalVars.bloodMoonNow);
        player.sendMessage(getLangManager().getTranslation("cmd_blood_moon_today") + GlobalVars.bloodMoonToday);
        player.sendMessage(getLangManager().getTranslation("cmd_harv_moon_enabled") + GlobalVars.harvestMoonEnabled);
        player.sendMessage(getLangManager().getTranslation("cmd_harv_moon_now") + GlobalVars.harvestMoonNow);
        player.sendMessage(getLangManager().getTranslation("cmd_harv_moon_today") + GlobalVars.harvestMoonToday);
        player.sendMessage(getLangManager().getTranslation("cmd_disabled_worlds") + String.join(", ", GlobalVars.disabledWorlds));
        player.sendMessage(getLangManager().getTranslation("cmd_curr_phase") + world.getName() + ": " + moonPhase);
    }

    @Subcommand("makebloodmoon")
    @CommandPermission("luma.command.makebloodmoon")
    @Description("Changes the current world state to Blood Moon")
    public void makeBloodMoon(Player player) {
        // this command is player only!!!
        // get the current world & moon state:
        World world = player.getWorld();
        @NotNull MoonPhase moonPhase = world.getMoonPhase();
        if(moonPhase != MoonPhase.NEW_MOON) {
            ResetFlags.resetAll();
            world.setTime(world.getTime() + (long)GlobalVars.newMoonOffset.getOrDefault(moonPhase, 0));
        }
        if(!GlobalVars.bloodMoonToday){
            GlobalVars.bloodMoonToday = true;
        }
        if(world.getTime() >= 13000 && !GlobalVars.bloodMoonNow) {
            GlobalVars.bloodMoonNow = true;
        }
    }

    @Subcommand("makeharvestmoon")
    @CommandPermission("luma.command.makeharvestmoon")
    @Description("Changes the current world state to Harvest Moon")
    public void makeHarvestMoon(Player player) {
        // this command is player only!!!
        // get the current world & moon state:
        World world = player.getWorld();
        @NotNull MoonPhase moonPhase = world.getMoonPhase();
        if(moonPhase != MoonPhase.FULL_MOON) {
            ResetFlags.resetAll();
            world.setTime(world.getTime() + (long)GlobalVars.fullMoonOffset.getOrDefault(moonPhase, 0));
        }
        if(!GlobalVars.harvestMoonToday){
            GlobalVars.harvestMoonToday = true;
        }
        if(world.getTime() >= 13000 && !GlobalVars.harvestMoonNow) {
            GlobalVars.harvestMoonNow = true;
        }
    }
}
