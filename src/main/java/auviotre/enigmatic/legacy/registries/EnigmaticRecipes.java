package auviotre.enigmatic.legacy.registries;

import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.contents.crafting.AnnihilatingRecipe;
import auviotre.enigmatic.legacy.contents.crafting.CursedShapedRecipe;
import auviotre.enigmatic.legacy.contents.crafting.ShapelessNoRemainRecipe;
import auviotre.enigmatic.legacy.contents.crafting.SpellstoneTableRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EnigmaticRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, EnigmaticLegacy.MODID);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CursedShapedRecipe>> CURSED_SHAPED = RECIPE_SERIALIZERS.register("cursed_shaped", () -> CursedShapedRecipe.SERIALIZER);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ShapelessNoRemainRecipe>> SHAPELESS_NO_REMAIN = RECIPE_SERIALIZERS.register("shapeless_no_remain", () -> ShapelessNoRemainRecipe.SERIALIZER);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SpellstoneTableRecipe>> SPELLSTONE_TABLE = RECIPE_SERIALIZERS.register("spellstone_table", () -> SpellstoneTableRecipe.SERIALIZER);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<AnnihilatingRecipe>> ANNIHILATING = RECIPE_SERIALIZERS.register("annihilating", () -> AnnihilatingRecipe.SERIALIZER);

    public static final DeferredRegister<RecipeBookCategory> RECIPE_BOOK_CATEGORIES = DeferredRegister.create(Registries.RECIPE_BOOK_CATEGORY, EnigmaticLegacy.MODID);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> SPELLSTONE_TABLE_CATEGORY = RECIPE_BOOK_CATEGORIES.register("spellstone_table", RecipeBookCategory::new);

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, EnigmaticLegacy.MODID);
    public static final DeferredHolder<RecipeType<?>, RecipeType<SpellstoneTableRecipe>> SPELLSTONE_CRAFTING = RECIPE_TYPES.register("spellstone_crafting", () -> RecipeType.simple(EnigmaticLegacy.location("spellstone_crafting")));
}
