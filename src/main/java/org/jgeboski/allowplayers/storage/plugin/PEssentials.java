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

package org.jgeboski.allowplayers.storage.plugin;

import java.lang.reflect.Method;

import org.bukkit.plugin.Plugin;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

import org.jgeboski.allowplayers.storage.StorageException;
import org.jgeboski.allowplayers.storage.StoragePlugin;

public class PEssentials extends StoragePlugin<Essentials>
{
    public PEssentials(Plugin plugin)
    {
        super(plugin);
    }

    public boolean checkIP(String player, String ip)
        throws StorageException
    {
        String a;
        User   p;

        p = plugin.getUser(player);
        a = p.getLastLoginAddress();

        return ip.equals(a);
    }

    public void setIP(String player, String ip)
        throws StorageException
    {
        Class  c;
        Method m;
        User   p;

        p = plugin.getUser(player);
        c = p.getClass();

        try {
            /* User -> UserData */
            c = c.getSuperclass();
            m = c.getDeclaredMethod("_setLastLoginAddress", String.class);

            m.setAccessible(true);
            m.invoke(p, ip);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        p.save();
    }
}
