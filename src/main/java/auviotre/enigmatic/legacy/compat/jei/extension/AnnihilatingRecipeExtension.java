package auviotre.enigmatic.legacy.compat.jei.extension;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import auviotre.enigmatic.legacy.contents.crafting.AnnihilatingRecipe;
import auviotre.enigmatic.legacy.registries.EnigmaticComponents;
import auviotre.enigmatic.legacy.registries.EnigmaticItems;
import auviotre.enigmatic.legacy.registries.EnigmaticTags;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
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

import java.util.List;

public class AnnihilatingRecipeExtension implements ICraftingCategoryExtension<AnnihilatingRecipe> {
    private static Ingredient abyssalItems() {
        return Ingredient.of(BuiltInRegistries.ITEM.getOrThrow(EnigmaticTags.Items.ABYSSAL_ITEMS));
    }

    @Override
    public List<SlotDisplay> getIngredients(RecipeHolder<AnnihilatingRecipe> recipeHolder) {
        SlotDisplay abyssal = abyssalItems().display();
        return List.of(abyssal, new SlotDisplay.ItemSlotDisplay(EnigmaticItems.EVIL_INGOT.get()), abyssal,
                SlotDisplay.Empty.INSTANCE, new SlotDisplay.ItemSlotDisplay(EnigmaticItems.ABYSSAL_HEART.get()), SlotDisplay.Empty.INSTANCE,
                abyssal, new SlotDisplay.ItemSlotDisplay(EnigmaticItems.TWISTED_HEART.get()), abyssal);
    }

    @Override
    public int getWidth(RecipeHolder<AnnihilatingRecipe> recipeHolder) {
        return 3;
    }

    @Override
    public int getHeight(RecipeHolder<AnnihilatingRecipe> recipeHolder) {
        return 3;
    }

    @Override
    public void setRecipe(@NotNull RecipeHolder<AnnihilatingRecipe> recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
        AnnihilatingRecipe recipe = recipeHolder.value();
        List<IRecipeSlotBuilder> inputs = craftingGridHelper.createAndSetInputs(builder, List.of(), 3, 3);
        Ingredient ingredient = abyssalItems();
        inputs.get(0).addIngredients(ingredient);
        inputs.get(1).addItemStack(EnigmaticItems.EVIL_INGOT.toStack());
        inputs.get(2).addIngredients(ingredient);
        inputs.get(3).addItemStack(ItemStack.EMPTY);
        ItemStack abyssalHeart = EnigmaticItems.ABYSSAL_HEART.toStack();
        abyssalHeart.set(EnigmaticComponents.TAINTABLE, true);
        inputs.get(4).addItemStack(abyssalHeart);
        inputs.get(5).addItemStack(ItemStack.EMPTY);
        inputs.get(6).addIngredients(ingredient);
        ItemStack twistedHeart = EnigmaticItems.TWISTED_HEART.toStack();
        twistedHeart.set(EnigmaticComponents.TAINTABLE, true);
        inputs.get(7).addItemStack(twistedHeart);
        inputs.get(8).addIngredients(ingredient);
        ItemStack result = recipe.getResultItem();
        craftingGridHelper.createAndSetOutputs(builder, List.of(result));
    }
}
