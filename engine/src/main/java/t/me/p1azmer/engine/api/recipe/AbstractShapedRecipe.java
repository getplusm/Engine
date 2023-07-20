package t.me.p1azmer.engine.api.recipe;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;

import java.util.Map;

public abstract class AbstractShapedRecipe<P extends NexPlugin<P>> extends ShapedRecipe implements IRecipe {

    private final String name;

    public AbstractShapedRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result) {
        super(key, result);
        this.name = key.getKey();
    }

    public AbstractShapedRecipe(@NotNull P plugin, @NotNull String name, @NotNull ItemStack result, @NotNull Map<Character, ItemStack> ingredients) {
        this(new NamespacedKey(plugin, name), result);
        ingredients.forEach(((character, itemStack) -> this.setIngredient(character, itemStack.getType())));
    }

    public AbstractShapedRecipe(@NotNull P plugin, @NotNull String name, @NotNull ItemStack result, @NotNull Map<Character, ItemStack> ingredients, @NotNull String[] shape) {
        this(plugin, name, result, ingredients);
        this.shape(shape);
    }

    public AbstractShapedRecipe(@NotNull P plugin, @NotNull String name, @NotNull ShapedRecipe recipe) {
        this(plugin, name, recipe.getResult(), recipe.getIngredientMap(), recipe.getShape());
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }
}
