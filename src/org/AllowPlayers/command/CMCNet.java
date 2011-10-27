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

public class CMCNet implements CommandExecutor
{
    public AllowPlayers ap;
    
    public CMCNet(AllowPlayers ap)
    {
        this.ap = ap;
    }
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!ap.hasPermission(sender, "allowplayers.mcn"))
            return true;
        
        if(args.length < 1) {
            Message.info(sender, command.getUsage());
            return true;
        }
        
        if(args[0].equalsIgnoreCase("force") && (args.length == 2))
            force(sender, args[1]);
        else if(args[0].equalsIgnoreCase("status"))
            onlineToSender(sender);
        else
            Message.info(sender, command.getUsage());
        
        return true;
    }
    
    private void onlineToSender(CommandSender sender)
    {
        String msg = "Minecraft.net Status: ";
        
        if(ap.online)
            msg += ChatColor.GREEN + "ONLINE";
        else
            msg += ChatColor.RED + "OFFLINE";
        
        Message.info(sender, msg);
    }
    
    private void force(CommandSender sender, String cmd)
    {
        if(!ap.hasPermission(sender, "allowplayers.mcn.force"))
            return;
        
        if(cmd.equalsIgnoreCase("online"))
            ap.setOnline(true);
        else if(cmd.equalsIgnoreCase("offline"))
            ap.setOnline(false);
        else if(cmd.equalsIgnoreCase("check"))
            ap.watcher.timeout = 0;
        
        onlineToSender(sender);
    }
}