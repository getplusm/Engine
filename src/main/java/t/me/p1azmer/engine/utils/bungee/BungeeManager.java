package t.me.p1azmer.engine.utils.bungee;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexEngine;
import t.me.p1azmer.engine.api.manager.AbstractManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BungeeManager extends AbstractManager<NexEngine> {

    private Map<String, Integer> serverOnlineCache;
    private Cache<String, Byte> lastServerCheck;

    public BungeeManager(@NotNull NexEngine plugin) {
        super(plugin);
    }


    @Override
    public void onLoad() {
        this.serverOnlineCache = new HashMap<>();
        this.lastServerCheck = CacheBuilder.newBuilder()
                .expireAfterWrite(3L, TimeUnit.SECONDS) // 3 seconds delay for checking for servers should be okay since we don't want to flood the plugin message channel
                .build();

        Messenger messenger = Bukkit.getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(plugin, "BungeeCord");
        messenger.registerIncomingPluginChannel(plugin, "BungeeCord", new BungeeServerInfoRetriever(this)); // register player count checker
    }

    @Override
    public void onShutdown() {
        Messenger messenger = Bukkit.getServer().getMessenger();
        messenger.unregisterOutgoingPluginChannel(plugin, "BungeeCord");
        messenger.unregisterIncomingPluginChannel(plugin, "BungeeCord"); // unregister player count checker

        if (this.serverOnlineCache != null) {
            this.serverOnlineCache.clear();
            this.serverOnlineCache = null;
        }
        if (this.lastServerCheck != null) {
            this.lastServerCheck.cleanUp();
            this.lastServerCheck = null;
        }
    }

    public void connect(Player player, String server) {
        plugin.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                out.writeUTF(server);
                player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
            } catch (Exception ignored) {
            }
        });
    }

    public void retrieveOnlinePlayers(Player player, String server) {
        if (lastServerCheck.asMap().containsKey(server)) {
            return; // do not check for players
        }
        lastServerCheck.put(server, (byte) 0);
        plugin.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("PlayerCount");
                out.writeUTF(server);
                player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
            } catch (Exception ignored) {
            }
        });
    }

    public Map<String, Integer> getServerOnlineCache() {
        return serverOnlineCache;
    }
}
