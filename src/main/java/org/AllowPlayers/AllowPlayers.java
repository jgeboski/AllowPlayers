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

import java.io.File;
import java.util.HashMap;

import net.minecraft.server.MinecraftServer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import be.Balor.bukkit.AdminCmd.AdminCmd;
import be.Balor.Player.ACPlayer;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

import org.AllowPlayers.command.CAllowPlayers;
import org.AllowPlayers.command.COnlineMode;

public class AllowPlayers extends JavaPlugin
{
    public static final String pluginName = "AllowPlayers";

    public Configuration config;
    public Watcher watcher;
    public boolean online;

    private EventListener events;

    public AdminCmd admincmd;
    public Essentials essentials;

    public void onLoad()
    {
        config  = new Configuration(new File(getDataFolder(), "config.yml"));
        events  = new EventListener(this);
        watcher = new Watcher(this);
        online  = true;

        admincmd   = null;
        essentials = null;
    }

    public void onEnable()
    {
        PluginManager pm;
        Plugin p;
        boolean b;

        pm = getServer().getPluginManager();
        p  = pm.getPlugin("AdminCmd");

        if(p != null)
            admincmd = (AdminCmd) p;

        p = pm.getPlugin("Essentials");

        if(p != null)
            essentials = (Essentials) p;

        b = (admincmd == null)    && (essentials == null) &&
            !admincmd.isEnabled() && !essentials.isEnabled();

        if(b) {
            Log.severe("Unable to find AdminCmd or Essentials");
            setEnabled(false);
            return;
        }

        getCommand("allowplayers").setExecutor(new CAllowPlayers(this));
        getCommand("onlinemode").setExecutor(new COnlineMode(this));

        config.load();
        events.register();
        watcher.start();
    }

    public void onDisable()
    {
        try {
            watcher.quit();
            watcher.join();
        } catch(Exception e) {}
    }

    /**
     * Test if a CommandSender has a specific permission
     *
     * @param sender  A CommandSender
     * @param perm    A string containing the permission node
     *
     * @return TRUE if the player has permission, otherwise FALSE
     **/
    public boolean hasPermission(CommandSender sender, String perm)
    {
        if(!(sender instanceof Player))
            return true;

        if(((Player) sender).hasPermission(perm))
            return true;

        Message.severe(sender, "You don't have permission to do that!");
        return false;
    }

    /**
     * Sends a message to all online players with a specific permission
     *
     * @param perm    A string containing the permission node
     * @param format  A format string
     * @param args    Arguments corresponding to @param format
     **/
    public void broadcast(String perm, String format, Object ... args)
    {
        Player[] players = getServer().getOnlinePlayers();

        for(Player player : players) {
            if(player.hasPermission(perm))
                Message.info((CommandSender) player, format, args);
        }

        Log.info(format, args);
    }

    /**
     * Set's AllowPlayers' minecraft.net status to online or offline
     *
     * @param online  TRUE to set minecraft.net as being online, FALSE
     *                to set minecraft.net as being offline
     **/
    public void setOnline(boolean online)
    {
        String msg = null;

        if(!this.online && online) {
            msg = ChatColor.GREEN + "Minecraft.net has come back " +
                                    "online.";
        } else if(this.online && !online) {
            msg = ChatColor.RED   + "Minecraft.net has gone " +
                                    "offline. Do not logout!";
        }

        if(msg != null)
            broadcast("allowplayers.message.notify", msg);

        this.online = online;
    }

    /**
     * Set the server's online mode state. Online mode determines if
     * a user is validly logged in with a player name.
     *
     * @param mode  TRUE to enable online, FALSE to disable
     **/
    public void setOnlineMode(boolean mode)
    {
        ((MinecraftServer) ((CraftServer) getServer()).getServer()).setOnlineMode(mode);
    }

    /**
     * Compare a player's IP to the locally stored address
     *
     * @param player  A Player
     * @param ip      A String containing the player's IP
     *
     * @return  TRUE if the player's address matched, otherwise FALSE
     **/
    public boolean checkPlayerIP(Player player, String ip)
    {
        String la;

        if(ip.length() < 1)
            return false;

        if(admincmd != null) {
            ACPlayer p;

            p  = ACPlayer.getPlayer(player);
            la = p.getInformation("last-ip").getString();
            la = la.replaceAll("/", "");

            return la.equals(ip);
        }

        if(essentials != null) {
            User u;

            u = essentials.getUser(player);
            la = u.getLastLoginAddress();

            return la.equals(ip);
        }

        return false;
    }
}
