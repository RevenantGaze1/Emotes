package io.github.com.jonashovden.emoteplugin.commands;

import io.github.com.jonashovden.emoteplugin.Main;

//import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EmotesCmd implements CommandExecutor {

	public Main plugin;

	public EmotesCmd(Main instance) {

		plugin = instance;

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (sender instanceof Player) {

			Player you = (Player) sender;

			int Cooldown = plugin.getConfig().getInt("cooldown.cooldown");
			int DefaultCooldown = plugin.getConfig().getInt("cooldown.default");

			if ((cmd.getName().equalsIgnoreCase("emotes") && you
					.hasPermission("emotes.command.emotes.use"))) {

				if (args.length == 0) {

					you.sendMessage(ChatColor.AQUA + "Available commands for "
							+ ChatColor.GOLD + "Emotes: ");
					you.sendMessage(ChatColor.BLUE + "/emotes");
					you.sendMessage(ChatColor.BLUE + "/emotes help "
							+ ChatColor.GREEN + "[1 | 2]");
					you.sendMessage(ChatColor.BLUE + "/emotes cooldown "
							+ ChatColor.GREEN + "[CHANGE | SHOW | RESET]");
					you.sendMessage(ChatColor.BLUE + "/emotes cooldown change "
							+ ChatColor.GREEN + "[SECONDS]");
					you.sendMessage(ChatColor.BLUE + "/emotes setdistance "
							+ ChatColor.GREEN + "[DISTANCE]");
					you.sendMessage(ChatColor.BLUE + "/emotes reloadconfig");
					you.sendMessage(ChatColor.BLUE + "/emotes version");
					you.sendMessage(ChatColor.BLUE + "/emote");
					you.sendMessage(ChatColor.BLUE + "Page " + ChatColor.RED
							+ "1" + ChatColor.BLUE + " of " + ChatColor.RED
							+ "2");

					return true;

				}

				else if (args.length == 1) {

					if (args[0].equalsIgnoreCase("help")) {

						you.sendMessage(ChatColor.AQUA
								+ "Available commands for " + ChatColor.GOLD
								+ "Emotes: ");
						you.sendMessage(ChatColor.BLUE + "/emotes");
						you.sendMessage(ChatColor.BLUE + "/emotes help "
								+ ChatColor.GREEN + "[1 | 2]");
						you.sendMessage(ChatColor.BLUE + "/emotes cooldown "
								+ ChatColor.GREEN + "[CHANGE | SHOW | RESET]");
						you.sendMessage(ChatColor.BLUE
								+ "/emotes cooldown change " + ChatColor.GREEN
								+ "<SECONDS>");
						you.sendMessage(ChatColor.BLUE + "/emotes setdistance "
								+ ChatColor.GREEN + "<DISTANCE>");
						you.sendMessage(ChatColor.BLUE + "/emotes reloadconfig");
						you.sendMessage(ChatColor.BLUE + "/emotes version");
						you.sendMessage(ChatColor.BLUE + "/emote");
						you.sendMessage(ChatColor.BLUE + "Page "
								+ ChatColor.RED + "1" + ChatColor.BLUE + " of "
								+ ChatColor.RED + "2");

						return true;

					}

					else if (args[0].equalsIgnoreCase("cooldown")
							&& you.hasPermission("emotes.command.emotes.cooldown")) {

						you.sendMessage(ChatColor.BLUE
								+ "Usage: /emotes cooldown [change | show | reset]");

						return true;

					}

					else if (args[0].equalsIgnoreCase("setdistance")) {

						you.sendMessage(ChatColor.BLUE
								+ "Usage: /emotes setdistance <distance>");

						return true;

					}

					else if (args[0].equalsIgnoreCase("reloadconfig")) {
						
						you.sendMessage(ChatColor.BLUE
								+ "Reloading Emotes v" 
								+ ChatColor.RED
								+ plugin.getDescription().getVersion()
								+ ChatColor.BLUE
								+ " config files.");

						plugin.reloadEmoteConfig();
						plugin.reloadConfig();

						return true;

					}

					else if (args[0].equalsIgnoreCase("version")
							&& you.hasPermission("emotes.command.emotes.version")) {

						//String serverVersion = Bukkit.getBukkitVersion();

						you.sendMessage(ChatColor.BLUE + "Emotes version "
								+ ChatColor.RED
								+ plugin.getDescription().getVersion()
								+ ChatColor.BLUE + " for Bukkit/Spigot version "
								//+ ChatColor.RED + serverVersion
								+ ChatColor.RED + "1.15.2"
								+ ChatColor.BLUE + " to "
								+ ChatColor.RED + "1.16.1"
								+ ChatColor.BLUE + "!");

						return true;

					}

					return true;

				}

				else if (args.length == 2) {

					if (args[0].equalsIgnoreCase("help")) {

						int helpSite = Integer.parseInt(args[1]);

						if (helpSite == 1) {

							you.sendMessage(ChatColor.AQUA
									+ "Available commands for "
									+ ChatColor.GOLD + "Emotes: ");
							you.sendMessage(ChatColor.BLUE + "/emotes");
							you.sendMessage(ChatColor.BLUE + "/emotes help "
									+ ChatColor.GREEN + "[1 | 2]");
							you.sendMessage(ChatColor.BLUE
									+ "/emotes cooldown " + ChatColor.GREEN
									+ "[CHANGE | SHOW | RESET]");
							you.sendMessage(ChatColor.BLUE
									+ "/emotes cooldown change "
									+ ChatColor.GREEN + "<SECONDS>");
							you.sendMessage(ChatColor.BLUE
									+ "/emotes setdistance " + ChatColor.GREEN
									+ "<DISTANCE>");
							you.sendMessage(ChatColor.BLUE
									+ "/emotes reloadconfig");
							you.sendMessage(ChatColor.BLUE + "/emotes version");
							you.sendMessage(ChatColor.BLUE + "/emote");
							you.sendMessage(ChatColor.BLUE + "Page "
									+ ChatColor.RED + "1" + ChatColor.BLUE
									+ " of " + ChatColor.RED + "2");

							return true;

						}

						else if (helpSite == 2) {

							you.sendMessage(ChatColor.AQUA
									+ "Available commands for "
									+ ChatColor.GOLD + "Emotes: ");
							you.sendMessage(ChatColor.BLUE + "/emote new "
									+ ChatColor.GREEN + "<NAME>");
							you.sendMessage(ChatColor.BLUE + "/emote use "
									+ ChatColor.GREEN + "<NAME>");
							you.sendMessage(ChatColor.BLUE + "/emote delete "
									+ ChatColor.GREEN + "<NAME>");
							you.sendMessage(ChatColor.BLUE + "/emote edit "
									+ ChatColor.GREEN
									+ " [SPMESSAGE | MESSAGE] <NAME> <MESSAGE>");
							you.sendMessage(ChatColor.BLUE + "/emote help");
							you.sendMessage(ChatColor.BLUE + "Page "
									+ ChatColor.RED + "2" + ChatColor.BLUE
									+ " of " + ChatColor.RED + "2");

							return true;

						}

					}

					else if (args[0].equalsIgnoreCase("cooldown")) {

						if (args[1].equalsIgnoreCase("change")
								&& you.hasPermission("emotes.command.emotes.cooldown.change")) {

							you.sendMessage(ChatColor.BLUE
									+ "Usage: /emotes cooldown change [seconds]");

							return true;

						}

						else if (args[1].equalsIgnoreCase("show")
								&& you.hasPermission("emotes.command.emotes.cooldown.show")) {

							you.sendMessage(ChatColor.BLUE
									+ "The cooldown for emote usage is" + " "
									+ ChatColor.RED + Cooldown + ChatColor.BLUE
									+ " seconds!");

							return true;

						}

						else if (args[1].equalsIgnoreCase("reset")
								&& you.hasPermission("emotes.command.emotes.cooldown.default")) {

							plugin.getConfig().set("cooldown.cooldown",
									DefaultCooldown);
							plugin.saveConfig();

							you.sendMessage(ChatColor.BLUE
									+ "The cooldown for emote usage is now reset to default!");

							return true;

						}

						return true;

					}

					else if (args[0].equalsIgnoreCase("setdistance")
							&& you.hasPermission("emotes.command.emotes.setdistance")) {

						int emotesDistance = Integer.parseInt(args[1]);

						plugin.getConfig().set("emotes-distance",
								emotesDistance);

						you.sendMessage(ChatColor.BLUE
								+ "The travel distance for emotes is now set to "
								+ ChatColor.RED + emotesDistance
								+ ChatColor.BLUE + " blocks!");

						return true;

					}

					return true;

				}

				else if (args.length == 3) {

					if (args[0].equalsIgnoreCase("cooldown")) {

						if (args[1].equalsIgnoreCase("change")
								&& you.hasPermission("emotes.command.emotes.cooldown.change")) {

							int NewCooldown = Integer.parseInt(args[2]);

							plugin.getConfig().set("cooldown.cooldown",
									NewCooldown);
							plugin.saveConfig();

							you.sendMessage(ChatColor.BLUE
									+ "The cooldown for emote usage is now"
									+ " " + ChatColor.RED + NewCooldown
									+ ChatColor.BLUE + " seconds!");

							return true;

						}

						return true;

					}

					return true;

				}

				else if (args.length > 3) {

					you.sendMessage(ChatColor.BLUE + "Too many arguments!");
					you.sendMessage(ChatColor.BLUE
							+ "Usage: /emotes <list | cooldown | setdistance>");

					return true;

				}

			}

			else {

				you.sendMessage(ChatColor.RED + "You don't have permission to do this");

				return true;

			}

		}

		else {

			sender.sendMessage(ChatColor.RED
					+ "This command can not be used from the console!");

		}

		return true;

	}
}
