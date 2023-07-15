package t.me.p1azmer.engine.actions.params.list;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.actions.params.IAutoValidated;
import t.me.p1azmer.engine.actions.params.IParamType;
import t.me.p1azmer.engine.actions.params.IParamValue;
import t.me.p1azmer.engine.actions.params.defaults.IParamBoolean;
import t.me.p1azmer.engine.integration.Hooks;

import java.util.Set;

public class AttackableParam extends IParamBoolean implements IAutoValidated {

    public AttackableParam() {
        super(IParamType.ATTACKABLE, "attackable");
    }

    @Override
    public void autoValidate(@NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamValue val) {
        boolean b = val.getBoolean();
        targets.removeIf(target -> {
            boolean attackable = Hooks.canFights(exe, target);
            return attackable != b;
        });
    }
}
