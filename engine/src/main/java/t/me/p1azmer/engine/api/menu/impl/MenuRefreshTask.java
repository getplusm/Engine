package t.me.p1azmer.engine.api.menu.impl;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexEngine;
import t.me.p1azmer.engine.api.server.AbstractTask;

public class MenuRefreshTask extends AbstractTask<NexEngine> {

    public MenuRefreshTask(@NotNull NexEngine plugin) {
        super(plugin, 1, false);
    }

    @Override
    public void action() {
        Menu.PLAYER_MENUS.values().forEach(menu -> {
            if (menu.getOptions().isReadyToRefresh()) {
                menu.update();
                menu.getOptions().setLastAutoRefresh(System.currentTimeMillis());
            }
        });
    }
}