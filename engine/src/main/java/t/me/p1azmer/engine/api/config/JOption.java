package t.me.p1azmer.engine.api.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.utils.StringUtil;
import t.me.p1azmer.engine.utils.TriFunction;
import t.me.p1azmer.engine.utils.wrapper.UniFormatter;
import t.me.p1azmer.engine.utils.wrapper.UniParticle;
import t.me.p1azmer.engine.utils.wrapper.UniSound;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JOption<T> {

    String path;
    T defaultValue;
    String[] description;
    Reader<T> reader;
    Writer<T> writer;

    @NonFinal
    T value;
    @NonFinal
    UnaryOperator<T> onRead;

    public JOption(@NotNull String path,
                   @NotNull Reader<T> reader,
                   @NotNull Writer<T> writer,
                   @NotNull T defaultValue,
                   @Nullable String... description) {
        this.path = path;
        this.description = description == null ? new String[0] : description;
        this.reader = reader;
        this.writer = writer;
        this.defaultValue = defaultValue;
    }

    @NotNull
    private static <T> JOption<T> create(@NotNull String path,
                                         @NotNull Reader<T> reader,
                                         @NotNull Writer<T> writer,
                                         @NotNull T defaultValue,
                                         @Nullable String... description) {
        return new JOption<>(path, reader, writer, defaultValue, description);
    }

    @NotNull
    public static <T> JOption<T> create(@NotNull String path,
                                        @NotNull Reader<T> reader,
                                        @NotNull Writer<T> writer,

                                        @NotNull Supplier<T> defaultValue,
                                        @Nullable String... description) {
        return create(path, reader, writer, defaultValue.get(), description);
    }

    @NotNull
    public static <T> JOption<T> create(@NotNull String path, @NotNull Reader<T> reader, @NotNull T defaultValue, @Nullable String... description) {
        return create(path, reader, JYML::set, defaultValue, description);
    }

    @NotNull
    public static <T> JOption<T> create(@NotNull String path, @NotNull Reader<T> reader, @NotNull Supplier<T> defaultValue, @Nullable String... description) {
        return create(path, reader, JYML::set, defaultValue, description);
    }

    @NotNull
    public static JOption<Boolean> create(@NotNull String path, boolean defaultValue, @Nullable String... description) {
        return create(path, JYML::getBoolean, defaultValue, description);
    }

    @NotNull
    public static JOption<Integer> create(@NotNull String path, int defaultValue, @Nullable String... description) {
        return create(path, JYML::getInt, defaultValue, description);
    }

    @NotNull
    public static JOption<int[]> create(@NotNull String path, int[] defaultValue, @Nullable String... description) {
        return create(path, JYML::getIntArray, JYML::setIntArray, defaultValue, description);
    }

    @NotNull
    public static JOption<Double> create(@NotNull String path, double defaultValue, @Nullable String... description) {
        return create(path, JYML::getDouble, defaultValue, description);
    }

    @NotNull
    public static JOption<Long> create(@NotNull String path, long defaultValue, @Nullable String... description) {
        return create(path, JYML::getLong, defaultValue, description);
    }

    @NotNull
    public static JOption<String> create(@NotNull String path, @NotNull String defaultValue, @Nullable String... description) {
        return create(path, JYML::getString, defaultValue, description);
    }

    @NotNull
    public static JOption<String[]> create(@NotNull String path, @NotNull String[] defaultValue, @Nullable String... description) {
        return create(path, JYML::getStringArray, JYML::setStringArray, defaultValue, description);
    }

    @NotNull
    public static JOption<List<String>> create(@NotNull String path, @NotNull List<String> defaultValue, @Nullable String... description) {
        return create(path, (cfg, path1, def) -> cfg.getStringList(path1), defaultValue, description);
    }

    @NotNull
    public static JOption<Set<String>> create(@NotNull String path, @NotNull Set<String> defaultValue, @Nullable String... description) {
        return create(path, (cfg, path1, def) -> cfg.getStringSet(path1), defaultValue, description);
    }

    @NotNull
    public static JOption<ItemStack> create(@NotNull String path, @NotNull ItemStack defaultValue, @Nullable String... description) {
        return create(path, JYML::getItem, JYML::setItem, defaultValue, description);
    }

    @NotNull
    public static JOption<UniSound> create(@NotNull String path, @NotNull UniSound defaultValue, @Nullable String... description) {
        Reader<UniSound> reader = (cfg, path1, def) -> UniSound.read(cfg, path1);
        Writer<UniSound> writer = (cfg, path1, obj) -> obj.write(cfg, path1);

        return create(path, reader, writer, defaultValue, description);
    }

    @NotNull
    public static JOption<UniParticle> create(@NotNull String path, @NotNull UniParticle defaultValue, @Nullable String... description) {
        Reader<UniParticle> reader = (cfg, path1, def) -> UniParticle.read(cfg, path1);
        Writer<UniParticle> writer = (cfg, path1, obj) -> obj.write(cfg, path1);

        return create(path, reader, writer, defaultValue, description);
    }

    @NotNull
    public static JOption<UniFormatter> create(@NotNull String path, @NotNull UniFormatter defaultValue, @Nullable String... description) {
        Reader<UniFormatter> reader = (cfg, path1, def) -> UniFormatter.read(cfg, path1);
        Writer<UniFormatter> writer = (cfg, path1, obj) -> obj.write(cfg, path1);

        return create(path, reader, writer, defaultValue, description);
    }

    @NotNull
    public static <E extends Enum<E>> JOption<E> create(@NotNull String path, @NotNull Class<E> clazz, @NotNull E defaultValue, @Nullable String... description) {
        Reader<E> reader = (cfg, path1, def) -> cfg.getEnum(path1, clazz, def);
        Writer<E> writer = (cfg, path1, obj) -> cfg.set(path1, obj.name());

        return create(path, reader, writer, defaultValue, description);
    }

    @NotNull
    public static <V> JOption<Set<V>> forSet(@NotNull String path,
                                             @NotNull Function<String, V> reader,
                                             @NotNull Writer<Set<V>> writer,
                                             @NotNull Supplier<Set<V>> defaultValue,
                                             @Nullable String... description) {
        return forSet(path, reader, writer, defaultValue.get(), description);
    }

    @NotNull
    public static <V> JOption<Set<V>> forSet(@NotNull String path,
                                             @NotNull Function<String, V> valFun,
                                             @NotNull Writer<Set<V>> writer,
                                             @NotNull Set<V> defaultValue,
                                             @Nullable String... description) {

        Reader<Set<V>> reader = (cfg, path1, def) -> cfg.getStringSet(path1).stream().map(valFun).filter(Objects::nonNull).collect(Collectors.toCollection(HashSet::new));

        return create(path, reader, writer, defaultValue, description);
    }

    @NotNull
    public static <K, V> JOption<Map<K, V>> forMap(@NotNull String path,
                                                   @NotNull Function<String, K> keyFun,
                                                   @NotNull TriFunction<JYML, String, String, V> valFun,
                                                   @NotNull Writer<Map<K, V>> writer,
                                                   @NotNull Supplier<Map<K, V>> defaultValue,
                                                   @Nullable String... description) {
        return forMap(path, keyFun, valFun, writer, defaultValue.get(), description);
    }

    @NotNull
    public static <K, V> JOption<Map<K, V>> forMap(@NotNull String path,
                                                   @NotNull Function<String, K> keyFun,
                                                   @NotNull TriFunction<JYML, String, String, V> valFun,
                                                   @NotNull Writer<Map<K, V>> writer,
                                                   @NotNull Map<K, V> defaultValue,
                                                   @Nullable String... description) {
        return forMap(path, keyFun, valFun, HashMap::new, writer, defaultValue, description);
    }

    @NotNull
    public static <K, V> JOption<TreeMap<K, V>> forTreeMap(@NotNull String path,
                                                           @NotNull Function<String, K> keyFun,
                                                           @NotNull TriFunction<JYML, String, String, V> valFun,
                                                           @NotNull Writer<TreeMap<K, V>> writer,
                                                           @NotNull Supplier<TreeMap<K, V>> defaultValue, @Nullable String... description) {
        return forTreeMap(path, keyFun, valFun, writer, defaultValue.get(), description);
    }

    @NotNull
    public static <K, V> JOption<TreeMap<K, V>> forTreeMap(@NotNull String path,
                                                           @NotNull Function<String, K> keyFun,
                                                           @NotNull TriFunction<JYML, String, String, V> valFun,
                                                           @NotNull Writer<TreeMap<K, V>> writer,
                                                           @NotNull TreeMap<K, V> defaultValue, @Nullable String... description) {
        return forMap(path, keyFun, valFun, TreeMap::new, writer, defaultValue, description);
    }

    @NotNull
    public static <K, V, M extends Map<K, V>> JOption<M> forMap(@NotNull String path,
                                                                @NotNull Function<String, K> keyFun,
                                                                @NotNull TriFunction<JYML, String, String, V> valFun,
                                                                @NotNull Supplier<M> mapSupplier,
                                                                @NotNull Writer<M> writer,
                                                                @NotNull M defaultValue, @Nullable String... description) {
        Reader<M> reader = (cfg, path1, def) -> {
            M map = mapSupplier.get();
            for (String id : cfg.getSection(path1)) {
                K key = keyFun.apply(id);
                V val = valFun.apply(cfg, path1, id);
                if (key == null || val == null) continue;

                map.put(key, val);
            }
            return map;
        };

        return create(path, reader, writer, defaultValue, description);
    }

    @NotNull
    public static <K, V extends Writeable> JOption<Map<K, V>> forMap(@NotNull String path,
                                                                     @NotNull Function<String, K> keyReadFun,
                                                                     @NotNull Function<K, String> keyWriteFun,
                                                                     @NotNull BiFunction<JYML, String, V> valReadFun,
                                                                     //@NotNull Supplier<Map<K, V>> mapSupplier,
                                                                     @NotNull Consumer<Map<K, V>> defaultValue,
                                                                     @Nullable String... description) {
        Reader<Map<K, V>> reader = (cfg, readPath, def) -> {
            var map = new HashMap<K, V>();//mapSupplier.get();
            for (String keyRaw : cfg.getSection(readPath)) {
                K key = keyReadFun.apply(keyRaw);
                V val = valReadFun.apply(cfg, readPath + "." + keyRaw);
                if (key == null || val == null) continue;

                map.put(key, val);
            }
            return map;
        };

        Writer<Map<K, V>> writer = (cfg, writePath, map) -> {
            map.forEach((key, value) -> {
                String keyRaw = keyWriteFun.apply(key);
                value.write(cfg, writePath + "." + keyRaw);
            });
        };

        // Linked map used only to preserve the order of default values when writing it in the config.
        Map<K, V> defaultMap = new LinkedHashMap<>();
        defaultValue.accept(defaultMap);

        return create(path, reader, writer, defaultMap, description);
    }

//    @NotNull
//    public static <K, V extends Writeable> JOption<HashMap<K, V>> forHashMap(@NotNull String path,
//                                                                                 @NotNull Function<String, K> keyReadFun,
//                                                                                 @NotNull Function<K, String> keyWriteFun,
//                                                                                 @NotNull BiFunction<JYML, String, V> valReadFun,
//                                                                                 @NotNull Consumer<HashMap<K, V>> defaultValue,
//                                                                                 @Nullable String... description) {
//        return forMap(path, keyReadFun, keyWriteFun, valReadFun, HashMap::new, defaultValue, description);
//    }

    @NotNull
    public static <V extends Writeable> JOption<Map<String, V>> forMapById(@NotNull String path,
                                                                           @NotNull BiFunction<JYML, String, V> valReadFun,
                                                                           @NotNull Consumer<Map<String, V>> defaultValue,
                                                                           @Nullable String... description) {
        return forMap(path, String::toLowerCase, key -> key, valReadFun, defaultValue, description);
    }

    @NotNull
    public static <E extends Enum<E>, V extends Writeable> JOption<Map<E, V>> forMapByEnum(@NotNull String path,
                                                                                           @NotNull Class<E> clazz,
                                                                                           @NotNull BiFunction<JYML, String, V> valReadFun,
                                                                                           @NotNull Consumer<Map<E, V>> defaultValue,
                                                                                           @Nullable String... description) {
        return forMap(path, str -> StringUtil.getEnum(str, clazz).orElse(null), Enum::name, valReadFun, defaultValue, description);
    }

    @NotNull
    public static <V> JOption<Map<String, V>> forMap(@NotNull String path,
                                                     @NotNull TriFunction<JYML, String, String, V> function,
                                                     @NotNull Writer<Map<String, V>> writer,
                                                     @NotNull Supplier<Map<String, V>> defaultValue,
                                                     @Nullable String... description) {
        return forMap(path, String::toLowerCase, function, writer, defaultValue.get(), description);
    }

    @NotNull
    public static <V> JOption<Map<String, V>> forMap(@NotNull String path,
                                                     @NotNull TriFunction<JYML, String, String, V> function,
                                                     @NotNull Writer<Map<String, V>> writer,
                                                     @NotNull Map<String, V> defaultValue,
                                                     @Nullable String... description) {
        return forMap(path, String::toLowerCase, function, writer, defaultValue, description);
    }

    @NotNull
    public JOption<T> onRead(@NotNull UnaryOperator<T> onRead) {
        this.onRead = onRead;
        return this;
    }

    @NotNull
    public T read(@NotNull JYML config) {
        if (!config.contains(this.getPath())) {
            this.write(config);
        }
        if (this.getDescription().length > 0 && !this.getDescription()[0].isEmpty()) {
            config.setComments(this.getPath(), this.getDescription());
        }

        UnaryOperator<T> operator = this.onRead == null ? value -> value : this.onRead;

        return (this.value = operator.apply(this.reader.read(config, this.getPath(), this.getDefaultValue())));
    }

    public void write(@NotNull JYML config) {
        this.getWriter().write(config, this.getPath(), this.get());
    }

    public boolean remove(@NotNull JYML config) {
        return config.remove(this.getPath());
    }

    @NotNull
    public T get() {
        return this.value == null ? this.getDefaultValue() : this.value;
    }

    public interface Reader<T> {

        @NotNull T read(@NotNull JYML config, @NotNull String path, @NotNull T def);
    }

    public interface Writer<T> {

        void write(@NotNull JYML config, @NotNull String path, @NotNull T obj);
    }
}
