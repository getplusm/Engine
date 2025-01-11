package t.me.p1azmer.engine;

import com.cjcrafter.foliascheduler.FoliaCompatibility;
import com.cjcrafter.foliascheduler.ServerImplementation;
import com.cjcrafter.foliascheduler.util.ServerVersions;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import t.me.p1azmer.engine.utils.EngineUtils;
import t.me.p1azmer.engine.utils.FileUtil;
import t.me.p1azmer.engine.utils.Reflex;

import java.io.File;
import java.lang.reflect.Field;
import java.util.function.Consumer;

@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class NexPlugin<P extends NexPlugin<P>> extends JavaPlugin {
    public static boolean isPaper = false;
    public static boolean isFolia = false;

    ConfigManager<P> configManager;
    LangManager<P> langManager;
    CommandManager<P> commandManager;
    ActionManager<P> actionManager;
    @Getter @Nullable ServerImplementation foliaScheduler;

    final boolean isEngine = this instanceof NexEngine;

    @NotNull
    protected abstract P getSelf();

    @NotNull
    @Override
    public File getFile() {
        return super.getFile();
    }

    @Override
    public void onEnable() {
        long loadTook = System.currentTimeMillis();
        Version.printCaution(getSelf());

        if (Version.getCurrent().isDropped()) {
            getPluginManager().disablePlugin(this);
            return;
        }

        if (isEngine()) {
            if (ServerVersions.isPaper()) {
                isPaper = true;
            }
            if (ServerVersions.isFolia()) {
                isFolia = true;
                foliaScheduler = new FoliaCompatibility(this).getServerImplementation();
                error("""
                        ==================================
                        Attention! When using Folia, many functions of the plugin may not work.
                        You can report all errors here:
                        https://github.com/getplusm/Engine/issues
                        ==================================
                        """);
            }
        } else {
            EngineUtils.ENGINE.addChildren(this);
        }

        loadManagers();
        info("Plugin loaded in " + (System.currentTimeMillis() - loadTook) + " ms!");
    }

    @Override
    public void onDisable() {
        unloadManagers();
    }

    public abstract void enable();

    public abstract void disable();

    public final void reload() {
        if (isEngine()) {
            loadConfig();
            loadLang();
            return;
        }
        unloadManagers();
        loadManagers();
    }

    @Override
    public final void reloadConfig() {
        getConfig().reload();
        loadConfig();
    }

    public final void reloadLang() {
        getLang().reload();
        loadLang();
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
                if (getPluginManager().getPermission(permission.getName()) == null) {
                    getPluginManager().addPermission(permission);
                }
            } catch (IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @NotNull
    public final JYML getConfig() {
        return getConfigManager().getConfig();
    }

    @NotNull
    public final JYML getLang() {
        return getLangManager().getConfig();
    }

    public void info(@NotNull String msg) {
        getLogger().info(msg);
    }

    public void warn(@NotNull String msg) {
        getLogger().warning(msg);
    }

    public void error(@NotNull String msg) {
        getLogger().severe(msg);
    }

    public void debug(@NotNull String msg) {
        info("[DEBUG] " + msg);
    }

    private void unregisterListeners() {
        for (Player player : getServer().getOnlinePlayers()) {
            Menu<?> menu = Menu.getMenu(player);
            if (menu != null) {
                player.closeInventory();
            }
        }
        HandlerList.unregisterAll(this);
    }

    protected void loadManagers() {
        P self = getSelf();

        registerHooks();
        configManager = new ConfigManager<>(self);
        langManager = new LangManager<>(self);
        commandManager = new CommandManager<>(self);
        actionManager = new ActionManager<>(self);

        configManager.setup();
        loadConfig();
        langManager.setup();
        loadLang();
        getLangManager().loadDefaults();
        registerPermissions();
        commandManager.setup();
        actionManager.setup();

        UserDataHolder<?, ?> dataHolder = null;
        if (this instanceof UserDataHolder) {
            dataHolder = (UserDataHolder<?, ?>) this;
            if (!dataHolder.setupDataHandlers()) {
                error("Could not setup plugin Data Handler!");
                getPluginManager().disablePlugin(this);
                return;
            }
        }

        enable();
        if (dataHolder != null) {
            dataHolder.getUserManager().loadOnlineUsers();
        }
    }

    private void unloadManagers() {
        disable();
        if (commandManager != null) {
            commandManager.shutdown();
        }

        if (actionManager != null) {
            actionManager.shutdown();
        }

        // Unregister ALL plugin listeners.
        unregisterListeners();

        // Save user data and disconnect from the database.
        if (this instanceof UserDataHolder<?, ?> dataHolder) {
            dataHolder.getUserManager().shutdown();
            dataHolder.getData().shutdown();
        }

        getConfigManager().shutdown();
        getLangManager().shutdown();

        if (isFolia && foliaScheduler != null) foliaScheduler.cancelTasks();
        if (isPaper) getServer().getScheduler().cancelTasks(this); // First stop all plugin tasks
    }

    @NotNull
    public final String getLabel() {
        return getLabels()[0];
    }

    @NotNull
    public final String[] getLabels() {
        return getConfigManager().commandAliases;
    }

    public final PluginMainCommand<P> getMainCommand() {
        return getCommandManager().getMainCommand();
    }

    @NotNull
    public final LangMessage getMessage(@NotNull LangKey key) {
        return getLangManager().getMessage(key);
    }

    @NotNull
    public final BukkitScheduler getScheduler() {
        return getServer().getScheduler();
    }

    @NotNull
    public final PluginManager getPluginManager() {
        return getServer().getPluginManager();
    }

    public ClassLoader getClazzLoader() {
        return getClassLoader();
    }

    public void extractResources(@NotNull String jarPath) {
        extractResources(jarPath, getDataFolder() + jarPath);
    }

    public void extractResources(@NotNull String jarPath, @NotNull String targetPath) {
        File destination = new File(targetPath);
        if (destination.exists()) return;

        if (jarPath.startsWith("/")) {
            jarPath = jarPath.substring(1);
        }
        if (jarPath.endsWith("/")) {
            jarPath = jarPath.substring(0, jarPath.length() - 1);
        }

        FileUtil.extractResources(getFile(), jarPath, destination);
    }

    public void runTask(@NotNull Consumer<BukkitTask> consumer) {
        Runnable runnable = () -> consumer.accept(null);
        if (isFolia && foliaScheduler != null) {
            foliaScheduler.global().run(runnable);
            return;
        }
        getScheduler().runTask(this, consumer);
    }

    public void runTaskAsync(@NotNull Consumer<BukkitTask> consumer) {
        Runnable runnable = () -> consumer.accept(null);
        if (isFolia && foliaScheduler != null) {
            foliaScheduler.async().runNow(runnable);
            return;
        }
        getScheduler().runTaskAsynchronously(this, consumer);
    }

    public void runTaskLater(@NotNull Consumer<BukkitTask> consumer, long delay) {
        Runnable runnable = () -> consumer.accept(null);
        if (isFolia && foliaScheduler != null) {
            foliaScheduler.global().runDelayed(runnable, delay);
            return;
        }
        getScheduler().runTaskLater(this, consumer, delay);
    }

    public void runTaskLaterAsync(@NotNull Consumer<BukkitTask> consumer, long delay) {
        Runnable runnable = () -> consumer.accept(null);
        if (isFolia && foliaScheduler != null) {
            foliaScheduler.async().runDelayed(runnable, delay);
            return;
        }
        getScheduler().runTaskLaterAsynchronously(this, consumer, delay);
    }

    public void runTaskTimer(@NotNull Consumer<BukkitTask> consumer, long delay, long interval) {
        Runnable runnable = () -> consumer.accept(null);
        if (isFolia && foliaScheduler != null) {
            foliaScheduler.global().runAtFixedRate(runnable, delay, interval);
            return;
        }
        getScheduler().runTaskTimer(this, consumer, delay, interval);
    }

    public void runTaskTimerAsync(@NotNull Consumer<BukkitTask> consumer, long delay, long interval) {
        Runnable runnable = () -> consumer.accept(null);
        if (isFolia && foliaScheduler != null) {
            foliaScheduler.async().runAtFixedRate(runnable, delay, interval);
            return;
        }
        getScheduler().runTaskTimerAsynchronously(this, consumer, delay, interval);
    }
}