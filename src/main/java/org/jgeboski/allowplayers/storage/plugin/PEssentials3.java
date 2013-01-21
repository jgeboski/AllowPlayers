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

import org.bukkit.plugin.Plugin;

import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.bukkit.BukkitPlugin;

import org.jgeboski.allowplayers.storage.StorageException;
import org.jgeboski.allowplayers.storage.StoragePlugin;

public class PEssentials3 extends StoragePlugin<IEssentials>
{
    public PEssentials3(Plugin plugin)
    {
        super(null);
        this.plugin = ((BukkitPlugin) plugin).getEssentials();
    }

    public boolean checkIP(String player, String ip)
        throws StorageException
    {
        String a;
        IUser  p;

        p = plugin.getUserMap().getUser(player);
        a = p.getData().getIpAddress();

        return ip.equals(a);
    }

    public void setIP(String player, String ip)
        throws StorageException
    {
        IUser p;

        p = plugin.getUserMap().getUser(player);
        p.getData().setIpAddress(ip);
        p.queueSave();
    }
}
