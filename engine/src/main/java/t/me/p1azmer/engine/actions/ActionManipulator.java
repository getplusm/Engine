package t.me.p1azmer.engine.actions;

import com.google.common.collect.Sets;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.actions.actions.AbstractActionExecutor;
import t.me.p1azmer.engine.actions.conditions.IConditionValidator;
import t.me.p1azmer.engine.actions.params.IParamResult;
import t.me.p1azmer.engine.actions.params.IParamType;
import t.me.p1azmer.engine.actions.targets.ITargetSelector;
import t.me.p1azmer.engine.api.config.JYML;
import t.me.p1azmer.engine.utils.Constants;
import t.me.p1azmer.engine.utils.EngineUtils;

import java.util.*;
import java.util.function.UnaryOperator;

public class ActionManipulator {

    private final NexPlugin<?> plugin;
    private final Map<String, ActionSection> actions;

    public ActionManipulator(@NotNull NexPlugin<?> plugin, @NotNull ActionManipulator copy) {
        this.plugin = plugin;
        this.actions = new LinkedHashMap<>();
        for (Map.Entry<String, ActionSection> en : copy.getActions().entrySet()) {
            this.actions.put(en.getKey(), new ActionSection(en.getValue()));
        }
    }

    public ActionManipulator(@NotNull NexPlugin<?> plugin) {
        this.plugin = plugin;
        this.actions = new LinkedHashMap<>();
    }

    public ActionManipulator(@NotNull NexPlugin<?> plugin, @NotNull JYML cfg, @NotNull String path) {
        this.plugin = plugin;
        this.actions = new LinkedHashMap<>();

        for (String id : cfg.getSection(path)) {
            String path2 = path + "." + id + ".";

            if (cfg.isSet(path2 + "conditions.list")) {
                List<String> list = cfg.getStringList(path2 + "conditions.list");
                cfg.set(path2 + "conditions.list", null);
                ActionManipulator.this.plugin.warn("Replaced old 'conditions.list' to new 'Conditions.List' formatting.");
                cfg.set(path2 + "Conditions.List", list);
                cfg.saveChanges();
            }
            if (cfg.isSet(path2 + "action-executors")) {
                List<String> list = cfg.getStringList(path2 + "action-executors");
                cfg.set(path2 + "action-executors", null);
                ActionManipulator.this.plugin.warn("Replaced old 'action-executors' to new 'Action.Executors' formatting.");
                cfg.set(path2 + "Action.Executors", list);
                cfg.saveChanges();
            }
            if (cfg.isSet(path2 + "target-selectors")) {
                List<String> list = cfg.getStringList(path2 + "target-selectors");
                cfg.set(path2 + "target-selectors", null);
                ActionManipulator.this.plugin.warn("Replaced old 'target-selectors' to new 'Target_Selectors' formatting.");
                cfg.set(path2 + "Target_Selectors", list);
                cfg.saveChanges();
            }
            if (cfg.isSet(path2 + "conditions.actions-on-fail")) {
                String name = cfg.getString(path2 + "conditions.actions-on-fail");
                cfg.set(path2 + "conditions.actions-on-fail", null);
                ActionManipulator.this.plugin.warn("Replaced old 'conditions.actions-on-fail' to new 'Action.Fail' formatting.");
                cfg.set(path2 + "Action.Fail", name);
                cfg.saveChanges();
            }
            if (cfg.isSet(path2 + "Actions.Fail")) {
                String name = cfg.getString(path2 + "Actions.Fail");
                cfg.set(path2 + "Actions.Fail", null);
                ActionManipulator.this.plugin.warn("Replaced old 'Actions.Fail' to new 'Action.Fail' formatting.");
                cfg.set(path2 + "Action.Fail", name);
                cfg.saveChanges();
            }
            if (cfg.isSet(path2 + "Conditions.Fail_Actions")) {
                String name = cfg.getString(path2 + "Conditions.Fail_Actions");
                cfg.set(path2 + "Conditions.Fail_Actions", null);
                ActionManipulator.this.plugin.warn("Replaced old 'Conditions.Fail_Actions' to new 'Action.Fail' formatting.");
                cfg.set(path2 + "Action.Fail", name);
                cfg.saveChanges();
            }
            if (cfg.isSet(path2 + "Action_Executors")) {
                List<String> list = cfg.getStringList(path2 + "Action_Executors");
                cfg.set(path2 + "Action_Executors", null);
                ActionManipulator.this.plugin.warn("Replaced old 'Action_Executors' to new 'Action.Executors' formatting.");
                cfg.set(path2 + "Action.Executors", list);
                cfg.saveChanges();
            }
            // Update Target Selectors format to the new one.
            if (!cfg.getSection(path2 + "Target_Selectors").isEmpty()) {
                List<String> selectorsUpdated = new ArrayList<>();
                for (String selectorId : cfg.getSection(path2 + "Target_Selectors")) {
                    List<String> selectors = cfg.getStringList(path2 + "Target_Selectors." + selectorId);
                    selectors.replaceAll(str -> str + " ~name: " + selectorId + ";");
                    selectorsUpdated.addAll(selectors);
                }
                cfg.set(path2 + "Target_Selectors", null);
                ActionManipulator.this.plugin.warn("Replaced old 'Target_Selectors' to new 'Target_Selectors' formatting.");
                cfg.set(path2 + "Target_Selectors", selectorsUpdated);
                cfg.saveChanges();
            }

            List<String> targetSelectors = cfg.getStringList(path2 + "Target_Selectors");
            List<String> conditionList = cfg.getStringList(path2 + "Conditions.List");
            String conditionActionOnFail = cfg.getString(path2 + "Action.Fail", "");
            List<String> actionExecutors = cfg.getStringList(path2 + "Action.Executors");

            ActionSection engine = new ActionSection(
                    id, targetSelectors, conditionList, conditionActionOnFail, actionExecutors);
            this.actions.put(id.toLowerCase(), engine);
        }
    }

