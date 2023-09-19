package t.me.p1azmer.engine.actions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.actions.AbstractActionExecutor;
import t.me.p1azmer.engine.actions.actions.list.*;
import t.me.p1azmer.engine.actions.conditions.IConditionValidator;
import t.me.p1azmer.engine.actions.conditions.list.*;
import t.me.p1azmer.engine.actions.params.IParam;
import t.me.p1azmer.engine.actions.params.IParamType;
import t.me.p1azmer.engine.actions.params.defaults.IParamBoolean;
import t.me.p1azmer.engine.actions.params.defaults.IParamNumber;
import t.me.p1azmer.engine.actions.params.defaults.IParamString;
import t.me.p1azmer.engine.actions.params.list.AllowSelfParam;
import t.me.p1azmer.engine.actions.params.list.AttackableParam;
import t.me.p1azmer.engine.actions.params.list.LocationParam;
import t.me.p1azmer.engine.actions.params.list.OffsetParam;
import t.me.p1azmer.engine.actions.targets.ITargetSelector;
import t.me.p1azmer.engine.actions.targets.list.Target_FromSight;
import t.me.p1azmer.engine.actions.targets.list.Target_Radius;
import t.me.p1azmer.engine.actions.targets.list.Target_Self;
import t.me.p1azmer.engine.api.manager.AbstractManager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ActionManager<P extends NexPlugin<P>> extends AbstractManager<P> {

    private Map<String, AbstractActionExecutor> actionExecutors;
    private Map<String, IConditionValidator> conditionValidators;
    private Map<String, ITargetSelector> targetSelectors;
    private Map<String, IParam> params;
    private Map<String, ActionManipulator> manipulators;

    public ActionManager(@NotNull P plugin) {
        super(plugin);
    }

    @Override
    public void onLoad() {
        this.actionExecutors = new HashMap<>();
        this.conditionValidators = new HashMap<>();
        this.params = new HashMap<>();
        this.targetSelectors = new HashMap<>();
        this.manipulators = new HashMap<>();

        this.setupDefaults();
    }

    @Override
    public void onShutdown() {
        if (this.manipulators != null) {
            this.manipulators.clear();
            this.manipulators = null;
        }
        if (this.actionExecutors != null) {
            this.actionExecutors.clear();
            this.actionExecutors = null;
        }
        if (this.conditionValidators != null) {
            this.conditionValidators.clear();
            this.conditionValidators = null;
        }
        if (this.targetSelectors != null) {
            this.targetSelectors.clear();
            this.targetSelectors = null;
        }
        if (this.params != null) {
            this.params.clear();
            this.params = null;
        }
    }

    @NotNull
    public Collection<AbstractActionExecutor> getExecutors() {
        return this.actionExecutors.values();
    }

    @NotNull
    public Collection<IConditionValidator> getConditionValidators() {
        return this.conditionValidators.values();
    }

    @NotNull
    public Collection<IParam> getParams() {
        return this.params.values();
    }

    @NotNull
    public Collection<ITargetSelector> getTargetSelectors() {
        return this.targetSelectors.values();
    }

    @NotNull
    public Collection<AbstractActionExecutor> getActionExecutors() {
        return this.actionExecutors.values();
    }

    public void registerExecutor(@NotNull AbstractActionExecutor executor) {
        if (this.actionExecutors.put(executor.getKey(), executor) != null) {
            plugin.info("[Actions Engine] Replaced registered action executor '" + executor.getKey() + "' with a new one.");
        }
    }

    public void registerCondition(@NotNull IConditionValidator conditionValidator) {
        if (this.conditionValidators.put(conditionValidator.getKey(), conditionValidator) != null) {
            plugin.info("[Actions Engine] Replaced registered condition validator '" + conditionValidator.getKey() + "' with a new one.");
        }
    }

    public void registerParam(@NotNull IParam param) {
        if (this.params.put(param.getKey(), param) != null) {
            plugin.info("[Actions Engine] Replaced registered param '" + param.getKey() + "' with a new one.");
        }
    }

    public void registerTargetSelector(@NotNull ITargetSelector selector) {
        if (this.targetSelectors.put(selector.getKey(), selector) != null) {
            plugin.info("[Actions Engine] Replaced registered target selector '" + selector.getKey() + "' with a new one.");
        }
    }

    public void registerManipulator(@NotNull String id, @NotNull ActionManipulator manipulator) {
        if (this.manipulators.put(id.toLowerCase(), manipulator) != null) {
            plugin.info("[Actions Engine] Replaced registered Action Manipulator '" + id + "' with a new one.");
        }
    }

    @Nullable
    public ActionManipulator getManipulator(@NotNull String id) {
        return this.manipulators.getOrDefault(id.toLowerCase(), null);
    }

    public void unregisterManipulator(@NotNull String id) {
        this.manipulators.remove(id.toLowerCase());
    }

    @Nullable
    public Parametized getParametized(@NotNull ActionCategory category, @NotNull String key) {
        if (category == ActionCategory.TARGETS) return this.getTargetSelector(key);
        if (category == ActionCategory.CONDITIONS) return this.getConditionValidator(key);
        if (category == ActionCategory.ACTIONS) return this.getActionExecutor(key);
        return null;
    }

    @Nullable
    public Collection<? extends Parametized> getParametized(@NotNull ActionCategory category) {
        if (category == ActionCategory.TARGETS) return this.getTargetSelectors();
        if (category == ActionCategory.CONDITIONS) return this.getConditionValidators();
        if (category == ActionCategory.ACTIONS) return this.getActionExecutors();
        return Collections.emptySet();
    }

    @Nullable
    public AbstractActionExecutor getActionExecutor(@NotNull String key) {
        return this.actionExecutors.getOrDefault(key.toUpperCase(), null);
    }

    @Nullable
    public IConditionValidator getConditionValidator(@NotNull String key) {
        return this.conditionValidators.getOrDefault(key.toUpperCase(), null);
    }

    @Nullable
    public ITargetSelector getTargetSelector(@NotNull String key) {
        return this.targetSelectors.getOrDefault(key.toUpperCase(), null);
    }

    @Nullable
    public IParam getParam(@NotNull String key) {
        return this.params.getOrDefault(key.toUpperCase(), null);
    }

    private void setupDefaults() {
        this.registerParam(new AllowSelfParam());
        this.registerParam(new AttackableParam());
        this.registerParam(new IParamNumber(IParamType.AMOUNT, "amount"));
        this.registerParam(new IParamNumber(IParamType.DELAY, "delay"));
        this.registerParam(new IParamNumber(IParamType.DISTANCE, "distance"));
        this.registerParam(new IParamNumber(IParamType.DURATION, "duration"));
        this.registerParam(new IParamBoolean(IParamType.FILTER, "filter"));
        this.registerParam(new IParamString(IParamType.MESSAGE, "message"));
        this.registerParam(new IParamString(IParamType.NAME, "name"));
        this.registerParam(new IParamString(IParamType.TYPE, "type"));
        this.registerParam(new IParamNumber(IParamType.SPEED, "speed"));
        this.registerParam(new IParamString(IParamType.TARGET, "target"));
        this.registerParam(new IParamString(IParamType.TITLES_TITLE, "title"));
        this.registerParam(new IParamString(IParamType.TITLES_SUBTITLE, "subtitle"));
        this.registerParam(new IParamNumber(IParamType.TITLES_FADE_IN, "fadeIn"));
        this.registerParam(new IParamNumber(IParamType.TITLES_STAY, "stay"));
        this.registerParam(new IParamNumber(IParamType.TITLES_FADE_OUT, "fadeOut"));
        this.registerParam(new IParamString(IParamType.BAR_COLOR_EMPTY, "color-empty"));
        this.registerParam(new IParamString(IParamType.BAR_COLOR_FILL, "color-fill"));
        this.registerParam(new LocationParam());
        this.registerParam(new OffsetParam());

        // CONDITIONS //
        this.registerCondition(new Condition_WorldTime(this.plugin));
        this.registerCondition(new Condition_Permission(this.plugin));
        this.registerCondition(new Condition_VaultBalance(this.plugin));
        this.registerCondition(new Condition_EntityHealth(this.plugin));
        this.registerCondition(new Condition_EntityType(this.plugin));
//        this.registerCondition(new Condition_Group(this.plugin)); old custom group request

        // EXECUTORS //
        this.registerExecutor(new Action_ActionBar(this.plugin));
        this.registerExecutor(new Action_Broadcast(this.plugin));
        this.registerExecutor(new Action_Burn(this.plugin));
        this.registerExecutor(new Action_Damage(this.plugin));
        this.registerExecutor(new Action_Health(this.plugin));
        this.registerExecutor(new Action_Message(this.plugin));
        this.registerExecutor(new Action_CommandPlayer(this.plugin));
        this.registerExecutor(new Action_CommandConsole(this.plugin));
        this.registerExecutor(new Action_CommandOp(this.plugin));
        this.registerExecutor(new Action_Hook(this.plugin));
        this.registerExecutor(new Action_Lightning(this.plugin));
        this.registerExecutor(new Action_ParticleSimple(this.plugin));
        this.registerExecutor(new Action_Potion(this.plugin));
        this.registerExecutor(new Action_ProgressBar(this.plugin));
        this.registerExecutor(new Action_Projectile(this.plugin));
        this.registerExecutor(new Action_Throw(this.plugin));
        this.registerExecutor(new Action_Saturation(this.plugin));
        this.registerExecutor(new Action_Hunger(this.plugin));
        this.registerExecutor(new Action_Sound(this.plugin));
        this.registerExecutor(new Action_Teleport(this.plugin));
        this.registerExecutor(new Action_Titles(this.plugin));
        this.registerExecutor(new Action_Goto(this.plugin));

        // TARGETS //
        this.registerTargetSelector(new Target_FromSight(this.plugin));
        this.registerTargetSelector(new Target_Self(this.plugin));
        this.registerTargetSelector(new Target_Radius(this.plugin));
    }
}
