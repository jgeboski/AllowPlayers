/*
 * Copyright 2012 James Geboski <jgeboski@gmail.com>
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

package org.AllowPlayers.storage;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class StorageManager
{
    private StorageType type;
    private Storage     storage;

    protected StorageManager(StorageType type, Plugin plugin)
        throws StorageException
    {
        this.type    = type;
        this.storage = type.getStorage(plugin);
    }

    public static StorageManager create(PluginManager pmanager, String plugin)
        throws StorageException
    {
        StorageType t;
        Plugin      p;

        t = StorageType.fromString(plugin);
        p = pmanager.getPlugin(t.getName());

        if ((p == null) || !p.isEnabled())
            throw new StorageException("Failed to grab plugin: %s", plugin);

        return new StorageManager(t, p);
    }

    public static StorageManager create(PluginManager pmanager)
        throws StorageException
    {
        Plugin p;

        for (StorageType t : StorageType.values()) {
            p = pmanager.getPlugin(t.getName());

            if ((p == null) || !p.isEnabled())
                continue;

            return new StorageManager(t, p);
        }

        throw new StorageException("Failed to grab a storage plugin");
    }

    public StorageType getType()
    {
        return type;
    }

    public boolean checkIP(String player, String ip)
        throws StorageException
    {
        return storage.checkIP(player, ip);
    }

    public boolean checkIP(Player player, String ip)
        throws StorageException
    {
        return checkIP(player.getName(), ip);
    }

    public void setIP(String player, String ip)
        throws StorageException
    {
        storage.setIP(player, ip);
    }

    public void setIP(Player player, String ip)
        throws StorageException
    {
        setIP(player, ip);
    }
}
