package t.me.p1azmer.engine.command.list;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.command.AbstractCommand;
import t.me.p1azmer.engine.api.command.CommandResult;
import t.me.p1azmer.engine.api.lang.LangColors;
import t.me.p1azmer.engine.lang.EngineLang;
import t.me.p1azmer.engine.utils.Colorizer;
import t.me.p1azmer.engine.utils.EngineUtils;

import java.util.Arrays;
import java.util.List;

public class AboutSubCommand<P extends NexPlugin<P>> extends AbstractCommand<P> {

    public AboutSubCommand(@NotNull P plugin) {
        super(plugin, new String[]{"about"});
        this.setDescription(plugin.getMessage(EngineLang.COMMAND_ABOUT_DESC));
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        List<String> info = Colorizer.apply(Arrays.asList(
                LangColors.GRAY,
                LangColors.YELLOW + ChatColor.BOLD + plugin.getName() + LangColors.ORANGE + " v" + plugin.getDescription().getVersion(),
                LangColors.GRAY + (plugin.getDescription().getDescription() == null || plugin.getDescription().getDescription().isEmpty() ? "Description to come later" : plugin.getDescription().getDescription()),
                LangColors.GRAY,
                LangColors.YELLOW + "\u25aa " + LangColors.GRAY + "API Version: " + LangColors.YELLOW + plugin.getDescription().getAPIVersion(),
                LangColors.YELLOW + "\u25aa " + LangColors.GRAY + "Made by " + LangColors.YELLOW + (plugin.getDescription().getAuthors().isEmpty() ? "plazmer" : plugin.getDescription().getAuthors().get(0)),
                LangColors.YELLOW + "\u25aa " + LangColors.GRAY + "Powered by " + LangColors.YELLOW + EngineUtils.ENGINE.getName(),
                LangColors.GRAY,
                LangColors.CYAN + ChatColor.UNDERLINE + "made in YAMAL" + LangColors.CYAN + " © 2022-2023",
                LangColors.GRAY));

        info.forEach(sender::sendMessage);
    }
}