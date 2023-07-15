package t.me.p1azmer.engine.api.data;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;

public interface UserDataHolder<P extends NexPlugin<P>, U extends AbstractUser<P>> {

    boolean setupDataHandlers();

    @NotNull AbstractUserDataHandler<P, U> getData();

    @NotNull AbstractUserManager<P, U> getUserManager();
}
