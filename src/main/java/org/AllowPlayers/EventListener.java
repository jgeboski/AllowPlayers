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

package org.AllowPlayers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.plugin.PluginManager;

public class EventListener implements Listener
{
    public AllowPlayers ap;

    public EventListener(AllowPlayers ap)
    {
        this.ap = ap;
    }

    public void register()
    {
        PluginManager pm;

        pm = ap.getServer().getPluginManager();
        pm.registerEvents(this, ap);
    }


    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerLogin(PlayerLoginEvent e)
    {
        Player p;
        String ip;

        if(!ap.enabled || ap.online)
            return;

        p  = e.getPlayer();
        ip = e.getKickMessage();

        if(ap.checkPlayerIP(p, ip)) {
            Log.info("%s [%s] was granted access to join", p, ip);
            return;
        }

        e.disallow(Result.KICK_OTHER,
                   "Minecraft.net is offline, and you're not " +
                   "recognized by our system; try back later");

        Log.info("%s [%s] was denied access to join", p, ip);
    }
}
