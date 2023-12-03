package t.me.p1azmer.engine.utils.txtanimation;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class Typewriter {

    public static List<String> execute(String text, String cursor, int pause, boolean reverse) {
        List<String> frames = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int counter = 0;

        int i;
        for (i = 0; i < pause; ++i) {
            frames.add("");
            frames.add("");
            if (!cursor.isEmpty()) {
                frames.add(cursor);
                frames.add(cursor);
            }
        }

        char[] var13 = text.toCharArray();

        for (char character : var13) {
            current.append(character);
            counter = counterCheck(frames, cursor, current, counter);
        }

        for (i = 0; i < pause; ++i) {
            counter = counterCheck(frames, cursor, current, counter);
        }

        if (reverse) {
            for (i = text.length(); i > 0; --i) {
                current.deleteCharAt(i - 1);
                counter = counterCheck(frames, cursor, current, counter);
            }
        }

        return frames;
    }

    private static int counterCheck(List<String> frames, String cursor, StringBuilder current, int counter) {
        if (counter < 2) {
            frames.add(current + "");
        } else {
            if (!current.isEmpty())
                frames.add(current + cursor);
            else
                frames.add(current.toString());
        }

        if (counter == 4) {
            counter = 0;
        } else {
            ++counter;
        }

        return counter;
    }
}
