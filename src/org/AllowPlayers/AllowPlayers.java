/*
 * Copyright 2011 James Geboski <jgeboski@gmail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.AllowPlayers;

import java.util.HashMap;

import net.minecraft.server.MinecraftServer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

public class AllowPlayers extends JavaPlugin
{
    public HashMap<String, Request> requests;
    public Watcher watcher;
    public boolean online;
    
    public AllowPlayers()
    {
        requests = new HashMap<String, Request>();
        online   = true;
        watcher  = new Watcher(this);
    }
    
    public void onEnable()
    {
        PluginManager pm = getServer().getPluginManager();
        
        getCommand("allowplayers").setExecutor(new CAllowPlayers(this));
        getCommand("mcnet").setExecutor(new CMCNet(this));
        getCommand("onlinemode").setExecutor(new COnlineMode(this));
        
        APPlayerListener pl = new APPlayerListener(this);
        pm.registerEvent(Type.PLAYER_JOIN,     pl, Priority.Normal, this);
        pm.registerEvent(Type.PLAYER_PRELOGIN, pl, Priority.Normal, this);
        
        watcher.start();
        
        Log.info("%s enabled", getDescription().getVersion());
    }
    
    public void onDisable()
    {
        try {
            watcher.quit = true;
            watcher.join();
        } catch(InterruptedException e) {}
        
        Log.info("%s disabled", getDescription().getVersion());
    }
    
    /**
     * Test if a CommandSender has a specific permission
     * 
     * @param sender  A CommandSender
     * @param perm    A string containing the permission node
     * 
     * @return TRUE if the player has permission, otherwise FALSE
     **/
    public boolean hasPermission(CommandSender sender, String perm)
    {
        if(!(sender instanceof Player))
            return true;
        
        if(((Player) sender).hasPermission(perm))
            return true;
        
        Message.severe(sender, "You don't have permission to do that!");
        return false;
    }
    
    /**
     * Sends a message to all online players with a specific permission
     * 
     * @param perm    A string containing the permission node
     * @param format  A format string
     * @param args    Arguments corresponding to @param format
     **/
    public void messagePermission(String perm, String format, Object ... args)
    {
        Player[] players = getServer().getOnlinePlayers();
        
        for(Player player : players) {
            if(!player.hasPermission(perm))
                continue;
            
            Message.info((CommandSender) player, format, args);
        }
        
        Log.info(format, args);
    }
    
    /**
     * Creates a new player request
     * 
     * @param player   A string containing the player's name
     * @param address  A string containing the player's address
     **/
    public void newRequest(String address, String player)
    {
        if((address.length() < 1) || (player.length() < 1))
            return;
        
        if(getRequest(player) != null)
            return;
        
        requests.put(player, new Request(address, player));
    }
    
    /**
     * Removes a player request by a player's name
     * 
     * @param player  A string containing the player's name
     **/
    public void removeRequest(String player)
    {
        if(player.length() < 1)
            return;
        
        requests.remove(player);
    }
    
    /**
     * Gets a player request by a player's name
     * 
     * @param player  A string containing the player's name
     * 
     * @return  On success, the player's request, otherwise FALSE
     **/
    public Request getRequest(String player)
    {
        return requests.get(player);
    }
    
    /**
     * Set's AllowPlayers' minecraft.net status to online or offline
     * 
     * @param online  TRUE to set minecraft.net as being online, FALSE
     *                to set minecraft.net as being offline
     **/
    public void setOnline(boolean online)
    {
        String msg = null;
        
        if(!this.online && online) {
            msg = ChatColor.GREEN + "Minecraft.net has come back " +
                "online.";
        } else if(this.online && !online) {
            msg = ChatColor.RED + "Minecraft.net has gone " +
                "offline. Do not logout!";
        }
        
        if(msg != null)
            messagePermission("allowplayers.msg.notify", msg);
        
        this.online = online;
    }
    
    /**
     * Set the server's online mode state. Online mode determines if
     * a user is validly logged in with a player name.
     * 
     * @param mode  TRUE to enable online, FALSE to disable
     **/
    public void setOnlineMode(boolean mode)
    {
        ((MinecraftServer) ((CraftServer) getServer()).getServer()).onlineMode = mode;
    }
}
