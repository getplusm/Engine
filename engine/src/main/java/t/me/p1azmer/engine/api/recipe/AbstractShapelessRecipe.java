package t.me.p1azmer.engine.api.recipe;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.config.JYML;
import t.me.p1azmer.engine.utils.StringUtil;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractShapelessRecipe<P extends NexPlugin<P>> extends ShapelessRecipe implements IRecipe {

    private final String name;

    public AbstractShapelessRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result) {
        super(key, result);
        this.name = key.getKey();

    }

    public AbstractShapelessRecipe(@NotNull P plugin, @NotNull String name, @NotNull ItemStack result, @NotNull List<RecipeChoice> ingredients) {
        this(new NamespacedKey(plugin, name), result);
        ingredients.forEach(this::addIngredient);
    }

    public AbstractShapelessRecipe(@NotNull P plugin, @NotNull String name, @NotNull ShapelessRecipe recipe) {
        this(plugin, name, recipe.getResult(), recipe.getChoiceList());
    }

    public AbstractShapelessRecipe(@NotNull P plugin, @NotNull JYML config, @NotNull String id, @NotNull String path) {
        this(plugin, config.getString(path + ".Name", id), config.getItem(path + ".Result"),
                config.getSection(path + ".Ingredients").stream().map(key -> {
                    Material material = StringUtil.getEnum(config.getString(path + ".Ingredients." + key, null), Material.class).orElse(null);
                    if (material != null)
                        return new RecipeChoice.MaterialChoice(material);
                    ItemStack itemStack = config.getItem(path + ".Ingredients." + key);
                    return new RecipeChoice.ExactChoice(itemStack);
                }).collect(Collectors.toList()));
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }
}
