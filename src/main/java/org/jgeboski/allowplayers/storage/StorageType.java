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

import java.lang.reflect.Constructor;

import org.bukkit.plugin.Plugin;

import be.Balor.bukkit.AdminCmd.AdminCmd;
import com.earth2me.essentials.Essentials;
import net.ess3.api.IEssentials;

import org.jgeboski.allowplayers.storage.plugin.PAdminCmd;
import org.jgeboski.allowplayers.storage.plugin.PEssentials;
import org.jgeboski.allowplayers.storage.plugin.PEssentials3;

public enum StorageType
{
    ADMINCMD    ("AdminCmd",     PAdminCmd.class),
    ESSENTIALS  ("Essentials",   PEssentials.class),
    ESSENTIALS3 ("Essentials-3", PEssentials3.class);

    private String name;
    private Class  storage;

    private StorageType(String name, Class storage)
    {
        this.name    = name;
        this.storage = storage;
    }

    public static StorageType fromString(String str)
        throws StorageException
    {
        for (StorageType t : StorageType.values()) {
            if (str.equalsIgnoreCase(t.getName()))
                return t;
        }

        throw new StorageException("Unsupported storage plugin: %s", str);
    }

    public String getName()
    {
        return name;
    }

    public StoragePlugin getStorage(Plugin plugin)
        throws StorageException
    {
        Constructor c;

        try {
            c = storage.getConstructor(Plugin.class);
            return (StoragePlugin) c.newInstance(plugin);
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }
}
