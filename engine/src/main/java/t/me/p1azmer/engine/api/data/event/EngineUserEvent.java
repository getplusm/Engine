package t.me.p1azmer.engine.api.data.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.data.AbstractUser;

public abstract class EngineUserEvent<P extends NexPlugin<P>> extends Event {

    private static final HandlerList handlerList = new HandlerList();

    private final AbstractUser<P> user;
    private long waitTime;

    public EngineUserEvent(@NotNull AbstractUser<P> user, long waitTime, boolean async) {
        super(async);
        this.user = user;
        this.waitTime = waitTime;
    }

    public EngineUserEvent(@NotNull AbstractUser<P> user, long waitTime) {
        this(user, waitTime, false);
    }

    public EngineUserEvent(@NotNull AbstractUser<P> user) {
        this(user, 0);
    }

    @NotNull
    public AbstractUser<P> getUser() {
        return this.user;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        if (waitTime < 0) waitTime = 0;

        this.waitTime = waitTime;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}