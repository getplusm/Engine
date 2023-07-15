package t.me.p1azmer.engine.actions.params.parser;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.actions.params.IParamValue;
import t.me.p1azmer.engine.utils.Colorizer;

public class ParamStringParser implements IParamParser {

    @Override
    @NotNull
    public IParamValue parseValue(@NotNull String str) {
        return new IParamValue(Colorizer.apply(str));
    }
}
