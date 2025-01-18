package t.me.p1azmer.engine.api.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.Version;
import t.me.p1azmer.engine.utils.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class JYML extends YamlConfiguration {
    static final String EXTENSION = ".yml";
    final File file;
    boolean changed;

    public JYML(@NotNull String path, @NotNull String file) {
        this(new File(path, file));
    }

    public JYML(@NotNull File file) {
        this.changed = false;
        this.options().width(512);

        FileUtil.create(file);
        this.file = file;
        this.reload();
    }

    public static boolean isConfig(@NotNull File file) {
        return file.getName().endsWith(EXTENSION);
    }

    @NotNull
    public static String getName(@NotNull File file) {
        String name = file.getName();

        if (isConfig(file)) {
            return name.substring(0, name.length() - EXTENSION.length());
        }
        return name;
    }


    @NotNull
    public static JYML loadOrExtract(@NotNull NexPlugin<?> plugin, @NotNull String path, @NotNull String file) {
        if (!path.endsWith("/")) {
            path += "/";
        }
        return loadOrExtract(plugin, path + file);
    }

    @NotNull
    public static JYML loadOrExtract(@NotNull NexPlugin<?> plugin, @NotNull String filePath) {
        if (!filePath.startsWith("/")) {
            filePath = "/" + filePath;
        }

        File file = new File(plugin.getDataFolder() + filePath);
        if (FileUtil.create(file)) {
            try (InputStream input = plugin.getClass().getResourceAsStream(filePath)) {
                if (input != null) FileUtil.copy(input, file);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return new JYML(file);
    }

    @NotNull
    public static List<JYML> loadAll(@NotNull String path) {
        return loadAll(path, false);
    }

    @NotNull
    public static List<JYML> loadAll(@NotNull String path, boolean deep) {
        return FileUtil.getConfigFiles(path, deep).stream().map(JYML::new).toList();
    }

    public void initializeOptions(@NotNull Class<?> clazz) {
        initializeOptions(clazz, this);
    }

    public static void initializeOptions(@NotNull Class<?> clazz, @NotNull JYML config) {
        for (JOption<?> value : Reflex.getFields(clazz, JOption.class)) {
            value.read(config);
        }
    }

    @NotNull
    public File getFile() {
        return this.file;
    }

    public void save() {
        try {
            this.save(this.file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public boolean saveChanges() {
        if (this.changed) {
            this.save();
            this.changed = false;
            return true;
        }
        return false;
    }

    public boolean reload() {
        try {
            this.load(this.file);
            this.changed = false;
            return true;
        } catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public boolean addMissing(@NotNull String path, @Nullable Object val) {
        if (this.contains(path)) return false;
        this.set(path, val);
        return true;
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        if (value instanceof Writeable writeable) {
            writeable.write(this, path);
            this.changed = true;
            return;
        } else if (value instanceof String str) {
            value = Colorizer.plain(str);
        } else if (value instanceof Collection<?> collection) {
            List<Object> list = new ArrayList<>(collection);
            list.replaceAll(obj -> obj instanceof String str ? Colorizer.plain(str) : obj);
            value = list;
        } else if (value instanceof Location location) {
            value = LocationUtil.serialize(location);
        } else if (value instanceof Enum<?> en) {
            value = en.name();
        }
        super.set(path, value);
        this.changed = true;
    }

    public void setComments(@NotNull String path, @Nullable String... comments) {
        this.setComments(path, Arrays.asList(comments));
    }

    public void setInlineComments(@NotNull String path, @Nullable String... comments) {
        this.setInlineComments(path, Arrays.asList(comments));
    }

    @Override
    public void setComments(@NotNull String path, @Nullable List<String> comments) {
        if (Version.isBehind(Version.V1_18_R2)) return;
        if (this.getComments(path).equals(comments)) return;

        super.setComments(path, comments);
        this.changed = true;
    }

    @Override
    public void setInlineComments(@NotNull String path, @Nullable List<String> comments) {
        if (Version.isBehind(Version.V1_18_R2)) return;
        super.setInlineComments(path, comments);
    }

    public boolean remove(@NotNull String path) {
        if (!this.contains(path)) return false;
        this.set(path, null);
        return true;
    }

    @NotNull
    public Set<String> getSection(@NotNull String path) {
        ConfigurationSection section = this.getConfigurationSection(path);
        return section == null ? Collections.emptySet() : section.getKeys(false);
    }

    @Override
    @Nullable
    public String getString(@NotNull String path) {
        String str = super.getString(path);
        return str == null || str.isEmpty() ? null : str;
    }

    @Override
    @NotNull
    public String getString(@NotNull String path, @Nullable String def) {
        String str = super.getString(path, def);
        return str == null ? "" : str;
    }

    @NotNull
    public Set<String> getStringSet(@NotNull String path) {
        return new HashSet<>(this.getStringList(path));
    }

    @Override
    public @Nullable Location getLocation(@NotNull String path) {
        String raw = this.getString(path);
        return raw == null ? null : LocationUtil.deserialize(raw);
    }

    public int[] getIntArray(@NotNull String path) {
        return getIntArray(path, new int[0]);
    }

    public int[] getIntArray(@NotNull String path, int[] def) {
        String str = this.getString(path);
        return str == null ? def : NumberUtil.getIntArray(str);
    }

    public void setIntArray(@NotNull String path, int[] arr) {
        if (arr == null) {
            this.set(path, null);
            return;
        }
        this.set(path, String.join(",", IntStream.of(arr).boxed().map(String::valueOf).toList()));
    }

    public @NotNull String[] getStringArray(@NotNull String path, @NotNull String[] def) {
        String str = this.getString(path);
        return str == null ? def : str.split(",");
    }

    public void setStringArray(@NotNull String path, String[] arr) {
        if (arr == null) {
            this.set(path, null);
            return;
        }
        this.set(path, String.join(",", arr));
    }

    public int[] getMenuSlots(@NotNull String path) {
        String input = Objects.requireNonNull(getString(path));
        if (input.contains("-") || input.contains(",")) {
            String[] ranges = input.split(",");
            List<Integer> slots = new ArrayList<>();

            for (String range : ranges) {
                if (range.contains("-")) {
                    String[] split = range.split("-");
                    if (split.length == 2) {
                        int start = NumberUtil.getInteger(split[0].trim(), -1);
                        int end = NumberUtil.getInteger(split[1].trim(), -1);

                        if (start >= 0 && start < 54 && end > 0 && end <= 54) {
                            for (int i = start; i <= end; i++) {
                                slots.add(i);
                            }
                        }
                    }
                } else {
                    int value = NumberUtil.getInteger(range.trim(), -1);
                    if (value != -1) {
                        slots.add(value);
                    }
                }
            }

            if (!slots.isEmpty()) {
                return slots.stream().mapToInt(i -> i).toArray();
            }
        }

        return getIntArray(path);
    }

    public @Nullable <T extends Enum<T>> T getEnum(@NotNull String path, @NotNull Class<T> clazz) {
        return StringUtil.getEnum(this.getString(path), clazz).orElse(null);
    }

    public @NotNull <T extends Enum<T>> T getEnum(@NotNull String path, @NotNull Class<T> clazz, @NotNull T def) {
        return StringUtil.getEnum(this.getString(path), clazz).orElse(def);
    }

    public @NotNull <T extends Enum<T>> List<T> getEnumList(@NotNull String path, @NotNull Class<T> clazz) {
        return this.getStringSet(path).stream().map(str -> StringUtil.getEnum(str, clazz).orElse(null))
                .filter(Objects::nonNull).toList();
    }

    public @NotNull ItemStack getItem(@NotNull String path, @Nullable ItemStack def) {
        ItemStack item = this.getItem(path);
        return item.getType().isAir() && def != null ? def : item;
    }

    public @NotNull ItemStack getItem(@NotNull String path) {
        if (!path.isEmpty() && !path.endsWith(".")) path = path + ".";

        Material material = BukkitThing.getMaterial(this.getString(path + "Material", BukkitThing.toString(Material.AIR)));
        if (material == null || material.isAir()) return new ItemStack(Material.AIR);

        ItemStack item = new ItemStack(material);
        item.setAmount(this.getInt(path + "Amount", 1));

        // -------- UPDATE OLD TEXTURE FIELD - START --------
        String headTexture = this.getString(path + "Head_Texture");
        if (headTexture != null && !headTexture.isEmpty()) {

            try {
                byte[] decoded = Base64.getDecoder().decode(headTexture);
                String decodedStr = new String(decoded, StandardCharsets.UTF_8);
                JsonElement element = JsonParser.parseString(decodedStr);

                String url = element.getAsJsonObject().getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
                url = url.substring(ItemUtil.TEXTURES_HOST.length());

                this.set(path + "SkinURL", url);
                this.remove(path + "Head_Texture");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        // -------- UPDATE OLD TEXTURE FIELD - END --------

        String headSkin = this.getString(path + "SkinURL");
        if (headSkin != null) {
            ItemUtil.setHeadSkin(item, headSkin);
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        int durability = this.getInt(path + "Durability");
        if (durability > 0 && meta instanceof Damageable damageable) {
            damageable.setDamage(durability);
        }

        String name = this.getString(path + "Name");
        meta.setDisplayName(Optional.ofNullable(name).map(Colorizer::apply).orElse(null));
        meta.setLore(Optional.of(this.getStringList(path + "Lore")).map(Colorizer::apply).orElse(null));

        for (String sKey : this.getSection(path + "Enchants")) {
            Enchantment enchantment = BukkitThing.getEnchantment(sKey);
            if (enchantment == null) continue;

            int level = this.getInt(path + "Enchants." + sKey);
            if (level <= 0) continue;

            meta.addEnchant(enchantment, level, true);
        }

        int model = this.getInt(path + "Custom_Model_Data");
        meta.setCustomModelData(model != 0 ? model : null);

        List<String> flags = this.getStringList(path + "Item_Flags");
        if (flags.contains(Placeholders.WILDCARD)) {
            meta.addItemFlags(ItemFlag.values());
        } else {
            flags.stream().map(str -> StringUtil.getEnum(str, ItemFlag.class).orElse(null)).filter(Objects::nonNull).forEach(meta::addItemFlags);
        }

        String colorRaw = this.getString(path + "Color");
        if (colorRaw != null && !colorRaw.isEmpty()) {
            Color color = StringUtil.getColor(colorRaw);
            if (meta instanceof LeatherArmorMeta armorMeta) {
                armorMeta.setColor(color);
            } else if (meta instanceof PotionMeta potionMeta) {
                potionMeta.setColor(color);
            }
        }

        meta.setUnbreakable(this.getBoolean(path + "Unbreakable"));
        item.setItemMeta(meta);

        return item;
    }

    public void setItem(@NotNull String path, @Nullable ItemStack item) {
        if (item == null) {
            this.set(path, null);
            return;
        }

        if (!path.endsWith(".")) path = path + ".";
        this.set(path.substring(0, path.length() - 1), null);

        Material material = item.getType();
        this.set(path + "Material", material.name());
        this.set(path + "Amount", item.getAmount() <= 1 ? null : item.getAmount());
        this.set(path + "SkinURL", ItemUtil.getHeadSkin(item));

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        if (meta instanceof Damageable damageable) {
            this.set(path + "Durability", damageable.getDamage() <= 0 ? null : damageable.getDamage());
        }

        this.set(path + "Name", meta.getDisplayName().isEmpty() ? null : meta.getDisplayName());
        this.set(path + "Lore", meta.getLore());

        for (Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet()) {
            this.set(path + "Enchants." + entry.getKey().getKey().getKey(), entry.getValue());
        }
        this.set(path + "Custom_Model_Data", meta.hasCustomModelData() ? meta.getCustomModelData() : null);

        Color color = null;
        String colorRaw = null;
        if (meta instanceof PotionMeta potionMeta) {
            color = potionMeta.getColor();
        } else if (meta instanceof LeatherArmorMeta armorMeta) {
            color = armorMeta.getColor();
        }
        if (color != null) {
            colorRaw = color.getRed() + "," + color.getGreen() + "," + color.getBlue();
        }
        this.set(path + "Color", colorRaw);

        List<String> itemFlags = new ArrayList<>(meta.getItemFlags().stream().map(ItemFlag::name).toList());
        this.set(path + "Item_Flags", itemFlags.isEmpty() ? null : itemFlags);
        this.set(path + "Unbreakable", meta.isUnbreakable() ? true : null);
    }

    @Nullable
    public ItemStack getItemEncoded(@NotNull String path) {
        String compressed = this.getString(path);
        if (compressed == null) return null;

        return ItemNbt.decompress(compressed);
    }

    public void setItemEncoded(@NotNull String path, @Nullable ItemStack item) {
        this.set(path, item == null ? null : ItemNbt.compress(item));
    }

    @NotNull
    public ItemStack[] getItemsEncoded(@NotNull String path) {
        return ItemNbt.decompress(this.getStringList(path));
    }

    public void setItemsEncoded(@NotNull String path, @NotNull List<ItemStack> item) {
        this.set(path, ItemNbt.compress(item));
    }

    @Nullable
    public Recipe getRecipe(@NotNull String path) {
        return getRecipe(path, null, null);
    }

    @Nullable
    public Recipe getRecipe(@NotNull String path, @Nullable UnaryOperator<ItemStack> itemFunction) {
        final Object def = get(path);
        return getRecipe(path, itemFunction, def instanceof Recipe ? (Recipe) def : null, null);
    }

    @Nullable
    public Recipe getRecipe(@NotNull String path, @Nullable UnaryOperator<ItemStack> itemFunction, @Nullable Recipe def) {
        return getRecipe(path, itemFunction, def, null);
    }

    @Nullable
    public Recipe getRecipe(@NotNull String path, @Nullable UnaryOperator<ItemStack> itemFunction, @Nullable Recipe def, @Nullable String name) {
        // section, shape, result, & ingredientMaterials
        final ConfigurationSection section = getConfigurationSection(path);
        if (section == null) return def;
        final List<String> shape = section.getStringList("Shape").stream()
                .map(String::toUpperCase)
                .toList();
        ItemStack result = this.getItem(path + "Result");
        if (shape.isEmpty() || result.getType().isAir()) return def;

        final Map<Character, Material> ingredientMaterials = new HashMap<>();
        for (String key : this.getSection(path + "Ingredients")) {
            final Material material = StringUtil.getEnum(this.getString(path + "Ingredients." + key, null), Material.class).orElse(Material.AIR);
            if (material.isAir()) continue;

            ingredientMaterials.put(key.toUpperCase().charAt(0), material);
        }
        if (ingredientMaterials.isEmpty()) return def;

        // Apply itemFunction
        if (itemFunction != null) result = itemFunction.apply(result);

        // Set name if null
        if (name == null) {
            final String[] split = path.split("\\.");
            name = split[split.length - 1];
        }

        // Shapeless
        switch (this.getString(path + "Type", null)) {
            case "Shapeless" -> {
                ShapelessRecipe shapeless = new ShapelessRecipe(new NamespacedKey(EngineUtils.ENGINE, name), result);
                for (Map.Entry<Character, Material> entry : ingredientMaterials.entrySet())
                    shapeless.addIngredient(shape.stream()
                            .mapToInt(s -> s.length() - s.replace(entry.getKey().toString(), "").length())
                            .sum(), entry.getValue());
                return shapeless;
            }
            // Furnace
            case "Furnace" -> {
                Material source = ingredientMaterials.values().stream().findFirst().orElse(Material.AIR);
                if (source.isAir()) {
                    return def;
                }
                int exp = this.getInt(path + "Exp", 0);
                int cookingTime = this.getInt(path + "Cooking_Time", 1);
                FurnaceRecipe furnaceRecipe = new FurnaceRecipe(new NamespacedKey(EngineUtils.ENGINE, name),
                        result,
                        source,
                        exp,
                        cookingTime);
                return furnaceRecipe;
            }
            // Shaped
            case "Shaped" -> {
                ShapedRecipe shaped = new ShapedRecipe(new NamespacedKey(EngineUtils.ENGINE, name), result);
                shaped.shape(shape.stream()
                        .map(string -> string.replace("-", " "))
                        .toArray(String[]::new));
                ingredientMaterials.forEach(shaped::setIngredient);
                return shaped;
            }
            default -> {
                return def;
            }
        }
    }

    public enum MetaDataType {
        BYTE,
        SHORT,
        INTEGER,
        LONG,
        FLOAT,
        DOUBLE,
        BOOLEAN,
        STRING,
        UUID,
        INTEGER_ARRAY,
        LONG_ARRAY,
        DOUBLE_ARRAY,
        STRING_ARRAY,
        TAG_CONTAINER_ARRAY,
        TAG_CONTAINER;
    }
}