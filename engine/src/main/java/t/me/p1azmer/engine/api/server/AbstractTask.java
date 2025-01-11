package t.me.p1azmer.engine.api.server;

import com.cjcrafter.foliascheduler.ServerImplementation;
import com.cjcrafter.foliascheduler.TaskImplementation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.NexPlugin;

public abstract class AbstractTask<P extends NexPlugin<P>> {

    @NotNull
    protected final P plugin;

    protected int taskId;
    @Nullable TaskImplementation<?> foliaTask;

    protected long interval;
    protected boolean async;

    public AbstractTask(@NotNull P plugin, int interval, boolean async) {
        this(plugin, interval * 20L, async);
    }

    public AbstractTask(@NotNull P plugin, long interval, boolean async) {
        this.plugin = plugin;
        this.interval = interval;
        this.async = async;
        this.taskId = -1;
    }

    public abstract void action();

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
                foliaTask = foliaScheduler.async().runAtFixedRate(this::action, 1L, interval);
            } else {
                this.taskId = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::action, 1L, interval).getTaskId();
            }
        } else {
            if (NexPlugin.isFolia && foliaScheduler != null) {
                foliaTask = foliaScheduler.global().runAtFixedRate(this::action, 1L, interval);
            } else {
                this.taskId = plugin.getServer().getScheduler().runTaskTimer(this.plugin, this::action, 1L, interval).getTaskId();
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
}