package t.me.p1azmer.engine.lang;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexEngine;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.config.JOption;
import t.me.p1azmer.engine.api.config.JYML;
import t.me.p1azmer.engine.api.editor.EditorLocale;
import t.me.p1azmer.engine.api.lang.LangKey;
import t.me.p1azmer.engine.api.lang.LangMessage;
import t.me.p1azmer.engine.api.manager.AbstractManager;
import t.me.p1azmer.engine.utils.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

public class LangManager<P extends NexPlugin<P>> extends AbstractManager<P> {

    public static final String DIR_LANG = "/lang/";

    protected static JYML assetsConfig;

    protected final Map<String, LangMessage> messages;
    @Deprecated protected final Map<String, String> placeholders;
    protected JYML config;

    public LangManager(@NotNull P plugin) {
        super(plugin);
        this.messages = new HashMap<>();
        this.placeholders = new HashMap<>();
    }

    @Override
    protected void onLoad() {
        String langCode = plugin.getConfigManager().languageCode;

        if (this.plugin.isEngine()) {
            this.downloadAssets(langCode);
            assetsConfig = JYML.loadOrExtract(plugin, DIR_LANG, "assets_" + langCode + ".yml");
        }

        this.plugin.getConfigManager().extractResources(DIR_LANG);
        this.config = JYML.loadOrExtract(plugin, DIR_LANG, "messages_" + langCode + ".yml");
        this.placeholders.putAll(JOption.forMap("Placeholders",
                (cfg, path, key) -> cfg.getString(path + "." + key, key),
                Map.of(
                        "%red%", Colors.RED,
                        "%green%", Colors.GREEN,
                        "%gray%", Colors.GRAY
                ),
                "Here you can create your own custom placeholders to use it in language config.",
                "Key = Placeholder, Value = Replacer."
        ).setWriter((cfg, path, map) -> map.forEach((place, value) -> cfg.set(path + "." + place, value))).read(this.config));

        // Inherit Engine messages
        if (!this.plugin.isEngine()) {
            EngineUtils.ENGINE.getLangManager().getMessages().forEach((key, message) -> {
                this.getMessages().put(key, new LangMessage(this.plugin, message.getRaw()));
            });
        }
    }

