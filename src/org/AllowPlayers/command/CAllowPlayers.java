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

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CAllowPlayers implements CommandExecutor
{
    public AllowPlayers ap;
    
    public CAllowPlayers(AllowPlayers ap)
    {
        this.ap = ap;
    }
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!ap.hasPermission(sender, "allowplayers.ap"))
            return true;
        
        if(args.length < 1) {
            Message.info(sender, command.getUsage());
            return true;
        }
        
        if(args[0].equalsIgnoreCase("pending")) {
            pending(sender, ((args.length == 2) ? args[1] : "0"));
            return true;
        }
        
        if(args.length != 2) {
            Message.info(sender, command.getUsage());
            return true;
        }
        
        if(args[0].equalsIgnoreCase("accept"))
            accept(sender, args[1], true);
        else if(args[0].equalsIgnoreCase("reject"))
            accept(sender, args[1], false);
        else
            Message.info(sender, command.getUsage());
        
        return true;
    }
    
    private void pending(CommandSender sender, String page)
    {
        int max, size;
        int p, m, i, t;
        Request[] r;
        
        max  = 5;
        size = ap.requests.size();
        
        if(size < 1) {
            Message.info(sender, "There are no peding requests");
            return;
        }
        
        try {
            p = Integer.parseInt(page);
        } catch(NumberFormatException e) {
            Message.info(sender, "That's not a page number!");
            return;
        }
        
        i = 0;
        
        if(p <= 0)
            i = 0;
        if(p > 0)
            i = (p * max) + 1;
        
        if(i > size)
            return;
        
        t = i + 5;
        if(t > size)
            t = size - 1;
        
        Message.info(sender,
            "Showing results %d to %d of %d pending requests",
            i, t, size);
        
        r = (Request[]) ap.requests.values().toArray();
        
        for(; i < t; i++)
            Message.info(sender, "Player: %s, IP: %s",
                r[i].player, r[i].address);
    }
    
    private void accept(CommandSender sender, String player, boolean accept)
    {
        Request request;
        
        if(!ap.hasPermission(sender, "allowplayers.ap.mod"))
            return;
        
        request = ap.getRequest(player);
        
        if(request == null) {
            Message.severe(sender, "Invalid player");
            return;
        }
        
        if(accept)
            request.accept();
        else
            request.reject();
        
        String msg = String.format("%s %s %s's login request",
            sender.getName(),(accept ? "accepted" : "rejected"), request.player);
        
        if(!ap.hasPermission(sender, "allowplayers.msg.request"))
            Message.info(sender, msg);
        
        ap.messagePermission("allowplayers.msg.request", msg);
    }
}