    public ActionManipulator(@NotNull JYML cfg, @NotNull String path) {
        this.plugin = EngineUtils.ENGINE;
        this.actions = new LinkedHashMap<>();

        for (String id : cfg.getSection(path)) {
            String path2 = path + "." + id + ".";

            if (cfg.isSet(path2 + "conditions.list")) {
                List<String> list = cfg.getStringList(path2 + "conditions.list");
                cfg.set(path2 + "conditions.list", null);
                plugin.warn("Replaced old 'conditions.list' to new 'Conditions.List' formatting.");
                cfg.set(path2 + "Conditions.List", list);
                cfg.saveChanges();
            }
            if (cfg.isSet(path2 + "action-executors")) {
                List<String> list = cfg.getStringList(path2 + "action-executors");
                cfg.set(path2 + "action-executors", null);
                plugin.warn("Replaced old 'action-executors' to new 'Action.Executors' formatting.");
                cfg.set(path2 + "Action.Executors", list);
                cfg.saveChanges();
            }
            if (cfg.isSet(path2 + "target-selectors")) {
                List<String> list = cfg.getStringList(path2 + "target-selectors");
                cfg.set(path2 + "target-selectors", null);
                plugin.warn("Replaced old 'target-selectors' to new 'Target_Selectors' formatting.");
                cfg.set(path2 + "Target_Selectors", list);
                cfg.saveChanges();
            }
            if (cfg.isSet(path2 + "conditions.actions-on-fail")) {
                String name = cfg.getString(path2 + "conditions.actions-on-fail");
                cfg.set(path2 + "conditions.actions-on-fail", null);
                plugin.warn("Replaced old 'conditions.actions-on-fail' to new 'Action.Fail' formatting.");
                cfg.set(path2 + "Action.Fail", name);
                cfg.saveChanges();
            }
            if (cfg.isSet(path2 + "Actions.Fail")) {
                String name = cfg.getString(path2 + "Actions.Fail");
                cfg.set(path2 + "Actions.Fail", null);
                plugin.warn("Replaced old 'Actions.Fail' to new 'Action.Fail' formatting.");
                cfg.set(path2 + "Action.Fail", name);
                cfg.saveChanges();
            }
            if (cfg.isSet(path2 + "Conditions.Fail_Actions")) {
                String name = cfg.getString(path2 + "Conditions.Fail_Actions");
                cfg.set(path2 + "Conditions.Fail_Actions", null);
                plugin.warn("Replaced old 'Conditions.Fail_Actions' to new 'Action.Fail' formatting.");
                cfg.set(path2 + "Action.Fail", name);
                cfg.saveChanges();
            }
            if (cfg.isSet(path2 + "Action_Executors")) {
                List<String> list = cfg.getStringList(path2 + "Action_Executors");
                cfg.set(path2 + "Action_Executors", null);
                plugin.warn("Replaced old 'Action_Executors' to new 'Action.Executors' formatting.");
                cfg.set(path2 + "Action.Executors", list);
                cfg.saveChanges();
            }
            // Update Target Selectors format to the new one.
            if (!cfg.getSection(path2 + "Target_Selectors").isEmpty()) {
                List<String> selectorsUpdated = new ArrayList<>();
                for (String selectorId : cfg.getSection(path2 + "Target_Selectors")) {
                    List<String> selectors = cfg.getStringList(path2 + "Target_Selectors." + selectorId);
                    selectors.replaceAll(str -> str + " ~name: " + selectorId + ";");
                    selectorsUpdated.addAll(selectors);
                }
                cfg.set(path2 + "Target_Selectors", null);
                plugin.warn("Replaced old 'Target_Selectors' to new 'Target_Selectors' formatting.");
                cfg.set(path2 + "Target_Selectors", selectorsUpdated);
                cfg.saveChanges();
            }

            List<String> targetSelectors = cfg.getStringList(path2 + "Target_Selectors");
            List<String> conditionList = cfg.getStringList(path2 + "Conditions.List");
            String conditionActionOnFail = cfg.getString(path2 + "Action.Fail", "");
            List<String> actionExecutors = cfg.getStringList(path2 + "Action.Executors");

            ActionSection engine = new ActionSection(
                    id, targetSelectors, conditionList, conditionActionOnFail, actionExecutors);
            this.actions.put(id.toLowerCase(), engine);
        }
    }

