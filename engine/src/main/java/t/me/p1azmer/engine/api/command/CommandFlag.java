package t.me.p1azmer.engine.api.command;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.utils.Colorizer;
import t.me.p1azmer.engine.utils.NumberUtil;
import t.me.p1azmer.engine.utils.StringUtil;

import java.util.function.Function;
import java.util.regex.Pattern;

public class CommandFlag<T> {

    private final String name;
    private final Pattern pattern;
    private final Function<String, T> parser;

    public CommandFlag(@NotNull String name, @NotNull Function<String, T> parser) {
        this.name = name;
        this.pattern = Pattern.compile("-" + name + "(\\s|$)([^-]*)");
        this.parser = parser;
    }

    @NotNull
    public static CommandFlag<World> worldFlag(@NotNull String name) {
        return new CommandFlag<>(name, Bukkit::getWorld);
    }

    @NotNull
    public static CommandFlag<String> stringFlag(@NotNull String name) {
        return new CommandFlag<>(name, Function.identity());
    }

    @NotNull
    public static CommandFlag<String> textFlag(@NotNull String name) {
        return new CommandFlag<>(name, Colorizer::apply);
    }

    @NotNull
    public static CommandFlag<Integer> intFlag(@NotNull String name) {
        return new CommandFlag<>(name, str -> NumberUtil.getAnyInteger(str, 0));
    }

    @NotNull
    public static CommandFlag<Double> doubleFlag(@NotNull String name) {
        return new CommandFlag<>(name, str -> NumberUtil.getAnyDouble(str, 0));
    }

    @NotNull
    public static CommandFlag<Boolean> booleanFlag(@NotNull String name) {
        return new CommandFlag<>(name, str -> true);
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getNamePrefixed() {
        return "-" + name;
    }

    @NotNull
    public Pattern getPattern() {
        return pattern;
    }

    @NotNull
    public Function<String, T> getParser() {
        return parser;
    }
}