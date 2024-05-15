package t.me.p1azmer.engine.actions.params.list;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.actions.params.IParam;
import t.me.p1azmer.engine.actions.params.IParamType;
import t.me.p1azmer.engine.actions.params.IParamValue;
import t.me.p1azmer.engine.actions.params.parser.IParamParser;
import t.me.p1azmer.engine.utils.NumberUtil;
import t.me.p1azmer.engine.utils.StringUtil;

public class OffsetParam extends IParam {

    private final IParamParser parser;

    public OffsetParam() {
        super(IParamType.OFFSET, "offset");

        this.parser = (str) -> {
            String[] split = str.replace(" ", "").split(",");

            double x = 0;
            x = NumberUtil.getAnyDouble(split[0], 0);

            double y = 0;
            if (split.length >= 2) {
                y = NumberUtil.getAnyDouble(split[1], 0);
            }

            double z = 0;
            if (split.length == 3) {
                z = NumberUtil.getAnyDouble(split[2], 0);
            }

            double[] arr = new double[]{x, y, z};

            return new IParamValue(arr);
        };
    }

    @Override
    @NotNull
    public IParamParser getParser() {
        return this.parser;
    }
}
