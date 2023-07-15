package t.me.p1azmer.engine.actions.conditions.list;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.conditions.IConditionType;
import t.me.p1azmer.engine.actions.conditions.IConditionValidator;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.params.IParamType;
import t.me.p1azmer.engine.actions.params.IParamValue;

import java.util.Set;
import java.util.function.Predicate;

public class Condition_WorldTime extends IConditionValidator {

    public Condition_WorldTime(@NotNull NexPlugin<?> plugin) {
        super(plugin, IConditionType.WORLD_TIME);
    }

    @Override
    public void registerParams() {
        this.registerParam(IParamType.TARGET);
        this.registerParam(IParamType.NAME);
        this.registerParam(IParamType.AMOUNT);
    }

    @Override
    @Nullable
    protected Predicate<Entity> validate(
            @NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamResult result) {

        String worldName = result.getParamValue(IParamType.NAME).getString(null);
        World world = worldName != null ? plugin.getServer().getWorld(worldName) : null;

        long timeReq = result.getParamValue(IParamType.AMOUNT).getInt(-1);
        IParamValue.IOperator oper = result.getParamValue(IParamType.AMOUNT).getOperator();

        return target -> {
            long timeWorld = world == null ? target.getWorld().getTime() : world.getTime();
            return oper.check(timeWorld, timeReq);
        };
    }

    @Override
    public boolean mustHaveTarget() {
        return false;
    }
}