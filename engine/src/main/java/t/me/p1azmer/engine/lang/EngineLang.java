package t.me.p1azmer.engine.lang;

import org.bukkit.ChatColor;
import t.me.p1azmer.engine.api.lang.LangKey;
import t.me.p1azmer.engine.editor.EditorManager;
import t.me.p1azmer.engine.utils.Placeholders;

import static t.me.p1azmer.engine.utils.Colors.*;
import static t.me.p1azmer.engine.utils.Placeholders.*;

public class EngineLang {

    public static final String NO_PREFIX = "<! prefix:\"false\" !>";
    public static final LangKey COMMAND_USAGE = LangKey.of("Command.Usage",
            "<! prefix:\"false\" !>" +
                    "\n" + GRAY +
                    "\n" + RED + "Error: " + GRAY + "Wrong arguments!" +
                    "\n" + RED + "Usage: " + YELLOW + "/" + COMMAND_LABEL + " " + ORANGE + Placeholders.COMMAND_USAGE +
                    "\n" + GRAY);

    public static final LangKey COMMAND_HELP_LIST = LangKey.of("Command.Help.List",
            "<! prefix:\"false\" !>" +
                    "\n" + GRAY +
                    "\n" + "  " + YELLOW + BOLD + PLUGIN_NAME_LOCALIZED + GRAY + " - " + YELLOW + BOLD + "Commands:" +
                    "\n" + GRAY +
                    "\n" + "  " + RED + BOLD + "<> " + GRAY + "- Required, " + GREEN + BOLD + "[] " + GRAY + "- Optional." +
                    "\n" + GRAY +
                    "\n" + "  " + YELLOW + "/" + COMMAND_LABEL + " " + ORANGE + Placeholders.COMMAND_USAGE + " " + GRAY + "- " + COMMAND_DESCRIPTION +
                    "\n" + GRAY);
    public static final LangKey COMMAND_HELP_DESC = LangKey.of("Command.Help.Desc", "Show help page.");
    public static final LangKey COMMAND_CHECKPERM_DESC = LangKey.of("Command.CheckPerm.Desc", "Print player permission info.");
    public static final LangKey COMMAND_CHECKPERM_USAGE = LangKey.of("Command.CheckPerm.Usage", "<player>");
    public static final LangKey COMMAND_ABOUT_DESC = LangKey.of("Command.About.Desc", "Some info about the plugin.");
    public static final LangKey COMMAND_RELOAD_DESC = LangKey.of("Command.Reload.Desc", "Reload the whole plugin.");
    public static final LangKey COMMAND_RELOAD_DONE = LangKey.of("Command.Reload.Done", "All data & configuration has been reloaded!");

    public static final LangKey TIME_DAY = new LangKey("Time.Day", "%s%d.");
    public static final LangKey TIME_HOUR = new LangKey("Time.Hour", "%s%h.");
    public static final LangKey TIME_MIN = new LangKey("Time.Min", "%s%min.");
    public static final LangKey TIME_SEC = new LangKey("Time.Sec", "%s%sec.");

    public static final LangKey OTHER_YES = LangKey.of("Other.Yes", GREEN + "Yes");
    public static final LangKey OTHER_NO = LangKey.of("Other.No", RED + "No");
    public static final LangKey OTHER_ANY = LangKey.of("Other.Any", "Any");
    public static final LangKey OTHER_NONE = LangKey.of("Other.None", "None");
    public static final LangKey OTHER_NEVER = LangKey.of("Other.Never", "Never");
    public static final LangKey OTHER_ONE_TIMED = LangKey.of("Other.OneTimed", "One-Timed");
    public static final LangKey OTHER_UNLIMITED = LangKey.of("Other.Unlimited", "Unlimited");
    public static final LangKey OTHER_INFINITY = LangKey.of("Other.Infinity", "\u221e");

    public static final LangKey ERROR_PLAYER_INVALID = LangKey.of("Error.Player.Invalid", RED + "Player not found.");
    public static final LangKey ERROR_WORLD_INVALID = LangKey.of("Error.World.Invalid", RED + "World not found.");
    public static final LangKey ERROR_NUMBER_INVALID = LangKey.of("Error.Number.Invalid", RED + "%num% is not a valid number.");
    public static final LangKey ERROR_TIME_FORMAT_INVALID = LangKey.of("Error.Time.Format.Invalid", RED + "%time% is not a valid time format.");
    public static final LangKey ERROR_PERMISSION_DENY = LangKey.of("Error.Permission.Deny", RED + "You don't have permissions to do that!");
    public static final LangKey ERROR_COMMAND_SELF = LangKey.of("Error.Command.Self", RED + "This command can not be used on yourself.");
    public static final LangKey ERROR_COMMAND_SENDER = LangKey.of("Error.Command.Sender", RED + "This command is for players only.");

    public static final LangKey ERROR_INVENTORY_IS_FULL = LangKey.of("Error.Player.Inventory.Full", RED + "Your Inventory &lis full" + RED + "!");
    public static final LangKey ERROR_ITEMS_DROP_UNDER_YOU = LangKey.of("Error.Player.Item.Drop.Under.You", RED + "Items &ldrop to the ground" + RED + "!");

    public static final LangKey EDITOR_TIP_EXIT = LangKey.of("Editor.Tip.Exit",
            "<! prefix:\"false\" !>" +
                    "<? showText:\"" + GRAY + "Click me or type " + RED + EditorManager.EXIT + "\" run_command:\"/" + EditorManager.EXIT + "\" ?>" + GRAY + "Click here to " + RED + "[Exit Edit Mode]</>");
    public static final LangKey EDITOR_TITLE_DONE = LangKey.of("Editor.Title.Done", GREEN + BOLD + "Done!");
    public static final LangKey EDITOR_TITLE_EDIT = LangKey.of("Editor.Title.Edit", GREEN + BOLD + "< Edit Mode >");
    public static final LangKey EDITOR_TITLE_ERROR = LangKey.of("Editor.Title.Error", RED + BOLD + "Error!");
    public static final LangKey EDITOR_ERROR_NUMBER_GENERIC = LangKey.of("Editor.Error.Number.Generic", GRAY + "Invalid number!");
    public static final LangKey EDITOR_ERROR_NUMBER_NOT_INT = LangKey.of("Editor.Error.Number.NotInt", GRAY + "Expecting " + RED + "whole" + GRAY + " number!");
    public static final LangKey EDITOR_ERROR_ENUM = LangKey.of("Editor.Error.Enum", GRAY + "Invalid Input!");
    public static final LangKey Editor_Message_Value_Helper = LangKey.of("Editor.Message.Value_Helper", ORANGE+"[ " + YELLOW + "Value Helper" + ORANGE + " ]");
    public static final LangKey Editor_Message_Click_Selection = LangKey.of("Editor.Message.Click_Selection", GRAY + "Click me to select " + BLUE);
    public static final LangKey Editor_Message_Previous_Page = LangKey.of("Editor.Message.Page.Previous", GRAY + "Previous Page");
    public static final LangKey Editor_Message_Next_Page = LangKey.of("Editor.Message.Page.Next", GRAY + "Next Page");
}