package t.me.p1azmer.engine.utils.txtanimation;

import org.bukkit.ChatColor;
import t.me.p1azmer.engine.utils.Colorizer;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class Wave {


    /**
     * @param text       - основной текст
     * @param colorsList - цвета через запятую
     *                   //     * @param length - длина цвета
     *                   //     * @param smooth - использовать РГБ
     * @return анимация
     */
    public static List<String> execute(String text, String colorsList) {
        List<String> frames = new ArrayList<>();
        String[] colors = colorsList.split(",");
        String formatting = ChatColor.getLastColors(text);
        text = text.replace(formatting, "");
        int counter = 0;
        int index = 0;
        int var9 = colors.length;

        for (int var10 = 0; var10 < var9; ++var10) {
            StringBuilder currentFrame = new StringBuilder();
            char[] var13 = text.toCharArray();
            for (char c : var13) {
                String result = Colorizer.apply(colors[index] + formatting);
                ++index;
                if (index >= colors.length) {
                    index = 0;
                }

                currentFrame.append(result).append(c);
            }

            ++counter;
            index = counter;
            frames.add(currentFrame.toString());
        }

        return frames;
    }

    public static List<String> execute(String string, int speed, int pause, String... args) {
        List<String> frames = new ArrayList<>();

        StringBuilder specialColors = new StringBuilder();
        for (String color : Colorizer.SPECIAL_COLORS) {
            if (string.contains(color)) {
                specialColors.append(color);
                string = string.replace(color, "");
            }
        }
        String stripped = Colorizer.strip(string);
        int length = stripped.length();
        int size = length / 4;
        for (int currentStep = 0; currentStep < length+size; currentStep+=speed) {
//            int currentStep = getCurrentStep(step, length+size, speed, pause);
            int index1 = currentStep > size ? currentStep - size : 0;
            int index2 = currentStep < size ? size - (size - currentStep) : currentStep;
            String start = index1 != 0 ? stripped.substring(0, index1) : "";
            String mid = length > index2 ? stripped.substring(index1, index2) : stripped.substring(index1);
            String end = length > index2 ? stripped.substring(index2) : "";
            frames.add(args[0] + specialColors + start + args[1] + specialColors + mid + args[0] + specialColors + end);
        }

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
