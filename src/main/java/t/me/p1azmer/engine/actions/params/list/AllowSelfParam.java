package t.me.p1azmer.engine.actions.params.list;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.actions.params.IAutoValidated;
import t.me.p1azmer.engine.actions.params.IParamType;
import t.me.p1azmer.engine.actions.params.IParamValue;
import t.me.p1azmer.engine.actions.params.defaults.IParamBoolean;

import java.util.Set;

public class AllowSelfParam extends IParamBoolean implements IAutoValidated {

    public AllowSelfParam() {
        super(IParamType.ALLOW_SELF, "allow-self");
    }

    @Override
    public void autoValidate(@NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamValue val) {
        boolean b = val.getBoolean();
        if (!b) {
            targets.remove(exe);
        } else {
            targets.add(exe);
        }
    }
}
