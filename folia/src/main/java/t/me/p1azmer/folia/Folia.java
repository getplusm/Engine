package t.me.p1azmer.folia;

import io.papermc.paper.threadedregions.RegionizedServerInitEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public final class Folia {
    private static final boolean CONFIG_EXISTS = classExists("io.papermc.paper.threadedregions.RegionizedServer") || classExists("io.papermc.paper.threadedregions.RegionizedServerInitEvent");

    public static boolean isFolia() {
        return CONFIG_EXISTS;
    }

    public static void schedule(final Plugin plugin, final Location location, final Runnable runnable) {
        Bukkit.getServer().getRegionScheduler().execute(plugin, location, runnable);
    }

    public static void scheduleFixed(final Plugin plugin, final Location location, final Runnable runnable, final long delay, final long period) {
        Bukkit.getServer().getRegionScheduler().runAtFixedRate(plugin, location, ignored -> runnable.run(), delay, period);
    }

    public static void scheduleFixedGlobal(final Plugin plugin, final Runnable runnable, final long delay, final long period) {
        Bukkit.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin, ignored -> runnable.run(), delay, period);
    }

    public static void execute(@NotNull Plugin plugin, @NotNull Runnable runnable) {
        Bukkit.getServer().getGlobalRegionScheduler().run(plugin, scheduledTask -> runnable.run());
    }

    public static void executeAsync(@NotNull Plugin plugin, @NotNull Runnable runnable) {
        Bukkit.getServer().getAsyncScheduler().runNow(plugin, scheduledTask -> runnable.run());
    }

    public static void executeLater(@NotNull Plugin plugin, @NotNull Runnable runnable, long delay) {
        Bukkit.getServer().getGlobalRegionScheduler().runDelayed(plugin, scheduledTask -> runnable.run(), delay);
    }

    public static void executeLaterAsync(@NotNull Plugin plugin, @NotNull Runnable runnable, long delay) {
        Bukkit.getServer().getAsyncScheduler().runDelayed(plugin, scheduledTask -> runnable.run(), delay, TimeUnit.MILLISECONDS);
    }

    public static void executeTimer(@NotNull Plugin plugin, @NotNull Runnable runnable, long delay) {
        Bukkit.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin, scheduledTask -> runnable.run(), delay, 0);
    }

    public static void executeTimerAsync(@NotNull Plugin plugin, @NotNull Runnable runnable, long delay) {
        Bukkit.getServer().getAsyncScheduler().runAtFixedRate(plugin, scheduledTask -> runnable.run(), delay, 0, TimeUnit.MILLISECONDS);
    }

    public static void cancelTasks(final Plugin plugin) {
        Bukkit.getServer().getAsyncScheduler().cancelTasks(plugin);
        Bukkit.getServer().getGlobalRegionScheduler().cancelTasks(plugin);
    }

    public static void onServerInit(final Plugin plugin, final Runnable runnable) {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onRegionisedServerInit(final RegionizedServerInitEvent event) {
                runnable.run();
            }
        }, plugin);
    }

    private static boolean classExists(final String clazz) {
        try {
            Class.forName(clazz);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}