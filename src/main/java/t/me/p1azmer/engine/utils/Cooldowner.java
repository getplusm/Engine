package t.me.p1azmer.engine.utils;

import java.util.HashMap;
import java.util.Map;

public class Cooldowner {

    private static final Map<String, Cooldowner> cooldowns = new HashMap<>();

    private long start;
    private final int time;
    private final String player;
    private final String name;

    public Cooldowner(String player, String name, int time) {
        this.player = player;
        this.name   = name;
        this.time   = time;
    }

    // Проверяем существует ли кулдаун.
    public static boolean isInCooldown(String player, String name) {
        if (getTimeLeft(player, name) >= 1) {
            return true;
        } else {
            remove(player, name);
            return false;
        }
    }

    // Убираем кулдаун.
    public static void remove(String player, String name) {
        try {
            cooldowns.remove(player + name);
        } catch (Exception ignored) {}
    }

    // Получаем оставшееся время кулдауна.
    public static int getTimeLeft(String player, String name) {
        Cooldowner cooldown = cooldowns.get(player + name);
        int f = -1;
        if (cooldown != null) {
            long now = System.currentTimeMillis();
            long time = cooldown.start;
            int r = (int) (now - time) / 1000;
            f = (r - cooldown.time) * (-1);
        }

        return f;
    }

    // Создаём кулдаун.
    public void start() {
        this.start = System.currentTimeMillis();
        cooldowns.put(this.player + this.name, this);
    }
}