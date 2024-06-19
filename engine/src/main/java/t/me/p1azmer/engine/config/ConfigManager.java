package t.me.p1azmer.engine.config;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.config.JOption;
import t.me.p1azmer.engine.api.config.JYML;
import t.me.p1azmer.engine.api.manager.AbstractManager;
import t.me.p1azmer.engine.lang.LangManager;
import t.me.p1azmer.engine.utils.Placeholders;

import static t.me.p1azmer.engine.utils.Colors2.*;

public class ConfigManager<P extends NexPlugin<P>> extends AbstractManager<P> {

  private JYML config;
  protected String filePath;

  public String pluginName;
  public String pluginPrefix;
  public String[] commandAliases;
  public String languageCode;

  public ConfigManager(@NotNull P plugin) {
    super(plugin);
    this.filePath = "config.yml";
  }

  @Override
  protected void onLoad() {
    this.config = JYML.loadOrExtract(this.plugin, this.filePath);

    this.pluginName = JOption.create("Plugin.Name", plugin.getName(),
                               "Localized plugin name. It's used in messages and with internal placeholders.")
                             .read(config);

    this.pluginPrefix = JOption.create("Plugin.Prefix", YELLOW + Placeholders.PLUGIN_NAME + GRAY + " » ",
                                 "Plugin prefix. Used in messages.",
                                 "You can use " + Placeholders.PLUGIN_NAME_LOCALIZED + " placeholder for a plugin name.")
                               .read(config).replace(Placeholders.PLUGIN_NAME, this.pluginName);

    this.commandAliases = JOption.create("Plugin.Command_Aliases", plugin.getName().toLowerCase(),
                                   "Command names that will be registered as main plugin commands.",
                                   "Do not leave this empty. Split multiple names with a comma.")
                                 .read(config).split(",");

    this.languageCode = JOption.create("Plugin.Language", "en",
                                 "Sets the plugin language.",
                                 "It will use language config from the '" + LangManager.DIR_LANG + "' sub-folder for specified language code.",
                                 "By default it's 'en', so 'messages_en.yml' will be used.")
                               .read(config).toLowerCase();

    this.config.saveChanges();
  }

  @Override
  protected void onShutdown() {

  }

  @NotNull
  public JYML getConfig() {
    return this.config;
  }

  @Deprecated
  public void extractResources(@NotNull String folder) {
    this.extractResources(folder, plugin.getDataFolder() + folder, false);
  }

  @Deprecated
  public void extractResources(@NotNull String jarPath, @NotNull String toPath) {
    this.extractResources(jarPath, toPath, false);
  }

  @Deprecated
  public void extractResources(@NotNull String jarPath, @NotNull String toPath, boolean override) {
    plugin().extractResources(jarPath, toPath);
  }
}