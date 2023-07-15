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

public class Action_Saturation extends AbstractActionExecutor {

    public Action_Saturation(@NotNull NexPlugin<?> plugin) {
        super(plugin, ActionType.SATURATION);
    }

    @Override
    public void registerParams() {
        this.registerParam(IParamType.AMOUNT);
        this.registerParam(IParamType.TARGET);
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
            double has          = livingEntity.getSaturation();
            if (percent) {
                amount2 = has * (amount / 100D);
            }

            livingEntity.setSaturation((float) (has + amount2));
        });
    }
}
