package t.me.p1azmer.engine.api.data.task;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.data.AbstractDataHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataSynchronizationTask<P extends NexPlugin<P>> {

    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public DataSynchronizationTask(@NotNull AbstractDataHandler<P> dataHandler) {
        scheduler.scheduleWithFixedDelay(() -> {
            try {
                dataHandler.onSynchronize();
            } catch (RuntimeException exception) {
                dataHandler.plugin().getLogger().log(Level.SEVERE, "Error while synchronizing data", exception);
            }
        }, 0, dataHandler.getConfig().syncInterval, TimeUnit.SECONDS);
    }

    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
