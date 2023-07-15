package t.me.p1azmer.engine.actions.params;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IParamValue {

    private int      integer   = -1;
    private double   duble     = -1;
    private double[] arrDouble = null;

    private boolean      bool = false;
    private String       str;
    private List<String> list;
    private IOperator    operator;

    private String raw;

    public static enum IOperator {
        GREATER(">"),
        LOWER("<"),
        EQUALS("="),
        ;

        public String val;

        private IOperator(@NotNull String val) {
            this.val = val;
        }

        @NotNull
        public static String clean(@NotNull String str) {
            for (IOperator o : values()) {
                str = str.replace(o.val, "");
            }
            return str;
        }

        @NotNull
        public static IOperator parse(@NotNull String str) {
            for (IOperator o : values()) {
                if (str.contains(o.val)) {
                    return o;
                }
            }
            return IOperator.EQUALS;
        }

        public boolean check(double from, double to) {
            switch (this) {
                case GREATER: {
                    return from > to;
                }
                case LOWER: {
                    return from < to;
                }
                case EQUALS: {
                    return from == to;
                }
            }
            return true;
        }
    }

    public IParamValue() {

    }

    public IParamValue(int i) {
        this.integer = i;
        this.setRaw(String.valueOf(i));
    }

    public IParamValue(double d) {
        this.duble = d;
        this.setRaw(String.valueOf(d));
    }

    public IParamValue(double[] arr) {
        this.arrDouble = arr;

        StringBuilder b = new StringBuilder();
        for (double d : arr) {
            if (b.length() > 0) b.append(",");
            b.append(String.valueOf(d));
        }
        this.setRaw(b.toString());
    }

    public IParamValue(boolean b) {
        this.bool = b;
        this.setRaw(String.valueOf(b));
    }

    public IParamValue(@NotNull String str) {
        this.str = str;
        this.setRaw(str);
    }

    public IParamValue(@NotNull List<String> list) {
        this.list = list;

        StringBuilder b = new StringBuilder();
        for (String s : list) {
            if (b.length() > 0) b.append(",");
            b.append(s);
        }
        this.setRaw(b.toString());
    }

    public IParamValue(@NotNull String[] str) {
        this(Arrays.asList(str));
    }

    @NotNull
    public String getRaw() {
        return raw;
    }

    public void setRaw(@NotNull String raw) {
        this.raw = raw;
    }

    //

    public int getInt(int def) {
        if (!this.hasInt()) return def;
        return this.integer;
    }

    public boolean hasInt() {
        return this.integer != -1;
    }

    public void setInt(int integer) {
        this.integer = integer;
    }

    //

    public double getDouble(double def) {
        if (!this.hasDouble()) return def;
        return this.duble;
    }

    public boolean hasDouble() {
        return this.duble != -1;
    }

    public void setDouble(double d) {
        this.duble = d;
    }

    //

    public double[] getDoubleArray() {
        if (!this.hasDoubleArray()) return new double[]{0, 0, 0};
        return this.arrDouble;
    }

    public boolean hasDoubleArray() {
        return this.arrDouble != null;
    }

    public void setDoubleArray(double[] arr) {
        this.arrDouble = arr;
    }

    //

    public boolean getBoolean() {
        return this.bool;
    }

    public void setBoolean(boolean b) {
        this.bool = b;
    }

    //

    public boolean hasOperator() {
        return this.operator != null;
    }

    public IOperator getOperator() {
        return this.operator;
    }

    public void setOperator(IOperator io) {
        this.operator = io;
        if (this.raw != null) {
            this.raw = this.operator.val + this.raw;
        }
    }

    //

    public String getString(String def) {
        if (!this.hasString()) return def;
        return this.str;
    }

    public boolean hasString() {
        return this.str != null;
    }

    public void setString(String str) {
        this.str = str;
    }

    //

    public List<String> getStringList() {
        if (!this.hasStringList()) return new ArrayList<>();
        return this.list;
    }

    public boolean hasStringList() {
        return this.list != null;
    }
}
