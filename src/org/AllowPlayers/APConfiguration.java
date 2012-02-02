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

import org.bukkit.configuration.file.YamlConfiguration;

public class APConfiguration extends YamlConfiguration
{
    private File file;
    
    public int maxPerPage;
    public int timeout;
    public int connTimeout;
    
    public APConfiguration(File file)
    {
        this.file = file;
    }
    
    public void load()
    {
        try {
            super.load(file);
        } catch(Exception e) {
            Log.warning("Unable to load: %s", file.toString());
        }
        
        maxPerPage  = getInt("allowplayers.maxPerPage",   10);
        timeout     = getInt("watcher.timeout",           60000);
        connTimeout = getInt("watcher.connectionTimeout", 10000);
        
        if(maxPerPage < 1)
            maxPerPage = 1;
        
        if(!file.exists())
            save();
    }
    
    public void save()
    {
        set("allowplayers.maxPerPage",   maxPerPage);
        set("watcher.timeout",           timeout);
        set("watcher.connectionTimeout", connTimeout);
        
        try {
            super.save(file);
        } catch(Exception e) {
            Log.warning("Unable to save: %s", file.toString());
        }
    }
}
