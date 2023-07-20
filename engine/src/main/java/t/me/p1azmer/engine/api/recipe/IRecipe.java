package t.me.p1azmer.engine.api.recipe;

import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;

public interface IRecipe extends Recipe {

    @NotNull String getName();

}
