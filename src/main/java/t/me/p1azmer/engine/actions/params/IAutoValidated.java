package t.me.p1azmer.engine.actions.params;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface IAutoValidated {

    public void autoValidate(@NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamValue val);
}
