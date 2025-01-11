package t.me.p1azmer.engine.utils.wrapper;

import com.cjcrafter.foliascheduler.ServerImplementation;
import com.cjcrafter.foliascheduler.TaskImplementation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.NexPlugin;

public class UniTask {

    private final NexPlugin<?> plugin;
    private final Runnable runnable;
    private final long interval;
    private final boolean async;

    @Nullable TaskImplementation<?> foliaTask;
    private int taskId;

    public UniTask(@NotNull NexPlugin<?> plugin, @NotNull Runnable runnable, long interval, boolean async) {
        this.plugin = plugin;
        this.runnable = runnable;
        this.interval = interval;
        this.async = async;

        this.taskId = -1;
    }

    @NotNull
    public static Builder builder(@NotNull NexPlugin<?> plugin) {
        return new Builder(plugin);
    }

    public final void restart() {
        this.stop();
        this.start();
    }

    public boolean start() {
        if (this.taskId >= 0) return false;
        if (this.interval <= 0L) return false;

        ServerImplementation foliaScheduler = plugin.getFoliaScheduler();
        if (this.async) {
            if (NexPlugin.isFolia && foliaScheduler != null) {
                foliaTask = foliaScheduler.async().runAtFixedRate(runnable, 1L, interval);
            } else {
                this.taskId = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, runnable, 1L, interval).getTaskId();
            }
        } else {
            if (NexPlugin.isFolia && foliaScheduler != null) {
                foliaTask = foliaScheduler.global().runAtFixedRate(runnable, 1L, interval);
            } else {
                this.taskId = plugin.getServer().getScheduler().runTaskTimer(this.plugin, runnable, 1L, interval).getTaskId();
            }
        }
        return true;
    }

    public boolean stop() {
        if (this.taskId < 0) return false;

        if (NexPlugin.isFolia && foliaTask != null) {
            foliaTask.cancel();
        } else {
            this.plugin.getServer().getScheduler().cancelTask(this.taskId);
        }
        this.taskId = -1;
        return true;
    }

    public static class Builder {

        private final NexPlugin<?> plugin;

        private Runnable runnable;
        private long interval;
        private boolean async;

        public Builder(@NotNull NexPlugin<?> plugin) {
            this.plugin = plugin;
        }

        @NotNull
        public Builder withRunnable(@NotNull Runnable runnable) {
            this.runnable = runnable;
            return this;
        }

        @NotNull
        public Builder withTicks(long interval) {
            this.interval = interval;
            return this;
        }

        @NotNull
        public Builder withSeconds(int interval) {
            this.interval = interval * 20L;
            return this;
        }

        @NotNull
        public Builder async() {
            this.async = true;
            return this;
        }

        @NotNull
        public UniTask build() {
            return new UniTask(plugin, runnable, interval, async);
        }

        @NotNull
        public UniTask buildAndRun() {
            UniTask task = this.build();
            task.start();
            return task;
        }
    }
}