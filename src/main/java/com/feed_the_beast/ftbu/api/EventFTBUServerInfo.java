package com.feed_the_beast.ftbu.api;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbu.world.ServerInfoFile;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EventFTBUServerInfo extends Event
{
    private final ServerInfoFile file;
    private final IForgePlayer player;
    private final boolean isOP;

    public EventFTBUServerInfo(ServerInfoFile f, IForgePlayer p, boolean o)
    {
        file = f;
        player = p;
        isOP = o;
    }

    public ServerInfoFile getFile()
    {
        return file;
    }

    public IForgePlayer getPlayer()
    {
        return player;
    }

    public boolean isOP()
    {
        return isOP;
    }
}