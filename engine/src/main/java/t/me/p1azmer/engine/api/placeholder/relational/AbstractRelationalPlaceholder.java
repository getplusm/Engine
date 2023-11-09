package t.me.p1azmer.engine.api.placeholder.relational;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.placeholder.Placeholder;
import t.me.p1azmer.engine.api.placeholder.PlaceholderExpansion;

import java.util.regex.Matcher;

public abstract class AbstractRelationalPlaceholder<P extends NexPlugin<P>> extends Placeholder<P> {

    public AbstractRelationalPlaceholder(@NotNull PlaceholderExpansion<P> expansion) {
        super(expansion);
    }

    public abstract String parse(@NotNull Matcher matcher, @Nullable Player player, @Nullable Player viewer);
}
