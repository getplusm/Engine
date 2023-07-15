package t.me.p1azmer.engine.api.data.event;

import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.data.AbstractUser;

public abstract class EngineUserEvent<P extends NexPlugin<P>, U extends AbstractUser<P>> extends Event {

    private final NexPlugin<?> plugin;
    private final U            user;

    public EngineUserEvent(@NotNull P plugin, @NotNull U user) {
        super(true);
        this.plugin = plugin;
        this.user = user;
    }

    @NotNull
    public NexPlugin<?> getPlugin() {
        return this.plugin;
    }

    @NotNull
    public U getUser() {
        return this.user;
    }
}