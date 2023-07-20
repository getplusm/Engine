package t.me.p1azmer.engine.api.recipe;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;

public abstract class AbstractCookingRecipe<P extends NexPlugin<P>, T extends CookingRecipe<T>> extends CookingRecipe<T> implements IRecipe {

    private final String name;

    public AbstractCookingRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result, @NotNull Material source, float experience, int cookingTime) {
        super(key, result, source, experience, cookingTime);
        this.name = key.getKey();
    }

    public AbstractCookingRecipe(@NotNull P plugin, @NotNull String name, @NotNull ItemStack result, @NotNull Material source, float experience, int cookingTime) {
        this(new NamespacedKey(plugin, name), result, source, experience, cookingTime);
    }

    public AbstractCookingRecipe(@NotNull P plugin, @NotNull String name, @NotNull ItemStack result, RecipeChoice recipeChoice, float experience, int cookingTime) {
        super(new NamespacedKey(plugin, name), result, recipeChoice, experience, cookingTime);
        this.name = name;
    }

    public AbstractCookingRecipe(@NotNull P plugin, @NotNull String name, @NotNull CookingRecipe<T> recipe) {
        this(plugin, name, recipe.getResult(), recipe.getInputChoice(), recipe.getExperience(), recipe.getCookingTime());
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }
}