    @NotNull
    public ActionManipulator replace(@NotNull UnaryOperator<String> func) {
        ActionManipulator manipulatorCopy = new ActionManipulator(plugin, this);

        for (ActionSection copyEngine : manipulatorCopy.getActions().values()) {
            copyEngine.getActionExecutors().replaceAll(func);
            copyEngine.getConditions().replaceAll(func);
            copyEngine.getTargetSelectors().replaceAll(func);
        }

        return manipulatorCopy;
    }

    @NotNull
    public Map<String, ActionSection> getActions() {
        return this.actions;
    }

    public void processAll(@NotNull Entity executor) {
        this.getActions().values().forEach(section -> this.process(executor, section.getId()));
    }

    public void process(@NotNull Entity exec) {
        this.process(exec, Collections.emptyMap());
    }

    public void process(@NotNull Entity exec, @NotNull Map<String, Set<Entity>> targetMap2) {
        if (this.actions.isEmpty()) return;
        String id = new ArrayList<>(this.actions.keySet()).get(0);
        this.process(exec, id, targetMap2);
    }

    public void process(@NotNull Entity exec, @NotNull String id) {
        this.process(exec, id, Collections.emptyMap());
    }

    public void process(@NotNull Entity exec, @NotNull String id, @NotNull Map<String, Set<Entity>> targetMap2) {
        ActionSection ae = this.actions.get(id.toLowerCase());
        if (ae == null) return;

        Map<String, Set<Entity>> targetMap = new HashMap<>();
        targetMap2.forEach((fromKey, fromVal) -> {
            targetMap.merge(fromKey, fromVal, (old, now) -> {
                Set<Entity> set = new HashSet<>(old);
                set.addAll(now);
                return set;
            });
        });

        Player p = null;
        if (EngineUtils.hasPlaceholderAPI() && exec instanceof Player) {
            p = (Player) exec;
        }

        // Precache target selectors for actions
        for (String selector : ae.getTargetSelectors()) {
            if (p != null) selector = PlaceholderAPI.setPlaceholders(p, selector);

            String selectorKey = selector.split(" ")[0].replace("[", "").replace("]", "");
            ITargetSelector targetSelector = plugin.getActionManager().getTargetSelector(selectorKey);
            if (targetSelector == null) {
                plugin.error("Invalid target selector '" + selectorKey + "' in '" + selector + "' !");
                continue;
            }
            IParamResult result = targetSelector.getParamResult(selector);
            String targetId = result.getParamValue(IParamType.NAME).getString(Constants.DEFAULT);
            Set<Entity> targets = new HashSet<>();

            targetSelector.select(exec, targets, selector);

            // We use merge instead of single set to prevent targets from
            // different selectors being removed due to different params.
            targetMap.merge(targetId, targets, (old, now) -> {
                Set<Entity> set = new HashSet<>(old);
                set.addAll(now);
                return set;
            });
        }


        // Check conditions
        for (String condition : ae.getConditions()) {
            if (p != null) {
                condition = PlaceholderAPI.setPlaceholders(p, condition);
            }

            String key = condition.split(" ")[0].replace("[", "").replace("]", "");
            IConditionValidator validator = plugin.getActionManager().getConditionValidator(key);
            if (validator == null) {
                plugin.error("Invalid condition validator '" + key + "' in '" + condition + "' !");
                continue;
            }

            if (!validator.process(exec, targetMap, condition, this)) {
                this.process(exec, ae.getConditionFailActions());
                return;
            }
        }

        // Run actions
        for (String action : ae.getActionExecutors()) {
            if (p != null) {
                action = PlaceholderAPI.setPlaceholders(p, action);
            }

            String key = action.split(" ")[0].replace("[", "").replace("]", "");
            AbstractActionExecutor executor = plugin.getActionManager().getActionExecutor(key);
            if (executor == null) {
                plugin.error("Invalid action executor '" + key + "' in '" + action + "' !");
                continue;
            }

            executor.process(exec, targetMap, action, this);
        }
    }

