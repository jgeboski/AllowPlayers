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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;

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
            url = new URL("https://sessionserver.mojang.com/session/minecraft/join");
        } catch (MalformedURLException e) { }
    }

    public void run()
    {
        HttpURLConnection conn;
        int code;

        while (!quit)
        {
            if (ap.enabled) {
                try {
                    conn = (HttpURLConnection) url.openConnection();

                    conn.setAllowUserInteraction(false);
                    conn.setConnectTimeout(ap.config.connTimeout);
                    conn.setReadTimeout(ap.config.connTimeout);
                    conn.setRequestMethod("POST");
                    conn.connect();

                    code = conn.getResponseCode();

                    if ((code != 200) && (code != 400))
                        throw new IOException();

                    if (!ap.online) {
                        ap.setOnline(true);
                        ap.setOnlineMode(true);
                    }
                } catch (IOException e) {
                    if (ap.online) {
                        ap.setOnline(false);
                        ap.setOnlineMode(false);
                    }
                }
            }

            try {
                synchronized (timeout) {
                    timeout.wait(ap.config.timeout);
                }
            } catch (InterruptedException e) {}
        }

        quit = false;
    }

    public void reset()
    {
        synchronized (timeout) {
            timeout.notify();
        }
    }

    public void quit()
    {
        quit = true;
        reset();
    }
}
