package t.me.p1azmer.engine.actions.params.defaults;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.actions.params.IParam;
import t.me.p1azmer.engine.actions.params.parser.IParamParser;

public class IParamNumber extends IParam {

    public IParamNumber(@NotNull String key, @NotNull String flag) {
        super(key, flag);
    }

    @Override
    @NotNull
    public final IParamParser getParser() {
        return IParamParser.NUMBER;
    }
}
