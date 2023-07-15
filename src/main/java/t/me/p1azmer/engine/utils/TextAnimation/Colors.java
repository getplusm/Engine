package t.me.p1azmer.engine.utils.TextAnimation;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Colors {

    private static final List<String> DEFAULT_COLORS = Lists.newArrayList("&c", "&6", "&e", "&a", "&b", "&d");

    public static List<String> execute(String string, int speed, int pause, String... args) {
        List<String> frames = new ArrayList<>();
        List<String> colors = DEFAULT_COLORS;
        if (args != null && args.length > 0) {
            colors.clear();
            colors.addAll(Arrays.asList(args));
        }
        for (int step = 0; step < colors.size(); step+= speed) {
            frames.add(colors.get(step) + string);
        }
        for (int i= 0; i< pause;i++)
            frames.add(string);

        return frames;
    }
}
