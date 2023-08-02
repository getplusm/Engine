package t.me.p1azmer.engine;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.actions.ActionManager;
import t.me.p1azmer.engine.api.command.GeneralCommand;
import t.me.p1azmer.engine.api.config.JYML;
import t.me.p1azmer.engine.api.data.UserDataHolder;
import t.me.p1azmer.engine.api.lang.LangKey;
import t.me.p1azmer.engine.api.lang.LangMessage;
import t.me.p1azmer.engine.api.menu.impl.Menu;
import t.me.p1azmer.engine.command.CommandManager;
import t.me.p1azmer.engine.command.PluginMainCommand;
import t.me.p1azmer.engine.config.ConfigManager;
import t.me.p1azmer.engine.lang.LangManager;
import t.me.p1azmer.engine.utils.CollectionsUtil;
import t.me.p1azmer.engine.utils.EngineUtils;
import t.me.p1azmer.engine.utils.Reflex;
import t.me.p1azmer.folia.Folia;

import java.io.File;
import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Logger;

public abstract class NexPlugin<P extends NexPlugin<P>> extends JavaPlugin {

    public static boolean isPaper = false;
    public static boolean isFolia = false;

    protected ConfigManager<P> configManager;
    protected LangManager<P> langManager;
    protected CommandManager<P> commandManager;
    protected ActionManager<P> actionManager;

    private Logger logger;
    private boolean isEngine;

    public final boolean isEngine() {
        return this.isEngine;
    }

    @NotNull
    protected abstract P getSelf();

    @NotNull
    @Override
    public File getFile() {
        return super.getFile();
    }

    @Override
    public final void onEnable() {
        long loadTook = System.currentTimeMillis();
        this.logger = this.getLogger();
        this.isEngine = this instanceof NexEngine;

        if (this.isEngine()) {
            if (!this.getServer().getVersion().contains("Spigot")) {
                isPaper = true;
                this.info("Seems like we have Paper based fork here...");
            }
            if (Folia.isFolia()) {
                isFolia = true;
                this.error("==================================");
                this.error("Attention! You have launched the Folia plugin.");
                this.error("Many functions may not work, since at the moment it is only being built to support it.");
                this.error("If you find a bug, please report it");
                this.error("https://github.com/getplusm/Engine/issues");
                this.error("==================================");
                Folia.setup(this);
            }
        } else {
            EngineUtils.ENGINE.addChildren(this);
        }

        if (Version.getCurrent().isDeprecated()) {
            this.warn("==================================");
            this.warn("WARNING: You're running an outdated/deprecated server version (" + Version.getCurrent().getLocalized() + ")!");
            this.warn("Support for this version will be dropped soon.");
            this.warn("Please, upgrade your server to at least " + CollectionsUtil.next(Version.getCurrent(), Predicate.not(Version::isDeprecated)).getLocalized() + ".");
            this.warn("==================================");
        }
        this.loadManagers();
        this.info("Plugin loaded in " + (System.currentTimeMillis() - loadTook) + " ms!");
    }

    @Override
    public final void onDisable() {
        this.unloadManagers();
    }

    public abstract void enable();

    public abstract void disable();

    public final void reload() {
        if (this.isEngine()) {
            this.loadConfig();
            this.loadLang();
            return;
        }
        this.unloadManagers();
        this.loadManagers();
    }

    @Override
    public final void reloadConfig() {
        this.getConfig().reload();
        this.loadConfig();
    }

    public final void reloadLang() {
        this.getLang().reload();
        this.loadLang();
    }

    public abstract void loadConfig();

    public abstract void loadLang();

    public abstract void registerHooks();

    public abstract void registerCommands(@NotNull GeneralCommand<P> mainCommand);

    public abstract void registerPermissions();

