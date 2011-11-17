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

import java.net.InetAddress;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent.Result;

public class APPlayerListener extends PlayerListener
{
    public AllowPlayers ap;
    
    public APPlayerListener(AllowPlayers ap)
    {
        this.ap = ap;
    }
    
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        if(!ap.online)
            ap.removeRequest(e.getPlayer().getName());
    }
    
    public void onPlayerPreLogin(PlayerPreLoginEvent e)
    {
        if(ap.online)
            return;
        
        InetAddress inaddr = e.getAddress();
        String address     = inaddr.getHostAddress();
        String player      = e.getName();
        Request request    = ap.getRequest(player);
        
        if(request != null) {
            if(request.state == Request.ACCEPT) {
                e.allow();
                return;
            }
            
            if(request.state == Request.REJECT) {
                e.disallow(Result.KICK_OTHER,
                    "Minecraft.net is still down; your login request" + 
                    "was REJECTED");
                return;
            }
            
            e.disallow(Result.KICK_OTHER,
                "Minecraft.net is still down; your login request " +
                "is still pending approval");
            
            ap.messagePermission("allowplayers.msg.request",
                "%s is still awaiting approval", player);
            
            return;
        }
        
        ap.newRequest(address, player);
        e.disallow(Result.KICK_OTHER,
            "Minecraft.net is down; an admin will approve your " +
            "login request, please try back shortly");
        
        ap.messagePermission("allowplayers.msg.request",
            "%s is awaiting approval", player);
    }
}
