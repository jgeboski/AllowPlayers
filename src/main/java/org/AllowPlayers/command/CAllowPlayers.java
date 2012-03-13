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

package org.AllowPlayers.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.AllowPlayers.AllowPlayers;
import org.AllowPlayers.Message;
import org.AllowPlayers.Request;

public class CAllowPlayers implements CommandExecutor
{
    public AllowPlayers ap;
    
    public CAllowPlayers(AllowPlayers ap)
    {
        this.ap = ap;
    }
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        String cmd;
        
        if(!ap.hasPermission(sender, "allowplayers.ap"))
            return true;
        
        if(args.length < 1) {
            Message.info(sender, command.getUsage());
            return true;
        }
        
        if(args[0].equalsIgnoreCase("requests")) {
            requests(sender, ((args.length == 2) ? args[1] : "0"));
            return true;
        } else if(args[0].equalsIgnoreCase("reload")) {
            reload(sender);
            return true;
        }
        
        if(args.length != 2) {
            Message.info(sender, command.getUsage());
            return true;
        }
        
        cmd = args[0].toLowerCase();
        
        if(cmd.matches("accept|approve|allow"))
            accept(sender, args[1], true);
        else if(cmd.matches("reject|deny|disallow"))
            accept(sender, args[1], false);
        else
            Message.info(sender, command.getUsage());
        
        return true;
    }
    
    private void requests(CommandSender sender, String page)
    {
        int size, p, i, t;
        Object[] objs;
        Request r;
        
        ChatColor color;
        char status;
        
        size = ap.requests.size();
        
        if(size < 1) {
            Message.info(sender, "There are no requests in the queue");
            return;
        }
        
        try {
            p = Integer.parseInt(page);
        } catch(NumberFormatException e) {
            Message.info(sender, "That's not a page number!");
            return;
        }
        
        t = (int) Math.ceil(((double) size) / ((double) ap.config.maxPerPage));
        
        if(p > t)
            p = t;
        
        i = (p >= 2) ? ((p - 1) * ap.config.maxPerPage) : 0;
        t = i + ap.config.maxPerPage;
        
        if(t > size)
            t = size;
        
        Message.info(sender,
            "Showing %d to %d of %d requests", i, t, size);
        
        objs = ap.requests.values().toArray();
        
        for(; (i < t) && (i < size); i++) {
            r = (Request) objs[i];
            
            switch(r.state) {
            case Request.ACCEPT:
                color  = ChatColor.GREEN;
                status = 'A';
                break;
            
            case Request.REJECT:
                color  = ChatColor.RED;
                status = 'R';
                break;
            
            default:
                color  = ChatColor.YELLOW;
                status = 'P';
            }
            
            Message.info(sender, "%s[%c] %s - %s",
                color, status, r.player, r.address);
        }
    }
    
    private void reload(CommandSender sender)
    {
        if(!ap.hasPermission(sender, "allowplayers.ap.reload"))
            return;
        
        ap.config.load();
        ap.requests.clear();
        ap.watcher.reset();
        
        Message.info(sender, "Configuration reloaded");
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
        
        String msg = String.format("%s%s %s %s's login request",
            (accept ? ChatColor.GREEN : ChatColor.RED), sender.getName(),
            (accept ? "accepted" : "rejected"), request.player);
        
        if(!ap.hasPermission(sender, "allowplayers.msg.request"))
            Message.info(sender, msg);
        
        ap.messagePermission("allowplayers.msg.request", msg);
    }
}