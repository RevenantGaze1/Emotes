package io.github.com.jonashovden.emoteplugin;

import io.github.com.jonashovden.emoteplugin.commands.EmoteCmd;
import io.github.com.jonashovden.emoteplugin.commands.EmotesCmd;
import io.github.com.jonashovden.emoteplugin.Metrics;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class Main extends JavaPlugin {

	public Plugin instance;

	File defaultConfigFile = new File(this.getDataFolder(), "config.yml");
	File emoteConfigFile = new File(this.getDataFolder(), "emotes.yml");

	FileConfiguration emoteConfig = YamlConfiguration
			.loadConfiguration(emoteConfigFile);
	FileConfiguration defaultConfig = YamlConfiguration
			.loadConfiguration(defaultConfigFile);

	@Override
	public void onEnable() {

		//Check for config file config.yml
		if (!defaultConfigFile.exists()) {

			getLogger().info("Generating default config file...");

			this.saveResource("config.yml", false);

			this.getConfig()
					.options()
					.header("Default config file for Emotes v"
							+ this.getDescription().getVersion());

			ConfigurationSection cooldownSection = this.getConfig()
					.createSection("cooldown");

			this.getConfig().set("cooldown.duration", 10);
			this.getConfig().set("cooldown.default", 10);
			this.getConfig().set("emotes-distance", 40);
			this.getConfig().set("use-default-emotes", true);
			this.getConfig().set("show-target-warning", true);
			this.getConfig().set("language-string.emote",
					"&7[&6Emotes&7] <emote>");
			this.getConfig()
					.set("language-string.warning.sameworld",
							"&7[&6Emotes&7] &bYou have just been emoted by&c <sender>&b,&c <distance> &bblocks away!");
			this.getConfig()
					.set("language-string.warning.diffworld",
							"&7[&6Emotes&7] &bYou have just been emoted by&c <sender>&b, from another world!");
			this.getConfig()
					.set("language-string.stillcooldown",
							"&7[&6Emotes&7] &bYou have &c<cooldown> &bseconds left before you can use another emote.");
			this.getConfig().set("no-permission-message",
					"You do not have permission to use this emote!");
			this.getConfig().set("enable_metrics", true);

			saveConfig();

			getLogger().info("Config.yml has been generated!");

		}

		else {

			getLogger().info("Found Config.yml");

		}

		if (!emoteConfigFile.exists()) {

			getLogger().info("Generating Emotes.yml...");

			this.getEmoteConfig()
					.options()
					.header("Custom emote configuration file\n"
							+ "Set 'use-default-emotes' to false in the main config if you want to disable the default emotes\n"
							+ "All the emotes you create will be added to 'emotes-list' and will be returned when doing /emote\n"
							+ "--------------------------------------------------------------------------------------------------------\n"
							+ "Sample setup:\n"
							+ "--------------------------------------------------------------------------------------------------------\n"
							+ "message = emote message with a target\n"
							+ "sp-message = emote message without a target\n"
							+ "--------------------------------------------------------------------------------------------------------\n"
							+ "When doing an emote with a target, <s> is the Sender, <t> is the Target\n"
							+ "When doing the emote without a target, <s> is the Sender\n"
							+ "--------------------------------------------------------------------------------------------------------\n"
							+ "emotes:\n"
							+ "  kiss:\n"
							+ "    description: Kiss!\n"
							+ "    usage: /emote use kiss\n"
							+ "    permission: emotes.emote.kiss\n"
							+ "    message: <s> kisses <t>! N'taww!\n"
							+ "    sp-message: <s> blows a kiss into the air. Who is able to catch it?\n"
							+ "--------------------------------------------------------------------------------------------------------\n"
							+ "According to this setup, this emote will return: <s> kisses <t>! N'taww!\n");

			ConfigurationSection emoteSection = this.getEmoteConfig()
					.createSection("emotes");
			ConfigurationSection emoteListSection = this.getEmoteConfig()
					.createSection("emotes-list");

			this.saveEmoteConfig();

			getLogger().info("Emotes.yml has been generated!");

		}

		else {

			getLogger().info("Found Emotes.yml");

		}
		
		
		if (this.getConfig().getBoolean("enable-metrics") == true) {
			
			//Enabling bStats
			int pluginId = 8450;
			
			Metrics metrics = new Metrics(this, pluginId);
			
			getLogger().info("Thank you for enabling metrics - if you want to opt-out from statistic tracking, disable it in the config.");
			
		}
		
		else {
			
			getLogger().info("Metrics disabled!");
			
		}

		getCommand("emotes").setExecutor(new EmotesCmd(this));
		getCommand("emote").setExecutor(new EmoteCmd(this));
		
		Cooldown cooldown = new Cooldown (this);

		boolean checkDefaultUse = this.getConfig().getBoolean(
				"use-default-emotes");

		ConfigurationSection emoteArgueSection = this.getEmoteConfig()
				.getConfigurationSection("emotes.argue");
		ConfigurationSection emoteFacepalmSection = this.getEmoteConfig()
				.getConfigurationSection("emotes.facepalm");
		ConfigurationSection emoteFlailSection = this.getEmoteConfig()
				.getConfigurationSection("emotes.flail");
		ConfigurationSection emoteGlompSection = this.getEmoteConfig()
				.getConfigurationSection("emotes.glomp");
		ConfigurationSection emoteGrumbleSection = this.getEmoteConfig()
				.getConfigurationSection("emotes.grumble");
		ConfigurationSection emoteHateSection = this.getEmoteConfig()
				.getConfigurationSection("emotes.hate");
		ConfigurationSection emoteHugSection = this.getEmoteConfig()
				.getConfigurationSection("emotes.hug");
		ConfigurationSection emoteKickSection = this.getEmoteConfig()
				.getConfigurationSection("emotes.kick");
		ConfigurationSection emoteKissSection = this.getEmoteConfig()
				.getConfigurationSection("emotes.kiss");
		ConfigurationSection emoteLoveSection = this.getEmoteConfig()
				.getConfigurationSection("emotes.love");
		ConfigurationSection emotePokeSection = this.getEmoteConfig()
				.getConfigurationSection("emotes.poke");
		ConfigurationSection emoteSmackSection = this.getEmoteConfig()
				.getConfigurationSection("emotes.smack");
		ConfigurationSection emoteTeabagSection = this.getEmoteConfig()
				.getConfigurationSection("emotes.teabag");
		ConfigurationSection emoteWaveSection = this.getEmoteConfig()
				.getConfigurationSection("emotes.wave");
		ConfigurationSection emoteWhistleSection = this.getEmoteConfig()
				.getConfigurationSection("emotes.whistle");
		ConfigurationSection emoteWinkSection = this.getEmoteConfig()
				.getConfigurationSection("emotes.wink");

		List<String> customEmotes = this.getEmoteConfig().getStringList(
				"emotes-list");

		if (this.getConfig().getBoolean("use-default-emotes") == true) {

			if (emoteArgueSection == null) {

				emoteArgueSection = this.getEmoteConfig().createSection(
						"emotes.argue");

				emoteArgueSection.set("description", "Argue with a player!");
				emoteArgueSection.set("usage", "/emote use argue <target>");
				emoteArgueSection.set("permission", "emotes.emote.argue");
				emoteArgueSection.set("message", "<s> argues with <t>!");
				emoteArgueSection.set("sp-message",
						"<s> argues incoherently into the air!");
				emoteArgueSection.set("colour", "&a");

				customEmotes.add("argue");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteFacepalmSection == null) {

				emoteFacepalmSection = this.getEmoteConfig().createSection(
						"emotes.facepalm");

				emoteFacepalmSection.set("description", "Facepalm!");
				emoteFacepalmSection.set("usage",
						"/emote use facepalm <target>");
				emoteFacepalmSection.set("permission", "emotes.emote.facepalm");
				emoteFacepalmSection.set("message", "<s> facepalms <t>!");
				emoteFacepalmSection.set("sp-message", "<s> facepalms!");
				emoteFacepalmSection.set("colour", "&a");

				customEmotes.add("facepalm");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteFlailSection == null) {

				emoteFlailSection = this.getEmoteConfig().createSection(
						"emotes.flail");

				emoteFlailSection.set("description", "Flail!");
				emoteFlailSection.set("usage", "/emote use flail <target>");
				emoteFlailSection.set("permission", "emotes.emote.flail");
				emoteFlailSection.set("message", "<s> and <t> flails!");
				emoteFlailSection.set("sp-message", "<s> flails!");
				emoteFlailSection.set("colour", "&a");

				customEmotes.add("flail");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteGlompSection == null) {

				emoteGlompSection = this.getEmoteConfig().createSection(
						"emotes.glomp");

				emoteGlompSection.set("description", "Glomp your friends!");
				emoteGlompSection.set("usage", "/emote use glomp <target>");
				emoteGlompSection.set("permission", "emotes.emote.glomp");
				emoteGlompSection.set("message",
						"<s> glomps <t>! It's Super effective!");
				emoteGlompSection.set("sp-message",
						"<s> glomps a block of cobblestone! "
								+ "It's not very effective...");
				emoteGlompSection.set("colour", "&a");

				customEmotes.add("glomp");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteGrumbleSection == null) {

				emoteGrumbleSection = this.getEmoteConfig().createSection(
						"emotes.grumble");

				emoteGrumbleSection.set("description", "Be the grumpy fellow!");
				emoteGrumbleSection.set("usage", "/emote use grumble <target>");
				emoteGrumbleSection.set("permission", "emotes.emote.grumble");
				emoteGrumbleSection.set("message",
						"<s> grumbles something incomprehensible to <t>"
								+ "! What did you say?");
				emoteGrumbleSection.set("sp-message",
						"<s> grumbles to themselves!");
				emoteGrumbleSection.set("colour", "&a");

				customEmotes.add("grumble");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteHateSection == null) {

				emoteHateSection = this.getEmoteConfig().createSection(
						"emotes.hate");

				emoteHateSection.set("description", "Hate other players!");
				emoteHateSection.set("usage", "/emote use hate <target>");
				emoteHateSection.set("permission", "emotes.emote.hate");
				emoteHateSection.set("message", "<s> hates <t>"
						+ "! Why so angry?");
				emoteHateSection.set("sp-message",
						"<s> hates everybody, just like Grumpy Cat!");
				emoteHateSection.set("colour", "&a");

				customEmotes.add("hate");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteHugSection == null) {

				emoteHugSection = this.getEmoteConfig().createSection(
						"emotes.hug");

				emoteHugSection.set("description",
						"Hug your friends and enemies!");
				emoteHugSection.set("usage", "/emote use hug <target>");
				emoteHugSection.set("permission", "emotes.emote.hug");
				emoteHugSection.set("message", "<s> hugs <t>! How cute!");
				emoteHugSection.set("sp-message",
						"<s> runs up to the closest tree and hugs it!");
				emoteHugSection.set("colour", "&a");

				customEmotes.add("hug");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteKickSection == null) {

				emoteKickSection = this.getEmoteConfig().createSection(
						"emotes.kick");

				emoteKickSection.set("description", "Kick your worst nemesis (or best friend)!");
				emoteKickSection.set("usage", "/emote use kick <target>");
				emoteKickSection.set("permission", "emotes.emote.kick");
				emoteKickSection.set("message",
						"<s> kicks <t> in their private parts! Ouch!");
				emoteKickSection.set("sp-message", "<s> kicks the bucket!");
				emoteKickSection.set("colour", "&a");

				customEmotes.add("kick");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteKissSection == null) {

				emoteKissSection = this.getEmoteConfig().createSection(
						"emotes.kiss");

				emoteKissSection.set("description", "Kiss your loved ones!");
				emoteKissSection.set("usage", "/emote use kiss <target>");
				emoteKissSection.set("permission", "emotes.emote.kiss");
				emoteKissSection.set("message", "<s> kisses <t>! N'taww!");
				emoteKissSection
						.set("sp-message",
								"<s> blows a kiss into the air! Who is able to catch it?");
				emoteKissSection.set("colour", "&a");

				customEmotes.add("kiss");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteLoveSection == null) {

				emoteLoveSection = this.getEmoteConfig().createSection(
						"emotes.love");

				emoteLoveSection.set("description", "Broadcast your love!");
				emoteLoveSection.set("usage", "/emote use love <target>");
				emoteLoveSection.set("permission", "emotes.emote.love");
				emoteLoveSection.set("message",
						"<s> loves <t>! What a sweet couple!");
				emoteLoveSection
						.set("sp-message",
								"<s> directs their love towards all neglected creepers!");
				emoteLoveSection.set("colour", "&a");

				customEmotes.add("love");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emotePokeSection == null) {

				emotePokeSection = this.getEmoteConfig().createSection(
						"emotes.poke");

				emotePokeSection.set("description", "Poke, poke!");
				emotePokeSection.set("usage", "/emote use poke <target>");
				emotePokeSection.set("permission", "emotes.emote.poke");
				emotePokeSection.set("message", "<s> pokes <t>! Poke back?");
				emotePokeSection.set("sp-message", "<s> pokes a chicken!");
				emotePokeSection.set("colour", "&a");

				customEmotes.add("poke");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteSmackSection == null) {

				emoteSmackSection = this.getEmoteConfig().createSection(
						"emotes.smack");

				emoteSmackSection.set("description", "Smack someone!");
				emoteSmackSection.set("usage", "/emote use smack <target>");
				emoteSmackSection.set("permission", "emotes.emote.smack");
				emoteSmackSection.set("message",
						"<s> smacks <t>! You had to do that?");
				emoteSmackSection.set("sp-message",
						"<s> smacks their palm against their sword!");
				emoteSmackSection.set("colour", "&a");

				customEmotes.add("smack");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteTeabagSection == null) {

				emoteTeabagSection = this.getEmoteConfig().createSection(
						"emotes.teabag");

				emoteTeabagSection.set("description", "Teabag your enemies!");
				emoteTeabagSection.set("usage", "/emote use teabag <target>");
				emoteTeabagSection.set("permission", "emotes.emote.teabag");
				emoteTeabagSection.set("message",
						"<s> teabags <t>! How embarassing!");
				emoteTeabagSection.set("sp-message",
						"<s> teabags a critter! What did that poor thing do?");
				emoteTeabagSection.set("colour", "&a");

				customEmotes.add("teabag");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteWaveSection == null) {

				emoteWaveSection = this.getEmoteConfig().createSection(
						"emotes.wave");

				emoteWaveSection
						.set("description", "Wave at other players!");
				emoteWaveSection.set("usage", "/emote use wave <target>");
				emoteWaveSection.set("permission", "emotes.emote.wave");
				emoteWaveSection.set("message",
						"<s> waves at <t>! Hey friend!");
				emoteWaveSection.set("sp-message",
						"<s> waves away a mosquito!");
				emoteWaveSection.set("colour", "&a");

				customEmotes.add("wave");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteWhistleSection == null) {

				emoteWhistleSection = this.getEmoteConfig().createSection(
						"emotes.whistle");

				emoteWhistleSection.set("description", "Whistle sweet tunes!");
				emoteWhistleSection.set("usage", "/emote use whistle <target>");
				emoteWhistleSection.set("permission", "emotes.emote.whistle");
				emoteWhistleSection.set("message", "<s> whistles at <t>!");
				emoteWhistleSection.set("sp-message", "<s> whistles a tune!");
				emoteWhistleSection.set("colour", "&a");

				customEmotes.add("whistle");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteWinkSection == null) {

				emoteWinkSection = this.getEmoteConfig().createSection(
						"emotes.wink");

				emoteWinkSection.set("description", "Wink!");
				emoteWinkSection.set("usage", "/emote use wink <target>");
				emoteWinkSection.set("permission", "emotes.emote.wink");
				emoteWinkSection.set("message", "<s> winks at <t>! ;)");
				emoteWinkSection.set("sp-message",
						"<s> winks at a creeper! ;)");
				emoteWinkSection.set("colour", "&a");

				customEmotes.add("wink");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			getLogger().info("Default emotes are enabled!");

		}

		else {

			if (emoteArgueSection != null) {

				this.getEmoteConfig().set("emotes.argue", null);

				customEmotes.remove("argue");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteFacepalmSection != null) {

				this.getEmoteConfig().set("emotes.facepalm", null);

				customEmotes.remove("facepalm");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteFlailSection != null) {

				this.getEmoteConfig().set("emotes.flail", null);

				customEmotes.remove("flail");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteGlompSection != null) {

				this.getEmoteConfig().set("emotes.glomp", null);

				customEmotes.remove("glomp");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteGrumbleSection != null) {

				this.getEmoteConfig().set("emotes.grumble", null);

				customEmotes.remove("grumble");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteHateSection != null) {

				this.getEmoteConfig().set("emotes.hate", null);

				customEmotes.remove("hate");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteHugSection != null) {

				this.getEmoteConfig().set("emotes.hug", null);

				customEmotes.remove("hug");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteKickSection != null) {

				this.getEmoteConfig().set("emotes.kick", null);

				customEmotes.remove("kick");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteKissSection != null) {

				this.getEmoteConfig().set("emotes.kiss", null);

				customEmotes.remove("kiss");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteLoveSection != null) {

				this.getEmoteConfig().set("emotes.love", null);

				customEmotes.remove("love");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emotePokeSection != null) {

				this.getEmoteConfig().set("emotes.poke", null);

				customEmotes.remove("poke");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteSmackSection != null) {

				this.getEmoteConfig().set("emotes.smack", null);

				customEmotes.remove("smack");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteTeabagSection != null) {

				this.getEmoteConfig().set("emotes.teabag", null);

				customEmotes.remove("teabag");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteWaveSection != null) {

				this.getEmoteConfig().set("emotes.wave", null);

				customEmotes.remove("wave");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteWhistleSection != null) {

				this.getEmoteConfig().set("emotes.whistle", null);

				customEmotes.remove("whistle");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			if (emoteWinkSection != null) {

				this.getEmoteConfig().set("emotes.wink", null);

				customEmotes.remove("wink");

				this.getEmoteConfig().set("emotes-list", customEmotes);

				this.saveEmoteConfig();

			}

			getLogger().info("Default emotes are disabled!");

		}
		
		List<String> customEmoteList = this.getEmoteConfig()
				.getStringList("emotes-list");
		
		String emotesListString = String.join(",", customEmoteList);

		getLogger().info(
				"Emotes v" + this.getDescription().getVersion()
						+ " has been enabled!");

	}

	@Override
	public void onDisable() {

		getLogger().info("Emotes has been disabled!");

	}

	public void reloadEmoteConfig() {

		if (emoteConfigFile == null) {

			emoteConfigFile = new File(this.getDataFolder(), "emotes.yml");

		}

		emoteConfig = YamlConfiguration.loadConfiguration(emoteConfigFile);

		InputStream emoteConfigStream = this.getResource("emotes.yml");

		if (emoteConfigStream != null) {

			YamlConfiguration blocksDefault = YamlConfiguration
					.loadConfiguration(new InputStreamReader(emoteConfigStream));
			emoteConfig.setDefaults(blocksDefault);

		}

	}

	public void saveEmoteConfig() {

		try {

			emoteConfig.save(emoteConfigFile);

		}

		catch (IOException ex) {

			Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE,
					"Could not save config to " + emoteConfigFile, ex);

		}

	}

	public FileConfiguration getEmoteConfig() {

		if (emoteConfig == null) {

			this.reloadEmoteConfig();

		}

		return emoteConfig;

	}

	public FileConfiguration getConfig() {

		if (defaultConfig == null) {

			this.reloadConfig();

		}

		return defaultConfig;

	}

}
