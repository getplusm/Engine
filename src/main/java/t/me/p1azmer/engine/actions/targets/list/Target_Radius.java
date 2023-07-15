package t.me.p1azmer.engine.actions.targets.list;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.params.IParamType;
import t.me.p1azmer.engine.actions.targets.ITargetSelector;
import t.me.p1azmer.engine.actions.targets.ITargetType;

import java.util.HashSet;
import java.util.Set;

public class Target_Radius extends ITargetSelector {

    public Target_Radius(@NotNull NexPlugin<?> plugin) {
        super(plugin, ITargetType.RADIUS);
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

        Set<Entity> disTargets = new HashSet<>();
        disTargets.addAll(exe.getNearbyEntities(dist, dist, dist));

        targets.addAll(disTargets); // Add all targets from this selector
    }
}
