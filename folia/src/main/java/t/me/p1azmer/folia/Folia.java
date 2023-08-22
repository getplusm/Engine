package t.me.p1azmer.folia;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.folia.scheduling.ScheduledTask;

public final class Folia {
    private static final boolean CONFIG_EXISTS = classExists("io.papermc.paper.threadedregions.RegionizedServer") || classExists("io.papermc.paper.threadedregions.RegionizedServerInitEvent");
    private static MorePaperLib morePaperLib;

    public static boolean isFolia() {
        return CONFIG_EXISTS;
    }

    public static void setup(@NotNull Plugin nexPlugin) {
        morePaperLib = new MorePaperLib(nexPlugin);
    }

    public static void shutdown() {
        if (morePaperLib != null) {
            morePaperLib.scheduling().cancelGlobalTasks();
            morePaperLib = null;
        }
    }

    public static ScheduledTask execute(@NotNull Runnable runnable) {
        if (morePaperLib == null) return null;
        return morePaperLib.scheduling().globalRegionalScheduler().run(runnable);
    }

    public static ScheduledTask executeAsync(@NotNull Runnable runnable) {
        if (morePaperLib == null) return null;
        return morePaperLib.scheduling().asyncScheduler().run(runnable);
    }

    public static ScheduledTask executeLater(@NotNull Runnable runnable, long delay) {
        if (morePaperLib == null) return null;
        return morePaperLib.scheduling().globalRegionalScheduler().runDelayed(runnable, delay);
    }

    public static ScheduledTask executeLaterAsync(@NotNull Runnable runnable, long delay) {
        if (morePaperLib == null) return null;
        return morePaperLib.scheduling().asyncScheduler().runDelayed(runnable, delay);
    }

    public static ScheduledTask executeTimer(@NotNull Runnable runnable, long delay) {
        return executeTimer(runnable, delay, 20);
    }

    public static ScheduledTask executeTimer(@NotNull Runnable runnable, long delay, long period) {
        if (morePaperLib == null) return null;
        return morePaperLib.scheduling().globalRegionalScheduler().runAtFixedRate(runnable, delay, period);
    }

    public static ScheduledTask executeTimerAsync(@NotNull Runnable runnable, long delay) {
        return executeTimerAsync(runnable, delay, 20);
    }

    public static ScheduledTask executeTimerAsync(@NotNull Runnable runnable, long delay, long period) {
        if (morePaperLib == null) return null;
        return morePaperLib.scheduling().asyncScheduler().runAtFixedRate(runnable, delay, period);
    }

    @Nullable
    public static MorePaperLib getMorePaperLib() {
        return morePaperLib;
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