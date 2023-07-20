package t.me.p1azmer.engine.api.recipe;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.manager.AbstractManager;

import java.util.ArrayList;
import java.util.List;

public class RecipeManager<P extends NexPlugin<P>> extends AbstractManager<P> {

    private final List<IRecipe> recipeList;

    public RecipeManager(@NotNull P plugin) {
        super(plugin);
        this.recipeList = new ArrayList<>();
    }

    @Override
    protected void onLoad() {
        this.recipeList.forEach(recipe -> this.plugin.getServer().addRecipe(recipe));
    }

    @Override
    protected void onShutdown() {
        this.recipeList.forEach(this::unregister);
    }

    public void add(@NotNull IRecipe recipe) {
        this.recipeList.add(recipe);
        this.register(recipe);
    }

    public void register(@NotNull IRecipe recipe) {
        this.recipeList.add(recipe);
        this.plugin.getServer().addRecipe(recipe);
    }

    public void unregister(@NotNull IRecipe recipe) {
        if (this.recipeList.remove(recipe)) {
            this.plugin.getServer().removeRecipe(new NamespacedKey(plugin, recipe.getName()));
        }
    }
}
