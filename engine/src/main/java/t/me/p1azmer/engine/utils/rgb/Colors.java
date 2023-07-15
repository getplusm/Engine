package t.me.p1azmer.engine.utils.rgb;

import com.google.common.collect.Lists;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;

public final class Colors {

    public static final char MAIN_COLOR_CHAR = '\u00A7';
    public static final char FAKE_COLOR_CHAR = '&';
    public static final String ALL_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx";

    private static final HashMap<String, Colors> list = new HashMap<>();

    public static final Colors BLACK = new Colors('0', "black", 0x000000);
    public static final Colors DARK_BLUE = new Colors('1', "dark_blue", 0x0000AA);
    public static final Colors DARK_GREEN = new Colors('2', "dark_green", 0x00AA00);
    public static final Colors DARK_AQUA = new Colors('3', "dark_aqua", 0x00AAAA);
    public static final Colors DARK_RED = new Colors('4', "dark_red", 0xAA0000);
    public static final Colors DARK_PURPLE = new Colors('5', "dark_purple", 0xAA00AA);
    public static final Colors GOLD = new Colors('6', "gold", 0xFFAA00);
    public static final Colors GRAY = new Colors('7', "gray", 0xAAAAAA);
    public static final Colors DARK_GRAY = new Colors('8', "dark_gray", 0x555555);
    public static final Colors BLUE = new Colors('9', "blue", 0x5555FF);
    public static final Colors GREEN = new Colors('a', "green", 0x55FF55);
    public static final Colors AQUA = new Colors('b', "aqua", 0x55FFFF);
    public static final Colors RED = new Colors('c', "red", 0xFF5555);
    public static final Colors LIGHT_PURPLE = new Colors('d', "light_purple", 0xFF55FF);
    public static final Colors YELLOW = new Colors('e', "yellow", 0xFFFF55);
    public static final Colors WHITE = new Colors('f', "white", 0xFFFFFF);
    public static final Colors MAGIC = new Colors('k', "obfuscated", null);
    public static final Colors BOLD = new Colors('l', "bold", null);
    public static final Colors STRIKETHROUGH = new Colors('m', "strikethrough", null);
    public static final Colors UNDERLINE = new Colors('n', "underline", null);
    public static final Colors ITALIC = new Colors('o', "italic", null);
    public static final Colors RESET = new Colors('r', "reset", null);

    private final String toString;

    private final Integer red;
    private final Integer green;
    private final Integer blue;

    private Colors(String name) {
        this(null, name, name);
    }

    private Colors(Character code, String name, int hex) {
        this(code, name, Integer.toHexString(hex));
    }

    private Colors(Character code, String name, String hex) {
        Color color = hex != null ? getCustomColor(hex) : null;
        this.red = color != null ? color.getRed() : null;
        this.green = color != null ? color.getGreen() : null;
        this.blue = color != null ? color.getBlue() : null;
        this.toString = code != null ? new String(new char[]{MAIN_COLOR_CHAR, code}) : generateMagic(name);
        list.put(name, this);
        if (name.contains("_")) list.put(name.replace("_", ""), this);
        if (code != null && hex != null) list.put(Character.toString(code), this);
    }

    @Override
    public boolean equals(Object value) {
        if (this == value) return true;
        if (value == null || getClass() != value.getClass()) return false;

        Colors other = (Colors) value;

        return Objects.equals(this.toString, other.toString);
    }

    @Override
    public String toString() {
        return toString;
    }

    public Integer getRed() {
        return red;
    }

    public Integer getGreen() {
        return green;
    }

    public Integer getBlue() {
        return blue;
    }

