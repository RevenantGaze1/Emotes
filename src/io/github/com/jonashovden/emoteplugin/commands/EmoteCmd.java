package io.github.com.jonashovden.emoteplugin.commands;

import java.util.List;

import io.github.com.jonashovden.emoteplugin.Cooldown;
import io.github.com.jonashovden.emoteplugin.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class EmoteCmd implements CommandExecutor {

	public Main plugin;

	public EmoteCmd(Main instance) {

		plugin = instance;

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (cmd.getName().equalsIgnoreCase("emote")) {

			if (sender instanceof Player) {

				Player senderPlayer = (Player) sender;

				if (args.length == 0) {

					if (senderPlayer.hasPermission("emotes.emote.list")) {

						List<String> customEmoteList = plugin.getEmoteConfig()
								.getStringList("emotes-list");

						senderPlayer.sendMessage(ChatColor.AQUA
								+ "Available custom emotes:");

						for (String customEmote : customEmoteList) {

							String emoteDescription = plugin.getEmoteConfig()
									.getString(
											"emotes." + customEmote
													+ ".description");

							if (emoteDescription
									.equalsIgnoreCase("no description! add it now!")) {

								senderPlayer.sendMessage(ChatColor.BLUE
										+ "/emote use " + customEmote
										+ ChatColor.GREEN + " <player>"
										+ ChatColor.AQUA + " - "
										+ ChatColor.RED + emoteDescription);

							}

							else {

								senderPlayer.sendMessage(ChatColor.BLUE
										+ "/emote use " + customEmote
										+ ChatColor.GREEN + " <player>"
										+ ChatColor.AQUA + " - "
										+ ChatColor.DARK_AQUA
										+ emoteDescription);

							}

						}

						return true;

					}

					else {

						senderPlayer.sendMessage(ChatColor.RED
								+ "You do not have permission to do this!");

						return true;

					}

				}

				else if (args.length == 1) {

					if (args[0].equalsIgnoreCase("delete")) {

						if (senderPlayer.hasPermission("emotes.emote.delete")) {

							senderPlayer.sendMessage(ChatColor.BLUE
									+ "Usage: /emote delete <emote>");

							return true;

						}

						else {

							senderPlayer.sendMessage(ChatColor.RED
									+ "You do not have permission to do this!");

							return true;

						}

					}

					else if (args[0].equalsIgnoreCase("new")) {

						if (senderPlayer.hasPermission("emotes.emote.new")) {

							senderPlayer.sendMessage(ChatColor.BLUE
									+ "Usage: /emote new <emote>");

							return true;

						}

						else {

							senderPlayer.sendMessage(ChatColor.RED
									+ "You do not have permission to do this!");

							return true;

						}

					}

					else if (args[0].equalsIgnoreCase("help")) {

						if (senderPlayer.hasPermission("emotes.emote.help")) {

							senderPlayer.sendMessage(ChatColor.AQUA
									+ "Available commands for custom emotes: ");
							senderPlayer.sendMessage(ChatColor.BLUE
									+ "/emote - " + ChatColor.DARK_AQUA
									+ "Returns a list of available emotes");
							senderPlayer.sendMessage(ChatColor.BLUE
									+ "/emote new " + ChatColor.GREEN
									+ "<NAME>" + ChatColor.BLUE + " - "
									+ ChatColor.DARK_AQUA
									+ "Create a new emote");
							senderPlayer.sendMessage(ChatColor.BLUE
									+ "/emote delete " + ChatColor.GREEN
									+ "<NAME>" + ChatColor.BLUE + " - "
									+ ChatColor.DARK_AQUA + "Deletes an emote");
							senderPlayer.sendMessage(ChatColor.BLUE
									+ "/emote help - " + ChatColor.DARK_AQUA
									+ "Shows this help screen");
							senderPlayer
									.sendMessage(ChatColor.BLUE
											+ "/emote edit "
											+ ChatColor.GREEN
											+ "[SPMESSAGE | MESSAGE | DESCRIPTION | PERMISSION]"
											+ " <NAME> <TEXT>" + ChatColor.BLUE
											+ " - " + ChatColor.DARK_AQUA
											+ "Edit an emote");

							return true;

						}

						else {

							senderPlayer.sendMessage(ChatColor.RED
									+ "You do not have permission to do this!");

							return true;

						}

					}

				}

				else if (args.length == 2) {

					if (args[0].equalsIgnoreCase("use")) {

						String emoteName = args[1].toLowerCase();
						String emotePermission = plugin.getEmoteConfig()
								.getString(
										"emotes." + emoteName + ".permission");
						String senderName = senderPlayer.getName();

						if ((senderPlayer.hasPermission("emotes.emote.use."
								+ emoteName)
								|| senderPlayer
										.hasPermission("emotes.emote.use.*")
								|| senderPlayer.hasPermission("emotes.emote.*")
								|| senderPlayer.hasPermission("emotes.*") || senderPlayer
									.hasPermission(emotePermission))) {

							ConfigurationSection customEmoteSection = plugin
									.getEmoteConfig()
									.getConfigurationSection(
											"emotes." + emoteName.toLowerCase());

							if (customEmoteSection == null) {

								senderPlayer.sendMessage(ChatColor.RED
										+ "This emote does not exist!");

								return true;

							}

							else {

								Long lastEmote = Cooldown.lastEmote
										.get(senderPlayer.getName());

								int CooldownValue = plugin.getConfig().getInt(
										"cooldown.cooldown");

								if (lastEmote == null
										|| lastEmote + (CooldownValue * 1000) < System
												.currentTimeMillis()) {

									int emotesDistance = plugin.getConfig()
											.getInt("emotes-distance");

									int distanceSquared = emotesDistance
											* emotesDistance;

									World senderWorld = senderPlayer.getWorld();

									for (Player p : Bukkit.getOnlinePlayers()) {

										World targetWorld = p.getWorld();

										if (emotesDistance == -1) {

											String emoteMessage = plugin
													.getEmoteConfig()
													.getString(
															"emotes."
																	+ emoteName
																	+ ".sp-message");
											String emoteMessageWithName = emoteMessage
													.replace("<s>", senderName);
											String emoteColour = plugin
													.getEmoteConfig()
													.getString(
															"emotes."
																	+ emoteName
																	+ ".colour");
											String emoteColoured = (emoteColour + emoteMessageWithName)
													.replaceAll(
															"(&([a-f0-9]))",
															"\u00A7$2");

											p.sendMessage(emoteColoured);

										}

										else {

											if (senderWorld == targetWorld) {
												
												double senderLocation = senderPlayer
														.getLocation().distanceSquared(
																p.getLocation());

												if (senderLocation < distanceSquared) {

													String emoteLanguageString = plugin
															.getConfig()
															.getString(
																	"language-string.emote");
													String emoteMessage = plugin
															.getEmoteConfig()
															.getString(
																	"emotes."
																			+ emoteName
																			+ ".sp-message");
													String emoteMessageWithName = emoteMessage
															.replace("<s>",
																	senderName);
													String emoteColour = plugin
															.getEmoteConfig()
															.getString(
																	"emotes."
																			+ emoteName
																			+ ".colour");
													String emoteColoured = (emoteColour + emoteMessageWithName)
															.replaceAll(
																	"(&([a-f0-9]))",
																	"\u00A7$2");
													String emoteLanguage = emoteLanguageString
															.replace("<emote>",
																	emoteColoured)
															.replaceAll(
																	"(&([a-f0-9]))",
																	"\u00A7$2");

													p.sendMessage(emoteLanguage);

												}

											}

										}

									}

									Cooldown.lastEmote.put(
											senderPlayer.getName(),
											System.currentTimeMillis());

									return true;

								}

								else {

									int cooldown = (int) (CooldownValue - ((System
											.currentTimeMillis() - (Cooldown.lastEmote
											.get(senderPlayer.getName()))) / 1000));

									String emoteCooldownWarning = plugin
											.getConfig()
											.getString(
													"language-string.stillcooldown");
									String cooldownLeft = Integer
											.toString(cooldown);

									senderPlayer
											.sendMessage(emoteCooldownWarning
													.replace("<cooldown>",
															cooldownLeft)
													.replaceAll(
															"(&([a-f0-9]))",
															"\u00A7$2"));
									
									return true;

								}

							}

						}

						else {

							String noPermMessage = plugin.getEmoteConfig()
									.getString("no-permission-message");

							senderPlayer.sendMessage(ChatColor.RED
									+ noPermMessage);

						}

					}

					else if (args[0].equalsIgnoreCase("new")) {

						if (senderPlayer.hasPermission("emotes.emote.new")) {

							String newEmoteName = args[1];

							List<String> customEmotes = plugin.getEmoteConfig()
									.getStringList("emotes-list");

							ConfigurationSection newEmoteSection = plugin
									.getEmoteConfig().getConfigurationSection(
											"emotes."
													+ newEmoteName
															.toLowerCase());

							if (newEmoteSection == null) {

								customEmotes.add(newEmoteName.toLowerCase());

								plugin.getEmoteConfig().set("emotes-list",
										customEmotes);

								newEmoteSection = plugin.getEmoteConfig()
										.createSection(
												"emotes."
														+ newEmoteName
																.toLowerCase());
								newEmoteSection.set("description",
										"No description! Add it now!");
								newEmoteSection.set("usage", "/emote use "
										+ newEmoteName.toLowerCase()
										+ " <target>");
								newEmoteSection.set(
										"permission",
										"emotes.emote."
												+ newEmoteName.toLowerCase());
								newEmoteSection.set("message", "");
								newEmoteSection.set("sp-message", "");
								newEmoteSection.set("colour", "&a");

								plugin.saveEmoteConfig();

								senderPlayer.sendMessage(ChatColor.AQUA
										+ "Emote " + newEmoteName + " added!");
								senderPlayer
										.sendMessage(ChatColor.AQUA
												+ "Do /emote edit <message | spmessage> <emote> to set the emote message(s)!");
								senderPlayer
										.sendMessage(ChatColor.AQUA
												+ "Use <s> where the sender's name will be shown and \n"
												+ "<t> where the target's name will be shown!");

								return true;

							}

							else {

								senderPlayer.sendMessage(ChatColor.RED
										+ "This command does already exist");

								return true;

							}

						}

						else {

							senderPlayer.sendMessage(ChatColor.RED
									+ "You do not have permission to do this!");

							return true;

						}

					}

					else if (args[0].equalsIgnoreCase("delete")) {

						String emoteName = args[1];

						ConfigurationSection customEmoteSection = plugin
								.getEmoteConfig().getConfigurationSection(
										"emotes." + emoteName);

						if (customEmoteSection != null) {

							plugin.getEmoteConfig().set("emotes." + emoteName,
									null);
							plugin.getEmoteConfig().getList("emotes-list")
									.remove(emoteName);

							plugin.saveEmoteConfig();

							senderPlayer.sendMessage(ChatColor.AQUA
									+ "The emote " + ChatColor.RED + emoteName
									+ ChatColor.AQUA + " has been removed!");

							return true;

						}

						else {

							senderPlayer
									.sendMessage(ChatColor.RED
											+ "The emote you are trying to delete does not exist!");

							return true;

						}

					}

					else {

						senderPlayer.sendMessage(ChatColor.RED
								+ "Invalid subcommand!");
						senderPlayer
								.sendMessage(ChatColor.RED
										+ "Do /emote help to list all /emote commands!");

						return true;

					}

				}

				else if (args.length > 2) {

					if (args[0].equalsIgnoreCase("new")) {

						if (senderPlayer.hasPermission("emotes.emote.new")) {

							senderPlayer.sendMessage(ChatColor.RED
									+ "Too many arguments!");
							senderPlayer.sendMessage(ChatColor.RED
									+ "Usage: /emote new <emote>");

							return true;

						}

						else {

							senderPlayer.sendMessage(ChatColor.RED
									+ "You do not have permission to do this!");

							return true;

						}

					}

					if (args.length == 3) {

						String emoteName = args[1].toLowerCase();

						if (args[0].equalsIgnoreCase("use")) {

							if ((senderPlayer.hasPermission("emotes.emote.use."
									+ emoteName)
									|| senderPlayer
											.hasPermission("emotes.emote.use.*")
									|| senderPlayer
											.hasPermission("emotes.emote.*") || senderPlayer
										.hasPermission("emotes.*"))) {

								String senderName = senderPlayer.getName();
								String targetName = args[2];

								ConfigurationSection customEmoteSection = plugin
										.getEmoteConfig()
										.getConfigurationSection(
												"emotes." + emoteName);

								if (customEmoteSection == null) {

									senderPlayer.sendMessage(ChatColor.RED
											+ "This emote does not exist!");

									return true;

								}

								else {

									if (Bukkit.getPlayerExact(targetName) != null) {

										Long lastEmote = Cooldown.lastEmote
												.get(senderPlayer.getName());

										int CooldownValue = plugin.getConfig()
												.getInt("cooldown.cooldown");

										if (lastEmote == null
												|| lastEmote
														+ (CooldownValue * 1000) < System
															.currentTimeMillis()) {

											int emotesDistance = plugin
													.getConfig().getInt(
															"emotes-distance");

											int distanceSquared = emotesDistance
													* emotesDistance;

											World senderWorld = senderPlayer
													.getWorld();

											for (Player p : Bukkit
													.getOnlinePlayers()) {

												boolean showTargetWarning = plugin
														.getConfig()
														.getBoolean(
																"show-target-warning");

												World targetWorld = p
														.getWorld();

												if (emotesDistance == -1) {

													String emoteLanguageString = plugin
															.getConfig()
															.getString(
																	"language-string.emote");
													String emoteMessage = plugin
															.getEmoteConfig()
															.getString(
																	"emotes."
																			+ emoteName
																			+ ".message");
													String emoteMessageWithName = emoteMessage
															.replace("<s>",
																	senderName);
													String emoteMessageWithNames = emoteMessageWithName
															.replace("<t>",
																	targetName);
													String emoteColour = plugin
															.getEmoteConfig()
															.getString(
																	"emotes."
																			+ emoteName
																			+ ".colour");
													String emoteColoured = (emoteColour + emoteMessageWithNames)
															.replaceAll(
																	"(&([a-f0-9]))",
																	"\u00A7$2");
													String emoteLanguage = emoteLanguageString
															.replace("<emote>",
																	emoteColoured)
															.replaceAll(
																	"(&([a-f0-9]))",
																	"\u00A7$2");

													p.sendMessage(emoteLanguage);

													if (showTargetWarning == true) {

														String emoteWarningString1 = plugin
																.getConfig()
																.getString(
																		"language-string.warning.sameworld");
														String emoteWarningString2 = plugin
																.getConfig()
																.getString(
																		"language-string.warning.diffworld");

														Player targetPlayer = Bukkit
																.getPlayer(targetName);

														World targetWarningWorld = targetPlayer
																.getWorld();

														if (senderWorld == targetWarningWorld) {
															
															double senderLocation = senderPlayer
																	.getLocation()
																	.distanceSquared(
																			p.getLocation());

															int senderDistance = (int) Math
																	.round(senderLocation);

															String senderDistanceString = Integer
																	.toString(senderDistance);

															targetPlayer
																	.sendMessage(emoteWarningString1
																			.replace(
																					"<distance>",
																					senderDistanceString)
																			.replace(
																					"<sender>",
																					senderPlayer
																							.getName())
																			.replaceAll(
																					"(&([a-f0-9]))",
																					"\u00A7$2"));

														}

														else {

															targetPlayer
																	.sendMessage(emoteWarningString2
																			.replaceAll(
																					"(&([a-f0-9]))",
																					"\u00A7$2"));

														}

													}

												}

												else {

													if (senderWorld == targetWorld) {
														
														double senderLocation = senderPlayer
																.getLocation()
																.distanceSquared(
																		p.getLocation());

														if (senderLocation < distanceSquared) {

															String emoteLanguageString = plugin
																	.getConfig()
																	.getString(
																			"language-string.emote");
															String emoteMessage = plugin
																	.getEmoteConfig()
																	.getString(
																			"emotes."
																					+ emoteName
																					+ ".message");
															String emoteMessageWithName = emoteMessage
																	.replace(
																			"<s>",
																			senderName);
															String emoteMessageWithNames = emoteMessageWithName
																	.replace(
																			"<t>",
																			targetName);
															String emoteColour = plugin
																	.getEmoteConfig()
																	.getString(
																			"emotes."
																					+ emoteName
																					+ ".colour");
															String emoteColoured = (emoteColour + emoteMessageWithNames)
																	.replaceAll(
																			"(&([a-f0-9]))",
																			"\u00A7$2");
															String emoteLanguage = emoteLanguageString
																	.replace(
																			"<emote>",
																			emoteColoured)
																	.replaceAll(
																			"(&([a-f0-9]))",
																			"\u00A7$2");

															p.sendMessage(emoteLanguage);

															if (showTargetWarning == true) {

																String emoteWarningString1 = plugin
																		.getConfig()
																		.getString(
																				"language-string.warning.sameworld");
																String emoteWarningString2 = plugin
																		.getConfig()
																		.getString(
																				"language-string.warning.diffworld");

																Player targetPlayer = Bukkit
																		.getPlayer(targetName);

																World targetWarningWorld = targetPlayer
																		.getWorld();

																if (senderWorld == targetWarningWorld) {

																	int senderDistance = (int) Math
																			.round(senderLocation);

																	String senderDistanceString = Integer
																			.toString(senderDistance);

																	targetPlayer
																			.sendMessage(emoteWarningString1
																					.replace(
																							"<distance>",
																							senderDistanceString)
																					.replace(
																							"<sender>",
																							senderPlayer
																									.getName())
																					.replaceAll(
																							"(&([a-f0-9]))",
																							"\u00A7$2"));

																}

																else {

																	targetPlayer
																			.sendMessage(emoteWarningString2
																					.replaceAll(
																							"(&([a-f0-9]))",
																							"\u00A7$2"));

																}

															}

														}

													}

												}

											}

											Cooldown.lastEmote.put(
													senderPlayer.getName(),
													System.currentTimeMillis());

											return true;

										}

										else {

											int cooldown = (int) (CooldownValue - ((System
													.currentTimeMillis() - (Cooldown.lastEmote
													.get(senderPlayer.getName()))) / 1000));

											String emoteCooldownWarning = plugin
													.getConfig()
													.getString(
															"language-string.stillcooldown");
											String cooldownLeft = Integer
													.toString(cooldown);

											senderPlayer
													.sendMessage(emoteCooldownWarning
															.replace(
																	"<cooldown>",
																	cooldownLeft)
															.replaceAll(
																	"(&([a-f0-9]))",
																	"\u00A7$2"));

											return true;

										}

									}

									else {

										senderPlayer.sendMessage(ChatColor.RED
												+ "This player is not online!");

										return true;

									}

								}

							}

							else {

								String noPermMessage = plugin.getEmoteConfig()
										.getString("no-permission-message");

								senderPlayer.sendMessage(ChatColor.RED
										+ noPermMessage);

							}

						}

					}

					else if (args[0].equalsIgnoreCase("edit")) {

						String editEmoteName = args[2];

						List<String> customEmotes = plugin.getEmoteConfig()
								.getStringList("emotes-list");

						if (customEmotes.contains(editEmoteName)) {

							StringBuilder emoteMessageBuilder = new StringBuilder();

							for (int i = 3; i < args.length; i++) {

								emoteMessageBuilder.append(args[i]);
								emoteMessageBuilder.append(" ");

							}

							if (args[1].equalsIgnoreCase("message")) {

								if (senderPlayer
										.hasPermission("emotes.emote.edit.message")) {

									String emoteMessage = emoteMessageBuilder
											.toString();

									plugin.getEmoteConfig().set(
											"emotes." + editEmoteName
													+ ".message", emoteMessage);
									plugin.saveEmoteConfig();

									senderPlayer.sendMessage(ChatColor.GOLD
											+ "The emote messages for " + ChatColor.AQUA + editEmoteName + ChatColor.GOLD
											+ " are:");
									senderPlayer.sendMessage(ChatColor.GREEN
											+ "With target - " + ChatColor.AQUA
											+ "'" + emoteMessage + "'");

									String emoteMessageWithoutTarget = plugin
											.getEmoteConfig().getString(
													"emotes." + editEmoteName
															+ ".sp-message");

									if (emoteMessageWithoutTarget
											.equalsIgnoreCase("")) {

										senderPlayer
												.sendMessage(ChatColor.GREEN
														+ "Without target - "
														+ ChatColor.RED
														+ "This emote's message has not been added! Add it now in emotes.yml, or use /emote edit spmessage <emote> <message>!");

									}

									else {

										senderPlayer
												.sendMessage(ChatColor.GREEN
														+ "Without target - "
														+ ChatColor.AQUA
														+ "'"
														+ emoteMessageWithoutTarget
														+ "'");

									}

									return true;

								}

								else {

									senderPlayer
											.sendMessage(ChatColor.RED
													+ "You do not have permission to do this!");

									return true;

								}

							}

							else if (args[1].equalsIgnoreCase("spmessage")) {

								if (senderPlayer
										.hasPermission("emotes.emote.edit.spmessage")) {

									String emoteMessage = emoteMessageBuilder
											.toString();

									plugin.getEmoteConfig().set(
											"emotes." + editEmoteName
													+ ".sp-message",
											emoteMessage);
									plugin.saveEmoteConfig();

									senderPlayer.sendMessage(ChatColor.GOLD
											+ "The emote messages for " + ChatColor.AQUA + editEmoteName + ChatColor.GOLD + " are:");

									String emoteMessageWithTarget = plugin
											.getEmoteConfig().getString(
													"emotes." + editEmoteName
															+ ".message");

									if (emoteMessageWithTarget
											.equalsIgnoreCase("")) {

										senderPlayer
												.sendMessage(ChatColor.GREEN
														+ "With target - "
														+ ChatColor.RED
														+ "This emote's message has not been added! Add it now in emotes.yml, or use /emote edit message <emote> <message>!");

									}

									else {

										senderPlayer
												.sendMessage(ChatColor.GREEN
														+ "With target - "
														+ ChatColor.AQUA
														+ "'"
														+ emoteMessageWithTarget
														+ "'");

									}

									senderPlayer.sendMessage(ChatColor.GREEN
											+ "Without target - "
											+ ChatColor.AQUA + "'"
											+ emoteMessage + "'");

									return true;

								}

								else {

									senderPlayer
											.sendMessage(ChatColor.RED
													+ "You do not have permission to do this!");

									return true;

								}

							}

							else if (args[1].equalsIgnoreCase("description")) {

								if (senderPlayer
										.hasPermission("emotes.emote.edit.description")) {

									String emoteMessage = emoteMessageBuilder
											.toString();

									plugin.getEmoteConfig().set(
											"emotes." + editEmoteName
													+ ".description",
											emoteMessage);
									plugin.saveEmoteConfig();

									senderPlayer
											.sendMessage(ChatColor.GOLD
													+ "The emote's description is now:");
									senderPlayer.sendMessage(ChatColor.AQUA
											+ "'" + emoteMessage + "'");

									return true;

								}

								else {

									senderPlayer
											.sendMessage(ChatColor.RED
													+ "You do not have permission to do this!");

									return true;

								}

							}

							else if (args[1].equalsIgnoreCase("permission")) {

								if (senderPlayer
										.hasPermission("emotes.emote.edit.permission")) {

									if (args.length == 4) {

										String emotePermission = args[3]
												.toLowerCase();

										plugin.getEmoteConfig().set(
												"emotes." + editEmoteName
														+ ".permission",
												emotePermission);
										plugin.saveEmoteConfig();

										senderPlayer
												.sendMessage(ChatColor.GOLD
														+ "The emote's permission is now:");
										senderPlayer.sendMessage(ChatColor.AQUA
												+ "'" + emotePermission + "'");

										return true;

									}

								}

								else {

									senderPlayer
											.sendMessage(ChatColor.RED
													+ "You do not have permission to do this!");

									return true;

								}

							}

							else {

								senderPlayer.sendMessage(ChatColor.RED
										+ "Invalid subcommand!");
								senderPlayer
										.sendMessage(ChatColor.RED
												+ "Do /emote help to list all /emote commands!");

								return true;

							}

						}

						else {

							senderPlayer.sendMessage(ChatColor.RED
									+ "This emote does not exist!");

							return true;

						}

					}

					else {

						senderPlayer.sendMessage(ChatColor.RED
								+ "Invalid subcommand!");
						senderPlayer
								.sendMessage(ChatColor.RED
										+ "Do /emote help to list all /emote commands!");

						return true;

					}

					return true;

				}

				else if (args.length >= 4) {

					if (senderPlayer.hasPermission("emotes.emote.use")) {

						String emoteName = args[1];

						if ((senderPlayer.hasPermission("emotes.emote."
								+ emoteName.toLowerCase()) || senderPlayer
								.isOp())) {

							if (args[0].equalsIgnoreCase("use")) {

								ConfigurationSection customEmoteSection = plugin
										.getEmoteConfig()
										.getConfigurationSection(
												"emotes." + emoteName);

								if (customEmoteSection == null) {

									senderPlayer.sendMessage(ChatColor.RED
											+ "This emote does not exist!");

									return true;

								}

								else {

									senderPlayer.sendMessage(ChatColor.RED
											+ "Too many arguments!");
									senderPlayer
											.sendMessage(ChatColor.RED
													+ plugin.getEmoteConfig()
															.getString(
																	"emotes."
																			+ emoteName
																			+ ".usage"));

									return true;

								}

							}

							else {

								senderPlayer.sendMessage(ChatColor.RED
										+ "Invalid subcommand!");
								senderPlayer
										.sendMessage(ChatColor.RED
												+ "Do /emote help to list all /emote commands!");

								return true;

							}

						}

					}

					else {

						senderPlayer.sendMessage(ChatColor.RED
								+ "You do not have permission to do this!");

						return true;

					}

				}

			}

			return true;

		}

		return true;

	}
}
