package t.me.p1azmer.engine.api.data.event;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.data.AbstractUser;

public class EngineUserUnloadEvent<P extends NexPlugin<P>, U extends AbstractUser<P>> extends EngineUserEvent<P, U> {

    private static final HandlerList handlerList = new HandlerList();

    public EngineUserUnloadEvent(@NotNull P plugin, @NotNull U user) {
        super(plugin, user);
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
