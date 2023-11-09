package t.me.p1azmer.engine.api.placeholder;

import me.clip.placeholderapi.expansion.Relational;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.placeholder.relational.AbstractRelationalPlaceholder;

import java.util.*;
import java.util.regex.Matcher;

public class PlaceholderExpansion<P extends NexPlugin<P>> extends me.clip.placeholderapi.expansion.PlaceholderExpansion implements Relational {

    public final List<Placeholder<P>> placeholders = new ArrayList<>();

    public final P plugin;

    public PlaceholderExpansion(@NotNull P plugin) {
        this.plugin = plugin;
    }

    public Map<String, CachedPlaceholder<P>> placeholderCache = new HashMap<>();

    @Override
    public String onRequest(@Nullable OfflinePlayer offlinePlayer, @NotNull String params) {
        if (offlinePlayer == null) return null;

        CachedPlaceholder<P> cachedPlaceholder = placeholderCache.computeIfAbsent(params, s -> {
            for (Placeholder<P> placeholder : placeholders) {
                Matcher matcher = placeholder.getPattern().matcher(params);
                if (!matcher.matches()) continue;
                return new CachedPlaceholder<>(matcher, placeholder);
            }
            return null;
        });
        if (cachedPlaceholder == null) return null;
        if (cachedPlaceholder.getPlaceholder() instanceof AbstractPlaceholder<P> placeholder) {
            return placeholder.parse(cachedPlaceholder.getMatcher(), offlinePlayer);
        }

        return null;
    }


    @Override
    public @NotNull String getIdentifier() {
        return plugin.getName();
    }

    @Override
    public @NotNull String getAuthor() {
        return "plazmer";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    public void setup() {
        super.register();
        this.plugin.warn("Register self (" + this.placeholders.size() + ") PAPI expansions");
    }

    public void shutdown() {
        super.unregister();
        this.plugin.warn("Unregister self (" + this.placeholders.size() + ") PAPI expansions");
    }

    @Deprecated
    public boolean unRegister() {
        return super.unregister();
    }

    @SafeVarargs
    public final PlaceholderExpansion<P> addPlaceholder(@NotNull Placeholder<P>... placeholders) {
        this.placeholders.addAll(Arrays.stream(placeholders).toList());
        return this;
    }


    @Override
    public String onPlaceholderRequest(@Nullable Player one, @Nullable Player two, @NotNull String params) {
        if (one == null || two == null) return null;

        CachedPlaceholder<P> cachedPlaceholder = placeholderCache.computeIfAbsent(params, s -> {
            for (Placeholder<P> placeholder : placeholders) {
                Matcher matcher = placeholder.getPattern().matcher(params);
                if (!matcher.matches()) continue;
                return new CachedPlaceholder<>(matcher, placeholder);
            }
            return null;
        });
        if (cachedPlaceholder == null) return null;
        if (cachedPlaceholder.getPlaceholder() instanceof AbstractRelationalPlaceholder<P> placeholder) {
            return placeholder.parse(cachedPlaceholder.getMatcher(), one, two);
        }
        return null;
    }
}