package t.me.p1azmer.engine.actions.actions.list;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.actions.AbstractActionExecutor;
import t.me.p1azmer.engine.actions.actions.ActionType;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.params.IParamType;
import t.me.p1azmer.engine.utils.MessageUtil;

import java.util.Set;

public class Action_Sound extends AbstractActionExecutor {

    public Action_Sound(@NotNull NexPlugin<?> plugin) {
        super(plugin, ActionType.SOUND);
    }

    @Override
    public void registerParams() {
        this.registerParam(IParamType.TARGET);
        this.registerParam(IParamType.NAME);
    }

    @Override
    protected void execute(@NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamResult result) {
        String name = result.getParamValue(IParamType.NAME).getString(null);
        if (name == null) return;

        for (Entity e : targets) {
            if (e.getType() == EntityType.PLAYER) {
                MessageUtil.sound((Player) e, name);
            }
        }
    }

    @Override
    public boolean mustHaveTarget() {
        return true;
    }

}
