package t.me.p1azmer.engine.api.server;

@Deprecated
public class JGroup {

    private final String luckPermsID;
    private final String name;
    private final int weight;
    private final String ru_displayName;
    private final String eng_displayName;

    private final String groupColor;
    private final String nameColor;
    private final String messageColor;

    private final String rgb_start;
    private final String rgb_middle;
    private final String rgb_end;

    public JGroup(String luckPermsID, String name, int weight, String ru_displayName, String eng_displayName, String groupColor, String nameColor, String messageColor, String rgb_start, String rgb_middle, String rgb_end) {
        this.luckPermsID = luckPermsID;
        this.name = name;
        this.weight = weight;
        this.ru_displayName = ru_displayName;
        this.eng_displayName = eng_displayName;
        this.groupColor = groupColor;
        this.nameColor = nameColor;
        this.messageColor = messageColor;
        this.rgb_start = rgb_start;
        this.rgb_middle = rgb_middle;
        this.rgb_end = rgb_end;
    }

    public String getLuckPermsID() {
        return luckPermsID;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public String getRu_displayName() {
        return ru_displayName;
    }

    public String getEng_displayName() {
        return eng_displayName;
    }

    public String getGroupColor() {
        return groupColor;
    }

    public String getNameColor() {
        return nameColor;
    }

    public String getMessageColor() {
        return messageColor;
    }

    public String getRgb_start() {
        return rgb_start;
    }

    public String getRgb_middle() {
        return rgb_middle;
    }

    public String getRgb_end() {
        return rgb_end;
    }

    public String getDisplayName(boolean ru) {
        return ru ? ru_displayName : eng_displayName;
    }

    public String getGroupPrefix(boolean ru) {
        return getGroupColor() + getDisplayName(ru) + nameColor + " ";
    }

    public String getGroupPrefixWithoutColor(boolean ru) {
        return getDisplayName(ru) + " ";
    }
}
