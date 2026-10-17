package auviotre.enigmatic.legacy.compat.jei;

import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.compat.jei.category.TaintingCategory;
import auviotre.enigmatic.legacy.contents.crafting.SpellstoneTableRecipe;
import auviotre.enigmatic.legacy.registries.EnigmaticRecipes;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import mezz.jei.api.recipe.types.IRecipeType;
import java.util.function.Supplier;

public class EnigmaticRecipeTypes {
    public static final IRecipeType<TaintingCategory.Recipe> TAINTING = IRecipeType.create(EnigmaticLegacy.MODID, "tainting", TaintingCategory.Recipe.class);
    public static final Supplier<IRecipeHolderType<SpellstoneTableRecipe>> SPELLSTONE_CRAFTING = IRecipeHolderType.createDeferred(EnigmaticRecipes.SPELLSTONE_CRAFTING);

}
