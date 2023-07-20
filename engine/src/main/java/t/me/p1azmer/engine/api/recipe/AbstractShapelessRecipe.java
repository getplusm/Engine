package t.me.p1azmer.engine.api.recipe;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;

import java.util.List;

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

    @NotNull
    @Override
    public String getName() {
        return name;
    }
}
