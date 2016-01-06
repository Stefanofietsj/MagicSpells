package com.nisovin.magicspells.spelleffects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.util.ConfigData;

public class BroadcastEffect extends SpellEffect {

	@ConfigData(field="message", dataType="String", defaultValue="")
	String message = "";
	
	@ConfigData(field="range", dataType="int", defaultValue="0")
	int range = 0;
	
	int rangeSq = 0;
	
	@ConfigData(field="targeted", dataType="boolean", defaultValue="false")
	boolean targeted = false;
	
	@Override
	public void loadFromString(String string) {
		message = string;
	}

	@Override
	public void loadFromConfig(ConfigurationSection config) {
		message = config.getString("message", message);
		range = config.getInt("range", range);
		rangeSq = range * range;
		targeted = config.getBoolean("targeted", targeted);
	}

	@Override
	public Runnable playEffectLocation(Location location) {
		broadcast(location, message);
		return null;
	}
	
	@Override
	public Runnable playEffectEntity(Entity entity) {
		if (targeted) {
			if (entity instanceof Player) {
				MagicSpells.sendMessage((Player)entity, message);
			}
		} else {
			String msg = message;
			if (entity instanceof Player) {
				msg = msg.replace("%a", ((Player)entity).getDisplayName()).replace("%t", ((Player)entity).getDisplayName()).replace("%n", ((Player)entity).getName());
			}
			broadcast(entity.getLocation(), msg);
		}
		return null;
	}
	
	private void broadcast(Location location, String message) {
		if (range <= 0) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				MagicSpells.sendMessage(player, message);
			}
		} else {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.getWorld().equals(location.getWorld()) && player.getLocation().distanceSquared(location) <= rangeSq) {
					MagicSpells.sendMessage(player, message);
				}
			}
		}
	}

}
