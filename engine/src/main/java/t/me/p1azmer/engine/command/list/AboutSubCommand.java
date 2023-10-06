package t.me.p1azmer.engine.command.list;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.command.AbstractCommand;
import t.me.p1azmer.engine.api.command.CommandResult;
import t.me.p1azmer.engine.lang.EngineLang;
import t.me.p1azmer.engine.utils.Colorizer;
import t.me.p1azmer.engine.utils.EngineUtils;

import java.util.Arrays;
import java.util.List;

import static t.me.p1azmer.engine.utils.Colors.*;

public class AboutSubCommand<P extends NexPlugin<P>> extends AbstractCommand<P> {

    public AboutSubCommand(@NotNull P plugin) {
        super(plugin, new String[]{"about"});
        this.setDescription(plugin.getMessage(EngineLang.COMMAND_ABOUT_DESC));
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        List<String> info = Colorizer.apply(Arrays.asList(
                GRAY,
                YELLOW + ChatColor.BOLD + plugin.getName() + ORANGE + " v" + plugin.getDescription().getVersion(),
                GRAY + (plugin.getDescription().getDescription() == null || plugin.getDescription().getDescription().isEmpty() ? "Description to come later" : plugin.getDescription().getDescription()),
                GRAY,
                YELLOW + "\u25aa " + GRAY + "API Version: " + YELLOW + plugin.getDescription().getAPIVersion(),
                YELLOW + "\u25aa " + GRAY + "Made by " + YELLOW + (plugin.getDescription().getAuthors().isEmpty() ? "plazmer" : plugin.getDescription().getAuthors().get(0)),
                YELLOW + "\u25aa " + GRAY + "Powered by " + YELLOW + EngineUtils.ENGINE.getName(),
                GRAY,
                CYAN + ChatColor.UNDERLINE + "made in YAMAL" + CYAN + " © 2022-2023",
                GRAY));

        info.forEach(sender::sendMessage);
    }
}