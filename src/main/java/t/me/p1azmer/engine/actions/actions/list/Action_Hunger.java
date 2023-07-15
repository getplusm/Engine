package t.me.p1azmer.engine.actions.actions.list;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.actions.AbstractActionExecutor;
import t.me.p1azmer.engine.actions.actions.ActionType;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.params.IParamType;
import t.me.p1azmer.engine.actions.params.IParamValue;

import java.util.Set;

public class Action_Hunger extends AbstractActionExecutor {

    public Action_Hunger(@NotNull NexPlugin<?> plugin) {
        super(plugin, ActionType.HUNGER);
    }


    @Override
    public void registerParams() {
        this.registerParam(IParamType.TARGET);
        this.registerParam(IParamType.AMOUNT);
    }

    @Override
    public boolean mustHaveTarget() {
        return true;
    }

    @Override
    protected void execute(@NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamResult result) {
        IParamValue value = result.getParamValue(IParamType.AMOUNT);

        double amount = value.getDouble(0);
        if (amount == 0) return;

        boolean percent = value.getBoolean();

        targets.forEach(target -> {
            if (!(target instanceof Player)) return;

            Player livingEntity = (Player) target;
            double amount2      = amount;
            double max          = 20;
            if (percent) {
                amount2 = max * (amount / 100D);
            }

            livingEntity.setFoodLevel((int) Math.min(livingEntity.getFoodLevel() + amount2, max));
        });
    }
}
