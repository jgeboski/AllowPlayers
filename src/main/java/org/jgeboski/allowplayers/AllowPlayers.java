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

package org.jgeboski.allowplayers;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.ensifera.animosity.craftirc.CraftIRC;
import com.ensifera.animosity.craftirc.EndPoint;
import com.ensifera.animosity.craftirc.RelayedMessage;

import org.jgeboski.allowplayers.command.CAllowPlayers;
import org.jgeboski.allowplayers.command.COnlineMode;
import org.jgeboski.allowplayers.listener.PlayerListener;
import org.jgeboski.allowplayers.util.Log;
import org.jgeboski.allowplayers.util.Message;
import org.jgeboski.allowplayers.util.Reflect;
import org.jgeboski.allowplayers.util.ReflectException;
import org.jgeboski.allowplayers.util.Utils;
import org.jgeboski.allowplayers.storage.StorageException;
import org.jgeboski.allowplayers.storage.StorageManager;

public class AllowPlayers extends JavaPlugin
{
    public Configuration  config;
    public Watcher        watcher;
    public StorageManager storage;

    public boolean enabled;
    public boolean online;

    public CraftIRC craftirc;
    public APPoint  apPoint;

    public void onLoad()
    {
        config   = new Configuration(new File(getDataFolder(), "config.yml"));
        watcher  = null;
        storage  = null;

        enabled  = true;
        online   = true;

        craftirc = null;
        apPoint  = null;
    }

    public void onEnable()
    {
        PluginManager pm;
        Plugin p;

        Log.init(getLogger());
        Message.init(getDescription().getName());

        pm = getServer().getPluginManager();
        config.load();

        try {
            if (config.storageType.equalsIgnoreCase("auto"))
                storage = StorageManager.create(pm);
            else
                storage = StorageManager.create(pm, config.storageType);
        } catch (StorageException e) {
            Log.severe(e.getMessage());
            setEnabled(false);
            return;
        }

        Log.info("Storage plugin: %s", storage.getType().getName());

        if (config.ircEnabled) {
            p  = pm.getPlugin("CraftIRC");

            if ((p != null) && p.isEnabled()) {
                craftirc = (CraftIRC) p;
                apPoint  = new APPoint();
            }

            if (!registerEndPoint(config.ircTag, apPoint))
                config.ircEnabled = false;
        }

        pm.registerEvents(new PlayerListener(this), this);

        getCommand("allowplayers").setExecutor(new CAllowPlayers(this));
        getCommand("onlinemode").setExecutor(new COnlineMode(this));

        watcher = new Watcher(this);
        watcher.start();
    }

    public void onDisable()
    {
        if (config.ircEnabled)
            craftirc.unregisterEndPoint(config.ircTag);

        try {
            watcher.quit();
            watcher.join();
        } catch (Exception e) {}
    }

    public void reload()
    {
        PluginManager pm;

        pm = getServer().getPluginManager();

        pm.disablePlugin(this);
        pm.enablePlugin(this);
    }

    private boolean registerEndPoint(String tag, Object ep)
    {
        if (craftirc == null)
            return false;

        if (craftirc.registerEndPoint(tag, (EndPoint) ep))
            return true;

        Log.severe("Unable to register CraftIRC tag: %s", tag);
        return false;
    }

    public void broadcast(String perm, String format, Object ... args)
    {
        String msg;
        String name;

        msg = String.format(format, args);
        Utils.broadcast(perm, msg);

        if (!config.ircEnabled)
            return;

        RelayedMessage rmsg;

        /* This typecasting is needed to prevent a ClassNotFoundException
         * from being thrown over com.ensifera.animosity.craftirc.EndPoint.
         */
        rmsg = craftirc.newMsg((EndPoint) ((Object) apPoint), null, "chat");
        name = getDescription().getName();

        if (!config.ircColored)
            msg = ChatColor.stripColor(msg);

        rmsg.setField("realSender", name);
        rmsg.setField("sender",     name);
        rmsg.setField("message",    msg);

        if (rmsg.post())
            return;

        registerEndPoint(config.ircTag, apPoint);
        rmsg.post();
    }

    public void setOnline(boolean online)
    {
        String msg = null;

        if (!this.online && online)
            msg = ChatColor.GREEN + "Minecraft.net has come back online.";
        else if (this.online && !online)
            msg = ChatColor.RED   + "Minecraft.net has gone offline.";

        if (msg != null)
            broadcast("allowplayers.notify", msg);

        this.online = online;
    }

    public void setOnlineMode(boolean mode)
    {
        Object s;
        Object o;

        s = getServer();

        try {
            o = Reflect.getField(s, "online");
            Reflect.setField(o, "value", mode);

            o = Reflect.invoke(s, "getServer");
            Reflect.invoke(o, "setOnlineMode", mode);
        } catch (ReflectException e) {
            symerror(e);
        }
    }

    public void setPlayerId(Player player, UUID id)
    {
        Object o;

        try {
            o = Reflect.invoke(player, "getHandle");
            Reflect.setField(o, "uniqueID", id);

            o = Reflect.invoke(o, "getProfile");
            Reflect.setField(o, "id", id);

            player.loadData();
        } catch (ReflectException e) {
            symerror(e);
        }
    }

    private void symerror(ReflectException expn)
    {
        Log.severe(expn.getMessage());
        Log.severe("CraftBukkit's internal symbols have changed!");
        Log.severe("Please report this as a bug.");
    }
}
