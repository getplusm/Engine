package t.me.p1azmer.engine.api.placeholder;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;

import java.util.regex.Pattern;

public abstract class Placeholder<P extends NexPlugin<P>> {

    protected final P plugin;
    protected final PlaceholderExpansion<P> expansion;

    private Pattern pattern;

    public Placeholder(@NotNull PlaceholderExpansion<P> expansion) {
        this.expansion = expansion;
        this.plugin = expansion.plugin;
    }

    @NotNull
    public abstract String getRegex();

    @NotNull
    public Pattern getPattern() {
        if (pattern == null) {
            pattern = Pattern.compile(getRegex());
        }
        return pattern;
    }

    @NotNull
    public P getPlugin() {
        return plugin;
    }

    @NotNull
    public PlaceholderExpansion<P> getExpansion() {
        return expansion;
    }
}
