package auviotre.enigmatic.legacy.contents.crafting;

import auviotre.enigmatic.legacy.api.item.ITaintable;
import auviotre.enigmatic.legacy.handlers.EnigmaticHandler;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;

public class CursedShapedRecipe extends ShapedRecipe {
    public static final MapCodec<CursedShapedRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Recipe.CommonInfo.MAP_CODEC.forGetter(recipe -> recipe.commonInfo),
            CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(recipe -> recipe.bookInfo),
            ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
    ).apply(instance, CursedShapedRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CursedShapedRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC, recipe -> recipe.commonInfo,
            CraftingRecipe.CraftingBookInfo.STREAM_CODEC, recipe -> recipe.bookInfo,
            ShapedRecipePattern.STREAM_CODEC, recipe -> recipe.pattern,
            ItemStackTemplate.STREAM_CODEC, recipe -> recipe.result,
            CursedShapedRecipe::new
    );
    public static final RecipeSerializer<CursedShapedRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
    private final ItemStackTemplate result;

    public CursedShapedRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo, ShapedRecipePattern pattern, ItemStackTemplate result) {
        super(commonInfo, bookInfo, pattern, result);
        this.result = result;
    }

    public ItemStack getResultItem() {
        return this.result.create();
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        ItemStack result = this.getResultItem();
        if (EnigmaticHandler.isCursedItem(result) || EnigmaticHandler.isBlessedItem(result)) {
            for (int i = 0; i < input.size(); i++) {
                ItemStack stack = input.getItem(i);
                if (stack.getItem() instanceof ITaintable && !ITaintable.isTainted(stack))
                    return false;
            }
        }
        return super.matches(input, level);
    }

    @Override
    @SuppressWarnings("unchecked")
    public RecipeSerializer<ShapedRecipe> getSerializer() {
        return (RecipeSerializer<ShapedRecipe>) (RecipeSerializer<?>) SERIALIZER;
    }
}
