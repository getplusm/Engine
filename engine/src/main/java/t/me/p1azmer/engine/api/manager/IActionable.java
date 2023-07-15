package t.me.p1azmer.engine.api.manager;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.actions.ActionManipulator;

public interface IActionable {

    @NotNull
    public ActionManipulator getActions();
}
