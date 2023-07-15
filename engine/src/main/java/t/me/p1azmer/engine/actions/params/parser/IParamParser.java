package t.me.p1azmer.engine.actions.params.parser;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.actions.params.IParamValue;

public interface IParamParser {

    public static final IParamParser BOOLEAN = new ParamBooleanParser();
    public static final IParamParser NUMBER  = new ParamNumberParser();
    public static final IParamParser STRING  = new ParamStringParser();

    @NotNull
    public IParamValue parseValue(@NotNull String str);
}
