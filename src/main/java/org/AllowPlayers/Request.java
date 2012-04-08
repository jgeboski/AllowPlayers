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

public class Request
{
    public static final int PENDING = 1 << 0;
    public static final int ACCEPT  = 1 << 1;
    public static final int REJECT  = 1 << 2;
    
    public int state;
    public String address;
    public String player;
    
    public Request(String address, String player)
    {
        this.address = address;
        this.player  = player;
        this.state   = PENDING;
    }
    
    /**
     * Set a request's state to accepted
     **/
    public void accept()
    {
        state = ACCEPT;
    }
    
    /**
     * Set a request's state to rejected
     **/
    public void reject()
    {
        state = REJECT;
    }
}