    private void downloadAssets(@NotNull String langCode) {
        File file = new File(plugin.getDataFolder().getAbsolutePath() + DIR_LANG, "assets_" + langCode + ".yml");
        if (file.exists()) return;

        FileUtil.create(file);

        this.plugin.info("Downloading assets for your language from github...");
        String url = "https://github.com/getplusm/Engine/raw/main/assets/" + langCode + ".yml";

        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        }
        catch (IOException exception) {
            //exception.printStackTrace();
            this.plugin.error("Could not download language assets for '" + langCode + "' (no such asset?).");
        }
    }

    @Override
    protected void onShutdown() {
        this.messages.clear();
        this.placeholders.clear();
    }

    public void loadDefaults() {
//        if (this.plugin.isEngine()) {
//            this.loadEnum(EntityType.class);
//            this.loadEnum(Material.class);
//
//            for (PotionEffectType type : PotionEffectType.values()) {
//                this.getConfig().addMissing("PotionEffectType." + type.getName(), StringUtil.capitalizeUnderscored(type.getName()));
//            }
//            for (Enchantment enchantment : Enchantment.values()) {
//                getEnchantment(enchantment);
//            }
//            for (World world : this.plugin.getServer().getWorlds()) {
//                getWorld(world);
//            }
//            this.getConfig().saveChanges();
//        }
    }

    @NotNull
    public JYML getConfig() {
        return config;
    }

    @NotNull
    public static JYML getAssetsConfig() {
        return assetsConfig;
    }

    @NotNull
    public Map<String, LangMessage> getMessages() {
        return messages;
    }

    @NotNull
    @Deprecated
    public Map<String, String> getPlaceholders() {
        return placeholders;
    }

    @NotNull
    public LangMessage getMessage(@NotNull LangKey key) {
        LangMessage message = this.getMessages().get(key.getPath());
        if (message == null) {
            message = this.loadMessage(key);
        }
        return message;
    }

    @NotNull
    public Optional<String> getMessage(@NotNull String path) {
        return Optional.ofNullable(this.getConfig().getString(path)).map(Colorizer::apply);
    }

    @NotNull
    private LangMessage loadMessage(@NotNull LangKey key) {
        if (this.write(key)) {
            this.getConfig().saveChanges();
        }

        List<String> list = this.getConfig().getStringList(key.getPath());
        String text = !list.isEmpty() ? String.join("\\n", list) : this.getConfig().getString(key.getPath(), "<Missing Message [" + key.getPath() + "]>");
        LangMessage message = new LangMessage(plugin, text);
        this.getMessages().put(key.getPath(), message);

        return message;
    }

    /**
     * Loads and sets missing messages from the specified class.
     * This method is used to generate the default language file or add new messages to it.
     *
     * @param clazz A class to load LangKey messages from.
     */
    public void loadMissing(@NotNull Class<?> clazz) {
        for (Field field : Reflex.getFields(clazz)) {
            if (!LangKey.class.isAssignableFrom(field.getType())) {
                continue;
            }

            LangKey langKey;
            try {
                langKey = (LangKey) field.get(this);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }

            // Do not load/set messages of super class(es) or if they are already present in the lang file.
            if (!field.getDeclaringClass().equals(clazz)) {
                continue;
            }

            // Clear old loaded messages.
            this.getMessages().remove(langKey.getPath());

            // For engine, we have to preload the message, so it can be added to child plugin's messages.
            if (this.plugin.isEngine()) {
                this.loadMessage(langKey);
            } else { // For child plugins, we can only write it to the config and not precache it.
                this.write(langKey);
            }
        }
        this.getConfig().saveChanges();
    }

    public void loadEditor(@NotNull Class<?> clazz) {
        for (Field field : Reflex.getFields(clazz)) {
            if (!EditorLocale.class.isAssignableFrom(field.getType())) {
                continue;
            }

            EditorLocale locale;
            try {
                locale = (EditorLocale) field.get(this);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }

            // Do not load/set messages of super class(es) or if they are already present in the lang file.
            if (!field.getDeclaringClass().equals(clazz)) {
                continue;
            }

            this.read(locale);
        }
        this.getConfig().saveChanges();
    }

    public void loadEnum(@NotNull Class<? extends Enum<?>> clazz) {
        if (!clazz.isEnum()) return;
        for (Object eName : clazz.getEnumConstants()) {
            String name = eName.toString();

            String path = clazz.getSimpleName() + "." + name;
            String val = StringUtil.capitalizeUnderscored(name);
            this.getConfig().addMissing(path, val);
        }
    }

    private boolean write(@NotNull LangKey key) {
        if (!this.getConfig().contains(key.getPath())) {
            String textDefault = key.getDefaultText();
            String[] textSplit = textDefault.split("\n");
            this.getConfig().set(key.getPath(), textSplit.length > 1 ? Arrays.asList(textSplit) : textDefault);
            return true;
        }
        return false;
    }

    private void read(@NotNull EditorLocale locale) {
        if (!this.getConfig().contains(locale.getKey())) {
            this.getConfig().set(locale.getKey() + ".Name", locale.getName());
            this.getConfig().set(locale.getKey() + ".Lore", locale.getLore());
        }
        locale.setLocalizedName(this.getConfig().getString(locale.getKey() + ".Name", locale.getName()));
        locale.setLocalizedLore(this.getConfig().getStringList(locale.getKey() + ".Lore"));
    }

    @NotNull
    public String getEnum(@NotNull Enum<?> e) {
        String path = e.getDeclaringClass().getSimpleName() + "." + e.name();
        String locEnum = this.getMessage(path).orElse(null);
        if (locEnum == null && !this.plugin.isEngine()) {
            return EngineUtils.ENGINE.getLangManager().getEnum(e);
        }
        return locEnum == null ? "null" : locEnum;
    }

    @NotNull
    public static String getPotionType(@NotNull PotionEffectType type) {
        return getAsset("PotionEffectType", type.getKey().getKey());
    }

    @NotNull
    public static String getEntityType(@NotNull EntityType type) {
        return getAsset("EntityType", type.getKey().getKey());
    }

    @NotNull
    public static String getMaterial(@NotNull Material type) {
        return getAsset("Material", type.getKey().getKey());
    }

    @NotNull
    public static String getWorld(@NotNull World world) {
        return getAsset("World", world.getName());
    }

    @NotNull
    public static String getEnchantment(@NotNull Enchantment enchantment) {
        return getAsset("Enchantment", enchantment.getKey().getKey());
    }

    @NotNull
    private static String getByObject(@NotNull String nameRaw, @NotNull String path) {
        LangManager<NexEngine> manager = EngineUtils.ENGINE.getLangManager();

        manager.getConfig().addMissing(path + "." + nameRaw, StringUtil.capitalizeUnderscored(nameRaw));
        manager.getConfig().saveChanges();

        return manager.getMessage(path + "." + nameRaw).orElse(nameRaw);
    }

    @NotNull
    public static String getBoolean(boolean b) {
        return EngineUtils.ENGINE.getLangManager().getMessage(b ? EngineLang.OTHER_YES : EngineLang.OTHER_NO).getLocalized();
    }

    @NotNull
    public static String getPlain(@NotNull LangKey key) {
        return EngineUtils.ENGINE.getLangManager().getMessage(key).getLocalized();
    }

    @NotNull
    public static Optional<String> getAsset(@NotNull String path) {
        return Optional.ofNullable(getAssetsConfig().getString(path)).map(Colorizer::apply);
    }

    @NotNull
    public static String getAsset(@NotNull String path, @NotNull String nameRaw) {
        getAssetsConfig().addMissing(path + "." + nameRaw, StringUtil.capitalizeUnderscored(nameRaw));
        getAssetsConfig().saveChanges();

        return getAsset(path + "." + nameRaw).orElse(nameRaw);
    }
}