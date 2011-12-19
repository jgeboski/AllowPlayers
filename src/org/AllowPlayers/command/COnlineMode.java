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

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class COnlineMode implements CommandExecutor
{
    public AllowPlayers ap;
    
    public COnlineMode(AllowPlayers ap)
    {
        this.ap = ap;
    }
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!ap.hasPermission(sender, "allowplayers.onlinemode"))
            return true;
        
        if(args.length < 1) {
            Message.info(sender, command.getUsage());
            return true;
        }
        
        if(args[0].equalsIgnoreCase("enable")) {
            ap.setOnlineMode(true);
            onlineModeToSender(sender);
        } else if(args[0].equalsIgnoreCase("disable")) {
            ap.setOnlineMode(false);
            onlineModeToSender(sender);
        } else if(args[0].equalsIgnoreCase("status")) {
            onlineModeToSender(sender);
        } else {
            Message.info(sender, command.getUsage());
        }
        
        return true;
    }
    
    private void onlineModeToSender(CommandSender sender)
    {
        String msg = "Online Mode: ";
        
        if(ap.getServer().getOnlineMode())
            msg += ChatColor.GREEN + "ENABLED";
        else
            msg += ChatColor.RED + "DISABLED";
        
        Message.info(sender, msg);
    }
}
