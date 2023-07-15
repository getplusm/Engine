package t.me.p1azmer.engine.integration.external;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import su.plo.voice.PlasmoVoice;

public class PlasmoVoiceHook {

    public boolean setup() {
        return Bukkit.getPluginManager().getPlugin("PlasmoVoice") != null;
    }

    public static PlasmoVoice getPlugin() {
        return PlasmoVoice.getInstance();
    }

    public static boolean hasVoiceChat(Player player) {
        return getPlugin().hasVoiceChat(player.getUniqueId());
    }
}
