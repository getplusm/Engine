package t.me.p1azmer.engine.api.data.event;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.data.AbstractUser;

public class EngineUserLoadEvent<P extends NexPlugin<P>, U extends AbstractUser<P>> extends EngineUserEvent<P> {

    public EngineUserLoadEvent(@NotNull U user, long ms) {
        super(user, ms, true);
    }
    public EngineUserLoadEvent(@NotNull U user) {
        this(user, 0);
    }
}