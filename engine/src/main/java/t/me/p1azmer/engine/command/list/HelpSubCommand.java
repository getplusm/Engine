package t.me.p1azmer.engine.command.list;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.command.AbstractCommand;
import t.me.p1azmer.engine.api.command.CommandResult;
import t.me.p1azmer.engine.lang.EngineLang;
import t.me.p1azmer.engine.utils.Placeholders;


public class HelpSubCommand<P extends NexPlugin<P>> extends AbstractCommand<P> {

    public HelpSubCommand(@NotNull P plugin) {
        super(plugin, new String[]{"help"});
        this.setDescription(plugin.getMessage(EngineLang.COMMAND_HELP_DESC));
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (!this.parent.hasPermission(sender)) {
            this.errorPermission(sender);
            return;
        }

        plugin.getMessage(EngineLang.COMMAND_HELP_LIST)
                .replace(str -> str.contains(Placeholders.COMMAND_LABEL), (line, list) -> {
                    this.parent.getChildrens().forEach(children -> {
                        if (!children.hasPermission(sender)) return;

                        list.add(children.replacePlaceholders().apply(line));
                    });
                }).send(sender);
    }
}