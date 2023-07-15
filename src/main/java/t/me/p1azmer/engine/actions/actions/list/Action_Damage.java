package t.me.p1azmer.engine.actions.actions.list;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.actions.AbstractActionExecutor;
import t.me.p1azmer.engine.actions.actions.ActionType;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.params.IParamType;

import java.util.Set;

public class Action_Damage extends AbstractActionExecutor {

    public Action_Damage(@NotNull NexPlugin<?> plugin) {
        super(plugin, ActionType.DAMAGE);
    }


    @Override
    public void registerParams() {
        this.registerParam(IParamType.TARGET);
        this.registerParam(IParamType.AMOUNT);
    }

    @Override
    protected void execute(@NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamResult result) {
        double dmg = result.getParamValue(IParamType.AMOUNT).getDouble(0);
        if (dmg == 0) return;

        Entity eDamager = exe;

        Projectile pj = null;

        if (eDamager instanceof Projectile) {
            pj = (Projectile) eDamager;
            ProjectileSource src = pj.getShooter();
            if (src instanceof LivingEntity) {
                eDamager = (Entity) src;
            }
        }

        for (Entity eTarget : targets) {
            if (!(eTarget instanceof LivingEntity)) continue;
            LivingEntity victim = (LivingEntity) eTarget;

            victim.damage(dmg, eDamager);
        }
    }

    @Override
    public boolean mustHaveTarget() {
        return true;
    }

}
