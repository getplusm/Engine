package t.me.p1azmer.engine.api.placeholder;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;

import java.util.regex.Matcher;

public class CachedPlaceholder<P extends NexPlugin<P>> {
    private final Matcher matcher;
    private final Placeholder<P> abstractPlaceholder;

    public CachedPlaceholder(@NotNull Matcher matcher, @NotNull Placeholder<P> abstractPlaceholder) {
        this.matcher = matcher;
        this.abstractPlaceholder = abstractPlaceholder;
    }

    @NotNull
    public Matcher getMatcher() {
        return matcher;
    }

    @NotNull
    public Placeholder<P> getPlaceholder() {
        return abstractPlaceholder;
    }
}
