package t.me.p1azmer.engine.utils.rgb.format;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import t.me.p1azmer.engine.utils.rgb.EnumChatFormat;

/**
 * Formatter for &lt;#RRGGBB&gt;
 */
public class MiniMessageFormat implements RGBFormatter {

    @Override
    public String reformat(String text) {
        if (!text.contains("<")) return text;
        String format = text.replace(EnumChatFormat.RESET.getFormat(), ""); //remove &r from header/footer newline inserts
        try {
            return LegacyComponentSerializer.legacySection().serialize(MiniMessage.miniMessage().deserialize(format));
        } catch (Throwable ignored) {
            return text;
        }
    }
}