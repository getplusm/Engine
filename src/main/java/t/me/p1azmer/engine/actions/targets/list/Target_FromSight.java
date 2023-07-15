package t.me.p1azmer.engine.actions.targets.list;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.params.IParamType;
import t.me.p1azmer.engine.actions.targets.ITargetSelector;
import t.me.p1azmer.engine.actions.targets.ITargetType;

import java.util.HashSet;
import java.util.Set;

public class Target_FromSight extends ITargetSelector {

    public Target_FromSight(@NotNull NexPlugin<?> plugin) {
        super(plugin, ITargetType.FROM_SIGHT);
    }


    @Override
    public void registerParams() {
        this.registerParam(IParamType.ALLOW_SELF);
        this.registerParam(IParamType.ATTACKABLE);
        this.registerParam(IParamType.DISTANCE);
    }

    @Override
    protected void validateTarget(Entity exe, Set<Entity> targets, IParamResult result) {
        double dist = -1;
        if (result.hasParam(IParamType.DISTANCE)) {
            dist = result.getParamValue(IParamType.DISTANCE).getDouble(0);
        } else return;

        if (dist <= 0) return;

        Location start = exe.getLocation();
        if (exe instanceof LivingEntity) {
            start = ((LivingEntity) exe).getEyeLocation();
        }

        Vector      increase   = start.getDirection();
        Set<Entity> disTargets = new HashSet<>();

        for (int counter = 0; counter < dist; counter++) {
            Location point = start.add(increase);

            Material wall = point.getBlock().getType();
            if (wall != Material.AIR && wall.isSolid()) {
                break;
            }

            disTargets.addAll(exe.getWorld().getNearbyEntities(point, 1.25, 1.25, 1.25));
        }

        targets.addAll(disTargets); // Add all targets from this selector
    }

}
