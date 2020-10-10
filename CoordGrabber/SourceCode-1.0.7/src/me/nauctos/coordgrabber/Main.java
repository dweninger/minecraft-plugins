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
	ArrayList<Integer> sendIds = new ArrayList<Integer>();
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
					String message = ChatColor.GOLD + "Your " + ChatColor.GRAY + "coordinates are: " + ChatColor.DARK_AQUA + "x: " 
							+ (int) loc.getX();
					if(!yHide) {
						message += ChatColor.DARK_AQUA + ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
					}else {
						message += ChatColor.DARK_AQUA + ", z: " + (int) loc.getZ();
					}
					player.sendMessage(message);
					return true;
				}
				if(player.hasPermission("coordgrabber.grabcoords.others") && args.length == 1) {
					// /getcoords player
					Player pOther = Bukkit.getPlayer(args[0]);
					Location loc = pOther.getLocation();
					String message = ChatColor.GRAY + "The coordinates of " + ChatColor.GOLD + args[0] + ChatColor.GRAY +" are: " + ChatColor.DARK_AQUA + "x: " 
							+ (int) loc.getX();
					if(!yHide) {
						message += ChatColor.DARK_AQUA + ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
					}else {
						message += ChatColor.DARK_AQUA + ", z: " + (int) loc.getZ();
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
						    	String message = ChatColor.GRAY + "The coordinates of " + ChatColor.GOLD + args[0] + ChatColor.GRAY + " are: " + ChatColor.DARK_AQUA + "x: " 
										+ (int) loc.getX();
						    	if(!yHide) {
									message += ChatColor.DARK_AQUA + ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
								}else {
									message += ChatColor.DARK_AQUA + ", z: " + (int) loc.getZ();
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
				if(args.length > 2) {
					player.sendMessage(ChatColor.DARK_RED + "Too many arguments!");
					return true;
				} else if(!(player.hasPermission("coordgrabber.grabcoords.own")) && args.length == 0) {
					player.sendMessage(ChatColor.DARK_RED + "You do not have permission to do that!");
					return true;
				} else if(!(player.hasPermission("coordgrabber.grabcoords.others")) && args.length == 1) {
					player.sendMessage(ChatColor.DARK_RED + "You do not have permission to do that!");
					return true;
				} else if(!(player.hasPermission("coordgrabber.grabcoords.others")) && args.length == 2) {
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
					String message = ChatColor.GRAY + "The coordinates of " + ChatColor.GOLD + args[0] + ChatColor.GRAY + " are: " + ChatColor.DARK_AQUA + "x: " 
							+ (int) loc.getX();
					if(!yHide) {
						message += ChatColor.DARK_AQUA + ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
					}else {
						message += ChatColor.DARK_AQUA + ", z: " + (int) loc.getZ();
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
						    	String message = ChatColor.GRAY + "The coordinates of " + ChatColor.GOLD + args[0] + ChatColor.GRAY + " are: " + ChatColor.DARK_AQUA + "x: " 
										+ (int) loc.getX();
						    	if(!yHide) {
									message += ChatColor.DARK_AQUA + ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
								}else {
									message += ChatColor.DARK_AQUA + ", z: " + (int) loc.getZ();
								}
						    	sender.sendMessage(message);
						    }
						}, 20L, delay); //0 Tick initial delay, 20 Tick (1 Second) between repeats
						sendIds.add(i);
					}catch (NumberFormatException nfe) {
						// Parameter not an integer
						sender.sendMessage(ChatColor.DARK_RED + "The second parameter must be an integer!");
					}
					
					return true;
				}
			}
		}
		
		/**
		 *  /sendcoords
		 */
		if(label.equalsIgnoreCase("sendcoords")) {
			if(sender instanceof Player) {
				// player
				Player player = (Player) sender;
				if(player.hasPermission("coordgrabber.sendcoords.own") && args.length == 1) {
					// /getcoords
					Location loc = player.getLocation();
					String message = ChatColor.GRAY + "The coordinates of " + ChatColor.GOLD + player.getName() + ChatColor.GRAY + " are: " + ChatColor.DARK_AQUA + "x: " 
							+ (int) loc.getX();
					if(!yHide) {
						message += ChatColor.DARK_AQUA + ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
					}else {
						message += ChatColor.DARK_AQUA + ", z: " + (int) loc.getZ();
					}
					Player pReciever = Bukkit.getPlayer(args[0]);
					pReciever.sendMessage(message);
					player.sendMessage(ChatColor.GRAY + "You sent " + ChatColor.GOLD + "your " + ChatColor.GRAY + "coords to " + ChatColor.GOLD + pReciever.getName() + ChatColor.GRAY + ".");
					return true;
				}
				if(player.hasPermission("coordgrabber.sendcoords.others") && args.length == 2) {
					// /getcoords player
					Player pOther = Bukkit.getPlayer(args[0]);
					Location loc = pOther.getLocation();
					String message = ChatColor.GRAY + "The coordinates of " + ChatColor.GOLD + args[0] + ChatColor.GRAY + " are: " +ChatColor.DARK_AQUA + "x: " 
							+ (int) loc.getX();
					if(!yHide) {
						message += ChatColor.DARK_AQUA + ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
					}else {
						message += ChatColor.DARK_AQUA + ", z: " + (int) loc.getZ();
					}
					Player pReciever = Bukkit.getPlayer(args[1]);
					pReciever.sendMessage(message);
					player.sendMessage(ChatColor.GRAY + "You sent " + ChatColor.GOLD + pOther.getName() + ChatColor.GRAY + " coords to " + ChatColor.GOLD + pReciever.getName() + ChatColor.GRAY + ".");
					return true;
				}
				if(player.hasPermission("coordgrabber.sendcoords.timer") && args.length == 3) {
					try {
						int delay = Integer.parseInt(args[2]);
						delay *= 20; // Make time be in seconds
						broadcastrunning++;
						Player pOther = Bukkit.getPlayer(args[0]);	
						// Timer loop
						int i = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
						    @Override
						    public void run() {
						    	Location loc = pOther.getLocation();
						    	String message = ChatColor.GRAY + "The coordinates of " + ChatColor.GOLD + args[0] + ChatColor.GRAY + " are: " + ChatColor.DARK_AQUA + "x: " 
										+ (int) loc.getX();
						    	if(!yHide) {
									message += ChatColor.DARK_AQUA + ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
								}else {
									message += ChatColor.DARK_AQUA + ", z: " + (int) loc.getZ();
								}
						    	Player pReciever = Bukkit.getPlayer(args[1]);
						    	pReciever.sendMessage(message);
						    }
						}, 20L, delay); //0 Tick initial delay, 20 Tick (1 Second) between repeats
						sendIds.add(i);
						player.sendMessage(ChatColor.GRAY + "You sent " + ChatColor.GOLD + args[0] + ChatColor.GRAY + " coords to " + ChatColor.GOLD + args[1] + ChatColor.GRAY + " on a timer.");
					}catch (NumberFormatException nfe) {
						// Parameter not an integer
						player.sendMessage(ChatColor.DARK_RED + "The third parameter must be an integer!");
					}
					
					return true;
				}
				// ERRORS
				if(args.length > 3) {
					player.sendMessage(ChatColor.DARK_RED + "Too many arguments!");
					return true;
				}else if(args.length == 0) {
					player.sendMessage(ChatColor.DARK_RED + "Too few arguments!");
					return true;
				}else if(!(player.hasPermission("coordgrabber.sendcoords.own")) && args.length == 1) {
					player.sendMessage(ChatColor.DARK_RED + "You do not have permission to do that!");
					return true;
				} else if(!(player.hasPermission("coordgrabber.sendcoords.others")) && args.length == 2) {
					player.sendMessage(ChatColor.DARK_RED + "You do not have permission to do that!");
					return true;
				} else if(!(player.hasPermission("coordgrabber.sendcoords.timer")) && args.length == 3) {
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
					String message = ChatColor.GRAY + "The coordinates of " + ChatColor.GOLD + args[0] + ChatColor.GRAY + " are: " + ChatColor.DARK_AQUA + "x: " 
							+ (int) loc.getX();
					if(!yHide) {
						message += ChatColor.DARK_AQUA + ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
					}else {
						message += ChatColor.DARK_AQUA + ", z: " + (int) loc.getZ();
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
						    	String message = ChatColor.GRAY + "The coordinates of " + ChatColor.GOLD + args[0] + ChatColor.GRAY + " are: " + ChatColor.DARK_AQUA + "x: " 
										+ (int) loc.getX();
						    	if(!yHide) {
									message += ChatColor.DARK_AQUA + ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
								}else {
									message += ChatColor.DARK_AQUA + ", z: " + (int) loc.getZ();
								}
						    	sender.sendMessage(message);
						    }
						}, 20L, delay); //0 Tick initial delay, 20 Tick (1 Second) between repeats
						sendIds.add(i);
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
				}else if(!(broadcastIds.size() > 0)) {
					player.sendMessage(ChatColor.DARK_RED + "There is not a broadcast timer running!");
					return true;
				}
				else {
					player.sendMessage(ChatColor.DARK_RED + "You do not have permission to do that!");
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
				}else if(!(grabIds.size() > 0)) {
					player.sendMessage(ChatColor.DARK_RED + "There is not a grab timer running!");
					return true;
				}
				else {
					player.sendMessage(ChatColor.DARK_RED + "You do not have permission to do that!");
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
		 *  /stopsendingcoords
		 *  
		 */
		if(label.equalsIgnoreCase("stopsendingcoords")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(player.hasPermission("coordgrabber.sendcoords.timer") && sendIds.size() > 0) {
					// PLAYER SENT
					for(int i = 0; i < sendIds.size(); i++) {
						Bukkit.getScheduler().cancelTask(sendIds.get(i));
					}
					sendIds.clear();
					broadcastrunning = 0;
					return true;
				}else if(!(sendIds.size() > 0)) {
					player.sendMessage(ChatColor.DARK_RED + "There is not a send timer running!");
					return true;
				}
				else {
					player.sendMessage(ChatColor.DARK_RED + "You do not have permission to do that!");
					return true;
				}
			} else {
				// CONSOLE SENT
				for(int i = 0; i < sendIds.size(); i++) {
					Bukkit.getScheduler().cancelTask(sendIds.get(i));
				}
				sendIds.clear();
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
						player.sendMessage(ChatColor.GRAY + "[CoordGrabber] The y coord is now visible.");
					} else {
						yHide = true;
						player.sendMessage(ChatColor.GRAY + "[CoordGrabber] The y coord is now hidden.");
					}
					return true;
				}
				else {
					player.sendMessage(ChatColor.DARK_RED + "You do not have permission to do that!");
					return true;
				}
			} else {
				// CONSOLE SENT
				if(yHide) {
					yHide = false;
					sender.sendMessage(ChatColor.GRAY + "[CoordGrabber] The y coord is now visible.");
				} else {
					yHide = true;
					sender.sendMessage(ChatColor.GRAY + "[CoordGrabber] The y coord is now hidden.");
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
					String message = ChatColor.GRAY + "The coordinates of " + ChatColor.GOLD + player.getName() + ChatColor.GRAY + " are: " + ChatColor.DARK_AQUA + "x: " 
							+ (int) loc.getX();
					if(!yHide) {
						message += ChatColor.DARK_AQUA + ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
					}else {
						message += ChatColor.DARK_AQUA + ", z: " + (int) loc.getZ();
					}
					Bukkit.broadcastMessage(message);
					return true;
				}
				if(player.hasPermission("coordgrabber.broadcoords.others") && args.length == 1) {
					// /broadcastcoords <player>
					Player pOther = Bukkit.getPlayer(args[0]);
					Location loc = pOther.getLocation();
					String message = ChatColor.GRAY + "The coordinates of " + ChatColor.GOLD + args[0] + ChatColor.GRAY + " are: " + ChatColor.DARK_AQUA + "x: " 
							+ (int) loc.getX();
					if(!yHide) {
						message += ChatColor.DARK_AQUA + ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
					}else {
						message += ChatColor.DARK_AQUA + ", z: " + (int) loc.getZ();
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
						    	String message = ChatColor.GRAY + "The coordinates of " + ChatColor.GOLD + args[0] + ChatColor.GRAY + " are: " + ChatColor.DARK_AQUA+ "x: " 
										+ (int) loc.getX();
						    	if(!yHide) {
									message += ChatColor.DARK_AQUA + ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
								}else {
									message += ChatColor.DARK_AQUA + ", z: " + (int) loc.getZ();
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
				// ERRORS
				if(args.length > 2) {
					player.sendMessage(ChatColor.DARK_RED + "Too many arguments!");
					return true;
				} else if(!(player.hasPermission("coordgrabber.broadcastcoords.own")) && args.length == 0) {
					player.sendMessage(ChatColor.DARK_RED + "You do not have permission to do that!");
					return true;
				} else if(!(player.hasPermission("coordgrabber.broadcastcoords.others")) && args.length == 1) {
					player.sendMessage(ChatColor.DARK_RED + "You do not have permission to do that!");
					return true;
				} else if(!(player.hasPermission("coordgrabber.broadcastcoords.timer")) && args.length == 2) {
					player.sendMessage(ChatColor.DARK_RED + "You do not have permission to do that!");
					return true;
				}
				
				player.sendMessage(ChatColor.DARK_RED + "You do not have permission to do that!");
				return true;
			} else {
				/**
				 * CONSOLE COMMANDS
				 */
				if(args.length == 1) {
					// broadcastcoords <player>
					Player pOther = Bukkit.getPlayer(args[0]);
					Location loc = pOther.getLocation();
					String message = ChatColor.GRAY + "The coordinates of " + ChatColor.GOLD + args[0] + ChatColor.GRAY + " are: " + ChatColor.DARK_AQUA + "x: " 
							+ (int) loc.getX();
					if(!yHide) {
						message += ChatColor.DARK_AQUA + ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
					}else {
						message += ChatColor.DARK_AQUA + ", z: " + (int) loc.getZ();
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
						    	String message = ChatColor.GRAY + "The coordinates of " + ChatColor.GOLD + args[0] + ChatColor.GRAY + " are: " + ChatColor.DARK_AQUA + "x: " 
										+ (int) loc.getX();
						    	if(!yHide) {
									message += ChatColor.DARK_AQUA + ", y: " + (int) loc.getY() + ", z: " + (int) loc.getZ();
								}else {
									message += ChatColor.DARK_AQUA + ", z: " + (int) loc.getZ();
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
