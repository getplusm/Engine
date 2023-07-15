package t.me.p1azmer.engine.actions.targets.list;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.targets.ITargetSelector;
import t.me.p1azmer.engine.actions.targets.ITargetType;

import java.util.HashSet;
import java.util.Set;

public class Target_Self extends ITargetSelector {

    public Target_Self(@NotNull NexPlugin<?> plugin) {
        super(plugin, ITargetType.SELF);
    }

    @Override
    public void registerParams() {

    }

    @Override
    protected void validateTarget(Entity exe, Set<Entity> targets, IParamResult result) {
        Set<Entity> disTargets = new HashSet<>();
        disTargets.add(exe);

        targets.addAll(disTargets); // Add all targets from this selector
    }
}
