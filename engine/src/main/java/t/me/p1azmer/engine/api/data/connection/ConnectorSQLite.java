package t.me.p1azmer.engine.api.data.connection;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.data.config.DataConfig;
import t.me.p1azmer.engine.utils.FileUtil;

public class ConnectorSQLite extends AbstractDataConnector {

    public ConnectorSQLite(@NotNull NexPlugin<?> plugin, @NotNull DataConfig config) {
        this(plugin, config.sqliteFilename.startsWith("@path=") ? FileUtil.relativize(config.sqliteFilename.replace("@path=", "")) : plugin.getDataFolder().getAbsolutePath() + "/" + config.sqliteFilename);
    }

    public ConnectorSQLite(@NotNull NexPlugin<?> plugin, @NotNull String filePath) {
        super(plugin, "jdbc:sqlite:" + filePath);
    }
}