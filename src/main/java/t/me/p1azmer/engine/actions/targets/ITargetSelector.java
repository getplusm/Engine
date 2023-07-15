package t.me.p1azmer.engine.actions.targets;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.Parametized;
import t.me.p1azmer.engine.actions.params.IAutoValidated;
import t.me.p1azmer.engine.actions.params.IParam;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.params.IParamType;

import java.util.Set;

public abstract class ITargetSelector extends Parametized {

    public ITargetSelector(@NotNull NexPlugin<?> plugin, @NotNull String key) {
        super(plugin, key);
        this.registerParam(IParamType.NAME);
    }

    public final void select(@NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull String fullStr) {
        IParamResult result = this.getParamResult(fullStr);

        this.validateTarget(exe, targets, result);
        this.autoValidate(exe, targets, result);
    }

    protected abstract void validateTarget(
            @NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamResult result);

    private final void autoValidate(
            @NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamResult result) {

        Entity executor = exe;
        if (exe instanceof Projectile) {
            Projectile       pj = (Projectile) exe;
            ProjectileSource ps = pj.getShooter();
            if (ps instanceof Entity) {
                executor = (Entity) ps;
            }
        }

        for (IParam param : this.getParams()) {
            if (!result.hasParam(param.getKey())) continue;
            if (!(param instanceof IAutoValidated)) continue;

            IAutoValidated auto = (IAutoValidated) param;
            auto.autoValidate(executor, targets, result.getParamValue(param.getKey()));
        }
    }
}
