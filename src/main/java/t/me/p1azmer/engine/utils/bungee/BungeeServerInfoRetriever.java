package t.me.p1azmer.engine.utils.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.utils.EngineUtils;

public class BungeeServerInfoRetriever implements PluginMessageListener {

    private final BungeeManager bungeeManager;

    public BungeeServerInfoRetriever(@NotNull BungeeManager manager) {
        this.bungeeManager = manager;
    }

    @Override
    public void onPluginMessageReceived(String channel, @NotNull Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        try {
            final ByteArrayDataInput in = ByteStreams.newDataInput(message);

            final String sub = in.readUTF();

            if (sub.equals("PlayerCount")) {
                final String server = in.readUTF();
                final int playerCount = in.readInt();

                if (this.bungeeManager.getServerOnlineCache().containsKey(server)) {
                    this.bungeeManager.getServerOnlineCache().replace(server, playerCount);
                    return;
                }

                this.bungeeManager.getServerOnlineCache().put(server, playerCount);
            }
        } catch (Exception exception) {
            EngineUtils.ENGINE.error("Could not retrieve player count. (Invalid server?)");
        }
    }
}