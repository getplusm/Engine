package t.me.p1azmer.engine.utils.TextAnimation;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Scroll {

    public static List<String> execute(String text, int width, int space) {
        List<String> frames = new ArrayList<>();
        int pos = 0;
        List<String> temp = new ArrayList<>();
        StringBuilder spacing;
        if (text.length() < width) {
            spacing = new StringBuilder();

            while (spacing.length() < width) {
                spacing.append(" ");
            }

            text = text + spacing;
        }

        width -= 2;

        for (int i = 0; i < text.length() - width; ++i) {
            temp.add(text.substring(i, i + width));
        }

        spacing = new StringBuilder();

        int i;
        for (i = 0; i < space; ++i) {
            temp.add(text.substring(text.length() - width + Math.min(i, width)) + spacing);
            if (spacing.length() < width) {
                spacing.append(" ");
            }
        }

        for (i = 0; i < width - space; ++i) {
            temp.add(text.substring(text.length() - width + space + i) + spacing + text.substring(0, i));
        }

        for (i = 0; i < space && i <= spacing.length(); ++i) {
            temp.add(spacing.substring(0, spacing.length() - i) + text.substring(0, width - Math.min(space, width) + i));
        }

        ChatColor stored = ChatColor.RESET;

        for (i = 0; i < temp.size(); ++i) {
            StringBuilder builder = new StringBuilder(temp.get(pos++ % temp.size()));
            if (builder.charAt(builder.length() - 1) == 167) {
                builder.setCharAt(builder.length() - 1, ' ');
            }

            if (builder.charAt(0) == 167) {
                ChatColor color = ChatColor.getByChar(builder.charAt(1));
                if (color != null) {
                    stored = color;
                    builder = new StringBuilder(temp.get(pos++ % temp.size()));
                    if (builder.charAt(0) != ' ') {
                        builder.deleteCharAt(0);
                    }
                }
            }

            frames.add(stored + builder.substring(1));
        }

        return frames;
    }
}
