package t.me.p1azmer.engine.actions.actions.list;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.actions.AbstractActionExecutor;
import t.me.p1azmer.engine.actions.actions.ActionType;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.params.IParamType;
import t.me.p1azmer.engine.utils.Colorizer;

import java.util.HashSet;
import java.util.Set;

public class Action_ProgressBar extends AbstractActionExecutor {

    public Action_ProgressBar(@NotNull NexPlugin<?> plugin) {
        super(plugin, ActionType.PROGRESS_BAR);
    }

    @Override
    public void registerParams() {
        this.registerParam(IParamType.TARGET);
        this.registerParam(IParamType.DURATION);
        this.registerParam(IParamType.TITLES_TITLE);
        this.registerParam(IParamType.TITLES_SUBTITLE);
        this.registerParam(IParamType.BAR_COLOR_EMPTY);
        this.registerParam(IParamType.BAR_COLOR_FILL);
    }

    @Override
    protected void execute(@NotNull Entity exe, @NotNull Set<Entity> targets, @NotNull IParamResult result) {
        int dura = (int) ((double) result.getParamValue(IParamType.DURATION).getInt(0) / 20D);
        if (dura <= 0) return;

        String title    = result.getParamValue(IParamType.TITLES_TITLE).getString("");
        String subtitle = result.getParamValue(IParamType.TITLES_SUBTITLE).getString("");

        String c1 = result.getParamValue(IParamType.BAR_COLOR_EMPTY).getString("");
        String c2 = result.getParamValue(IParamType.BAR_COLOR_FILL).getString("");

        Set<Player> users = new HashSet<>();
        for (Entity e : targets) {
            if (e.getType() != EntityType.PLAYER) continue;
            Player p = (Player) e;
            users.add(p);
        }

        playAnimDelay(users, dura, title, subtitle, c1, c2);
    }

    @Override
    public boolean mustHaveTarget() {
        return true;
    }

    private void playAnimDelay(Set<Player> p, int time, String title, String ch, String c1, String c2) {
        double pers = 20 / time;
        double step = 20 / pers;

        AnimDelay a = new AnimDelay(p, (int) step, title, ch, c1, c2);
        a.runTaskTimer(plugin, 0L, (int) step);
    }

    public static class AnimDelay extends BukkitRunnable {

        private Set<Player> players;
        private int         progress;
        private int         step;
        private String      title;
        private String      c1;
        private String      c2;
        private String      bar;

        public AnimDelay(Set<Player> p, int step, String title, String ch, String c1, String c2) {
            this.progress = 0;
            this.players = p;
            this.step = step;
            this.title = Colorizer.apply(title);//.replace("_", " ");
            this.c1 = Colorizer.apply(c1);
            this.c2 = Colorizer.apply(c2);

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 20; i++) {
                sb.insert(i, ch);
            }
            this.bar = sb.toString();
        }

        @Override
        public void run() {
            if (this.progress >= 20) {
                this.cancel();
                return;
            }
            this.paintBar();
            ++this.progress;
        }

        private void paintBar() {
            String       kist = this.bar;
            StringBuffer sb   = new StringBuffer(kist);

            sb.setLength(sb.length());
            sb.insert(0, c2);
            if (progress < 20) {
                sb.insert(this.progress + c1.length() + 1, c1);
            }
            kist = sb.toString();

            for (Player pl : players) {
                pl.sendTitle(title, kist, 0, step, 20);
            }
        }
    }
}
