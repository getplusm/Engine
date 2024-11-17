package t.me.p1azmer.engine.actions.conditions.list;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.Version;
import t.me.p1azmer.engine.actions.conditions.IConditionType;
import t.me.p1azmer.engine.actions.conditions.IConditionValidator;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.params.IParamType;
import t.me.p1azmer.engine.actions.params.IParamValue;
import t.me.p1azmer.engine.utils.EntityUtil;

import java.util.Set;
import java.util.function.Predicate;

public class Condition_EntityHealth extends IConditionValidator {

    public Condition_EntityHealth(@NotNull NexPlugin<?> plugin) {
        super(plugin, IConditionType.ENTITY_HEALTH);
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
    @Nullable
    protected Predicate<Entity> validate(
            @NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamResult result) {

        IParamValue valHp = result.getParamValue(IParamType.AMOUNT);
        if (!valHp.hasDouble()) return null;

        double hpReq = valHp.getDouble(-1);
        boolean isPercent = valHp.getBoolean();
        IParamValue.IOperator operator = valHp.getOperator();

        return target -> {
            if (!(target instanceof LivingEntity livingEntity)) return false;

            Attribute healthAttribute = Attribute.valueOf("GENERIC_MAX_HEALTH");
            if (Version.isAtLeast(Version.MC_1_21_3)) {
                healthAttribute = Attribute.MAX_HEALTH;
            }
            double hpTarget = livingEntity.getHealth();
            double hpTargetMax = EntityUtil.getAttribute(livingEntity, healthAttribute);

            if (isPercent) {
                hpTarget = hpTarget / hpTargetMax * 100D;
            }

            return operator.check(hpTarget, hpReq);
        };
    }

}
