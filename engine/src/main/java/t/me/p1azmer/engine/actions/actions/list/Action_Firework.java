package t.me.p1azmer.engine.actions.actions.list;

//import org.bukkit.entity.Entity;
//import org.jetbrains.annotations.NotNull;
//import t.me.p1azmer.engine.NexPlugin;
//import t.me.p1azmer.engine.actions.actions.AbstractActionExecutor;
//import t.me.p1azmer.engine.actions.actions.ActionType;
//import t.me.p1azmer.engine.actions.params.IParamResult;
//import t.me.p1azmer.engine.actions.params.IParamType;
//import t.me.p1azmer.engine.utils.EntityUtil;
//
//import java.util.Set;
//
//public class Action_Firework extends AbstractActionExecutor {
//
//    public Action_Firework(@NotNull NexPlugin<?> plugin) {
//        super(plugin, ActionType.FIREWORK);
//    }
//
//
//    @Override
//    public void registerParams() {
//        this.registerParam(IParamType.TARGET);
//    }
//
//    @Override
//    protected void execute(@NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamResult result) {
//        if (!targets.isEmpty()) {
//            for (Entity target : targets) {
//                EntityUtil.spawnRandomFirework(target.getLocation());
//            }
//        } else {
//            EntityUtil.spawnRandomFirework(exe.getLocation());
//        }
//    }
//
//    @Override
//    public boolean mustHaveTarget() {
//        return false;
//    }
//
//}
