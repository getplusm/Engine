package t.me.p1azmer.engine.utils.TextAnimation;

import t.me.p1azmer.engine.utils.Colorizer;

import java.util.ArrayList;
import java.util.List;

public class Burn {

    public static List<String> execute(String text, int speed, int pause, String... args) {
        List<String> frames = new ArrayList<>();

        StringBuilder specialColors = new StringBuilder();
        for (String color : Colorizer.SPECIAL_COLORS) {
            if (text.contains(color)) {
                specialColors.append(color);
                text = text.replace(color, "");
            }
        }
        String stripped = Colorizer.strip(text);
        for (int currentStep = 0; currentStep < stripped.length(); currentStep += speed) {

            String start = stripped.substring(0, currentStep);
            String end = stripped.substring(currentStep);
            frames.add(args[1] + specialColors + start + args[0] + specialColors + end);
        }
        for (int i = 0; i < pause; i++)
            frames.add(text);
        return frames;
    }

    static int getCurrentStep(long step, int maxSteps, int speed, int pause) {
        if (maxSteps <= 0) return 0;
        long actualStep = step / speed;
        // Adapt the pause to speed.
        int actualPause = pause <= 0 ? 0 : pause / speed;
        int currentStep = (int) (actualStep % (maxSteps + actualPause));
        return Math.min(currentStep, maxSteps);
    }
}
