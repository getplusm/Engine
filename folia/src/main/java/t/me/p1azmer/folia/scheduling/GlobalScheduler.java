package t.me.p1azmer.folia.scheduling;

import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import org.bukkit.plugin.Plugin;

import java.time.Duration;
import java.util.function.Consumer;

final class GlobalScheduler implements RegionalScheduler {

	private final GlobalRegionScheduler globalRegionScheduler;
	private final Plugin plugin;

	GlobalScheduler(Plugin plugin) {
		this.globalRegionScheduler = plugin.getServer().getGlobalRegionScheduler();
		this.plugin = plugin;
	}

	void cancelTasks() {
		globalRegionScheduler.cancelTasks(plugin);
	}

	@Override
	public ScheduledTask run(Runnable command) {
		return new FoliaTask(
				globalRegionScheduler.run(plugin, (task) -> command.run())
		);
	}

	@Override
	public void run(Consumer<ScheduledTask> command) {
		globalRegionScheduler.run(plugin, (task) -> command.accept(new FoliaTask(task)));
	}

	@Override
	public ScheduledTask runDelayed(Runnable command, long delay) {
		return new FoliaTask(
				globalRegionScheduler.runDelayed(plugin, (task) -> command.run(), delay)
		);
	}

	@Override
	public void runDelayed(Consumer<ScheduledTask> command, long delay) {
		globalRegionScheduler.runDelayed(plugin, (task) -> command.accept(new FoliaTask(task)), delay);
	}

	@Override
	public ScheduledTask runAtFixedRate(Runnable command, long initialDelay, long period) {
		return new FoliaTask(
				globalRegionScheduler.runAtFixedRate(plugin, (task) -> command.run(), initialDelay, period)
		);
	}

	@Override
	public void runAtFixedRate(Consumer<ScheduledTask> command, long initialDelay, long period) {
		globalRegionScheduler.runAtFixedRate(plugin, (task) -> command.accept(new FoliaTask(task)), initialDelay, period);
	}

}