    public static Colors getLastColor(String value) {
        char[] chars = set(value).toLowerCase().toCharArray();
        Colors result = Colors.WHITE;
        for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i] != MAIN_COLOR_CHAR) continue;
            if (ALL_CODES.indexOf(chars[i + 1]) == -1) continue;
            if (chars[i + 1] == 'x' && chars.length + i >= 14 + i) {
                result = get(String.valueOf(new char[]{chars[i + 3], chars[i + 5], chars[i + 7], chars[i + 9], chars[i + 11], chars[i + 13]}));
                i += 14;
            } else {
                result = get(Character.toString(chars[i + 1]));
            }
        }
        return result;
    }

    // Удалить цвета в строке.
    public static String clear(String value) {
        if (value == null) return null;
        return Patterns.color.matcher(set(value)).replaceAll("");
    }

    // В случае ошибок возвращаем этот текст.
    public static String none() {
        return RED + "-" + Colors.RESET;
    }

    // Покрасить список строк.
    public static java.util.List<String> set(java.util.List<String> list) {
        ArrayList<String> result = Lists.newArrayList();
        for (String row : list) result.add(set(row));
        return result;
    }

    // Покрасить строку.
    public static String set(String value) {
        char[] chars = value.toCharArray();

        // Перебор всех символов в строке.
        for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i] != FAKE_COLOR_CHAR) continue;
            if (ALL_CODES.indexOf(chars[i + 1]) == -1) continue;

            chars[i] = Colors.MAIN_COLOR_CHAR;
            chars[i + 1] = Character.toLowerCase(chars[i + 1]);
        }

        String result = new String(chars);

        result = setGradientHex(result);
        result = setCustomHex(Patterns.ampersandHexFormat, result);
        result = setCustomHex(Patterns.bracketHexFormat, result);
        result = setMojangHex(result);

        return result;
    }

    private static String setCustomHex(Patterns pattern, String value) {
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            value = value.replace(matcher.group(), get(matcher.group()).toString());
        }
        return value;
    }

    private static String setMojangHex(String value) {
        Matcher matcher = Patterns.mojangHexFormat.matcher(value);
        while (matcher.find()) {
            char[] data = matcher.group().toCharArray();
            value = value.replace(matcher.group(), get(String.valueOf(new char[]{data[3], data[5], data[7], data[9], data[11], data[13]})).toString());
        }
        return value;
    }

    private static String setGradientHex(String value) {
        Matcher matcher = Patterns.gradientHexFormat.matcher(value);
        while (matcher.find()) {
            String format = matcher.group();
            String message = format.substring(10, format.length() - 10);
            Colors start = Colors.get(format.substring(2, 8));
            Colors end = Colors.get(format.substring(format.length() - 8, format.length() - 2));
            value = value.replace(format, asGradient(start, message.toCharArray(), end));
        }
        return value;
    }

    private static String asGradient(Colors start, char[] message, Colors end) {
        StringBuilder sb = new StringBuilder();
        int length = message.length;

        for (int i = 0; i < length; i++) {
            int red = (int) (start.getRed() + (float) (end.getRed() - start.getRed()) / (length - 1) * i);
            int green = (int) (start.getGreen() + (float) (end.getGreen() - start.getGreen()) / (length - 1) * i);
            int blue = (int) (start.getBlue() + (float) (end.getBlue() - start.getBlue()) / (length - 1) * i);
            sb.append(String.format("{#%02X%02X%02X}", red, green, blue).toLowerCase()).append(message[i]);
        }

        return sb.toString();
    }

    public static Colors get(String value) {
        if (value == null) return Colors.WHITE;
        value = value.toLowerCase().replaceAll("[#&{}]", "");

        Colors result = list.get(value);
        if (result != null) return result;

        try {
            Integer.parseInt(value, 16);
        } catch (NumberFormatException e) {
            return Colors.WHITE;
        }

        return new Colors(value);
    }

    public static HashMap<String, Colors> getAllColors() {
        return list;
    }

    public static String generateMagic(String value) {
        StringBuilder magic = new StringBuilder(MAIN_COLOR_CHAR + "x");
        for (char c : value.toCharArray()) {
            magic.append(MAIN_COLOR_CHAR).append(c);
        }
        return magic.toString();
    }

    public static Color getCustomColor(String value) {
        try {
            return Color.decode("#" + value);
        } catch (Exception ignored) {
            return null;
        }
    }
}