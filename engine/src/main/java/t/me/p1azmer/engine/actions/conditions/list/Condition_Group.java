package t.me.p1azmer.engine.actions.conditions.list;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.conditions.IConditionType;
import t.me.p1azmer.engine.actions.conditions.IConditionValidator;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.params.IParamType;
import t.me.p1azmer.engine.integration.external.LuckpermsHook;

import java.util.Set;
import java.util.function.Predicate;

public class Condition_Group extends IConditionValidator {

    public Condition_Group(@NotNull NexPlugin<?> plugin) {
        super(plugin, IConditionType.GROUP);
    }


    @Override
    public void registerParams() {
        this.registerParam(IParamType.TARGET);
        this.registerParam(IParamType.NAME);
    }

    @Override
    public boolean mustHaveTarget() {
        return true;
    }

    @Override
    @Nullable
    protected Predicate<Entity> validate(
            @NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamResult result) {

        String node = result.getParamValue(IParamType.NAME).getString(null);
        if (node == null) return null;

        return target -> LuckpermsHook.getCustomGroup(target.getUniqueId()).getLuckPermsID().contains(node);
    }
}