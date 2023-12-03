package t.me.p1azmer.engine.utils.txtanimation;

import org.bukkit.ChatColor;
import t.me.p1azmer.engine.utils.Colorizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Deprecated
public class Fade {

    public List<String> execute(String text, boolean smooth, int length, boolean in, boolean out) {
        if (smooth) {
            return color(text, length);
        } else {
            List<String> var3 = new ArrayList<>();
            if (in) {
                execute(text, var3);
            }

            if (out) {
                b(text, var3);
            }

            return var3;
        }
    }

    private static void execute(String var1, List<String> var2) {
        var2.addAll(Arrays.asList("§0" + var1, "§8" + var1, "§7" + var1, "§0" + var1));
    }

    private static void b(String var1, List<String> var2) {
        var2.addAll(Arrays.asList("§0" + var1, "§7" + var1, "§8" + var1, "§0" + var1));
    }

    public static List<String> color(String text, int length) {
        String var3 = ChatColor.getLastColors(text = Colorizer.apply(text));
        text = text.replace(var3, "");
        List<String> var4 = new ArrayList<>();

        for (int var5 = 0; var5 < length; ++var5) {
            var4.add(Colorizer.apply(var3 + text));
        }

        return var4;
    }
}
