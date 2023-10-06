package t.me.p1azmer.engine.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.command.CommandResult;
import t.me.p1azmer.engine.api.command.GeneralCommand;

public class PluginMainCommand<P extends NexPlugin<P>> extends GeneralCommand<P> {

    public PluginMainCommand(@NotNull P plugin) {
        super(plugin, plugin.getLabels());
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {}
}