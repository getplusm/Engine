package t.me.p1azmer.engine.api.placeholder.relational;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.placeholder.Placeholder;

import java.util.regex.Matcher;

public abstract class AbstractRelationalPlaceholder extends Placeholder {

    public AbstractRelationalPlaceholder(@NotNull NexPlugin<?> plugin) {
        super(plugin);
    }

    public abstract String parse(@NotNull Matcher matcher, @NotNull Player player, @NotNull Player viewer);
}
