/*
 * Copyright 2011-2013 James Geboski <jgeboski@gmail.com>
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

package org.jgeboski.allowplayers.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import org.jgeboski.allowplayers.AllowPlayers;
import org.jgeboski.allowplayers.util.Log;
import org.jgeboski.allowplayers.storage.StorageException;

public class PlayerListener implements Listener
{
    public AllowPlayers ap;

    public PlayerListener(AllowPlayers ap)
    {
        this.ap = ap;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerLogin(PlayerLoginEvent event)
    {
        Player p;
        String s;
        String ip;

        if (!ap.enabled || ap.online)
            return;

        p  = event.getPlayer();
        s  = p.getName();
        ip = event.getAddress().getHostAddress();

        try {
            if (ap.storage.checkIP(p, ip)) {
                Log.info("%s [%s] granted access to join", s, ip);
                return;
            }
        } catch (StorageException e) {
            Log.severe(e.getMessage());
        }

        event.disallow(Result.KICK_OTHER,
                   "Minecraft.net is offline, and you are not " +
                   "recognized. Try back later.");

        Log.info("%s [%s] denied access to join", s, ip);
    }
}
