package me.nauctos.coordgrabber;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin{

	@Override
	public void onEnable() {
		// Startup
		// Reloads
		// plugin Reloads
		
	}
	
	@Override
	public void onDisable() {
		// shutdown
		// reload
		// plugin reload
		
	}
	boolean stopbroadcasting = false;
	int broadcastrunning = 0;
	ArrayList<Integer> broadcastIds = new ArrayList<Integer>();
	ArrayList<Integer> grabIds = new ArrayList<Integer>();
	boolean yHide = false;
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		/**
		 *  /grabcoords
		 */
		if(label.equalsIgnoreCase("grabcoords")) {
			if(sender instanceof Player) {
				// player
				Player player = (Player) sender;
				if(player.hasPermission("coordgrabber.grabcoords.own") && args.length == 0) {
					// /getcoords
					Location loc = player.getLocation();
					String message = "Your coordinates are: x: " 
							+ (int) loc.getX();
					if(!yHide) {
						message += ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
					}else {
						message += ", z: " + (int) loc.getZ();
					}
					player.sendMessage(message);
					return true;
				}
				if(player.hasPermission("coordgrabber.grabcoords.others") && args.length == 1) {
					// /getcoords player
					Player pOther = Bukkit.getPlayer(args[0]);
					Location loc = pOther.getLocation();
					String message = "The coordinates of " + args[0] + " are: x: " 
							+ (int) loc.getX() + ", y: " + (int) loc.getY();
					if(!yHide) {
						message += ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
					}else {
						message += ", z: " + (int) loc.getZ();
					}
					player.sendMessage(message);
					return true;
				}
				if(player.hasPermission("coordgrabber.grabcoords.timer") && args.length == 2) {
					try {
						int delay = Integer.parseInt(args[1]);
						delay *= 20; // Make time be in seconds
						broadcastrunning++;
						Player pOther = Bukkit.getPlayer(args[0]);	
						// Timer loop
						int i = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
						    @Override
						    public void run() {
						    	Location loc = pOther.getLocation();
						    	String message = "The coordinates of " + args[0] + " are: x: " 
										+ (int) loc.getX() + ", y: " + (int) loc.getY();
						    	if(!yHide) {
									message += ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
								}else {
									message += ", z: " + (int) loc.getZ();
								}
						    	player.sendMessage(message);
						    }
						}, 20L, delay); //0 Tick initial delay, 20 Tick (1 Second) between repeats
						grabIds.add(i);
					}catch (NumberFormatException nfe) {
						// Parameter not an integer
						player.sendMessage(ChatColor.DARK_RED + "The second parameter must be an integer!");
					}
					
					return true;
				}
				// ERRORS
				if(args.length > 1) {
					player.sendMessage(ChatColor.DARK_RED + "Too many arguments!");
					return true;
				} else if(!(player.hasPermission("coordgrabber.grabcoords.own")) && args.length == 0) {
					player.sendMessage(ChatColor.DARK_RED + "You do not have permission to do that!");
					return true;
				} else if(!(player.hasPermission("coordgrabber.grabcoords.others")) && args.length == 1) {
					player.sendMessage(ChatColor.DARK_RED + "You do not have permission to do that!");
					return true;
				}
				return true;
			} else {
				/**
				 * CONSOLE COMMANDS
				 */
				if(args.length == 0) {
					sender.sendMessage("Too few arguments! Specify the name of the player!");
					return true;
				}
				if(args.length == 1) {
					// /getcoords player
					Player pOther = Bukkit.getPlayer(args[0]);
					Location loc = pOther.getLocation();
					String message = "The coordinates of " + args[0] + " are: x: " 
							+ (int) loc.getX() + ", y: " + (int) loc.getY();
					if(!yHide) {
						message += ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
					}else {
						message += ", z: " + (int) loc.getZ();
					}
					sender.sendMessage(message);
					return true;
				}
				if(args.length == 2) {
					try {
						int delay = Integer.parseInt(args[1]);
						delay *= 20; // Make time be in seconds
						broadcastrunning++;
						Player pOther = Bukkit.getPlayer(args[0]);	
						// Timer loop
						int i = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
						    @Override
						    public void run() {
						    	Location loc = pOther.getLocation();
						    	String message = "The coordinates of " + args[0] + " are: x: " 
										+ (int) loc.getX();
						    	if(!yHide) {
									message += ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
								}else {
									message += ", z: " + (int) loc.getZ();
								}
						    	sender.sendMessage(message);
						    }
						}, 20L, delay); //0 Tick initial delay, 20 Tick (1 Second) between repeats
						grabIds.add(i);
					}catch (NumberFormatException nfe) {
						// Parameter not an integer
						sender.sendMessage(ChatColor.DARK_RED + "The second parameter must be an integer!");
					}
					
					return true;
				}
			}
		}
		
		/**
		 *  /stopbroadcastingcoords
		 *  
		 */
		if(label.equalsIgnoreCase("stopbroadcastingcoords")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(player.hasPermission("coordgrabber.broadcastcoords.timer") && broadcastIds.size() > 0) {
					// PLAYER SENT
					for(int i = 0; i < broadcastIds.size(); i++) {
						Bukkit.getScheduler().cancelTask(broadcastIds.get(i));
					}
					broadcastIds.clear();
					broadcastrunning = 0;
					return true;
				}else if(!(broadcastrunning > 0)) {
					player.sendMessage(ChatColor.DARK_RED + "There is not a broadcast running!");
					return true;
				}
				else {
					player.sendMessage(ChatColor.DARK_RED + "You do not have permission to execute that command!");
					return true;
				}
			} else {
				// CONSOLE SENT
				for(int i = 0; i < broadcastIds.size(); i++) {
					Bukkit.getScheduler().cancelTask(broadcastIds.get(i));
				}
				broadcastIds.clear();
				broadcastrunning = 0;
				return true;
			}
			
		}
		/**
		 *  /stopgrabbingcoords
		 *  
		 */
		if(label.equalsIgnoreCase("stopgrabbingcoords")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(player.hasPermission("coordgrabber.grabcoords.timer") && grabIds.size() > 0) {
					// PLAYER SENT
					for(int i = 0; i < grabIds.size(); i++) {
						Bukkit.getScheduler().cancelTask(grabIds.get(i));
					}
					grabIds.clear();
					broadcastrunning = 0;
					return true;
				}else if(!(broadcastrunning > 0)) {
					player.sendMessage(ChatColor.DARK_RED + "There is not a broadcast running!");
					return true;
				}
				else {
					player.sendMessage(ChatColor.DARK_RED + "You do not have permission to execute that command!");
					return true;
				}
			} else {
				// CONSOLE SENT
				for(int i = 0; i < grabIds.size(); i++) {
					Bukkit.getScheduler().cancelTask(grabIds.get(i));
				}
				grabIds.clear();
				broadcastrunning = 0;
				return true;
			}
			
		}
		/**
		 *  /toggley
		 *  
		 */
		if(label.equalsIgnoreCase("cgtoggley")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(player.hasPermission("coordgrabber.toggley")) {
					// PLAYER SENT
					if(yHide) {
						yHide = false;
						player.sendMessage("[CoordGrabber] The y coord is now visible.");
					} else {
						yHide = true;
						player.sendMessage("[CoordGrabber] The y coord is now hidden.");
					}
					return true;
				}
				else {
					player.sendMessage(ChatColor.DARK_RED + "You do not have permission to execute that command!");
					return true;
				}
			} else {
				// CONSOLE SENT
				if(yHide) {
					yHide = false;
					sender.sendMessage("[CoordGrabber] The y coord is now visible.");
				} else {
					yHide = true;
					sender.sendMessage("[CoordGrabber] The y coord is now hidden.");
				}
				return true;
			}
			
		}
		/**
		 *  /broadcastcoords
		 */
		if(label.equalsIgnoreCase("broadcastcoords")) {
			if(sender instanceof Player) {
				// player
				Player player = (Player) sender;
				if(player.hasPermission("coordgrabber.broadcastcoords.own") && args.length == 0) {
					// /broadcastcoords
					Location loc = player.getLocation();
					String message = "The coordinates of " + player.getName() + " are: x: " 
							+ (int) loc.getX();
					if(!yHide) {
						message += ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
					}else {
						message += ", z: " + (int) loc.getZ();
					}
					Bukkit.broadcastMessage(message);
					return true;
				}
				if(player.hasPermission("coordgrabber.broadcoords.others") && args.length == 1) {
					// /broadcastcoords <player>
					Player pOther = Bukkit.getPlayer(args[0]);
					Location loc = pOther.getLocation();
					String message = "The coordinates of " + args[0] + " are: x: " 
							+ (int) loc.getX();
					if(!yHide) {
						message += ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
					}else {
						message += ", z: " + (int) loc.getZ();
					}
					Bukkit.broadcastMessage(message);
					return true;
				}
				/**
				 *  /broadcastcoords <player> <timeInSeconds>
				 *  Broadcast on a timer!
				 */
				if(player.hasPermission("coordgrabber.broadcastcoords.timer") && args.length == 2) {
					try {
						int delay = Integer.parseInt(args[1]);
						delay *= 20; // Make time be in seconds
						broadcastrunning++;
						Player pOther = Bukkit.getPlayer(args[0]);	
						// Timer loop
						int i = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
						    @Override
						    public void run() {
						    	Location loc = pOther.getLocation();
						    	String message = "The coordinates of " + args[0] + " are: x: " 
										+ (int) loc.getX();
						    	if(!yHide) {
									message += ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
								}else {
									message += ", z: " + (int) loc.getZ();
								}
								Bukkit.broadcastMessage(message);	
						    }
						}, 20L, delay); //0 Tick initial delay, 20 Tick (1 Second) between repeats
						broadcastIds.add(i);
					}catch (NumberFormatException nfe) {
						// Parameter not an integer
						player.sendMessage(ChatColor.DARK_RED + "The second parameter must be an integer!");
					}
					
					return true;
				}
				player.sendMessage(ChatColor.DARK_RED + "You do not have permission to execute that command!");
				return true;
			} else {
				/**
				 * CONSOLE COMMANDS
				 */
				if(args.length == 1) {
					// broadcastcoords <player>
					Player pOther = Bukkit.getPlayer(args[0]);
					Location loc = pOther.getLocation();
					String message = "The coordinates of " + args[0] + " are: x: " 
							+ (int) loc.getX();
					if(!yHide) {
						message += ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
					}else {
						message += ", z: " + (int) loc.getZ();
					}
					Bukkit.broadcastMessage(message);
					return true;
				}
				if(args.length == 2) {
					// broadcastcoords <player> <timeInSeconds>
					try {
						int delay = Integer.parseInt(args[1]);
						delay *= 20; // Make time be in seconds
						broadcastrunning++;
						Player pOther = Bukkit.getPlayer(args[0]);
						// Timer loop
						int i = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
						    @Override
						    public void run() {
						    	Location loc = pOther.getLocation();
						    	String message = "The coordinates of " + args[0] + " are: x: " 
										+ (int) loc.getX();
						    	if(!yHide) {
									message += ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
								}else {
									message += ", z: " + (int) loc.getZ();
								}
								Bukkit.broadcastMessage(message);	
						    }
						}, 20L, delay); //0 Tick initial delay, 20 Tick (1 Second) between repeats
						broadcastIds.add(i);
					}catch (NumberFormatException nfe) {
						// Parameter not an integer
						sender.sendMessage(ChatColor.DARK_RED + "The second parameter must be an integer!");
					}
					return true;
				}
			}
		}
		
		return false;
	}
	
}
