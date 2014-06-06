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

import java.util.UUID;

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

    public boolean syncIp()
    {
        return false;
    }

    public boolean syncId()
    {
        return true;
    }

    public String getIp(String player)
        throws StorageException
    {
        ACPlayer p;
        String   s;

        p = getACPlayer(player);
        s = p.getInformation("last-ip").getString();

        if (s != null)
            s = s.replaceAll("/", "");

        return s;
    }

    public UUID getId(String player)
        throws StorageException
    {
        ACPlayer p;
        String   s;

        p = getACPlayer(player);
        s = p.getInformation("uuid").getString();

        if (s == null)
            return null;

        return UUID.fromString(s);
    }

    public void setIp(String player, String ip)
        throws StorageException
    {
        ACPlayer p;

        p = getACPlayer(player);
        p.setInformation("last-ip", ip);
    }

    public void setId(String player, UUID id)
        throws StorageException
    {
        ACPlayer p;

        p = getACPlayer(player);
        p.setInformation("uuid", id.toString());
    }

    private ACPlayer getACPlayer(String player)
        throws StorageException
    {
        ACPlayer p;

        p = ACPlayer.getPlayer(player);

        if (p == null)
            throw new StorageException("Failed to obtain ACPlayer");

        return p;
    }
}
