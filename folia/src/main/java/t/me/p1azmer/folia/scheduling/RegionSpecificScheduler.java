package t.me.p1azmer.folia.scheduling;

import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.Objects;
import java.util.function.Consumer;

final class RegionSpecificScheduler implements RegionalScheduler {

    private final RegionScheduler regionScheduler;
    private final Plugin plugin;
    private final World world;
    private final int chunkX;
    private final int chunkZ;

    RegionSpecificScheduler(Plugin plugin, World world, int chunkX, int chunkZ) {
        this.regionScheduler = plugin.getServer().getRegionScheduler();
        this.plugin = plugin;
        this.world = Objects.requireNonNull(world, "world");
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    @Override
    public ScheduledTask run(Runnable command) {
        return new FoliaTask(
                regionScheduler.run(plugin, world, chunkX, chunkZ, (task) -> command.run())
        );
    }

    @Override
    public void run(Consumer<ScheduledTask> command) {
        regionScheduler.run(
                plugin, world, chunkX, chunkZ, (task) -> command.accept(new FoliaTask(task))
        );
    }

    @Override
    public ScheduledTask runDelayed(Runnable command, long delay) {
        return new FoliaTask(
                regionScheduler.runDelayed(plugin, world, chunkX, chunkZ, (task) -> command.run(), delay)
        );
    }

    @Override
    public void runDelayed(Consumer<ScheduledTask> command, long delay) {
        regionScheduler.runDelayed(
                plugin, world, chunkX, chunkZ, (task) -> command.accept(new FoliaTask(task)), delay
        );
    }

    @Override
    public ScheduledTask runAtFixedRate(Runnable command, long initialDelay, long period) {
        return new FoliaTask(
                regionScheduler.runAtFixedRate(plugin, world, chunkX, chunkZ, (task) -> command.run(), initialDelay, period)
        );
    }

    @Override
    public void runAtFixedRate(Consumer<ScheduledTask> command, long initialDelay, long period) {
        regionScheduler.runAtFixedRate(
                plugin, world, chunkX, chunkZ, (task) -> command.accept(new FoliaTask(task)), initialDelay, period
        );
    }

}