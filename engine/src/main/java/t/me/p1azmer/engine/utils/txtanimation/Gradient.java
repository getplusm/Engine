package t.me.p1azmer.engine.utils.txtanimation;

import org.bukkit.ChatColor;
import t.me.p1azmer.engine.utils.Colorizer;

import java.util.Collections;
import java.util.List;

@Deprecated
public class Gradient {
    public static List<String> execute(String text) {
        return Collections.singletonList(color(text));
    }

    private static String color(String text) {
        String var2 = ChatColor.getLastColors(text = Colorizer.apply(text));
        int var3 = (text = text.replace(var2, "")).length();
        StringBuilder var4 = new StringBuilder();

        for (int var5 = 0; var5 < var3; ++var5) {
            var4.append(Colorizer.gradient(text)).append(var2).append(text.charAt(var5));
        }

        return var4.toString();
    }
}
