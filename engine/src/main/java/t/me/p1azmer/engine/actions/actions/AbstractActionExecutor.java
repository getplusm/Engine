package t.me.p1azmer.engine.actions.actions;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.ActionManipulator;
import t.me.p1azmer.engine.actions.Parametized;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.params.IParamType;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractActionExecutor extends Parametized {

    public AbstractActionExecutor(@NotNull NexPlugin<?> plugin, @NotNull String key) {
        super(plugin, key);
        this.registerParam(IParamType.DELAY);
    }

    public abstract boolean mustHaveTarget();

    protected abstract void execute(@NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamResult result);

    public final void process(@NotNull Entity exe, @NotNull Map<String, Set<Entity>> targetMap,
                              @NotNull String fullStr, @NotNull ActionManipulator manipulator) {

        IParamResult result = this.getParamResult(fullStr);

        if (fullStr.contains(FLAG_NO_DELAY)) {
            fullStr = fullStr.replace(FLAG_NO_DELAY, "");
        } else if (result.hasParam(IParamType.DELAY)) {
            int delay = result.getParamValue(IParamType.DELAY).getInt(0);
            final String fullStr2 = fullStr;

            if (delay > 0) {
                if (NexPlugin.isFolia && plugin.getFoliaScheduler() != null) {
                    plugin.getFoliaScheduler().global().runDelayed(() -> {
                        this.process(exe, targetMap, fullStr2 + FLAG_NO_DELAY, manipulator);
                    }, delay);
                    return;
                }
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> this.process(exe, targetMap, fullStr2 + FLAG_NO_DELAY, manipulator), delay);
                return;
            }
        }

        if (this.mustHaveTarget() && !result.hasParam(IParamType.TARGET)) {
            plugin.warn("No Target specified for action: " + fullStr);
            return;
        }

        Set<Entity> targets = new HashSet<>();
        String[] tsSplit = result.getParamValue(IParamType.TARGET).getString("").split(",");
        for (String ts : tsSplit) {
            if (ts.isEmpty()) continue;
            ts = ts.toLowerCase();
            if (targetMap.containsKey(ts)) {
                targets.addAll(targetMap.get(ts));
            } else {
                plugin.warn("Invalid Target specified for action: " + fullStr);
            }
        }

        if (this.mustHaveTarget() && targets.isEmpty()) {
            return;
        }

        if (this.getKey().equalsIgnoreCase(ActionType.GOTO)) {
            String id = result.getParamValue(IParamType.NAME).getString(null);
            if (id == null) return;

            manipulator.process(exe, id);
        } else {
            this.execute(exe, targets, result);
        }
    }
}
