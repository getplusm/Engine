package t.me.p1azmer.engine.utils.wrapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.api.config.JOption;
import t.me.p1azmer.engine.api.config.JYML;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UniFormatter {

    DecimalFormat formatter;
    String format;
    RoundingMode rounding;

    private UniFormatter(@NotNull String format, @NotNull RoundingMode rounding) {
        this.format = format;
        this.rounding = rounding;
        this.formatter = new DecimalFormat(format, new DecimalFormatSymbols(Locale.US));
        this.formatter.setRoundingMode(rounding);
    }

    @NotNull
    public static UniFormatter of(@NotNull String format, @NotNull RoundingMode rounding) {
        return new UniFormatter(format, rounding);
    }

    @NotNull
    public static UniFormatter read(@NotNull JYML config, @NotNull String path) {
        String format = JOption.create(path + ".Format", "#,###.##").read(config);
        RoundingMode rounding = JOption.create(path + ".Rounding", RoundingMode.class, RoundingMode.HALF_EVEN).read(config);

        return of(format, rounding);
    }

    public void write(@NotNull JYML config, @NotNull String path) {
        config.set(path + ".Format", this.getFormat());
        config.set(path + ".Rounding", this.getRounding().name().toLowerCase());
    }

    public String format(double value) {
        return this.formatter.format(value);
    }
}
