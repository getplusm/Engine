package t.me.p1azmer.engine.api.placeholder;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;

public class CachedPlaceholder {
    private final Matcher matcher;
    private final Placeholder abstractPlaceholder;

    public CachedPlaceholder(@NotNull Matcher matcher, @NotNull Placeholder abstractPlaceholder) {
        this.matcher = matcher;
        this.abstractPlaceholder = abstractPlaceholder;
    }

    @NotNull
    public Matcher getMatcher() {
        return matcher;
    }

    @NotNull
    public Placeholder getPlaceholder() {
        return abstractPlaceholder;
    }
}
