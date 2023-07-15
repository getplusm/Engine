package t.me.p1azmer.engine.actions.params.parser;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.actions.params.IParamValue;
import t.me.p1azmer.engine.utils.StringUtil;

public class ParamNumberParser implements IParamParser {

    @Override
    @NotNull
    public IParamValue parseValue(@NotNull String str) {
        boolean               perc = str.contains("%");
        IParamValue.IOperator oper = IParamValue.IOperator.parse(str);

        str = IParamValue.IOperator.clean(str);
        double amount = StringUtil.getDouble(str.replace("%", ""), 0D, true);

        IParamValue val = new IParamValue((int) amount);
        val.setBoolean(perc);
        val.setDouble(amount);
        val.setOperator(oper);
        if (perc) val.setRaw(val.getRaw() + "%");

        return val;
    }
}
