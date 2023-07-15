package t.me.p1azmer.engine.utils.TextAnimation;

import java.util.ArrayList;
import java.util.List;

public class Rainbow {

    public static List<String> execute(String text) {
        List<String> frames = new ArrayList<>();

        int i;
        for (i = 0; i < 9; ++i) {
            frames.add("§" + i + text);
        }

        for (i = 97; i < 102; ++i) {
            frames.add("§" + (char) i + text);
        }

        return frames;
    }
}
