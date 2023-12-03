package t.me.p1azmer.engine.utils.txtanimation;

import t.me.p1azmer.engine.utils.Colorizer;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class Appear {
    public static List<String> execute(String text, boolean fade) {
        List<String> var2 = new ArrayList<>();
        int var9;
        if (!fade) {
            for (var9 = 0; var9 < text.length(); ++var9) {
                var2.add(text.substring(0, var9));
            }

        } else {
            for (var9 = 0; var9 < text.length() + 3; ++var9) {
                String var4 = text.substring(0, Math.min(var9, text.length()));
                String var5 = text.substring(Math.min(var9, text.length()), Math.min(text.length(), Math.max(0, var9 + 1)));
                String var6 = text.substring(Math.max(0, Math.min(var9 + 1, text.length())), Math.min(text.length(), Math.max(0, var9 + 2)));
                String var7 = text.substring(Math.max(0, Math.min(var9 + 2, text.length())), Math.min(text.length(), Math.max(0, var9 + 3)));
                var2.add(Colorizer.apply(var4 + "§7" + var5 + "§8" + var6 + "§0" + var7));
            }

        }
        return var2;
    }
}
