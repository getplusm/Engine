package t.me.p1azmer.folia;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
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
   * Accesses scheduling
   *
   * @return the scheduling wrapper
   */
  @NotNull
  @Contract(value = "-> new", pure = true)
  public GracefulScheduling scheduling() {
    if (this.scheduling == null)
      this.scheduling = new GracefulScheduling(this);
    return this.scheduling;
  }
}