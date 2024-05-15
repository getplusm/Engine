package t.me.p1azmer.engine.actions.actions.list;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.actions.AbstractActionExecutor;
import t.me.p1azmer.engine.actions.actions.ActionType;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.params.IParamType;
import t.me.p1azmer.engine.utils.NumberUtil;
import t.me.p1azmer.engine.utils.StringUtil;

import java.util.Set;

public class Action_Teleport extends AbstractActionExecutor {

    public Action_Teleport(@NotNull NexPlugin<?> plugin) {
        super(plugin, ActionType.TELEPORT);
    }

    @Override
    public void registerParams() {
        this.registerParam(IParamType.TARGET);
        this.registerParam(IParamType.LOCATION);
    }

    @Override
    protected void execute(@NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamResult result) {
        String locRaw = result.getParamValue(IParamType.LOCATION).getString(null);
        if (locRaw == null) return;

        Location locExe = exe.getLocation();

        locRaw = locRaw
                .replace("%executor.world%", exe.getWorld().getName())
                .replace("%executor.x%", String.valueOf(locExe.getX()))
                .replace("%executor.y%", String.valueOf(locExe.getY()))
                .replace("%executor.z%", String.valueOf(locExe.getZ()));

        for (Entity e : targets) {
            Location locE = e.getLocation();
            locRaw = locRaw
                    .replace("%target.world%", exe.getWorld().getName())
                    .replace("%target.x%", String.valueOf(locE.getX()))
                    .replace("%target.y%", String.valueOf(locE.getY()))
                    .replace("%target.z%", String.valueOf(locE.getZ()));

            String[] split = locRaw.replace(" ", "").split(",");
            World    world = plugin.getServer().getWorld(split[0]);
            if (world == null) continue;


            double   x   = split.length >= 2 ? NumberUtil.getAnyDouble(split[1], 0) : 0;
            double   y   = split.length >= 3 ? NumberUtil.getAnyDouble(split[2], 0) : 0;
            double   z   = split.length >= 4 ? NumberUtil.getAnyDouble(split[3], 0) : 0;
            Location loc = new Location(world, x, y, z);

            e.teleport(loc);
        }
    }

    @Override
    public boolean mustHaveTarget() {
        return true;
    }

}
