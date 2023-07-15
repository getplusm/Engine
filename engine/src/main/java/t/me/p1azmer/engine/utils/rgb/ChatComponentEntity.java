package t.me.p1azmer.engine.utils.rgb;

import com.google.common.base.Preconditions;

import java.util.UUID;

public class ChatComponentEntity extends IChatBaseComponent {

    private final String CONTENTS;

    public ChatComponentEntity(String type, UUID id, String name) {
        Preconditions.checkNotNull(type, "type");
        Preconditions.checkNotNull(id, "id");
        this.CONTENTS = String.format("{\"type\":\"%s\",\"id\":\"%s\",\"name\":{\"text\":\"%s\"}}", type, id, name);
    }

    @Override
    public String toRawText() {
        return toString();
    }

    @Override
    public String toString() {
        return CONTENTS;
    }
}
