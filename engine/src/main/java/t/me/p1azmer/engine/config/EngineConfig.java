package t.me.p1azmer.engine.config;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.node.NodeType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.NexEngine;
import t.me.p1azmer.engine.api.config.JYML;
import t.me.p1azmer.engine.api.server.JGroup;
import t.me.p1azmer.engine.integration.external.LuckpermsHook;
import t.me.p1azmer.engine.utils.EngineUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Deprecated
public class EngineConfig {

    private static Map<String, JGroup> SERVER_GROUPS;

    public static JGroup DEFAULT_GROUP;


    public static void load(@NotNull NexEngine engine) {
        JYML cfg = engine.getConfig();

        // Deprecated code
        // create default group
        if (cfg.getSection("JGroup").isEmpty()) {
            cfg.createSection("JGroup");
            cfg.addMissing("JGroup", "default");
            cfg.addMissing("JGroup.default.name", "default");
            cfg.addMissing("JGroup.default.weight", 1);
            cfg.addMissing("JGroup.default.ruDisplayName", "Игрок");
            cfg.addMissing("JGroup.default.engDisplayName", "Player");
            cfg.addMissing("JGroup.default.groupColor", "&7");
            cfg.addMissing("JGroup.default.nameColor", "&7");
            cfg.addMissing("JGroup.default.messageColor", "&8");
            cfg.addMissing("JGroup.default.rgb.start", "&8");
            cfg.addMissing("JGroup.default.rgb.middle", "&8");
            cfg.addMissing("JGroup.default.rgb.end", "&8");
        }

        cfg.setComments("JGroup", "Кастомные группы для сервера. Они синхронизированы с luckperms");
        //
        LuckPerms perms = LuckpermsHook.getPlugin();
        GroupManager groupManager = perms.getGroupManager();
        SERVER_GROUPS = new HashMap<>();
        cfg.getSection("JGroup").forEach(luckPermsID -> {
            String path = "JGroup." + luckPermsID + ".";
            JGroup group = new JGroup(luckPermsID, cfg.getString(path + "name"), cfg.getInt(path + "weight"),
                    cfg.getString(path + "ruDisplayName"), cfg.getString(path + "engDisplayName"), cfg.getString(path + "groupColor")
                    , cfg.getString(path + "nameColor"), cfg.getString(path + "messageColor")
                    , cfg.getString(path + "rgb.start"), cfg.getString(path + "rgb.middle"), cfg.getString(path + "rgb.end"));
            engine.info("Loaded " + group.getLuckPermsID() + ". " + (groupManager.isLoaded(group.getLuckPermsID()) ? "its loaded on luckperms" : "without luckperms"));
            SERVER_GROUPS.put(luckPermsID, group);
        });
        DEFAULT_GROUP = SERVER_GROUPS.getOrDefault("default", new JGroup("default", "default", 1, "Игрок", "Player", "&7", "&7", "&8", "&8", "&8", "&8"));
        cfg.saveChanges();
        SERVER_GROUPS.values().forEach(group -> {
            if (groupManager.isLoaded(group.getLuckPermsID()))
                return;
            try {
                Group createdGroup = groupManager.createAndLoadGroup(group.getLuckPermsID()).get();
                groupManager.modifyGroup(createdGroup.getName(), group1 -> {
                    group1.getNodes(NodeType.WEIGHT).forEach(weightNode -> {
                        weightNode.toBuilder().weight(group.getWeight()).build();
                    });
                    group1.getNodes(NodeType.DISPLAY_NAME).forEach(displayNameNode -> {
                        displayNameNode.toBuilder().displayName(group.getDisplayName(false)).build();
                    });
                    group1.getNodes(NodeType.PREFIX).forEach(prefixNode -> {
                        prefixNode.toBuilder().prefix(group.getGroupPrefix(false)).build();
                    });
                });
                groupManager.saveGroup(createdGroup);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @NotNull
    public static UUID getIdForSkullTexture(@NotNull String base64) {
        JYML cfg = EngineUtils.ENGINE.getConfig();

        UUID uuid;
        String idRaw = cfg.getString("Head_Texture_Cache." + base64, UUID.randomUUID().toString());
        try {
            uuid = UUID.fromString(idRaw);
        } catch (IllegalArgumentException e) {
            uuid = UUID.randomUUID();
        }

        cfg.addMissing("Head_Texture_Cache." + base64, uuid.toString());
        cfg.saveChanges();

        return uuid;
    }

    @Nullable
    public static JGroup getGroup(String luckpermsID) {
        return SERVER_GROUPS.getOrDefault(luckpermsID, null);
    }

    @NotNull
    public static JGroup getGroup(int weight) {
        return SERVER_GROUPS.values().stream().filter(f -> f.getWeight() == weight).findFirst().orElseGet(() -> DEFAULT_GROUP);
    }

    public static Map<String, JGroup> getServerGroups() {
        return SERVER_GROUPS;
    }
}