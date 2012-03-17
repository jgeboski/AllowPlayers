/* 
 * Copyright 2011-2012 James Geboski <jgeboski@gmail.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.AllowPlayers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.plugin.PluginManager;

public class EventListener implements Listener
{
    public AllowPlayers ap;
    
    public EventListener(AllowPlayers ap)
    {
        this.ap = ap;
    }
    
    public void register()
    {
        PluginManager pm;
        
        pm = ap.getServer().getPluginManager();
        pm.registerEvents(this, ap);
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        if(!ap.online)
            ap.removeRequest(e.getPlayer().getName());
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerLogin(PlayerLoginEvent e)
    {
        Player player;
        String splayer;
        Request request;
        String addr;
        
        if(ap.online)
            return;
        
        player  = e.getPlayer();
        splayer = player.getName();
        request = ap.getRequest(splayer);
        
        if(request == null) {
            ap.newRequest(e.getKickMessage(), splayer);
            e.disallow(Result.KICK_OTHER,
                "Minecraft.net is down. An admin will approve your " +
                "login request. Try back shortly.");
            
            ap.messagePermission("allowplayers.msg.request",
                "%s%s is awaiting approval", ChatColor.YELLOW, splayer);
        } else if(request.state == Request.ACCEPT) {
            if(!request.address.equals(e.getKickMessage())) {
                ap.removeRequest(splayer);
                e.disallow(Result.KICK_OTHER,
                    "Your request IP did not match your login IP. " +
                    "Please try again.");
                
                ap.messagePermission("allowplayers.msg.request",
                    "%s%s attempted to join with a different IP",
                    ChatColor.RED, splayer);
            } else {
                e.allow();
            }
        } else if(request.state == Request.REJECT) {
            e.disallow(Result.KICK_OTHER,
                "Minecraft.net is still down. Your login request" + 
                "was REJECTED");
            
            ap.messagePermission("allowplayers.msg.request",
                "%s%s attempted to join", ChatColor.RED, splayer);
        } else {
            e.disallow(Result.KICK_OTHER,
                "Minecraft.net is still down. Your login request " +
                "is still pending approval.");
            
            ap.messagePermission("allowplayers.msg.request",
                "%s%s is still awaiting approval",
                ChatColor.YELLOW, splayer);
        }
    }
}
