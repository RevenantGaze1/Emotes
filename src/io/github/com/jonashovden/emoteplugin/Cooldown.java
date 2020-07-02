package io.github.com.jonashovden.emoteplugin;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class Cooldown {
	
	public static Main plugin;

	public Cooldown(Main instance) {

		plugin = instance;

	}
	
	public static HashMap<String, Long> lastEmote = new HashMap<String, Long>();
	
	public static boolean cooldownFinished(Player player) {
		
		String playerName = player.getName();
		
		int cooldownDurationValue = plugin.getConfig().getInt("cooldown.duration");
		int cooldownDurationMillis = cooldownDurationValue * 1000;
		
		Long lastEmoteInstance = lastEmote
				.get(playerName);
		
		if (lastEmoteInstance == null) {
			
			return true;
		}
		
		else if (lastEmoteInstance
				+ cooldownDurationMillis 
				< System.currentTimeMillis()) {
			
			return true;			
		}
		
		else {
			
			
			return false;
		}
		
	}
	
}