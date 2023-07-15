package t.me.p1azmer.engine.actions.actions.list;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.actions.AbstractActionExecutor;
import t.me.p1azmer.engine.actions.actions.ActionType;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.params.IParamType;

import java.util.Set;

public class Action_Hook extends AbstractActionExecutor {

    public Action_Hook(@NotNull NexPlugin<?> plugin) {
        super(plugin, ActionType.HOOK);
    }


    @Override
    public void registerParams() {
        this.registerParam(IParamType.TARGET);
    }

    @Override
    protected void execute(@NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamResult result) {
        for (Entity target : targets) {
            Location localLocation1 = target.getLocation();
            Location localLocation2 = localLocation1.subtract(exe.getLocation());

            Vector localVector = localLocation2.getDirection().normalize().multiply(-1.4D);

            if (localVector.getY() >= 1.15D) {
                localVector.setY(localVector.getY() * 0.45D);
            } else if (localVector.getY() >= 1.0D) {
                localVector.setY(localVector.getY() * 0.6D);
            } else if (localVector.getY() >= 0.8D) {
                localVector.setY(localVector.getY() * 0.85D);
            }

            if (localVector.getY() <= 0.0D) {
                localVector.setY(-localVector.getY() + 0.3D);
            }
            if (Math.abs(localLocation2.getX()) <= 1.0D) {
                localVector.setX(localVector.getX() * 1.2D);
            }
            if (Math.abs(localLocation2.getZ()) <= 1.0D) {
                localVector.setZ(localVector.getZ() * 1.2D);
            }

            double d1 = localVector.getX() * -2;
            double d2 = localVector.getY() * -2;
            double d3 = localVector.getZ() * -2;

            if (d1 >= -3.0D) d1 *= -0.5D;
            if (d2 >= -3.0D) d2 *= -0.5D;
            if (d3 >= -3.0D) d3 *= -0.5D;

            localVector.setX(d1);
            localVector.setY(d2);
            localVector.setZ(d3);

            target.setVelocity(localVector);
        }
    }

    @Override
    public boolean mustHaveTarget() {
        return true;
    }

}
