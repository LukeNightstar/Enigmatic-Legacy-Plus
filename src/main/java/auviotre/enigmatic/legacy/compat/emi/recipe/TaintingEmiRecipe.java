package auviotre.enigmatic.legacy.compat.emi.recipe;

import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.api.item.ITaintable;
import auviotre.enigmatic.legacy.compat.emi.EMIHandler;
import auviotre.enigmatic.legacy.registries.EnigmaticComponents;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Same layout as the JEI TaintingCategory: the heart becomes tainted while the ring condition is worn.
 */
public class TaintingEmiRecipe extends BasicEmiRecipe {
    private static final Identifier SPRITE = EnigmaticLegacy.location("jei/tainting_gui");

    public TaintingEmiRecipe(ItemStack input, Ingredient condition) {
        super(EMIHandler.TAINTING, EnigmaticLegacy.location("/tainting/" + BuiltInRegistries.ITEM.getKey(input.getItem()).getPath()), 108, 42);
        this.inputs.add(EmiStack.of(input));
        this.catalysts.add(EmiIngredient.of(condition));
        ItemStack output = input.copy();
        if (output.getItem() instanceof ITaintable) output.set(EnigmaticComponents.TAINTABLE.get(), true);
        this.outputs.add(EmiStack.of(output));
    }

    public void addWidgets(WidgetHolder widgets) {
        widgets.addDrawable(0, 0, 108, 42, (graphics, mouseX, mouseY, delta) ->
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITE, 0, 0, 108, 42));
        widgets.addSlot(this.inputs.get(0), 12, 12);
        widgets.addSlot(this.outputs.get(0), 78, 12).recipeContext(this);
        widgets.addSlot(this.catalysts.get(0), 45, 22).drawBack(false).catalyst(true);
    }
}
