package t.me.p1azmer.engine.utils;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexEngine;

public class EngineUtils {

    public static final NexEngine ENGINE = NexEngine.getPlugin(NexEngine.class);

    public static final String VAULT = "Vault";
    public static final String PLACEHOLDER_API = "PlaceholderAPI";
    public static final String FLOODGATE = "floodgate";
    public static final String WORLD_GUARD = "WorldGuard";
    public static final String MONSTERS = "Monsters";
    public static final String NPC = "ServersNPC";
    public static final String REGION_COMMAND = "RegionCommand";

    public static boolean hasPlugin(@NotNull String pluginName) {
        Plugin plugin = ENGINE.getPluginManager().getPlugin(pluginName);
        return plugin != null;
    }

    public static boolean hasPlaceholderAPI() {
        return hasPlugin(PLACEHOLDER_API);
    }

    public static boolean hasVault() {
        return hasPlugin(VAULT);
    }

    public static boolean hasFloodgate() {
        return hasPlugin(FLOODGATE);
    }

    public static boolean hasNPC() {
        return hasPlugin(NPC);
    }

    public static boolean hasWorldGuard() {
        return hasPlugin(WORLD_GUARD);
    }

    public static boolean hasMonsters() {
        return hasPlugin(MONSTERS);
    }

    public static boolean hasRegionCommand() {
        return hasPlugin(REGION_COMMAND);
    }
}