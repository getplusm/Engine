package t.me.p1azmer.engine.api.config;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.api.config.JYML;

public interface Writeable {

    void write(@NotNull JYML config, @NotNull String path);
}