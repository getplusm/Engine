package t.me.p1azmer.engine;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.api.command.GeneralCommand;
import t.me.p1azmer.engine.api.editor.EditorLocales;
import t.me.p1azmer.engine.api.menu.impl.MenuListener;
import t.me.p1azmer.engine.editor.EditorManager;
import t.me.p1azmer.engine.integration.external.VaultHook;
import t.me.p1azmer.engine.lang.EngineLang;
import t.me.p1azmer.engine.utils.EngineUtils;

import java.util.HashSet;
import java.util.Set;

public class NexEngine extends NexPlugin<NexEngine> {

    private final Set<NexPlugin<?>> childrens = new HashSet<>();

    private EditorManager editorManager;
    private MenuListener menuListener;

    @Override
    @NotNull
    protected NexEngine getSelf() {
        return this;
    }

    @Override
    public void enable() {
        this.menuListener = new MenuListener(this);
        this.menuListener.registerListeners();

        this.editorManager = new EditorManager(this);
        this.editorManager.setup();
    }

    @Override
    public void disable() {
        if (this.editorManager != null) {
            this.editorManager.shutdown();
            this.editorManager = null;
        }
        if (this.menuListener != null) {
            this.menuListener.unregisterListeners();
            this.menuListener = null;
        }

        if (EngineUtils.hasVault()) VaultHook.shutdown();
    }

    @Override
    public void registerHooks() {
        if (EngineUtils.hasVault()) {
            VaultHook.setup();
        }
    }

    @Override
    public void registerCommands(@NotNull GeneralCommand<NexEngine> mainCommand) {

    }

    @Override
    public void registerPermissions() {

    }

    @Override
    public void loadConfig() {

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