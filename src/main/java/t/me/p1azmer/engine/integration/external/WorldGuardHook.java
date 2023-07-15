package t.me.p1azmer.engine.integration.external;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.utils.EngineUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class WorldGuardHook {

    static WorldGuard worldGuard = WorldGuard.getInstance();

    public static boolean canFights(@NotNull Entity damager, @NotNull Entity victim) {
        return EngineUtils.hasWorldGuard() && WorldGuardPlugin.inst().createProtectionQuery().testEntityDamage(damager, victim);
    }

    public static boolean isInRegion(@NotNull Entity entity, @NotNull String region) {
        return EngineUtils.hasWorldGuard() && getRegion(entity).equalsIgnoreCase(region);
    }

    @NotNull
    public static String getRegion(@NotNull Entity entity) {
        return EngineUtils.hasWorldGuard() ? getRegion(entity.getLocation()) : "";
    }

    @NotNull
    public static String getRegion(@NotNull Location loc) {
        ProtectedRegion region = getProtectedRegion(loc);
        return EngineUtils.hasWorldGuard() ? region == null ? "" : region.getId() : "";
    }

    @Nullable
    public static ProtectedRegion getProtectedRegion(@NotNull Entity entity) {
        return EngineUtils.hasWorldGuard() ? getProtectedRegion(entity.getLocation()) : null;
    }

    @Nullable
    public static ProtectedRegion getProtectedRegion(@NotNull Location location) {
        if (!EngineUtils.hasWorldGuard()) return null;
        World world = location.getWorld();
        if (world == null) return null;

        com.sk89q.worldedit.world.World sworld = BukkitAdapter.adapt(world);
        BlockVector3 vector3 = BukkitAdapter.adapt(location).toVector().toBlockPoint();
        RegionManager regionManager = worldGuard.getPlatform().getRegionContainer().get(sworld);
        if (regionManager == null) return null;

        ApplicableRegionSet set = regionManager.getApplicableRegions(vector3);
        return set.getRegions().stream().max(Comparator.comparingInt(ProtectedRegion::getPriority)).orElse(null);
    }

    @NotNull
    public static Collection<ProtectedRegion> getProtectedRegions(@NotNull World w) {
        if (!EngineUtils.hasWorldGuard()) return Collections.emptyList();
        com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(w);
        RegionManager regionManager = worldGuard.getPlatform().getRegionContainer().get(world);

        return regionManager == null ? Collections.emptySet() : regionManager.getRegions().values();
    }
}