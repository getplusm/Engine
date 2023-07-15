package t.me.p1azmer.engine.task;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexEngine;
import t.me.p1azmer.engine.api.server.AbstractTask;
import t.me.p1azmer.engine.utils.MessageUtil;

import java.util.ArrayList;
import java.util.Map;

import static t.me.p1azmer.engine.utils.MessageUtil.sendSetSlotPacket;

public class MessageItemTask extends AbstractTask<NexEngine> {
    public MessageItemTask(@NotNull NexEngine plugin) {
        super(plugin, 2, true);
    }

    @Override
    public void action() {
        for (Map.Entry<Player, ItemStack> entry : new ArrayList<>(MessageUtil.ITEM_MESSAGE_CACHE.entrySet())) {
            Player player = entry.getKey();
            ItemStack item = entry.getValue();

            sendSetSlotPacket(player, player.getInventory().getHeldItemSlot(), item);

            MessageUtil.ITEM_MESSAGE_CACHE.remove(player);
        }
    }
}
