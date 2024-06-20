package t.me.p1azmer.engine;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
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
import t.me.p1azmer.engine.utils.*;
import t.me.p1azmer.engine.utils.collections.Lists;
import t.me.p1azmer.playerBlockTracker.PlayerBlockTracker;

import java.util.HashSet;
import java.util.List;
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

        this.testMethods();
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
        if (EngineUtils.hasVault() && VaultHook.hasPermissions()) {
            mainCommand.addChildren(new CheckPermCommand(this));
        }
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

    private void testMethods() {
        if (Version.getCurrent() == Version.UNKNOWN) {
            this.warn("Server Version: UNSUPPORTED \u2718");
        }
        else this.info("Server Version: " + Version.getCurrent().getLocalized() + " \u2714");

        if (EntityUtil.setupEntityCounter(this)) {
            this.info("Entity Id Counter: OK \u2714");
        }
        else this.error("Entity Id Counter: FAIL \u2718");

        if (this.testItemNbt()) {
            this.info("Item NBT Compress: OK \u2714");
        }
        else this.error("Item NBT Compress: FAIL \u2718");
    }

    private boolean testItemNbt() {
        if (!ItemNbt.setup(this)) return false;

        ItemStack testItem = new ItemStack(Material.DIAMOND_SWORD);
        ItemUtil.editMeta(testItem, meta -> {
            meta.setDisplayName("Test Item");
            meta.setLore(Lists.newList("Test Lore 1", "Test Lore 2", "Test Lore 3"));
            meta.addEnchant(Enchantment.FIRE_ASPECT, 10, true);
        });

        String nbt = ItemNbt.compress(testItem);
        if (nbt == null) return false;

        ItemStack decompressed = ItemNbt.decompress(nbt);
        return decompressed != null && decompressed.isSimilar(testItem);
    }
}