    public static boolean processConditions(@NotNull NexPlugin<?> plugin, @NotNull Entity exec, @NotNull List<String> condis) {
        return processConditions(plugin, exec, condis, Collections.emptyMap());
    }

    public static boolean processConditions(
            @NotNull NexPlugin<?> plugin,
            @NotNull Entity exec,
            @NotNull List<String> condis,
            @NotNull Map<String, Set<Entity>> targetMap2) {

        Map<String, Set<Entity>> targetMap = new HashMap<>();
        targetMap.put(Constants.DEFAULT, Sets.newHashSet(exec));
        targetMap2.forEach((fromKey, fromVal) -> {
            targetMap.merge(fromKey, fromVal, (old, now) -> {
                Set<Entity> set = new HashSet<>(old);
                set.addAll(now);
                return set;
            });
        });

        Player p = null;
        if (EngineUtils.hasPlaceholderAPI() && exec instanceof Player) {
            p = (Player) exec;
        }

        // Check conditions
        for (String condition : condis) {
            if (p != null) {
                condition = PlaceholderAPI.setPlaceholders(p, condition);
            }

            String key = condition.split(" ")[0].replace("[", "").replace("]", "");
            IConditionValidator validator = plugin.getActionManager().getConditionValidator(key);
            if (validator == null) {
                plugin.error("Invalid condition validator '" + key + "' in '" + condition + "' !");
                continue;
            }

            if (!validator.process(exec, targetMap, condition)) {
                return false;
            }
        }
        return true;
    }
}
