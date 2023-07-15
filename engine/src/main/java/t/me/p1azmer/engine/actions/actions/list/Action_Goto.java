package t.me.p1azmer.engine.actions.actions.list;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.actions.AbstractActionExecutor;
import t.me.p1azmer.engine.actions.actions.ActionType;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.params.IParamType;

import java.util.Set;

public class Action_Goto extends AbstractActionExecutor {

    public Action_Goto(@NotNull NexPlugin<?> plugin) {
        super(plugin, ActionType.GOTO);
    }


    @Override
    public void registerParams() {
        this.registerParam(IParamType.NAME);
    }

    @Override
    protected void execute(@NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamResult result) {

    }

    @Override
    public boolean mustHaveTarget() {
        return false;
    }
}
