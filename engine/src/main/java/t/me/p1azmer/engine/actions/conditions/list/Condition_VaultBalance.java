package t.me.p1azmer.engine.actions.conditions.list;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.conditions.IConditionType;
import t.me.p1azmer.engine.actions.conditions.IConditionValidator;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.params.IParamType;
import t.me.p1azmer.engine.actions.params.IParamValue;
import t.me.p1azmer.engine.integration.Hooks;
import t.me.p1azmer.engine.integration.external.VaultHook;
import t.me.p1azmer.engine.utils.EngineUtils;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Predicate;

public class Condition_VaultBalance extends IConditionValidator {


    public Condition_VaultBalance(@NotNull NexPlugin<?> plugin) {
        super(plugin, IConditionType.VAULT_BALANCE);
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
    @Nullable
    protected Predicate<Entity> validate(
            @NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamResult result) {

        if (!EngineUtils.hasVault()) return null;

        IParamValue val = result.getParamValue(IParamType.AMOUNT);
        double amount = val.getDouble(-1);
        if (amount == -1) return null;

        IParamValue.IOperator oper = val.getOperator();

        return target -> {
            if (target.getType() == EntityType.PLAYER) {
                Player p = (Player) target;
                double balance = VaultHook.getBalance(p);
                return oper.check(balance, amount);
            }
            return false;
        };
    }
}
