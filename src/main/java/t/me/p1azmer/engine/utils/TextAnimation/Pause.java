package t.me.p1azmer.engine.utils.TextAnimation;

import java.util.ArrayList;
import java.util.List;

public class Pause {

    public static List<String> execute(String text, int pause) {
        List<String> frames = new ArrayList<>();

        for (int i = 0; i < pause; ++i) {
            frames.add(text);
        }

        return frames;
    }
}
