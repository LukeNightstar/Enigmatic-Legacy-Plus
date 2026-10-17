package auviotre.enigmatic.legacy.client;

import auviotre.enigmatic.legacy.EnigmaticLegacy;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;

import java.util.List;

/**
 * 26.1.2 port: the client no longer has a recipe manager. Recipes that the server opts into syncing
 * (see {@code EnigmaticEventHandler#onDatapackSync}) arrive here and are kept for recipe viewers.
 */
@EventBusSubscriber(modid = EnigmaticLegacy.MODID, value = Dist.CLIENT)
public final class ClientRecipes {
    private static RecipeMap recipes = RecipeMap.EMPTY;

    private ClientRecipes() {
    }

    // Highest priority so the map is in place before recipe viewers rebuild from the same event.
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    private static void onRecipesReceived(RecipesReceivedEvent event) {
        recipes = event.getRecipeMap();
    }

    public static <I extends RecipeInput, T extends Recipe<I>> List<RecipeHolder<T>> byType(RecipeType<T> type) {
        return List.copyOf(recipes.byType(type));
    }
}
