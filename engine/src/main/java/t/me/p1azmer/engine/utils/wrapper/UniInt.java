package t.me.p1azmer.engine.utils.wrapper;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.api.config.JYML;
import t.me.p1azmer.engine.utils.random.Rnd;

public final class UniInt {

    private final int minInclusive;
    private final int maxInclusive;

    private UniInt(int var0, int var1) {
        this.minInclusive = var0;
        this.maxInclusive = var1;
    }

    @NotNull
    public static UniInt of(int var0, int var1) {
        return new UniInt(var0, var1);
    }

    @NotNull
    public static UniInt read(@NotNull JYML cfg, @NotNull String path) {
        int min = cfg.getInt(path + ".Min");
        int max = cfg.getInt(path + ".Max");
        return of(min, max);
    }

    public void write(@NotNull JYML cfg, @NotNull String path) {
        cfg.set(path + ".Min", this.getMinValue());
        cfg.set(path + ".Max", this.getMaxValue());
    }

    public int roll() {
        return Rnd.get(this.minInclusive, this.maxInclusive);
    }

    public int getMinValue() {
        return this.minInclusive;
    }

    public int getMaxValue() {
        return this.maxInclusive;
    }

    @Override
    public String toString() {
        return "[" + this.minInclusive + "-" + this.maxInclusive + "]";
    }
}