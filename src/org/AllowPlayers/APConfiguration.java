/* 
 * Copyright 2011 James Geboski <jgeboski@gmail.com>
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

import org.bukkit.configuration.file.YamlConfiguration;

public class APConfiguration extends YamlConfiguration
{
    private File file;
    
    public int timeout;
    public int connTimeout;
    
    public APConfiguration(File file)
    {
        this.file = file;
        
        timeout     = 60000;
        connTimeout = 10000;
    }
    
    public void load()
    {
        try {
            super.load(file);
        } catch(Exception e) {
            Log.warning("Unable to load: %s", file.toString());
        }
        
        timeout     = getInt("watcher.timeout",           60000);
        connTimeout = getInt("watcher.connectionTimeout", 10000);
        
        if(file.exists())
            return;
        
        set("watcher.timeout",           60000);
        set("watcher.connectionTimeout", 10000);
        
        save();
    }
    
    public void save()
    {
        try {
            super.save(file);
        } catch(Exception e) {
            Log.warning("Unable to save: %s", file.toString());
        }
    }
}