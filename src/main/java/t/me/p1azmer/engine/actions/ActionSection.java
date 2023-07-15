package t.me.p1azmer.engine.actions;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.api.config.JYML;

import java.util.ArrayList;
import java.util.List;

public class ActionSection {

    private List<String> targetSelectors;
    private List<String> conditionList;
    private String conditionActionOnFail;
    private List<String> actionExecutors;

    private final String id;

    public ActionSection(
            @NotNull String id,
            @NotNull List<String> targetSelectors,
            @NotNull List<String> conditionList,
            @NotNull String conditionActionOnFail,
            @NotNull List<String> actionExecutors
    ) {
        this.id = id;
        this.targetSelectors = targetSelectors;
        this.conditionList = conditionList;
        this.conditionActionOnFail = conditionActionOnFail;
        this.actionExecutors = actionExecutors;
    }

    public ActionSection(@NotNull ActionSection from) {
        this(from.id, new ArrayList<>(from.getTargetSelectors()), new ArrayList<>(from.getConditions()), from.conditionActionOnFail, new ArrayList<>(from.getActionExecutors()));
        this.conditionList = new ArrayList<>(from.getConditions()); // New list
    }

    public void save(@NotNull JYML cfg, @NotNull String path) {
        if (!path.endsWith(this.id + "."))
            path = path + this.id + ".";
        if (!path.endsWith("."))
            path = path + ".";
        cfg.set(path + "Target_Selectors", this.getTargetSelectors());
        cfg.set(path + "Conditions.List", this.getConditions());
        cfg.set(path + "Action.Fail", this.getConditionFailActions());
        cfg.set(path + "Action.Executors", this.getActionExecutors());
        cfg.saveChanges();
    }

    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public List<String> getTargetSelectors() {
        return this.targetSelectors;
    }

    @NotNull
    public List<String> getConditions() {
        return this.conditionList;
    }

    @NotNull
    public String getConditionFailActions() {
        return this.conditionActionOnFail;
    }

    @NotNull
    public List<String> getActionExecutors() {
        return this.actionExecutors;
    }

    public void setActionExecutors(@NotNull List<String> actionExecutors) {
        this.actionExecutors = actionExecutors;
    }

    public void setConditions(@NotNull List<String> conditionsList) {
        this.conditionList = conditionsList;
    }

    public void setConditionActionOnFail(String conditionActionOnFail) {
        this.conditionActionOnFail = conditionActionOnFail;
    }

    public void setConditionList(List<String> conditionList) {
        this.conditionList = conditionList;
    }

    public void setTargetSelectors(List<String> targetSelectors) {
        this.targetSelectors = targetSelectors;
    }
}
