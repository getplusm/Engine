package t.me.p1azmer.engine.actions.params.list;

import t.me.p1azmer.engine.actions.params.IParamType;
import t.me.p1azmer.engine.actions.params.defaults.IParamString;

public class LocationParam extends IParamString {

    public LocationParam() {
        super(IParamType.LOCATION, "location");
    }
}
