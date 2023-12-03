package t.me.p1azmer.engine.utils.txtanimation;

@Deprecated
public class Align {

    public static String execute(String text, int width, boolean right) {
        StringBuilder space = new StringBuilder();

        space.append(" ".repeat(Math.max(0, width - text.length())));

        return right ? space + text : text + space;
    }

}
