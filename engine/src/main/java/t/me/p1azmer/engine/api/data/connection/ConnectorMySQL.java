package t.me.p1azmer.engine.api.data.connection;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.data.config.DataConfig;

public class ConnectorMySQL extends AbstractDataConnector {

    public ConnectorMySQL(@NotNull NexPlugin<?> plugin, @NotNull DataConfig config) {
        this(plugin, config.mysqlHost, config.mysqlBase, config.mysqlUser, config.mysqlPassword);
    }

    public ConnectorMySQL(@NotNull NexPlugin<?> plugin,
                          @NotNull String host, @NotNull String base,
                          @NotNull String userName, @NotNull String password) {
        super(plugin, "jdbc:mysql://" + host + "/" + base + "?allowPublicKeyRetrieval=true&useSSL=false", userName, password);
    }
}