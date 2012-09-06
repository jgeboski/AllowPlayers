package org.AllowPlayers;

import java.util.List;

import com.ensifera.animosity.craftirc.EndPoint;
import com.ensifera.animosity.craftirc.RelayedMessage;

public class APPoint implements EndPoint
{
    public Type getType()
    {
        return Type.MINECRAFT;
    }

    public void messageIn(RelayedMessage msg)
    {

    }

    public boolean userMessageIn(String username, RelayedMessage msg)
    {
        return false;
    }

    public boolean adminMessageIn(RelayedMessage msg)
    {
        return false;
    }

    public List<String> listUsers()
    {
        return null;
    }

    public List<String> listDisplayUsers()
    {
        return null;
    }
}
