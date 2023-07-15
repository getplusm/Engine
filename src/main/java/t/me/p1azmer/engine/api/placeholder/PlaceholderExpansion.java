package t.me.p1azmer.engine.api.placeholder;

import me.clip.placeholderapi.expansion.Relational;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.placeholder.relational.AbstractRelationalPlaceholder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class PlaceholderExpansion<P extends NexPlugin<P>> extends me.clip.placeholderapi.expansion.PlaceholderExpansion implements Relational {

    public final List<Placeholder> placeholders = new ArrayList<>();

    public final P plugin;

    public PlaceholderExpansion(P plugin) {
        this.plugin = plugin;
    }

    public Map<String, CachedPlaceholder> placeholderCache = new HashMap<>();


    @Override
    public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        if (offlinePlayer == null) return null;

        CachedPlaceholder cachedPlaceholder = placeholderCache.computeIfAbsent(params, s -> {
            for (Placeholder placeholder : placeholders) {
                Matcher matcher = placeholder.getPattern().matcher(params);
                if (!matcher.matches()) continue;
                return new CachedPlaceholder(matcher, placeholder);
            }
            return null;
        });
        if (cachedPlaceholder == null) return null;
        if (cachedPlaceholder.getPlaceholder() instanceof AbstractPlaceholder placeholder) {
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

    @Override
    public boolean register() {
        return super.register();
    }

    public boolean unRegister() {
        return super.unregister();
    }

    @Override
    public String onPlaceholderRequest(Player one, Player two, String params) {
        if (one == null || two == null) return null;

        CachedPlaceholder cachedPlaceholder = placeholderCache.computeIfAbsent(params, s -> {
            for (Placeholder placeholder : placeholders) {
                Matcher matcher = placeholder.getPattern().matcher(params);
                if (!matcher.matches()) continue;
                return new CachedPlaceholder(matcher, placeholder);
            }
            return null;
        });
        if (cachedPlaceholder == null) return null;
        if (cachedPlaceholder.getPlaceholder() instanceof AbstractRelationalPlaceholder placeholder) {
            return placeholder.parse(cachedPlaceholder.getMatcher(), one, two);
        }
        return null;
    }
}