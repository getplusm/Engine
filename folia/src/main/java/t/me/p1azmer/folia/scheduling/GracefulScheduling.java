package t.me.p1azmer.folia.scheduling;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import t.me.p1azmer.folia.MorePaperLib;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Bridges scheduling APIs between Folia and Bukkit. Callers must specify where tasks are run:
 * whether the global region, a certain region, or on an entity scheduler, per Folia mechanics.
 * When running on Bukkit, automatic fallback occurs to the BukkitScheduler.
 */
public class GracefulScheduling {
    private final MorePaperLib morePaperLib;

    private Map<Integer, ScheduledTask> scheduledTaskMap;

    /**
     * Creates with the specified folia detection mechanism
     *
     * @param morePaperLib central command
     */
    public GracefulScheduling(MorePaperLib morePaperLib) {
        this.morePaperLib = morePaperLib;
        scheduledTaskMap = new LinkedHashMap<>();
    }

    public int generateTaskId(ScheduledTask task) {
        int size = this.scheduledTaskMap.size() + 1;
        this.scheduledTaskMap.put(size, task);
        return size;
    }

    /**
     * Obtains the asynchronous scheduler, which performs async scheduling appropriately on Folia or Bukkit
     * using either the Folia AsyncScheduler or Bukkit BukkitScheduler
     *
     * @return the asynchronous scheduler
     */
    public AsynchronousScheduler asyncScheduler() {
        return new GlobalAsyncScheduler(morePaperLib.getPlugin());
    }

    /**
     * Obtains a scheduler which posts tasks to the global region on Folia, or the main thread on Bukkit
     *
     * @return a scheduler for the global region
     */
    public RegionalScheduler globalRegionalScheduler() {
        return new GlobalScheduler(morePaperLib.getPlugin());
    }

    /**
     * Obtains a scheduler which posts tasks to a specific region on Folia. <b>Remember that it is inappropriate
     * to use region specific scheduling for entities</b> <br>
     * <br>
     * On Bukkit, this scheduler simply posts to the main thread.
     *
     * @param location the location
     * @return a scheduler for the specific region
     */
    public RegionalScheduler regionSpecificScheduler(Location location) {
        return regionSpecificScheduler(location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    /**
     * Obtains a scheduler which posts tasks to a specific region on Folia. <b>Remember that it is inappropriate
     * to use region specific scheduling for entities</b> <br>
     * <br>
     * On Bukkit, this scheduler simply posts to the main thread.
     *
     * @param world  the world
     * @param chunkX the chunk X coordinate
     * @param chunkZ the chunk Z coordinate
     * @return a scheduler for the specific region
     */
    public RegionalScheduler regionSpecificScheduler(World world, int chunkX, int chunkZ) {
        return new RegionSpecificScheduler(morePaperLib.getPlugin(), world, chunkX, chunkZ);
    }

    /**
     * Whether the current thread is the thread ticking the global region on Folia, or the main thread on Bukkit
     *
     * @return true if currently running on the global region thread, or the main thread on Bukkit
     */
    public boolean isOnGlobalRegionThread() {
        Server server = morePaperLib.getPlugin().getServer();
        return server.isGlobalTickThread();
    }

    public void cancelTask(int taskId) {
        ScheduledTask task = this.scheduledTaskMap.get(taskId);
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }
    }

    /**
     * Cancels tasks submitted by the owning plugin, where possible. It is not possible to clear tasks
     * on specific entity/region schedulers when running on Folia: no such API exists.
     */
    public void cancelGlobalTasks() {
        new GlobalAsyncScheduler(morePaperLib.getPlugin()).cancelTasks();
        new GlobalScheduler(morePaperLib.getPlugin()).cancelTasks();
        scheduledTaskMap.values().forEach(ScheduledTask::cancel);
        scheduledTaskMap.clear();
        scheduledTaskMap = null;
    }
}