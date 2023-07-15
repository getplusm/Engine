package t.me.p1azmer.engine.actions.params;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.actions.params.parser.IParamParser;

import java.util.regex.Pattern;

public abstract class IParam {

    protected final String  key;
    protected final String  flag;
    protected final Pattern pattern;

    public IParam(@NotNull String key, @NotNull String flag) {
        this.key = key.toUpperCase();
        this.flag = flag.toLowerCase();
        this.pattern = Pattern.compile("(~)+(" + this.getFlag() + ")+?(:)+(.*?)(;)");
    }

    @NotNull
    public final String getKey() {
        return this.key;
    }

    @NotNull
    public final Pattern getPattern() {
        return this.pattern;
    }

    @NotNull
    public final String getFlag() {
        return this.flag;
    }

    @NotNull
    public abstract IParamParser getParser();
}
