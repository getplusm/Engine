package t.me.p1azmer.engine.api.data.connection;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.data.config.DataConfig;

public class ConnectorMySQL extends AbstractDataConnector {

    public ConnectorMySQL(@NotNull NexPlugin<?> plugin, @NotNull DataConfig config) {
        super(plugin, "jdbc:mysql://" + config.mysqlHost + "/" + config.mysqlBase + config.mysqlParameters,
                config.mysqlUser, config.mysqlPassword, config.mysqlSize, config.mysqlIdle,
                config.mysqlLifetime, config.mysqlKeepalive, config.mysqlTimeout);
    }

    public ConnectorMySQL(@NotNull NexPlugin<?> plugin, @NotNull DataConfig config, @NotNull String poolName) {
        super(plugin, "jdbc:mysql://" + config.mysqlHost + "/" + config.mysqlBase + config.mysqlParameters,
                config.mysqlUser, config.mysqlPassword, config.mysqlSize, config.mysqlIdle,
                config.mysqlLifetime, config.mysqlKeepalive, config.mysqlTimeout, poolName);
    }
}