package t.me.p1azmer.engine.actions.conditions.list;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.conditions.IConditionType;
import t.me.p1azmer.engine.actions.conditions.IConditionValidator;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.params.IParamType;

import java.util.Set;
import java.util.function.Predicate;

public class Condition_EntityType extends IConditionValidator {

    public Condition_EntityType(@NotNull NexPlugin<?> plugin) {
        super(plugin, IConditionType.ENTITY_TYPE);
    }


    @Override
    public void registerParams() {
        this.registerParam(IParamType.NAME);
        this.registerParam(IParamType.TARGET);
    }

    @Override
    public boolean mustHaveTarget() {
        return true;
    }

    @Override
    @Nullable
    protected Predicate<Entity> validate(
            @NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamResult result) {

        String types = result.getParamValue(IParamType.NAME).getString("");
        if (types == null || types.isEmpty()) return null;

        return target -> types.equalsIgnoreCase(target.getType().name());
    }

}
