/*
 * Copyright 2012-2013 James Geboski <jgeboski@gmail.com>
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

package org.jgeboski.allowplayers.storage.plugin;

import org.bukkit.plugin.Plugin;

import be.Balor.bukkit.AdminCmd.AdminCmd;
import be.Balor.Player.ACPlayer;

import org.jgeboski.allowplayers.storage.StorageException;
import org.jgeboski.allowplayers.storage.StoragePlugin;

public class PAdminCmd extends StoragePlugin<AdminCmd>
{
    public PAdminCmd(Plugin plugin)
    {
        super(plugin);
    }

    public boolean checkIP(String player, String ip)
        throws StorageException
    {
        String   a;
        ACPlayer p;

        p = ACPlayer.getPlayer(player);
        a = p.getInformation("last-ip").getString();

        if (a == null)
            return false;

        a = a.replaceAll("/", "");

        return ip.equals(a);
    }

    public void setIP(String player, String ip)
        throws StorageException
    {
        ACPlayer p;

        p = ACPlayer.getPlayer(player);
        p.setInformation("last-ip", ip);
    }
}
