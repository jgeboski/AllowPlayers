/*
 * Copyright 2011 James Geboski <jgeboski@gmail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.AllowPlayers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Watcher extends Thread
{
    /* Time between checks in seconds */
    public int ctimeout = 10;
    
    public AllowPlayers ap;
    public volatile boolean quit;
    public volatile long timeout;
    private URL url;
    
    public Watcher(AllowPlayers ap)
    {
        this.ap = ap;
        quit    = false;
        
        try {
            url = new URL("http://session.minecraft.net/game/checkserver.jsp");
        } catch(MalformedURLException e) {}
    }
    
    public void run()
    {
        while(!quit)
        {
            URLConnection urlc;
            
            for(timeout = ctimeout * 2; !quit && (timeout > 0); timeout--) {
                try {
                    Thread.sleep(500);
                } catch(InterruptedException e) {}
            }
            
            try {
                urlc = url.openConnection();
                urlc.setAllowUserInteraction(false);
                urlc.setConnectTimeout(2000);
                urlc.setReadTimeout(2000);
                urlc.getContent();
                ap.setOnline(true);
            } catch(IOException e) {
                ap.setOnline(false);
            }
        }
        
        /* Let's allow for rerunning when the plugin reloads */
        quit = false;
    }
}
