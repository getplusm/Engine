package t.me.p1azmer.engine.actions.actions.list;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.actions.AbstractActionExecutor;
import t.me.p1azmer.engine.actions.actions.ActionType;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.params.IParamType;

import java.util.Set;

public class Action_Lightning extends AbstractActionExecutor {

    public Action_Lightning(@NotNull NexPlugin<?> plugin) {
        super(plugin, ActionType.LIGHTNING);
    }


    @Override
    public void registerParams() {
        this.registerParam(IParamType.TARGET);
    }

    @Override
    protected void execute(@NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamResult result) {
        for (Entity e : targets) {
            e.getWorld().strikeLightning(e.getLocation());
        }
    }

    @Override
    public boolean mustHaveTarget() {
        return true;
    }

}
