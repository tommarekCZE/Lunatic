package org.evlis.lunamatic.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.evlis.lunamatic.GlobalVars;

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

    @Subcommand("status")
    @CommandPermission("luma.command.status")
    @Description("Displays the status of plugin variables")
    public void onStatus(CommandSender sender) {
        // Display GlobalVars status
        sender.sendMessage("Blood Moon Now: " + GlobalVars.bloodMoonNow);
        sender.sendMessage("Blood Moon Today: " + GlobalVars.bloodMoonToday);
        sender.sendMessage("Harvest Moon Now: " + GlobalVars.harvestMoonNow);
        sender.sendMessage("Harvest Moon Today: " + GlobalVars.harvestMoonToday);
    }
}
