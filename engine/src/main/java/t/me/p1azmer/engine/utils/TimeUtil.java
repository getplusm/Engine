package t.me.p1azmer.engine.utils;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.lang.EngineLang;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtil {

    @NotNull
    public static String formatTime(long time) {
        long days = TimeUnit.MILLISECONDS.toDays(time);
        long hours = TimeUnit.MILLISECONDS.toHours(time) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60;

        StringBuilder str = new StringBuilder();
        if (days > 0) {
            if (str.length() > 0) {
                str.append(" ");
            }
            str.append(EngineUtils.ENGINE.getMessage(EngineLang.TIME_DAY).replace("%s%", days).getLocalized());
        }
        if (hours > 0) {
            if (str.length() > 0) {
                str.append(" ");
            }
            str.append(EngineUtils.ENGINE.getMessage(EngineLang.TIME_HOUR).replace("%s%", hours).getLocalized());
        }
        if (minutes > 0) {
            if (str.length() > 0) {
                str.append(" ");
            }
            str.append(EngineUtils.ENGINE.getMessage(EngineLang.TIME_MIN).replace("%s%", minutes).getLocalized());
        }
        if (str.length() == 0 || seconds > 0) {
            if (str.length() > 0) {
                str.append(" ");
            }
            str.append(EngineUtils.ENGINE.getMessage(EngineLang.TIME_SEC).replace("%s%", seconds).getLocalized());
        }

        return StringUtil.oneSpace(str.toString());
    }

    @NotNull
    public static String formatTimeLeft(long max, long min) {
        long time = max - min;
        return formatTime(time);
    }

    @NotNull
    public static String formatTimeLeft(long until) {
        return formatTime(until - System.currentTimeMillis());
    }

    @NotNull
    public static LocalTime getLocalTimeOf(long ms) {
        long hours = TimeUnit.MILLISECONDS.toHours(ms) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(ms) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60;

        return LocalTime.of((int) hours, (int) minutes, (int) seconds);
    }

    @NotNull
    public static LocalDateTime getLocalDateTimeOf(long ms) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), TimeZone.getDefault().toZoneId());
    }

    public static long toEpochMillis(@NotNull LocalDateTime dateTime) {
        Instant instant = dateTime.atZone(TimeZone.getDefault().toZoneId()).toInstant();
        return instant.toEpochMilli();
    }

    public static long getLongTimeOf(String value) {
        if (!value.matches("(.*)(m|w|k|n|d|p|h|t)(.*)")) {
            return 0;
        }

        String[] list = value.split("/(,?\\s+)|((?<=[a-z])(?=\\d))|((?<=\\d)(?=[a-z]))/i");

        long until = Instant.now().getEpochSecond();

        for (String str : list) {
            String name = str.replaceAll("\\d", "");
            int nr = Integer.parseInt(str.replaceAll("\\D", ""));

            if (name.equalsIgnoreCase("months") || name.equalsIgnoreCase("month")
                    || name.equalsIgnoreCase("mon")) {
                until += (long) nr * 60 * 60 * 24 * 7 * 4;
            } else if (name.equalsIgnoreCase("weeks") || name.equalsIgnoreCase("week")
                    || name.equalsIgnoreCase("w")) {
                until += (long) nr * 60 * 60 * 24 * 7;
            } else if (name.equalsIgnoreCase("days") || name.equalsIgnoreCase("day")
                    || name.equalsIgnoreCase("d")) {
                until += (long) nr * 60 * 60 * 24;
            } else if (name.equalsIgnoreCase("hours") || name.equalsIgnoreCase("hour")
                    || name.equalsIgnoreCase("h")) {
                until += (long) nr * 60 * 60;
            } else if (name.equalsIgnoreCase("minutes") || name.equalsIgnoreCase("minute")
                    || name.equalsIgnoreCase("min") || name.equalsIgnoreCase("m")) {
                until += nr * 60L;
            } else if (name.equalsIgnoreCase("seconds") || name.equalsIgnoreCase("second")
                    || name.equalsIgnoreCase("sec") || name.equalsIgnoreCase("s")) {
                until += nr;
            }
        }
        return until;
    }
}