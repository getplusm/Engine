package t.me.p1azmer.engine.command.list;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.command.AbstractCommand;
import t.me.p1azmer.engine.api.command.CommandResult;
import t.me.p1azmer.engine.lang.EngineLang;

public class ReloadSubCommand<P extends NexPlugin<P>> extends AbstractCommand<P> {

    public ReloadSubCommand(@NotNull P plugin, @NotNull Permission permission) {
        this(plugin, permission.getName());
    }

    public ReloadSubCommand(@NotNull P plugin, @NotNull String permission) {
        super(plugin, new String[]{"reload"}, permission);
        this.setDescription(plugin.getMessage(EngineLang.COMMAND_RELOAD_DESC));
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        plugin.reload();
        plugin.getMessage(EngineLang.COMMAND_RELOAD_DONE).send(sender);
    }
}