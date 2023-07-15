package t.me.p1azmer.engine.actions.params.parser;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.actions.params.IParamValue;

public class ParamBooleanParser implements IParamParser {

    @Override
    @NotNull
    public IParamValue parseValue(@NotNull String str) {
        boolean b = Boolean.valueOf(str);
        return new IParamValue(b);
    }
}
