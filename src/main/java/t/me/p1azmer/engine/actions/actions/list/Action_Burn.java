package t.me.p1azmer.engine.actions.actions.list;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.actions.AbstractActionExecutor;
import t.me.p1azmer.engine.actions.actions.ActionType;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.params.IParamType;

import java.util.Set;

public class Action_Burn extends AbstractActionExecutor {

    public Action_Burn(@NotNull NexPlugin<?> plugin) {
        super(plugin, ActionType.BURN);
    }


    @Override
    public void registerParams() {
        this.registerParam(IParamType.TARGET);
        this.registerParam(IParamType.DURATION);
    }

    @Override
    protected void execute(@NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamResult result) {
        int dura = 0;
        if (result.hasParam(IParamType.DURATION)) {
            dura = result.getParamValue(IParamType.DURATION).getInt(0);
        }
        if (dura <= 0) return;

        for (Entity e : targets) {
            e.setFireTicks(e.getFireTicks() + dura);
        }
    }

    @Override
    public boolean mustHaveTarget() {
        return true;
    }

}
