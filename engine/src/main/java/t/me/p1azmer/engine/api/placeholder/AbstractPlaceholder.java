package t.me.p1azmer.engine.api.placeholder;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;

import java.util.regex.Matcher;

public abstract class AbstractPlaceholder<P extends NexPlugin<P>> extends Placeholder<P> {

    public AbstractPlaceholder(@NotNull PlaceholderExpansion<P> expansion) {
        super(expansion);
    }

    public abstract String parse(@NotNull Matcher matcher, @NotNull OfflinePlayer player);
}
