package t.me.p1azmer.engine.actions.actions.list;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.actions.AbstractActionExecutor;
import t.me.p1azmer.engine.actions.actions.ActionType;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.params.IParamType;
import t.me.p1azmer.engine.utils.values.UniParticle;

import java.util.Set;

public class Action_ParticleSimple extends AbstractActionExecutor {

    public Action_ParticleSimple(@NotNull NexPlugin<?> plugin) {
        super(plugin, ActionType.PARTICLE_SIMPLE);
    }


    @Override
    public void registerParams() {
        this.registerParam(IParamType.NAME);
        this.registerParam(IParamType.TARGET);
        this.registerParam(IParamType.AMOUNT);
        this.registerParam(IParamType.SPEED);
        this.registerParam(IParamType.OFFSET);
    }

    @Override
    protected void execute(@NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamResult result) {
        String name = result.getParamValue(IParamType.NAME).getString(null);
        if (name == null) return;

        double[] offset = result.getParamValue(IParamType.OFFSET).getDoubleArray();

        int amount = result.getParamValue(IParamType.AMOUNT).getInt(30);

        float speed = (float) result.getParamValue(IParamType.SPEED).getDouble(0.1);

        UniParticle uniParticle = UniParticle.of(name);
        if (uniParticle == null) return;

        for (Entity e : targets) {
            Location loc;
            if (e instanceof LivingEntity) {
                loc = ((LivingEntity) e).getEyeLocation();
            } else loc = e.getLocation();

            uniParticle.play(loc, offset[0], offset[1], offset[2], speed, amount);
        }
    }

    @Override
    public boolean mustHaveTarget() {
        return true;
    }

}
