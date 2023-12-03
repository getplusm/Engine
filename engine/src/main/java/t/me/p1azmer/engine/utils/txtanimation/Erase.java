package t.me.p1azmer.engine.utils.txtanimation;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class Erase {
    public static List<String> execute(String text, boolean fade) {
        List<String> var2 = new ArrayList<>();
        int var9;
        if (!fade) {
            for (var9 = 0; var9 < text.length(); ++var9) {
                var2.add(text.substring(0, text.length() - var9));
            }

        } else {
            for (var9 = 0; var9 < text.length() + 3; ++var9) {
                int var4 = text.length() - var9;
                String var5 = text.substring(0, Math.min(Math.max(var4, 0), text.length()));
                String var6 = text.substring(Math.max(0, Math.min(var4, text.length())), Math.min(text.length(), Math.max(0, var4 + 1)));
                String var7 = text.substring(Math.max(0, Math.min(var4 + 1, text.length())), Math.min(text.length(), Math.max(0, var4 + 2)));
                String var10 = text.substring(Math.max(0, Math.min(var4 + 2, text.length())), Math.min(text.length(), Math.max(0, var4 + 3)));
                var2.add(var5 + "§7" + var6 + "§8" + var7 + "§0" + var10);
            }

        }
        return var2;
    }
}
