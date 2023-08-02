package t.me.p1azmer.folia.scheduling;

import java.util.function.Consumer;

/**
 * Base interface for all schedulers
 *
 */
public interface SchedulerBase {

	/**
	 * Schedules a task
	 *
	 * @param command what to run
	 * @return the task
	 */
	ScheduledTask run(Runnable command);

	/**
	 * Schedules a task
	 *
	 * @param command what to run
	 */
	void run(Consumer<ScheduledTask> command);

}