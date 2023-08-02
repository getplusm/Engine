package t.me.p1azmer.folia.scheduling;

import org.bukkit.plugin.Plugin;

/**
 * A wrapper for either a standard BukkitTask or a Folia ScheduledTask
 */
public interface ScheduledTask {

	/**
	 * The plugin which created this task
	 *
	 * @return the owning plugin
	 */
	Plugin owningPlugin();

	int taskId();

	/**
	 * Cancels the task
	 */
	void cancel();

	/**
	 * Determines whether the task was cancelled
	 *
	 * @return true if cancelled
	 */
	boolean isCancelled();

}