package com.walrusone.skywars.commands;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.walrusone.skywars.SkyWarsReloaded;
import com.walrusone.skywars.utilities.Messaging;

public class RegisterMapCommand implements CommandExecutor {

	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		boolean hasPerm = false;
		if (!(sender instanceof Player)) {
			hasPerm = true;
		} else if (sender instanceof Player) {
			Player player = (Player) sender;
			if (SkyWarsReloaded.perms.has(player, "swr.maps")) {
				hasPerm = true;
			}
		} else {
			sender.sendMessage(new Messaging.MessageFormatter().format("error.cmd-no-perm"));
		}
		if (hasPerm) {
			if (args.length == 2) {
				String worldName = args[1].toLowerCase();
				if (SkyWarsReloaded.getMC().mapExists(worldName)) {
					File dataDirectory = new File(SkyWarsReloaded.get().getDataFolder(), "maps");
					File newMap = new File (dataDirectory, worldName);
					if (newMap.isDirectory()) {
						if (SkyWarsReloaded.getMC().registerMap(worldName)) {
							sender.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", worldName).format("maps.registered"));
						} else {
							sender.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", worldName).format("error.map-not-registered"));
						}
					} else {
						sender.sendMessage(new Messaging.MessageFormatter().format("error-register-is-it-saved"));
					}
				} else {
					sender.sendMessage(new Messaging.MessageFormatter().format("error-register-not-exist"));
				}
			}else {
				sender.sendMessage(ChatColor.RED + "USAGE: /swr register <map name>");
			}
		} 
		return true;
	}
}
