package t.me.p1azmer.folia.scheduling;

import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.Nullable;
import t.me.p1azmer.folia.Folia;

class FoliaTask implements ScheduledTask {

    private final io.papermc.paper.threadedregions.scheduler.ScheduledTask task;
    private final int taskId;

    FoliaTask(io.papermc.paper.threadedregions.scheduler.ScheduledTask task) {
        this.task = task;
        this.taskId = Folia.getMorePaperLib().scheduling().generateTaskId(this);
    }

    static FoliaTask ofNullable(io.papermc.paper.threadedregions.scheduler.@Nullable ScheduledTask task) {
        return (task == null) ? null : new FoliaTask(task);
    }

    @Override
    public Plugin owningPlugin() {
        return task.getOwningPlugin();
    }

    @Override
    public int taskId() {
        return this.taskId;
    }

    @Override
    public void cancel() {
        task.cancel();
    }

    @Override
    public boolean isCancelled() {
        return task.isCancelled();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoliaTask foliaTask = (FoliaTask) o;
        return task.equals(foliaTask.task);
    }

    @Override
    public int hashCode() {
        return task.hashCode();
    }

    @Override
    public String toString() {
        return "FoliaTask{" +
                "task=" + task +
                "id=" + taskId +
                '}';
    }

}