package t.me.p1azmer.folia;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.folia.scheduling.GracefulScheduling;

import java.util.Objects;

/**
 * Main usage point for MorePaperLib
 * taken from https://github.com/A248/MorePaperLib
 */
public class MorePaperLib {

    private final Plugin plugin;
    private GracefulScheduling scheduling;

    /**
     * Creates from a {@code Plugin} to use
     *
     * @param plugin the plugin to use
     */
    public MorePaperLib(Plugin plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin");
    }

    /**
     * Returns the plugin associated with this
     *
     * @return the plugin
     */
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * This method is used by MorePaperLib to detect the presence of certain features. It may
     * be used by callers as a convenience. It may also be overridden to force the usage of specific
     * API, but please note that the calling patterns used by MorePaperLib are strictly unspecified.
     *
     * @param clazz          the class in which to check
     * @param methodName     the name of the method
     * @param parameterTypes the raw parameter types
     * @return true if it exists
     */
    public boolean methodExists(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            clazz.getMethod(methodName, parameterTypes);
            return true;
        } catch (NoSuchMethodException ex) {
            return false;
        }
    }

    /**
     * Accesses scheduling
     *
     * @return the scheduling wrapper
     */
    @NotNull
    public GracefulScheduling scheduling() {
        if (this.scheduling == null)
            this.scheduling = new GracefulScheduling(this);
        return this.scheduling;
    }

}