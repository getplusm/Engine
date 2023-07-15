package t.me.p1azmer.engine.actions.actions.list;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.actions.AbstractActionExecutor;
import t.me.p1azmer.engine.actions.actions.ActionType;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.params.IParamType;

import java.util.Set;

public class Action_CommandConsole extends AbstractActionExecutor {

    public Action_CommandConsole(@NotNull NexPlugin<?> plugin) {
        super(plugin, ActionType.COMMAND_CONSOLE);
    }



    @Override
    public void registerParams() {
        this.registerParam(IParamType.MESSAGE);
        this.registerParam(IParamType.TARGET);
    }

    @Override
    protected void execute(@NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamResult result) {
        if (!result.hasParam(IParamType.MESSAGE)) return;

        String text = result.getParamValue(IParamType.MESSAGE).getString(null);
        if (text == null) return;

        text = text.replace("%executor%", exe.getName());

        if (!targets.isEmpty()) {
            for (Entity e : targets) {
                String text2 = text.replace("%target%", e.getName());
                plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), text2);
            }
        } else {
            plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), text);
        }
    }

    @Override
    public boolean mustHaveTarget() {
        return false;
    }
}
