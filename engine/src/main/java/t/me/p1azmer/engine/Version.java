package t.me.p1azmer.engine;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.utils.EntityUtil;
import t.me.p1azmer.engine.utils.ItemNbt;
import t.me.p1azmer.engine.utils.collections.Lists;
import t.me.p1azmer.engine.utils.version.VersionComponent;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Version {
    V1_17_R1("1.17.1", Status.OUTDATED),
    V1_18_R1("1.18.1", Status.OUTDATED),
    V1_18_R2("1.18.2", Status.OUTDATED),
    V1_19_R1("1.19.2", Status.OUTDATED),
    V1_19_R2("1.19.3", Status.OUTDATED),
    V1_19_R3("1.19.4", Status.OUTDATED),
    V1_20_R1("1.20.1", Status.OUTDATED),
    V1_20_R2("1.20.2"),
    V1_20_R3("1.20.4"),
    MC_1_20_6("1.20.6"),
    MC_1_21_0("1.21"),
    MC_1_21("1.21.1"),
    MC_1_21_2("1.21.2"),
    MC_1_21_3("1.21.3"),
    MC_1_21_4("1.21.4"),
    MC_1_21_5("1.21.5"),

    UNKNOWN("Unknown");


    public static final String CRAFTBUKKIT_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();
    private static final Set<VersionComponent> LOADED_COMPONENTS = new HashSet<>();

    @Getter
    private static Version current;

    String localized;
    Status status;

    Version(@NotNull String localized) {
        this(localized, Status.SUPPORTED);
    }

    @NotNull
    @Deprecated
    public static String getProtocol() {
        return Bukkit.getServer().getBukkitVersion();
    }

    public static void load(@NotNull NexEngine core) {
        String bukkitVersion = core.getServer().getBukkitVersion();
        String bukkitName = core.getServer().getName();
        String exact = bukkitVersion.split("-")[0];

        current = Stream.of(values()).sorted(Comparator.reverseOrder()).filter(version -> exact.equalsIgnoreCase(version.getLocalized())).findFirst().orElse(UNKNOWN);
        core.info("Server version detected as " + bukkitName + " " + current.getLocalized() + ".");

        loadComponents(core);
    }

    private static void loadComponents(@NotNull NexEngine core) {
        LOADED_COMPONENTS.clear();

        loadComponent(core, VersionComponent.ENTITY_ID_GENERATOR, EntityUtil.loadEntityCounter(core));
        loadComponent(core, VersionComponent.ITEM_NBT_COMPRESSOR, ItemNbt.load(core));

        if (hasComponent(VersionComponent.ITEM_NBT_COMPRESSOR) && !ItemNbt.test()) {
            core.error(VersionComponent.ITEM_NBT_COMPRESSOR.name() + ": Test failed.");
        }
    }

    private static void loadComponent(@NotNull NexEngine core, @NotNull VersionComponent component, boolean state) {
        if (state) {
            LOADED_COMPONENTS.add(component);
            core.info("[Core Components] " + component.name() + ": Loaded");
        } else core.error("[Core Components] " + component.name() + ": Error");
    }

    public static void printCaution(@NotNull NexPlugin<?> plugin) {
        if (current != UNKNOWN && current.isSupported()) return;

        plugin.warn("=".repeat(35));

        if (current == Version.UNKNOWN) {
            plugin.warn("WARNING: This plugin is not supposed to run on this server version!");
            plugin.warn("If server version is newer than " + values()[values().length - 1] + ", then wait for an update please.");
            plugin.warn("Otherwise this plugin will not work properly or even load.");
        } else if (current.isDeprecated()) {
            plugin.warn("WARNING: You're running an outdated server version (" + current.getLocalized() + ")!");
            plugin.warn("This version will no longer be supported in future relases.");
            plugin.warn("Please upgrade your server to " + Lists.next(current, (Version::isSupported)).getLocalized() + ".");
        } else if (current.isDropped()) {
            plugin.error("ERROR: You're running an unsupported server version (" + current.getLocalized() + ")!");
            plugin.error("Please upgrade your server to " + Lists.next(current, (Version::isSupported)).getLocalized() + ".");
        }

        plugin.warn("ABSOLUTELY NO DISCORD SUPPORT WILL BE PROVIDED");
        plugin.warn("=".repeat(35));
    }

    public static boolean hasComponent(@NotNull VersionComponent component) {
        return LOADED_COMPONENTS.contains(component);
    }

    public boolean isDeprecated() {
        return this.status == Status.OUTDATED;
    }

    public boolean isDropped() {
        return this.status == Status.DROPPED;
    }

    public boolean isSupported() {
        return this.status == Status.SUPPORTED;
    }

    public boolean isLower(@NotNull Version version) {
        return this.ordinal() < version.ordinal();
    }

    public boolean isHigher(@NotNull Version version) {
        return this.ordinal() > version.ordinal();
    }

    public static boolean isAtLeast(@NotNull Version version) {
        return version.isCurrent() || getCurrent().isHigher(version);
    }

    public static boolean isAbove(@NotNull Version version) {
        return getCurrent().isHigher(version);
    }

    public static boolean isBehind(@NotNull Version version) {
        return getCurrent().isLower(version);
    }

    public boolean isCurrent() {
        return this == Version.getCurrent();
    }

    public enum Status {
        SUPPORTED,
        OUTDATED,
        DROPPED
    }

}












