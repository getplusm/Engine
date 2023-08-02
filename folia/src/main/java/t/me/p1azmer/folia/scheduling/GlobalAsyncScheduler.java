package t.me.p1azmer.folia.scheduling;

import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

final class GlobalAsyncScheduler implements AsynchronousScheduler {

    private final AsyncScheduler asyncScheduler;
    private final Plugin plugin;

    GlobalAsyncScheduler(Plugin plugin) {
        this.asyncScheduler = plugin.getServer().getAsyncScheduler();
        this.plugin = plugin;
    }

    void cancelTasks() {
        asyncScheduler.cancelTasks(plugin);
    }

    @Override
    public void execute(@NotNull Runnable command) {
        asyncScheduler.runNow(plugin, (task) -> command.run());
    }

    @Override
    public ScheduledTask run(Runnable command) {
        return new FoliaTask(
                asyncScheduler.runNow(plugin, (task) -> command.run())
        );
    }

    @Override
    public void run(Consumer<ScheduledTask> command) {
        asyncScheduler.runNow(plugin, (task) -> command.accept(new FoliaTask(task)));
    }

    @Override
    public ScheduledTask runDelayed(Runnable command, long delay) {
        return new FoliaTask(
                asyncScheduler.runDelayed(plugin, (task) -> command.run(), delay, TimeUnit.MILLISECONDS)
        );
    }

    @Override
    public void runDelayed(Consumer<ScheduledTask> command, long delay) {
        asyncScheduler.runDelayed(
                plugin, (task) -> command.accept(new FoliaTask(task)), delay, TimeUnit.MILLISECONDS
        );
    }

    @Override
    public ScheduledTask runAtFixedRate(Runnable command, long initialDelay, long period) {
        return new FoliaTask(
                asyncScheduler.runAtFixedRate(
                        plugin, (task) -> command.run(), initialDelay, period, TimeUnit.MILLISECONDS
                )
        );
    }

    @Override
    public void runAtFixedRate(Consumer<ScheduledTask> command, long initialDelay, long period) {
        asyncScheduler.runAtFixedRate(
                plugin, (task) -> command.accept(new FoliaTask(task)), initialDelay, period, TimeUnit.MILLISECONDS
        );
    }

}