package t.me.p1azmer.engine.actions.conditions;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.ActionManipulator;
import t.me.p1azmer.engine.actions.Parametized;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.params.IParamType;
import t.me.p1azmer.engine.utils.Constants;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public abstract class IConditionValidator extends Parametized {

    public IConditionValidator(@NotNull NexPlugin<?> plugin, @NotNull String key) {
        super(plugin, key);
        this.registerParam(IParamType.MESSAGE);
        this.registerParam(IParamType.FILTER);
    }

    public abstract boolean mustHaveTarget();

    @Nullable
    protected abstract Predicate<Entity> validate(
            @NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamResult result);


    public final boolean process(
            @NotNull Entity exe,
            @NotNull Map<String, Set<Entity>> targetMap,
            @NotNull String fullStr) {
        return this.process(exe, targetMap, fullStr, null);
    }

    public final boolean process(
            @NotNull Entity exe,
            @NotNull Map<String, Set<Entity>> targetMap,
            @NotNull String fullStr,
            @Nullable ActionManipulator manipulator) {

        IParamResult result = this.getParamResult(fullStr);

        // Check for empty map because it may contain default executor
        // in some cases
        if (this.mustHaveTarget() && !result.hasParam(IParamType.TARGET) && targetMap.isEmpty()) {
            plugin.warn("No Target specified for condition: " + fullStr);
            return false;
        }

        Set<Entity> targets = new HashSet<>();
        String[] tsSplit = result.getParamValue(IParamType.TARGET).getString("").split(",");
        for (String ts : tsSplit) {
            if (ts.isEmpty()) continue;
            ts = ts.toLowerCase();
            if (targetMap.containsKey(ts)) {
                targets.addAll(targetMap.get(ts));
            } else {
                plugin.warn("Invalid Target specified for condition: " + fullStr);
            }
        }

        // Fine target entities.
        if (targets.isEmpty()) {
            // If no target was provided, but plugin has support for default target,
            // then we add them.
            if (targetMap.containsKey(Constants.DEFAULT)) {
                targets.addAll(targetMap.get(Constants.DEFAULT));
            }
            // If there is no default target and the condition must have target,
            // then action should be interrupted.
            else if (this.mustHaveTarget()) {
                return false;
            }
            // If target param is not must have, then we add executor to be able to
            // test condition predicates.
            else {
                targets.add(exe);
            }
        }

        boolean isFilter = result.getParamValue(IParamType.FILTER).getBoolean();
        Predicate<Entity> predicate = this.validate(exe, targets, result);
        if (predicate == null) {
            plugin.error("Could not validate condition: '" + fullStr + "'. Skipping...");
            return true;
        }

        String msg = result.getParamValue(IParamType.MESSAGE).getString(null);
        if (isFilter) {
            targets.removeIf(target -> {
                if (!predicate.test(target)) {
                    if (msg != null) target.sendMessage(msg);
                    return true;
                }
                return false;
            });
        } else {
            if (!targets.stream().allMatch(predicate)) {
                if (msg != null) targets.forEach(target -> target.sendMessage(msg));
                return false;
            }
        }

        // Remove filtered targets from the original target map
        // to avoid them being applied on actions.
        for (String targetId : tsSplit) {
            targetId = targetId.toLowerCase();
            if (targetMap.containsKey(targetId)) {
                targetMap.get(targetId).removeIf(target2 -> !targets.contains(target2));
            }
        }

        //targetMap.values().removeIf(Set::isEmpty);
        return true;
    }
}
