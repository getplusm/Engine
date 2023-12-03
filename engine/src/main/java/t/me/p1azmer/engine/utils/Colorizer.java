package t.me.p1azmer.engine.utils;

import com.google.common.collect.ImmutableMap;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.utils.random.Rnd;
import t.me.p1azmer.engine.utils.regex.RegexUtil;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Colorizer {

    public static final List<String> SPECIAL_COLORS = Arrays.asList("&l", "&n", "&o", "&k", "&m");
    private static final Map<Color, ChatColor> COLORS = ImmutableMap.<Color, ChatColor>builder()
            .put(new Color(0), ChatColor.getByChar('0'))
            .put(new Color(170), ChatColor.getByChar('1'))
            .put(new Color(43520), ChatColor.getByChar('2'))
            .put(new Color(43690), ChatColor.getByChar('3'))
            .put(new Color(11141120), ChatColor.getByChar('4'))
            .put(new Color(11141290), ChatColor.getByChar('5'))
            .put(new Color(16755200), ChatColor.getByChar('6'))
            .put(new Color(11184810), ChatColor.getByChar('7'))
            .put(new Color(5592405), ChatColor.getByChar('8'))
            .put(new Color(5592575), ChatColor.getByChar('9'))
            .put(new Color(5635925), ChatColor.getByChar('a'))
            .put(new Color(5636095), ChatColor.getByChar('b'))
            .put(new Color(16733525), ChatColor.getByChar('c'))
            .put(new Color(16733695), ChatColor.getByChar('d'))
            .put(new Color(16777045), ChatColor.getByChar('e'))
            .put(new Color(16777215), ChatColor.getByChar('f'))
            .build();
    public static final Pattern PATTERN_HEX = Pattern.compile("#([A-Fa-f0-9]{6})");
    public static final Pattern PATTERN_HEX_BRACKETS = Pattern.compile("<#([A-Fa-f0-9]{6})>");
    public static final Pattern PATTERN_GRADIENT = Pattern.compile("<gradient:" + PATTERN_HEX.pattern() + ">(.*?)</gradient:" + PATTERN_HEX.pattern() + ">");

    @NotNull
    public static String apply(@NotNull String str) {
        return hex(gradient(legacy(str)));
    }

    @NotNull
    public static List<String> apply(@NotNull List<String> list) {
        list.replaceAll(Colorizer::apply);
        return list;
    }

    @NotNull
    public static Set<String> apply(@NotNull Set<String> set) {
        return set.stream().map(Colorizer::apply).collect(Collectors.toSet());
    }

    @NotNull
    public static String legacyHex(@NotNull String str) {
        return hex(legacy(str));
    }

    @NotNull
    public static String legacy(@NotNull String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    @NotNull
    public static String hex(@NotNull String str) {
        Matcher matcher = PATTERN_HEX.matcher(str);
        StringBuilder buffer = new StringBuilder(str.length() + 4 * 8);
        while (RegexUtil.matcherFind(matcher)) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer,
                    ChatColor.COLOR_CHAR + "x" + ChatColor.COLOR_CHAR + group.charAt(0) +
                            ChatColor.COLOR_CHAR + group.charAt(1) + ChatColor.COLOR_CHAR + group.charAt(2) +
                            ChatColor.COLOR_CHAR + group.charAt(3) + ChatColor.COLOR_CHAR + group.charAt(4) +
                            ChatColor.COLOR_CHAR + group.charAt(5));
        }
        return matcher.appendTail(buffer).toString();
    }

    private static ChatColor[] createGradient(@NotNull Color start, @NotNull Color end, int length) {
        ChatColor[] colors = new ChatColor[length];
        for (int index = 0; index < length; index++) {
            double percent = (double) index / (double) length;

            int red = (int) (start.getRed() + percent * (end.getRed() - start.getRed()));
            int green = (int) (start.getGreen() + percent * (end.getGreen() - start.getGreen()));
            int blue = (int) (start.getBlue() + percent * (end.getBlue() - start.getBlue()));

            Color color = new Color(red, green, blue);
            colors[index] = ChatColor.of(color);
        }
        return colors;
    }

    @NotNull
    public static String gradient(@NotNull String string) {
        Matcher matcher = PATTERN_GRADIENT.matcher(string);
        while (RegexUtil.matcherFind(matcher)) {
            String start = matcher.group(1);
            String end = matcher.group(3);
            String content = matcher.group(2);

            Color colorStart = new Color(Integer.parseInt(start, 16));
            Color colorEnd = new Color(Integer.parseInt(end, 16));
            ChatColor[] colors = createGradient(colorStart, colorEnd, Colorizer.strip(content).length());

            StringBuilder gradiented = new StringBuilder();
            StringBuilder specialColors = new StringBuilder();
            char[] characters = content.toCharArray();
            int outIndex = 0;
            for (int index = 0; index < characters.length; index++) {
                if (characters[index] == ChatColor.COLOR_CHAR) {
                    if (index + 1 < characters.length) {
                        if (characters[index + 1] == 'r') {
                            specialColors.setLength(0);
                        } else {
                            specialColors.append(characters[index]);
                            specialColors.append(characters[index + 1]);
                        }
                        index++;
                    } else gradiented.append(colors[outIndex++]).append(specialColors).append(characters[index]);
                } else gradiented.append(colors[outIndex++]).append(specialColors).append(characters[index]);
            }

            string = string.replace(matcher.group(0), gradiented.toString());
        }
        return string;
    }

    @NotNull
    public static String plain(@NotNull String str) {
        return plainLegacy(plainHex(str));
    }

    @NotNull
    public static String plainLegacy(@NotNull String str) {
        return str.replace(ChatColor.COLOR_CHAR, '&');
    }

    @NotNull
    public static String plainHex(@NotNull String str) {
        StringBuilder buffer = new StringBuilder(str);

        int index;
        while ((index = buffer.toString().indexOf(ChatColor.COLOR_CHAR + "x")) >= 0) {
            int count = 0;
            buffer.replace(index, index + 2, "#");

            for (int point = index + 1; count < 6; point += 1) {
                buffer.deleteCharAt(point);
                count++;
            }
        }

        return buffer.toString();
    }

    @NotNull
    public static String strip(@NotNull String str) {
        String stripped = ChatColor.stripColor(str);
        return stripped == null ? "" : stripped;
    }

    @NotNull
    public static String restrip(@NotNull String str) {
        return strip(apply(str));
    }

    @NotNull
    public static Color getColor(@NotNull String colorRaw) {
        String[] rgb = colorRaw.split(",");
        int red = StringUtil.getInteger(rgb[0], 0);
        if (red < 0) red = Rnd.get(255);

        int green = rgb.length >= 2 ? StringUtil.getInteger(rgb[1], 0) : 0;
        if (green < 0) green = Rnd.get(255);

        int blue = rgb.length >= 3 ? StringUtil.getInteger(rgb[2], 0) : 0;
        if (blue < 0) blue = Rnd.get(255);

        return new Color(red, green, blue);
    }

    @NotNull
    public static Color getColor(int red, int green, int blue) {
        if (red < 0) red = Rnd.get(255);

        if (green < 0) green = Rnd.get(255);

        if (blue < 0) blue = Rnd.get(255);

        return new Color(red, green, blue);
    }

    @NotNull
    private static String injectColors(@NotNull String source, ChatColor[] colors) {
        StringBuilder specialColors = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        String[] characters = source.split("");
        int outIndex = 0;
        for (int i = 0; i < characters.length; i++) {
            if (characters[i].equals("&") || characters[i].equals("§")) {
                if (i + 1 < characters.length) {
                    if (characters[i + 1].equals("r")) {
                        specialColors.setLength(0);
                    } else {
                        specialColors.append(characters[i]);
                        specialColors.append(characters[i + 1]);
                    }
                    i++;
                } else stringBuilder.append(colors[outIndex++]).append(specialColors).append(characters[i]);
            } else stringBuilder.append(colors[outIndex++]).append(specialColors).append(characters[i]);
        }
        return stringBuilder.toString();
    }

    /**
     * Colors a String with rainbow colors.
     *
     * @param string     The string which should have rainbow colors
     * @param saturation The saturation of the rainbow colors
     * @since 1.0.3
     */
    @Nonnull
    public static String rainbow(@Nonnull String string, float saturation) {
        StringBuilder specialColors = new StringBuilder();
        for (String color : SPECIAL_COLORS) {
            if (string.contains(color)) {
                specialColors.append(color);
                string = string.replace(color, "");
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        ChatColor[] colors = createRainbow(string.length(), saturation);
        String[] characters = string.split("");
        for (int i = 0; i < string.length(); i++) {
            stringBuilder.append(colors[i]).append(specialColors).append(characters[i]);
        }
        return stringBuilder.toString();
    }

    /**
     * Returns a rainbow array of chat colors.
     *
     * @param step       How many colors we return
     * @param saturation The saturation of the rainbow
     * @return The array of colors
     */
    @Nonnull
    private static ChatColor[] createRainbow(int step, float saturation) {
        ChatColor[] colors = new ChatColor[step];
        double colorStep = (1.00 / step);
        for (int i = 0; i < step; i++) {
            Color color = Color.getHSBColor((float) (colorStep * i), saturation, saturation);
            colors[i] = ChatColor.of(color);
        }
        return colors;
    }

    /**
     * Returns the closest legacy color from an rgb color
     *
     * @param color The color we want to transform
     */
    @Nonnull
    private static ChatColor getClosestColor(Color color) {
        Color nearestColor = null;
        double nearestDistance = Integer.MAX_VALUE;

        for (Color constantColor : COLORS.keySet()) {
            double distance = Math.pow(color.getRed() - constantColor.getRed(), 2) + Math.pow(color.getGreen() - constantColor.getGreen(), 2) + Math.pow(color.getBlue() - constantColor.getBlue(), 2);
            if (nearestDistance > distance) {
                nearestColor = constantColor;
                nearestDistance = distance;
            }
        }
        return COLORS.get(nearestColor);
    }
}