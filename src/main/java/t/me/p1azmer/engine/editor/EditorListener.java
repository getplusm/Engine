package t.me.p1azmer.engine.editor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexEngine;
import t.me.p1azmer.engine.api.editor.InputHandler;
import t.me.p1azmer.engine.api.editor.InputWrapper;
import t.me.p1azmer.engine.api.manager.AbstractListener;
import t.me.p1azmer.engine.utils.Colorizer;
import t.me.p1azmer.engine.utils.StringUtil;

import java.util.HashSet;

public class EditorListener extends AbstractListener<NexEngine> {

    public EditorListener(@NotNull NexEngine plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e) {
        EditorManager.endEdit(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatText(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        InputHandler handler = EditorManager.getInputHandler(player);
        if (handler == null) return;

        event.getRecipients().clear();
        event.setCancelled(true);

        InputWrapper wrapper = new InputWrapper(event);

        this.plugin.runTask(task -> {
            if (wrapper.getTextRaw().equalsIgnoreCase(EditorManager.EXIT) || handler.handle(wrapper)) {
                EditorManager.endEdit(player);
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();

        InputHandler handler = EditorManager.getInputHandler(player);
        if (handler == null) return;

        e.setCancelled(true);

        String raw = e.getMessage();
        String text = Colorizer.apply(raw.substring(1));
        if (text.startsWith(EditorManager.VALUES)) {
            String[] split = text.split(" ");
            int page = split.length >= 2 ? StringUtil.getInteger(split[1], 0) : 0;
            boolean auto = split.length >= 3 && Boolean.parseBoolean(split[2]);
            EditorManager.displayValues(player, auto, page);
            return;
        }

        AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(true, player, text, new HashSet<>());
        InputWrapper wrapper = new InputWrapper(event);

        this.plugin.runTask(task -> {
            if (wrapper.getTextRaw().equalsIgnoreCase(EditorManager.EXIT) || handler.handle(wrapper)) {
                EditorManager.endEdit(player);
            }
        });
    }
}