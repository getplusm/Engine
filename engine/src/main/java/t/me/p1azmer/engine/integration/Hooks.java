package t.me.p1azmer.engine.integration;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexEngine;
import t.me.p1azmer.engine.integration.external.WorldGuardHook;
import t.me.p1azmer.engine.utils.EngineUtils;
import t.me.p1azmer.engine.utils.PlayerUtil;

import java.util.Map;
import java.util.Set;

public class Hooks {

    public static final String VAULT = "Vault";

    public static final String PLASMOVOICE = "PlasmoVoice";

    public static final String LUCKPERMS = "LuckPerms";
    public static final String PLACEHOLDER_API = "PlaceholderAPI";
    public static final String WORLD_GUARD = "WorldGuard";
    public static final String MONSTERS = "Monsters";
    public static final String NPC = "ServersNPC";

    public static final String FLOODGATE = "floodgate";

    public static final String REGION_COMMAND = "RegionCommand";
    private static final NexEngine ENGINE = EngineUtils.ENGINE;

    @Deprecated
    @NotNull
    public static String getPermissionGroup(@NotNull Player player) {
        return PlayerUtil.getPermissionGroup(player);
    }

    @Deprecated
    @NotNull
    public static Set<String> getPermissionGroups(@NotNull Player player) {
        return PlayerUtil.getPermissionGroups(player);
    }

    @Deprecated
    @NotNull
    public static String getPrefix(@NotNull Player player) {
        return PlayerUtil.getPrefix(player);
    }

    @Deprecated
    @NotNull
    public static String getSuffix(@NotNull Player player) {
        return PlayerUtil.getSuffix(player);
    }

    public static boolean hasPlugin(@NotNull String pluginName) {
        Plugin plugin = ENGINE.getPluginManager().getPlugin(pluginName);
        return plugin != null;// && p.isEnabled();
    }


    public static boolean canFights(@NotNull Entity attacker, @NotNull Entity victim) {
        if (attacker.equals(victim)) return false;
        if (victim.isInvulnerable() || !(victim instanceof LivingEntity)) return false;

//        if (isNPC(victim)) {
//            if (!hasPlugin("Sentinel")) {
//                return false;
//            }
//
//            NPC npc = CitizensAPI.getNPCRegistry().getNPC(victim);
//            if (!npc.hasTrait(SentinelTrait.class)) {
//                return false;
//            }
//        }

        if (EngineUtils.hasWorldGuard()) {
            return WorldGuardHook.canFights(attacker, victim);
        }
//        if (hasRegionCommand()) {
//            return RegionCommandHook.canFights(attacker, victim);
//        }

        return true;
    }
}