package t.me.p1azmer.engine.api.recipe;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.config.JYML;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractShapedRecipe<P extends NexPlugin<P>> extends ShapedRecipe implements IRecipe {

    private final String name;

    public AbstractShapedRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result) {
        super(key, result);
        this.name = key.getKey();
    }

    public AbstractShapedRecipe(@NotNull P plugin, @NotNull String name, @NotNull ItemStack result, @NotNull Map<Character, ItemStack> ingredients, @Nullable List<String> shape) {
        this(new NamespacedKey(plugin, name), result);
        if (shape != null && shape.size() > 0) {
            this.shape(shape.stream().map(string -> string.replace("-", " ")).toArray(String[]::new));
        } else {
            this.shape(ingredients.keySet().stream().map(String::valueOf).toArray(String[]::new));
        }
        if (this.getShape().length == 0) {
            plugin.error("The recipe cannot be created because the recipe '" + this.getKey().getKey() + "' form is empty");
            return;
        }
        ingredients.forEach(((character, itemStack) -> this.setIngredient(character, itemStack.getType())));
    }

    public AbstractShapedRecipe(@NotNull P plugin, @NotNull String name, @NotNull ShapedRecipe recipe) {
        this(plugin, name, recipe.getResult(), recipe.getIngredientMap(), Arrays.stream(recipe.getShape()).toList());
    }

    public AbstractShapedRecipe(@NotNull P plugin, @NotNull JYML config, @NotNull String id, @NotNull String path) {
        this(plugin, config.getString(path + ".Name", id), config.getItem(path + ".Result"), config.getSection(path + ".Ingredients").stream().collect(Collectors.toMap(s -> s.toUpperCase().charAt(0), v -> config.getItem(path + ".Ingredients." + v + ".Item"))), config.getStringList(path + ".Shape"));
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }
}
