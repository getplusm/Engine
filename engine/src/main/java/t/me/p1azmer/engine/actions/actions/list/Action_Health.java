package t.me.p1azmer.engine.actions.actions.list;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.actions.AbstractActionExecutor;
import t.me.p1azmer.engine.actions.actions.ActionType;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.params.IParamType;
import t.me.p1azmer.engine.actions.params.IParamValue;
import t.me.p1azmer.engine.utils.EntityUtil;

import java.util.Set;

public class Action_Health extends AbstractActionExecutor {

    public Action_Health(@NotNull NexPlugin<?> plugin) {
        super(plugin, ActionType.HEALTH);
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

        double hp = value.getDouble(0);
        if (hp == 0) return;

        boolean percent = value.getBoolean();

        targets.forEach(target -> {
            if (!(target instanceof LivingEntity)) return;

            LivingEntity livingEntity = (LivingEntity) target;
            double       hp2          = hp;
            double       maxHp        = EntityUtil.getAttribute(livingEntity, Attribute.GENERIC_MAX_HEALTH);
            if (percent) {
                hp2 = maxHp * (hp / 100D);
            }

            livingEntity.setHealth(Math.min(livingEntity.getHealth() + hp2, maxHp));
        });
    }
}
