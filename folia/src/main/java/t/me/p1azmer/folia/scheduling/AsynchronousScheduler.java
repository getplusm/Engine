package t.me.p1azmer.folia.scheduling;

import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * The asynchronous scheduler. The scheduler doubles as an {@link Executor} implementation
 * which simply forwards calls to {@link #run(Runnable)}
 */
public interface AsynchronousScheduler extends SchedulerBase, Executor {

    /**
     * Schedules a delayed task
     *
     * @param command what to run
     * @param delay   the delay
     * @return the task
     */
    ScheduledTask runDelayed(Runnable command, long delay);

    /**
     * Schedules a delayed task
     *
     * @param command what to run
     * @param delay   the delay
     */
    void runDelayed(Consumer<ScheduledTask> command, long delay);

    /**
     * Schedules a repeating task
     *
     * @param command      what to run
     * @param initialDelay the initial delay in ticks
     * @param period       the period in ticks
     * @return the task
     */
    ScheduledTask runAtFixedRate(Runnable command, long initialDelay, long period);

    /**
     * Schedules a repeating task
     *
     * @param command      what to run
     * @param initialDelay the initial delay in ticks
     * @param period       the period in ticks
     */
    void runAtFixedRate(Consumer<ScheduledTask> command, long initialDelay, long period);

}