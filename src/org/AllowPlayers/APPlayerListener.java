/* 
 * Copyright 2011 James Geboski <jgeboski@gmail.com>
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

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

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
                "Minecraft.net is down; an admin will approve your " +
                "login request, please try back shortly");
            
            ap.messagePermission("allowplayers.msg.request",
                "%s is awaiting approval", splayer);
        } else if(request.state == Request.ACCEPT) {
            e.allow();
        } else if(request.state == Request.REJECT) {
            e.disallow(Result.KICK_OTHER,
                "Minecraft.net is still down; your login request" + 
                "was REJECTED");
        } else {
            e.disallow(Result.KICK_OTHER,
                "Minecraft.net is still down; your login request " +
                "is still pending approval");
            
            ap.messagePermission("allowplayers.msg.request",
                "%s is still awaiting approval", splayer);
        }
    }
}
