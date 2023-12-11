package t.me.p1azmer.engine;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.api.command.GeneralCommand;
import t.me.p1azmer.engine.api.editor.EditorLocales;
import t.me.p1azmer.engine.api.menu.impl.MenuListener;
import t.me.p1azmer.engine.api.menu.impl.MenuRefreshTask;
import t.me.p1azmer.engine.command.list.CheckPermCommand;
import t.me.p1azmer.engine.command.list.ReloadSubCommand;
import t.me.p1azmer.engine.config.EngineConfig;
import t.me.p1azmer.engine.editor.EditorManager;
import t.me.p1azmer.engine.integration.external.VaultHook;
import t.me.p1azmer.engine.lang.EngineLang;
import t.me.p1azmer.engine.utils.EngineUtils;
import t.me.p1azmer.engine.utils.Placeholders;
import t.me.p1azmer.playerBlockTracker.PlayerBlockTracker;

import java.util.HashSet;
import java.util.Set;

public class NexEngine extends NexPlugin<NexEngine> {

    private final Set<NexPlugin<?>> childrens = new HashSet<>();

    private EditorManager editorManager;
    private MenuListener menuListener;
    private MenuRefreshTask menuRefreshTask;

    @Override
    @NotNull
    protected NexEngine getSelf() {
        return this;
    }

    @Override
    public void enable() {
        this.menuListener = new MenuListener(this);
        this.menuListener.registerListeners();

        this.menuRefreshTask = new MenuRefreshTask(this);
        this.menuRefreshTask.start();

        this.editorManager = new EditorManager(this);
        this.editorManager.setup();
    }

    @Override
    public void disable() {
        if (this.editorManager != null) this.editorManager.shutdown();
        if (this.menuListener != null) this.menuListener.unregisterListeners();
        if (this.menuRefreshTask != null) this.menuRefreshTask.stop();

        if (EngineUtils.hasVault()) VaultHook.shutdown();
        PlayerBlockTracker.shutdown();
    }

    @Override
    public void registerHooks() {
        if (EngineUtils.hasVault()) {
            VaultHook.setup();
        }
    }

    @Override
    public void registerCommands(@NotNull GeneralCommand<NexEngine> mainCommand) {
        mainCommand.addChildren(new ReloadSubCommand<>(this, Placeholders.WILDCARD));
        mainCommand.addChildren(new CheckPermCommand(this));
    }

    @Override
    public void registerPermissions() {

    }

    @Override
    public void loadConfig() {
        this.getConfig().initializeOptions(EngineConfig.class);
    }

    @Override
    public void loadLang() {
        this.getLangManager().loadMissing(EngineLang.class);
        this.getLangManager().loadEditor(EditorLocales.class);
        this.getLang().saveChanges();
    }

    void addChildren(@NotNull NexPlugin<?> child) {
        this.childrens.add(child);
        child.info("Powered by: " + this.getName());
    }

    @NotNull
    public Set<NexPlugin<?>> getChildrens() {
        return this.childrens;
    }
}