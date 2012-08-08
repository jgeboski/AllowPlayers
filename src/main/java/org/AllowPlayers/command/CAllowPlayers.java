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

public class CAllowPlayers implements CommandExecutor
{
    public AllowPlayers ap;

    public CAllowPlayers(AllowPlayers ap)
    {
        this.ap = ap;
    }

    public boolean onCommand(CommandSender sender, Command command,
                             String label, String[] args)
    {
        String cmd;

        if(!ap.hasPermission(sender, "allowplayers.ap"))
            return true;

        if(args.length < 1)
            info(sender);
        else if(args[0].equalsIgnoreCase("check"))
            check(sender);
        else if(args[0].equalsIgnoreCase("reload"))
            reload(sender);
        else
            Message.info(sender, command.getUsage());

        return true;
    }

    private void info(CommandSender sender)
    {
        String msg = "Minecraft.net Status: ";

        if(ap.online)
            msg += ChatColor.GREEN + "ONLINE";
        else
            msg += ChatColor.RED + "OFFLINE";

        Message.info(sender, ap.getDescription().getFullName());
        Message.info(sender, msg);
    }

    private void check(CommandSender sender)
    {
        if(!ap.hasPermission(sender, "allowplayers.ap.check"))
            return;

        ap.watcher.reset();
        Message.info(sender, "Checking Minecraft.net status...");
    }

    private void reload(CommandSender sender)
    {
        if(!ap.hasPermission(sender, "allowplayers.ap.reload"))
            return;

        ap.config.load();
        ap.watcher.reset();

        Message.info(sender, "Configuration reloaded");
    }
}
