package t.me.p1azmer.engine.actions.params;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class IParamResult {

    private              Map<String, IParamValue> values;
    private static final IParamValue              EMPTY_PARAM = new IParamValue();

    public IParamResult(@NotNull Map<String, IParamValue> values) {
        this.values = values;
    }

    @NotNull
    public IParamValue getParamValue(@NotNull String key) {
        return this.values.getOrDefault(key.toUpperCase(), EMPTY_PARAM);
    }

    public boolean hasParam(@NotNull String key) {
        return this.values.containsKey(key.toUpperCase());
    }
}
