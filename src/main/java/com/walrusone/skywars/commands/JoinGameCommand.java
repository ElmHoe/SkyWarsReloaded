package com.walrusone.skywars.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.walrusone.skywars.SkyWarsReloaded;
import com.walrusone.skywars.game.Game;
import com.walrusone.skywars.game.Game.GameState;
import com.walrusone.skywars.game.GamePlayer;
import com.walrusone.skywars.utilities.Messaging;

public class JoinGameCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    		boolean signJoinMode = SkyWarsReloaded.get().getConfig().getBoolean("signJoinMode");
    		boolean hasPerm = false;
    		if (!(sender instanceof Player)) {
    			sender.sendMessage(new Messaging.MessageFormatter().format("error.must-be-player"));
    		} else if (sender instanceof Player) {
    			Player player = (Player) sender;
    			if (SkyWarsReloaded.perms.has(player, "swr.play")) {
    				hasPerm = true;
    			}
    		} else {
    			sender.sendMessage(new Messaging.MessageFormatter().format("error.cmd-no-perm"));
    		}
    		if (hasPerm) {
    			if (args.length == 1) {
    				if (sender instanceof Player) {
    					Player player = (Player) sender;
    					GamePlayer gPlayer = SkyWarsReloaded.getPC().getPlayer(player.getUniqueId());
    			    	if (!signJoinMode) {
        					if (!gPlayer.inGame()) {
        						Game game = SkyWarsReloaded.getGC().findGame();
        	                    if (game != null) {
        	                        game.addPlayer(gPlayer);
        	                    } else {
        	                    	SkyWarsReloaded.getGC().addToQueue(gPlayer);
        	                    	gPlayer.getP().sendMessage(new Messaging.MessageFormatter().format("game.no-game-available"));
        	                    } 
        					} else {
        						sender.sendMessage(new Messaging.MessageFormatter().format("error.no-perm-in-game"));
        					}
    			    	} else {
    			    		Game game = findGame();
    			    		if (game != null && game.getState() == GameState.PREGAME) {
    			                game.addPlayer(gPlayer);
    			    	}
    				}
    			} else {
    				sender.sendMessage(ChatColor.RED + "USAGE: /swr join");
    			}
    		} 
    	}
    	return true;
    }

    private Game findGame() {
		Game game = null;
		int highest = 0;
		for (Game g: SkyWarsReloaded.getGC().getGames()) {
			if (highest <= g.getPlayers().size() && g.getState() == GameState.PREGAME) {
				highest = g.getPlayers().size();
				game = g;
			}
		}
		return game;
    }
}
