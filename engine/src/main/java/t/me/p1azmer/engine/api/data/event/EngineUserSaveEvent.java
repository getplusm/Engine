package t.me.p1azmer.engine.api.data.event;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.data.AbstractUser;

public class EngineUserSaveEvent<P extends NexPlugin<P>, U extends AbstractUser<P>> extends EngineUserEvent<P> {
    public EngineUserSaveEvent(@NotNull U user, long ms) {
        super(user, ms, false);
    }

    public EngineUserSaveEvent(@NotNull U user) {
        this(user, 0);
    }
}