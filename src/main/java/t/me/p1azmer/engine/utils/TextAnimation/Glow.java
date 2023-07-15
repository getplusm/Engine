package t.me.p1azmer.engine.utils.TextAnimation;

import t.me.p1azmer.engine.utils.Colorizer;

import java.util.ArrayList;
import java.util.List;

public class Glow {

    public static List<String> execute(String text, String normal, String start, String middle, String end, int pause, int speed, int size) {
        List<String> frames = new ArrayList<>();
        int lineSize;
        if ((lineSize = size) > text.length()) {
            lineSize = text.length() - 1;
        }

        int i;
        for (i = 0; i < text.length() + lineSize; i += speed) {
            int startGi = Math.max(i - size, 0);
            int midGi = Math.max(startGi + (startGi > 0 ? 1 : 0), 0) + (i - size == 0 ? 1 : 0);
            String frame = Colorizer.apply(normal + text.substring(0, startGi) + start + text.substring(Math.min(Math.max(startGi, 0), startGi),
                    Math.min(midGi, text.length())) + middle + text.substring(midGi, Math.min(Math.max(i - 1, 0), text.length()))
                    + end + text.substring(Math.max(Math.min(i - 1, text.length()), 0), Math.min(i, text.length()))
                    + normal + text.substring(Math.min(i, text.length())));
            frames.add(frame);
        }

        for (i = 0; i < pause; ++i) {
            frames.add(normal + text);
        }

        return frames;
    }
}
