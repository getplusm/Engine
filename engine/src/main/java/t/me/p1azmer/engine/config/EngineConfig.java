package t.me.p1azmer.engine.config;

import t.me.p1azmer.engine.api.config.JOption;

public class EngineConfig {
    public static final JOption<Boolean> USER_DEBUG_ENABLED = JOption.create("UserData.Debug",
            false,
            "Enables debug messages for user data management.",
            "[Default is false]");

    public static final JOption<Integer> USER_CACHE_LIFETIME = JOption.create("UserData.Cache.LifeTime",
            300,
            "Sets how long (in seconds) user data will be cached for offline users",
            "until removed and needs to be loaded from the database again.",
            "[Default is 300 (5 minutes)]");

    public static final JOption<Boolean> USER_CACHE_NAME_AND_UUID = JOption.create("UserData.Cache.Names_And_UUIDs",
            true,
            "Sets whether or not plugin will cache player names and UUIDs.",
            "This will improve database performance when checking if user exists, but will increase memory usage.",
            "[Default is true]");

    public static final JOption<Boolean> RESPECT_PLAYER_DISPLAYNAME = JOption.create("Engine.Respect_Player_DisplayName",
            false,
            "Sets whether or not 'Player#getDisplayName' can be used to find & get players in addition to regular 'Player#getName'.",
            "This is useful if you want to use custom player nicknames in commands.",
            "(Works only for NexEngine based plugins.)",
            "[Default is false]");

    @Deprecated
    public static final JOption<Boolean> DATA_FIXER_ENABLED = JOption.create("DataFixer.Enabled",
            false,
            "When enabled, uses Mojang's DataFixer util to update ItemStacks from <= 1.20.4 NBT format to 1.20.5+ NBT format.",
            "IMPORTANT NOTE: You need to use this setting only once! Enable it, reboot the server and re-save all configurations that stores compressed item data: shops, crates, etc."
    );
}
