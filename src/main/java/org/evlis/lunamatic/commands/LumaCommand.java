package org.evlis.lunamatic.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import io.papermc.paper.world.MoonPhase;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.evlis.lunamatic.GlobalVars;
import org.jetbrains.annotations.NotNull;

@CommandAlias("luma")
public class LumaCommand extends BaseCommand {

    private final Plugin plugin; // stores the reference to your main plugin

    public LumaCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Default
    public void defCommand(CommandSender sender) {
        // Display GlobalVars status
        sender.sendMessage("You are running Lunamatic v" + plugin.getPluginMeta().getVersion());
    }

    @Subcommand("reload")
    @CommandPermission("luma.command.reload")
    @Description("Reloads the plugin configuration")
    public void onReload(CommandSender sender) {
        // Display GlobalVars status
        try {
            plugin.reloadConfig();
            sender.sendMessage("Lunamatic reload successful!");
        } catch (Exception e) {
            sender.sendMessage("Lunamatic encountered an error: " + e.getMessage());
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
        player.sendMessage("Blood Moon Enabled: " + GlobalVars.bloodMoonEnabled);
        player.sendMessage("Blood Moon Now: " + GlobalVars.bloodMoonNow);
        player.sendMessage("Blood Moon Today: " + GlobalVars.bloodMoonToday);
        player.sendMessage("Harvest Moon Enabled: " + GlobalVars.harvestMoonEnabled);
        player.sendMessage("Harvest Moon Now: " + GlobalVars.harvestMoonNow);
        player.sendMessage("Harvest Moon Today: " + GlobalVars.harvestMoonToday);
        player.sendMessage("Disabled worlds: " + String.join(", ", GlobalVars.disabledWorlds));
        player.sendMessage("Current moon phase for world " + world.getName() + ": " + moonPhase);
    }
}
