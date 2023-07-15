package t.me.p1azmer.engine.utils.rgb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Patterns {
    color(Pattern.compile("(?i)" + '\u00A7' + "[0-9A-FK-ORX]")),
    bracketHexFormat(Pattern.compile("\\{#[0-9a-fA-F]{6}}")),
    ampersandHexFormat(Pattern.compile("&#[0-9a-fA-F]{6}")),
    mojangHexFormat(Pattern.compile("&x[&0-9a-fA-F]{12}")),
    gradientHexFormat(Pattern.compile("\\{#[0-9a-fA-F]{6}>}[^{]*\\{#[0-9a-fA-F]{6}<}")),
    player(Pattern.compile("^[a-zA-Z0-9_]{2,16}$")),
    log4j(Pattern.compile(".*\\$\\{[^}]*}.*")),
    cyrillic(Pattern.compile(".*\\p{InCyrillic}.*")),
    ip(Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$")),
    advertisement(Pattern.compile("[-a-zA-Zà-ÿÀ-ß0-9@:%_+.~#?&/=]{2,256}\\.[a-zA-Z]{2,4}\\b(/[-a-zA-Zà-ÿÀ-ß0-9@:%_+~#?&/=]*)?"));

    public final Pattern value;

    Patterns(Pattern pattern) {
        this.value = pattern;
    }

    public boolean find(String input) {
        return this.value.matcher(input).find();
    }

    public Matcher matcher(String input) {
        return this.value.matcher(input);
    }

    public boolean matches(String input) {
        return this.value.matcher(input).matches();
    }
}