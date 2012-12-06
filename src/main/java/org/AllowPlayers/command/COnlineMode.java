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

public class COnlineMode implements CommandExecutor
{
    public AllowPlayers ap;

    public COnlineMode(AllowPlayers ap)
    {
        this.ap = ap;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        String c;

        if (!ap.hasPermissionM(sender, "allowplayers.command.onlinemode"))
            return true;

        if (args.length < 1) {
            info(sender);
            return true;
        }

        c = args[0].toLowerCase();

        if (c.matches("e|enable|on|online")) {
            ap.setOnlineMode(true);
            info(sender);
        } else if (c.matches("d|disable|off|offline")) {
            ap.setOnlineMode(false);
            info(sender);
        } else if (c.matches("s|stat|status")) {
            info(sender);
        } else {
            Message.info(sender, command.getUsage());
        }

        return true;
    }

    private void info(CommandSender sender)
    {
        String msg;

        msg = "Online Mode: ";

        if (ap.getServer().getOnlineMode())
            msg += ChatColor.GREEN + "Enabled";
        else
            msg += ChatColor.RED   + "Disabled";

        Message.info(sender, msg);
    }
}
