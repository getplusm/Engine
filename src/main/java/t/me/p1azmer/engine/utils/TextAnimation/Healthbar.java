package t.me.p1azmer.engine.utils.TextAnimation;

import java.util.Collections;
import java.util.List;

public class Healthbar {
    public static List<String> execute(String text, String symbol, String fill, String empty, int size, int max) {

        int var6;
        double var8;
        double var10;
        try {
            var6 = size;
            var8 = max;
            var10 = Double.parseDouble(text);
        } catch (NumberFormatException var16) {
            String var13 = var16.getMessage().replace("\"", "\\\"").replace("'", "\\'");
            return Collections.singletonList("§cОшибка: " + var13);
        }

        double var12 = var10 / var8;
        int var18 = (int) Math.min(Math.ceil((double) var6 * var12), var8);
        StringBuilder var19;
        (var19 = new StringBuilder()).append(fill);

        int var20;
        for (var20 = 0; var20 < var18; ++var20) {
            var19.append(symbol);
        }

        var19.append(empty);

        for (var20 = 0; var20 < var6 - var18; ++var20) {
            var19.append(symbol);
        }

        return Collections.singletonList(var19.toString());
    }
}
