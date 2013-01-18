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
import org.AllowPlayers.util.IPUtils;
import org.AllowPlayers.util.Message;
import org.AllowPlayers.util.Utils;
import org.AllowPlayers.storage.StorageException;

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
        String c;

        if (!Utils.hasPermission(sender, "allowplayers.ap"))
            return true;

        if (args.length < 1) {
            info(sender);
            return true;
        }

        c = args[0].toLowerCase();

        if (c.matches("c|check"))
            check(sender);
        else if (c.matches("e|enable|on|online"))
            enable(sender);
        else if (c.matches("d|disable|off|offline"))
            disable(sender);
        else if (c.matches("r|rel|reload"))
            reload(sender);
        else if (c.matches("s|set"))
            set(sender, args);
        else
            Message.info(sender, command.getUsage());

        return true;
    }

    private void check(CommandSender sender)
    {
        if (!Utils.hasPermission(sender, "allowplayers.ap.check"))
            return;

        ap.watcher.reset();
        Message.info(sender, "Checking Minecraft.net status...");
    }

    private void disable(CommandSender sender)
    {
        if (!Utils.hasPermission(sender, "allowplayers.ap.toggle"))
            return;

        ap.enabled = false;
        ap.watcher.reset();
        ap.setOnlineMode(true);

        Message.info(sender, "Toggled off.");
    }

    public void enable(CommandSender sender)
    {
        if (!Utils.hasPermission(sender, "allowplayers.ap.toggle"))
            return;

        ap.enabled = true;
        ap.watcher.reset();

        Message.info(sender, "Toggled on.");
    }

    private void info(CommandSender sender)
    {
        String msg;

        Message.info(sender, "%s%s", ChatColor.GRAY,
                     ap.getDescription().getFullName());

        Message.info(sender, "Storage Plugin:       %s",
                     ap.storage.getType().getName());

        msg = "AllowPlayers Status:  ";

        if (ap.enabled)
            msg += ChatColor.GREEN + "Enabled";
        else
            msg += ChatColor.RED   + "Disabled";

        Message.info(sender, msg);
        msg = "Minecraft.net Status: ";

        if (ap.online)
            msg += ChatColor.GREEN + "Online";
        else
            msg += ChatColor.RED   + "Offline";

        Message.info(sender, msg);
    }

    private void reload(CommandSender sender)
    {
        if (!Utils.hasPermission(sender, "allowplayers.ap.reload"))
            return;

        ap.reload();

        Message.info(sender, "Configuration reloaded.");
    }

    private void set(CommandSender sender, String[] args)
    {
        if (!Utils.hasPermission(sender, "allowplayers.ap.set"))
            return;

        if (args.length < 3) {
            Message.severe(sender, "Specify a username and IP address.");
            return;
        }

        if (!IPUtils.isAddress(args[2])) {
            Message.severe(sender, "Specify a valid IP address.");
            return;
        }

        try {
            ap.storage.setIP(args[1], args[2]);
            Message.info(sender, "Set IP to %s for %s", args[2], args[1]);
        } catch (StorageException e) {
            Message.severe(sender, e.getMessage());
        }
    }
}
