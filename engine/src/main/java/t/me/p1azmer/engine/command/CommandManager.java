package t.me.p1azmer.engine.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.command.GeneralCommand;
import t.me.p1azmer.engine.api.manager.AbstractManager;
import t.me.p1azmer.engine.command.list.AboutSubCommand;
import t.me.p1azmer.engine.command.list.HelpSubCommand;
import t.me.p1azmer.engine.utils.ArrayUtil;

import java.util.HashSet;
import java.util.Set;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommandManager<P extends NexPlugin<P>> extends AbstractManager<P> {

    final Set<GeneralCommand<P>> commands = new HashSet<>();
    PluginMainCommand<P> mainCommand;

    public CommandManager(@NotNull P plugin) {
        super(plugin);
    }

    @Override
    public void onLoad() {
        if (plugin.getConfigManager().commandAliases == null || plugin.getConfigManager().commandAliases.length == 0) {
            plugin.error("Could not register plugin commands!");
            return;
        }

        // Create main plugin command and attach help sub-command as a default executor.
        mainCommand = new PluginMainCommand<>(plugin);
        mainCommand.addDefaultCommand(new HelpSubCommand<>(plugin));

        // Register child plugin sub-commands to the main plugin command.
        plugin.registerCommands(mainCommand);

        if (!plugin.isEngine()) {
            mainCommand.addChildren(new AboutSubCommand<>(plugin));
        }

        // Register main command as a bukkit command.
        registerCommand(mainCommand);
    }

    @Override
    public void onShutdown() {
        for (GeneralCommand<P> command : new HashSet<>(commands)) {
            unregisterCommand(command);
            command.getChildrens().clear();
        }
        commands.clear();
    }

    @Nullable
    public GeneralCommand<P> getCommand(@NotNull String alias) {
        return getCommands().stream()
                .filter(command -> ArrayUtil.contains(command.getAliases(), alias))
                .findFirst().orElse(null);
    }

    public void registerCommand(@NotNull GeneralCommand<P> command) {
        if (commands.add(command)) {
            CommandRegister.register(plugin, command);
        }
    }

    public boolean unregisterCommand(@NotNull String alias) {
        GeneralCommand<P> command = getCommand(alias);
        if (command != null) {
            return unregisterCommand(command);
        }
        return false;
    }

    public boolean unregisterCommand(@NotNull GeneralCommand<P> command) {
        if (commands.remove(command)) {
            return CommandRegister.unregister(command.getAliases()[0]);
        }
        return false;
    }
}
