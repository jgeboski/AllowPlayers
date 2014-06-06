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

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.earth2me.essentials.UserData;

import org.jgeboski.allowplayers.storage.StorageException;
import org.jgeboski.allowplayers.storage.StoragePlugin;
import org.jgeboski.allowplayers.util.Reflect;
import org.jgeboski.allowplayers.util.ReflectException;

public class PEssentials extends StoragePlugin<Essentials>
{
    public PEssentials(Plugin plugin)
    {
        super(plugin);
    }

    public boolean syncIp()
    {
        return false;
    }

    public boolean syncId()
    {
        return false;
    }

    public String getIp(String player)
        throws StorageException
    {
        User u;

        u = plugin.getUser(player);

        return u.getLastLoginAddress();
    }

    public UUID getId(String player)
        throws StorageException
    {
        User   u;
        Object o;

        try {
            u = plugin.getUser(player);
            o = Reflect.getField((UserData) u, "config");
            o = Reflect.invoke(o, "getString", "uuid", "");

            return UUID.fromString((String) o);
        } catch (ReflectException e) {
            throw new StorageException(e);
        }
    }

    public void setIp(String player, String ip)
        throws StorageException
    {
        User u;

        u = plugin.getUser(player);

        try {
            Reflect.invoke(u, "_setLastLoginAddress", ip);
            u.save();
        } catch (ReflectException e) {
            throw new StorageException(e);
        }
    }

    public void setId(String player, UUID id)
        throws StorageException
    {
        User   u;
        Object o;

        try {
            u = plugin.getUser(player);
            o = Reflect.getField((UserData) u, "config");

            Reflect.invoke(o, "set", "uuid", id.toString());
        } catch (ReflectException e) {
            throw new StorageException(e);
        }
    }

    private User getUser(String player)
        throws StorageException
    {
        User u;

        u = plugin.getUserMap().getUser(player);

        if (u == null)
            throw new StorageException("Failed to obtain User");

        return u;
    }
}
