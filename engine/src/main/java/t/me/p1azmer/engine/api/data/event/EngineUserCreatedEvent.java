package t.me.p1azmer.engine.api.data.event;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.data.AbstractUser;

public class EngineUserCreatedEvent<P extends NexPlugin<P>, U extends AbstractUser<P>> extends EngineUserEvent<P> {

    public EngineUserCreatedEvent(@NotNull U user) {
        super(user, 0, true);
    }

}