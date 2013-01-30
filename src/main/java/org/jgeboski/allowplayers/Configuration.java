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

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import org.jgeboski.allowplayers.util.Log;

public class Configuration extends YamlConfiguration
{
    private File file;

    public String storageType;

    public int timeout;
    public int connTimeout;

    public boolean ircEnabled;
    public boolean ircColored;
    public String  ircTag;

    public Configuration(File file)
    {
        this.file = file;

        storageType = "auto";

        timeout     = 60000;
        connTimeout = 10000;

        ircEnabled  = false;
        ircColored  = true;
        ircTag      = "allowplayers";
    }

    public void load()
    {
        ConfigurationSection cs;

        try {
            super.load(file);
        } catch (Exception e) {
            Log.warning("Unable to load: %s", file.toString());
        }

        cs          = getConfigurationSection("storage");
        storageType = cs.getString("type", storageType);

        cs          = getConfigurationSection("watcher");
        timeout     = cs.getInt("timeout",           timeout);
        connTimeout = cs.getInt("connectionTimeout", connTimeout);

        cs          = getConfigurationSection("irc");
        ircEnabled  = cs.getBoolean("enabled", ircEnabled);
        ircColored  = cs.getBoolean("colored", ircEnabled);
        ircTag      = cs.getString("tag",      ircTag);

        if (!file.exists())
            save();
    }

    public void save()
    {
        ConfigurationSection cs;

        cs = getConfigurationSection("storage");
        cs.set("type", storageType);

        cs = getConfigurationSection("watcher");
        cs.set("timeout",           timeout);
        cs.set("connectionTimeout", connTimeout);

        cs = getConfigurationSection("irc");
        cs.set("enabled", ircEnabled);
        cs.set("colored", ircEnabled);
        cs.set("tag",     ircTag);

        try {
            super.save(file);
        } catch (Exception e) {
            Log.warning("Unable to save: %s", file.toString());
        }
    }

    public ConfigurationSection getConfigurationSection(String path)
    {
        ConfigurationSection ret;

        ret = super.getConfigurationSection(path);

        if (ret == null)
            ret = createSection(path);

        return ret;
    }
}
