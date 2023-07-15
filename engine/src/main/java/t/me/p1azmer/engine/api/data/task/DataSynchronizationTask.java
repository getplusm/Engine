package t.me.p1azmer.engine.api.data.task;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.data.AbstractDataHandler;
import t.me.p1azmer.engine.api.server.AbstractTask;

public class DataSynchronizationTask<P extends NexPlugin<P>> extends AbstractTask<P> {

    private final AbstractDataHandler<P> dataHandler;

    public DataSynchronizationTask(@NotNull AbstractDataHandler<P> dataHandler) {
        super(dataHandler.plugin(), dataHandler.getConfig().syncInterval, true);
        this.dataHandler = dataHandler;
    }

    @Override
    public void action() {
        this.dataHandler.onSynchronize();
    }
}