    public void registerPermissions(@NotNull Class<?> clazz) {
        for (Field field : Reflex.getFields(clazz)) {
            if (!Permission.class.isAssignableFrom(field.getType())) continue;
            if (!field.canAccess(null)) continue;

            try {
                Permission permission = (Permission) field.get(null);
                if (this.getPluginManager().getPermission(permission.getName()) == null) {
                    this.getPluginManager().addPermission(permission);
                }
            } catch (IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @NotNull
    public final JYML getConfig() {
        return this.getConfigManager().getConfig();
    }

    @NotNull
    public final JYML getLang() {
        return this.getLangManager().getConfig();
    }

    public final void info(@NotNull String msg) {
        this.logger.info(msg);
    }

    public final void warn(@NotNull String msg) {
        this.logger.warning(msg);
    }

    public final void error(@NotNull String msg) {
        this.logger.severe(msg);
    }

    private void unregisterListeners() {
        for (Player player : this.getServer().getOnlinePlayers()) {
            Menu<?> menu = Menu.getMenu(player);
            if (menu != null) {
                player.closeInventory();
            }
        }
        HandlerList.unregisterAll(this);
    }

    protected void loadManagers() {
        // Setup plugin Hooks.
        this.registerHooks();

        // Setup ConfigManager before any other managers.
        this.configManager = new ConfigManager<>(this.getSelf());
        this.configManager.setup();
        this.loadConfig();

        // Setup language manager after the main config.
        this.langManager = new LangManager<>(this.getSelf());
        this.langManager.setup();
        this.loadLang();
        this.getLangManager().loadDefaults();

        this.registerPermissions();

        // Register plugin commands.
        this.commandManager = new CommandManager<>(this.getSelf());
        this.commandManager.setup();

        // Register actions
        this.actionManager = new ActionManager<>(this.getSelf());
        this.actionManager.setup();

        // Connect to the database if present.
        UserDataHolder<?, ?> dataHolder = null;
        if (this instanceof UserDataHolder) {
            dataHolder = (UserDataHolder<?, ?>) this;
            if (!dataHolder.setupDataHandlers()) {
                this.error("Could not setup plugin Data Handler!");
                this.getPluginManager().disablePlugin(this);
                return;
            }
        }

        // Custom plugin loaders.
        this.enable();

        // Load plugin users only when full plugin is loaded.
        if (dataHolder != null) {
            dataHolder.getUserManager().loadOnlineUsers();
        }
    }

    private void unloadManagers() {
        if (isFolia) {
            Folia.shutdown();
        } else {
            this.getServer().getScheduler().cancelTasks(this); // First stop all plugin tasks
        }

        this.disable();
        if (this.commandManager != null) {
            this.commandManager.shutdown();
        }

        if (this.actionManager != null) {
            this.actionManager.shutdown();
        }

        // Unregister ALL plugin listeners.
        this.unregisterListeners();

        // Save user data and disconnect from the database.
        if (this instanceof UserDataHolder<?, ?> dataHolder) {
            dataHolder.getUserManager().shutdown();
            dataHolder.getData().shutdown();
        }

        this.getConfigManager().shutdown();
        this.getLangManager().shutdown();
    }

    @NotNull
    public final String getLabel() {
        return this.getLabels()[0];
    }

    @NotNull
    public final String[] getLabels() {
        return this.getConfigManager().commandAliases;
    }

    public final PluginMainCommand<P> getMainCommand() {
        return this.getCommandManager().getMainCommand();
    }

    @NotNull
    public final ConfigManager<P> getConfigManager() {
        return this.configManager;
    }

    @NotNull
    public final LangManager<P> getLangManager() {
        return this.langManager;
    }

    @NotNull
    public final LangMessage getMessage(@NotNull LangKey key) {
        return this.getLangManager().getMessage(key);
    }

    @NotNull
    public final CommandManager<P> getCommandManager() {
        return this.commandManager;
    }

    @NotNull
    public ActionManager<P> getActionManager() {
        return actionManager;
    }

    @NotNull
    public final BukkitScheduler getScheduler() {
        return this.getServer().getScheduler();
    }

    @NotNull
    public final PluginManager getPluginManager() {
        return this.getServer().getPluginManager();
    }

    public ClassLoader getClazzLoader() {
        return this.getClassLoader();
    }

    public void runTask(@NotNull Consumer<BukkitTask> consumer) {
        Runnable runnable = () -> consumer.accept(null);
        if (isFolia) {
            Folia.execute(runnable);
            return;
        }
        this.getScheduler().runTask(this, consumer);
    }

    public void runTaskAsync(@NotNull Consumer<BukkitTask> consumer) {
        Runnable runnable = () -> consumer.accept(null);
        if (isFolia) {
            Folia.executeAsync(runnable);
            return;
        }
        this.getScheduler().runTaskAsynchronously(this, consumer);
    }

    public void runTaskLater(@NotNull Consumer<BukkitTask> consumer, long delay) {
        Runnable runnable = () -> consumer.accept(null);
        if (isFolia) {
            Folia.executeLater(runnable, delay);
            return;
        }
        this.getScheduler().runTaskLater(this, consumer, delay);
    }

    public void runTaskLaterAsync(@NotNull Consumer<BukkitTask> consumer, long delay) {
        Runnable runnable = () -> consumer.accept(null);
        if (isFolia) {
            Folia.executeLaterAsync(runnable, delay);
            return;
        }
        this.getScheduler().runTaskLaterAsynchronously(this, consumer, delay);
    }

    public void runTaskTimer(@NotNull Consumer<BukkitTask> consumer, long delay, long interval) {
        Runnable runnable = () -> consumer.accept(null);
        if (isFolia) {
            Folia.executeTimer(runnable, delay);
            return;
        }
        this.getScheduler().runTaskTimer(this, consumer, delay, interval);
    }

    public void runTaskTimerAsync(@NotNull Consumer<BukkitTask> consumer, long delay, long interval) {
        Runnable runnable = () -> consumer.accept(null);
        if (isFolia) {
            Folia.executeTimerAsync(runnable, delay);
            return;
        }
        this.getScheduler().runTaskTimerAsynchronously(this, consumer, delay, interval);
    }
}