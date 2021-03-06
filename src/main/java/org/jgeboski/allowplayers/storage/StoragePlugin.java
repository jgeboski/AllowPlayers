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

package org.jgeboski.allowplayers.storage;

import java.util.UUID;

import org.bukkit.plugin.Plugin;

public abstract class StoragePlugin<T>
{
    public T plugin;

    public StoragePlugin(Plugin plugin)
    {
        this.plugin = (T) plugin;
    }

    public abstract boolean syncIp();
    public abstract boolean syncId();

    public abstract String getIp(String player)
        throws StorageException;

    public abstract UUID getId(String player)
        throws StorageException;

    public abstract void setIp(String player, String ip)
        throws StorageException;

    public abstract void setId(String player, UUID id)
        throws StorageException;
}
