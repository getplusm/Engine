package t.me.p1azmer.engine.api.menu.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.Version;
import t.me.p1azmer.engine.api.editor.EditorLocale;
import t.me.p1azmer.engine.api.editor.EditorLocales;
import t.me.p1azmer.engine.api.editor.InputHandler;
import t.me.p1azmer.engine.api.lang.LangKey;
import t.me.p1azmer.engine.api.lang.LangMessage;
import t.me.p1azmer.engine.api.menu.MenuItemType;
import t.me.p1azmer.engine.api.menu.click.ClickHandler;
import t.me.p1azmer.engine.api.menu.item.MenuItem;
import t.me.p1azmer.engine.editor.EditorManager;
import t.me.p1azmer.engine.utils.ItemUtil;

public class EditorMenu<P extends NexPlugin<P>, T> extends Menu<P> {

    protected final T object;

    public EditorMenu(@NotNull P plugin, @NotNull T object, @NotNull String title, int size) {
        super(plugin, title, size);
        this.object = object;
    }

    @NotNull
    protected MenuItem addNextPage(int... slots) {
        ItemStack item = ItemUtil.getSkinHead("f32ca66056b72863e98f7f32bd7d94c7a0d796af691c9ac3a9136331352288f9");
        MenuItem menuItem = this.addItem(item, EditorLocales.NEXT_PAGE, slots);
        menuItem.setType(MenuItemType.PAGE_NEXT);
        menuItem.setClick(ClickHandler.forNextPage(this));
        return menuItem;
    }

    @NotNull
    protected MenuItem addPreviousPage(int... slots) {
        ItemStack item = ItemUtil.getSkinHead("86971dd881dbaf4fd6bcaa93614493c612f869641ed59d1c9363a3666a5fa6");
        MenuItem menuItem = this.addItem(item, EditorLocales.PREVIOUS_PAGE, slots);
        menuItem.setType(MenuItemType.PAGE_PREVIOUS);
        menuItem.setClick(ClickHandler.forPreviousPage(this));
        return menuItem;
    }

    @NotNull
    protected MenuItem addReturn(int... slots) {
        ItemStack item = ItemUtil.getSkinHead("be9ae7a4be65fcbaee65181389a2f7d47e2e326db59ea3eb789a92c85ea46");
        return this.addItem(item, EditorLocales.RETURN, slots);
    }

    @NotNull
    protected MenuItem addCreation(@NotNull EditorLocale locale, int... slots) {
        ItemStack item = ItemUtil.getSkinHead("5ff31431d64587ff6ef98c0675810681f8c13bf96f51d9cb07ed7852b2ffd1");
        return this.addItem(item, locale, slots);
    }

    @NotNull
    protected MenuItem addExit(int... slots) {
        ItemStack item = ItemUtil.getSkinHead("27548362a24c0fa8453e4d93e68c5969ddbde57bf6666c0319c1ed1e84d89065");

        MenuItem menuItem = this.addItem(item, EditorLocales.CLOSE, slots);
        menuItem.setClick((viewer, event) -> viewer.getPlayer().closeInventory());
        return menuItem;
    }

    @NotNull
    public MenuItem addItem(@NotNull Material material, @NotNull EditorLocale locale, int... slots) {
        return this.addItem(new ItemStack(material), locale, slots);
    }

    @NotNull
    public MenuItem addItem(@NotNull ItemStack item, @NotNull EditorLocale locale, int... slots) {
        ItemUtil.editMeta(item, meta -> {
            meta.setDisplayName(locale.getLocalizedName());
            meta.setLore(locale.getLocalizedLore());
            meta.addItemFlags(ItemFlag.values());
        });

        MenuItem menuItem = new MenuItem(item).setPriority(100).setSlots(slots);
        this.addItem(menuItem);
        return menuItem;
    }

    public void handleInput(@NotNull MenuViewer viewer, @NotNull LangKey prompt, @NotNull InputHandler handler) {
        this.handleInput(viewer.getPlayer(), prompt, handler);
    }

    public void handleInput(@NotNull Player player, @NotNull LangKey prompt, @NotNull InputHandler handler) {
        this.handleInput(player, this.plugin.getMessage(prompt), handler);
    }

    public void handleInput(@NotNull Player player, @NotNull LangMessage prompt, @NotNull InputHandler handler) {
        EditorManager.prompt(player, prompt.getLocalized());
        EditorManager.startEdit(player, handler);
        this.plugin.runTask(task -> player.closeInventory());
    }

    @NotNull
    public T getObject() {
        return object;
    }
}