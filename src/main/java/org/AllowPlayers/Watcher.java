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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Watcher extends Thread
{
    private volatile boolean quit;
    private Object timeout;
    private URL url;
    
    public AllowPlayers ap;
    
    public Watcher(AllowPlayers ap)
    {
        this.ap = ap;
        quit    = false;
        timeout = new Object();
        
        try {
            url = new URL("http://session.minecraft.net/game/checkserver.jsp");
        } catch(MalformedURLException e) {}
    }
    
    public void run()
    {
        while(!quit)
        {
            URLConnection urlc;
            
            try {
                urlc = url.openConnection();
                urlc.setAllowUserInteraction(false);
                urlc.setConnectTimeout(ap.config.connTimeout);
                urlc.setReadTimeout(ap.config.connTimeout);
                urlc.getContent();
                
                ap.setOnline(true);
                ap.setOnlineMode(true);
            } catch(IOException e) {
                ap.setOnline(false);
                ap.setOnlineMode(false);
            }
            
            try {
                synchronized(timeout) {
                    timeout.wait(ap.config.timeout);
                }
            } catch(InterruptedException e) {}
        }
        
        /* Let's allow for rerunning when the plugin reloads */
        quit = false;
    }
    
    public void reset()
    {
        synchronized(timeout) {
            timeout.notify();
        }
    }
    
    public void quit()
    {
        quit = true;
        reset();
    }
}