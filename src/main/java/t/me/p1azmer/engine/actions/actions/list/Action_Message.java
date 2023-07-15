package t.me.p1azmer.engine.actions.actions.list;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.actions.AbstractActionExecutor;
import t.me.p1azmer.engine.actions.actions.ActionType;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.params.IParamType;
import t.me.p1azmer.engine.utils.PlayerUtil;

import java.util.Set;

public class Action_Message extends AbstractActionExecutor {

    public Action_Message(@NotNull NexPlugin<?> plugin) {
        super(plugin, ActionType.MESSAGE);
    }


    @Override
    public void registerParams() {
        this.registerParam(IParamType.TARGET);
        this.registerParam(IParamType.MESSAGE);
    }

    @Override
    protected void execute(@NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamResult result) {
        if (!result.hasParam(IParamType.MESSAGE)) return;

        String text = result.getParamValue(IParamType.MESSAGE).getString(null);
        if (text == null) return;

        text = text.replace("%executor%", exe.getName());

        for (Entity e : targets) {
            PlayerUtil.sendRichMessage(e, text.replace("%target%", e.getName()));
        }
    }

    @Override
    public boolean mustHaveTarget() {
        return true;
    }

}
