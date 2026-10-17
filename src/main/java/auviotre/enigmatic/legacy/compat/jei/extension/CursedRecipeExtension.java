package auviotre.enigmatic.legacy.compat.jei.extension;

import java.util.Optional;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import auviotre.enigmatic.legacy.api.item.ITaintable;
import auviotre.enigmatic.legacy.contents.crafting.CursedShapedRecipe;
import auviotre.enigmatic.legacy.registries.EnigmaticComponents;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CursedRecipeExtension implements ICraftingCategoryExtension<CursedShapedRecipe> {
    @Override
    public List<SlotDisplay> getIngredients(RecipeHolder<CursedShapedRecipe> recipeHolder) {
        return recipeHolder.value().pattern.ingredients().stream().map(ingredient -> ingredient.map(Ingredient::display).orElse(SlotDisplay.Empty.INSTANCE)).toList();
    }

    @Override
    public int getWidth(RecipeHolder<CursedShapedRecipe> recipeHolder) {
        return recipeHolder.value().getWidth();
    }

    @Override
    public int getHeight(RecipeHolder<CursedShapedRecipe> recipeHolder) {
        return recipeHolder.value().getHeight();
    }

    @Override
    public void setRecipe(@NotNull RecipeHolder<CursedShapedRecipe> recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
        CursedShapedRecipe recipe = recipeHolder.value();
        List<List<ItemStack>> inputs = new ArrayList<>();
        for (Optional<Ingredient> ingredient : recipe.pattern.ingredients()) {
            List<ItemStack> list = new ArrayList<>();
            for (ItemStack stack : ingredient.map(value -> value.items().map(ItemStack::new).toList()).orElse(List.of())) {
                if (stack.getItem() instanceof ITaintable) stack.set(EnigmaticComponents.TAINTABLE, true);
                list.add(stack);
            }
            inputs.add(list);
        }
        craftingGridHelper.createAndSetInputs(builder, inputs, recipe.getWidth(), recipe.getHeight());
        ItemStack result = recipe.getResultItem();
        if (result.getItem() instanceof ITaintable) result.set(EnigmaticComponents.TAINTABLE, true);
        craftingGridHelper.createAndSetOutputs(builder, List.of(result));
    }
}
