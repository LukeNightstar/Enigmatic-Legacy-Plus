package auviotre.enigmatic.legacy.contents.crafting;

import auviotre.enigmatic.legacy.registries.EnigmaticBlocks;
import auviotre.enigmatic.legacy.registries.EnigmaticItems;
import auviotre.enigmatic.legacy.registries.EnigmaticRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellstoneTableRecipe implements Recipe<SpellstoneTableRecipe.Input> {
    private static final Codec<ItemStack> RESULT_CODEC = ItemStackTemplate.CODEC.xmap(ItemStackTemplate::create, ItemStackTemplate::fromNonEmptyStack);
    public static final MapCodec<SpellstoneTableRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            RESULT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
            Codec.INT.optionalFieldOf("debris_count", 0).forGetter(recipe -> recipe.debrisCount),
            Ingredient.CODEC.listOf(1, 7).fieldOf("ingredients").forGetter(recipe -> recipe.ingredients),
            Codec.BOOL.optionalFieldOf("all_different", false).forGetter(recipe -> recipe.allDifferent)
    ).apply(instance, SpellstoneTableRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, SpellstoneTableRecipe> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, recipe -> recipe.result,
            ByteBufCodecs.VAR_INT, recipe -> recipe.debrisCount,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), recipe -> recipe.ingredients,
            ByteBufCodecs.BOOL, recipe -> recipe.allDifferent,
            SpellstoneTableRecipe::new
    );
    public static final RecipeSerializer<SpellstoneTableRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
    final ItemStack result;
    final int debrisCount;
    final List<Ingredient> ingredients;
    final boolean allDifferent;

    public SpellstoneTableRecipe(ItemStack result, int count, List<Ingredient> ingredients, boolean flag) {
        this.result = result;
        this.debrisCount = count;
        this.ingredients = ingredients;
        this.allDifferent = flag;
    }

    @Override
    public boolean matches(@NotNull Input input, Level level) {
        ItemStack core = input.core();
        if (core.isEmpty() || !core.is(EnigmaticItems.SPELLCORE)) return false;
        if (!input.noSpellstone) return false;
        if (input.debris().getCount() < this.debrisCount) return false;
        int size = input.ingredients().size();
        List<ItemStack> items = new ArrayList<>(size);
        for (ItemStack ingredient : input.ingredients())
            if (!ingredient.isEmpty()) items.add(ingredient);
        if (allDifferent) {
            Map<Item, Integer> map = new HashMap<>();
            for (ItemStack stack : items) {
                if (map.containsKey(stack.getItem())) {
                    map.put(stack.getItem(), map.get(stack.getItem()) + 1);
                } else map.put(stack.getItem(), 1);
            }
            for (Integer value : map.values()) {
                if (value > 1) return false;
            }
        }
        return RecipeMatcher.findMatches(items, this.ingredients) != null;
    }

    public NonNullList<ItemStack> getRemainingItems(Input input) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        input.debris().shrink(this.debrisCount);
        nonnulllist.set(0, input.debris().copy());
        for (int i = 2; i < nonnulllist.size(); ++i) {
            ItemStackTemplate remainder = input.getItem(i).getCraftingRemainder();
            if (remainder != null) {
                nonnulllist.set(i, remainder.create());
            }
        }

        return nonnulllist;
    }

    @Override
    public ItemStack assemble(Input input) {
        return this.result.copy();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return EnigmaticRecipes.SPELLSTONE_TABLE_CATEGORY.get();
    }

    public int getCount() {
        return this.debrisCount;
    }

    public boolean isAllDifferent() {
        return this.allDifferent;
    }

    public List<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public ItemStack getResultItem() {
        return this.result;
    }

    public ItemStack getToastSymbol() {
        return EnigmaticBlocks.SPELLSTONE_TABLE.toStack();
    }

    @Override
    public RecipeSerializer<SpellstoneTableRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<SpellstoneTableRecipe> getType() {
        return EnigmaticRecipes.SPELLSTONE_CRAFTING.get();
    }

    public record Input(ItemStack core, ItemStack debris, List<ItemStack> ingredients,
                        boolean noSpellstone) implements RecipeInput {
        public ItemStack getItem(int id) {
            if (id == 0) return core;
            else if (id == 1) return debris;
            else if (id >= size()) throw new IllegalArgumentException("Recipe does not contain slot " + id);
            return ingredients.get(id - 2);
        }

        public int size() {
            return ingredients.size() + 2;
        }
    }
}
