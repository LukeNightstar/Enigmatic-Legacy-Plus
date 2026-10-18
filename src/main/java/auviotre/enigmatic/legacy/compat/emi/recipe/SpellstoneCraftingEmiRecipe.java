package auviotre.enigmatic.legacy.compat.emi.recipe;

import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.compat.emi.EMIHandler;
import auviotre.enigmatic.legacy.contents.crafting.SpellstoneTableRecipe;
import auviotre.enigmatic.legacy.registries.EnigmaticItems;
import auviotre.enigmatic.legacy.registries.EnigmaticTags;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Same layout as the JEI SpellstoneCraftingCategory. Inputs are ordered like the table's menu slots
 * (debris, spellstone, spellcore, 7 ingredients) so SpellstoneTableRecipeHandler can fill them by index.
 */
public class SpellstoneCraftingEmiRecipe extends BasicEmiRecipe {
    private static final ResourceLocation SPRITE = EnigmaticLegacy.location("jei/spellstone_table_gui");
    private static final ResourceLocation ICON = EnigmaticLegacy.location("jei/spellstone_table_icon");
    // Item positions in the 132x80 background: debris, spellstone, spellcore, then the 7 ingredient slots.
    private static final int[][] POSITIONS = {
            {5, 32}, {111, 32}, {58, 32},
            {38, 52}, {31, 32}, {38, 12}, {58, 5}, {78, 12}, {85, 32}, {78, 52}
    };
    private final boolean allDifferent;

    public SpellstoneCraftingEmiRecipe(ResourceLocation id, SpellstoneTableRecipe recipe, boolean fragmentation) {
        super(EMIHandler.SPELLSTONE_CRAFTING, id, 132, 80);
        this.allDifferent = recipe.isAllDifferent();
        if (fragmentation) {
            this.inputs.add(EmiStack.EMPTY);
            this.inputs.add(EmiIngredient.of(EnigmaticTags.Items.SPELLSTONES));
            this.inputs.add(EmiStack.EMPTY);
        } else {
            this.inputs.add(recipe.getCount() > 0 ? EmiStack.of(EnigmaticItems.SPELLSTONE_DEBRIS, recipe.getCount()) : EmiStack.EMPTY);
            this.inputs.add(EmiStack.EMPTY);
            this.inputs.add(EmiStack.of(EnigmaticItems.SPELLCORE));
        }
        List<Ingredient> ingredients = recipe.getIngredients();
        for (int i = 0; i < 7; i++) {
            if (i >= ingredients.size()) {
                this.inputs.add(EmiStack.EMPTY);
                continue;
            }
            ItemStack[] items = ingredients.get(i).getItems();
            if (this.allDifferent && items.length > 0) {
                // Rotate each slot's cycle so the displayed (and auto-filled) materials differ, as in JEI.
                List<EmiStack> rotated = new ArrayList<>(items.length);
                for (int j = 0; j < items.length; j++) rotated.add(EmiStack.of(items[(j + i) % items.length]));
                this.inputs.add(EmiIngredient.of(rotated));
            } else this.inputs.add(EmiIngredient.of(ingredients.get(i)));
        }
        this.outputs.add(EmiStack.of(recipe.getResultItem(Minecraft.getInstance().level.registryAccess())));
    }

    public void addWidgets(WidgetHolder widgets) {
        widgets.addDrawable(0, 0, 132, 80, (graphics, mouseX, mouseY, delta) -> graphics.blitSprite(SPRITE, 0, 0, 132, 80));
        if (this.allDifferent) {
            widgets.addDrawable(124, 0, 8, 8, (graphics, mouseX, mouseY, delta) -> graphics.blitSprite(ICON, 0, 0, 8, 8));
            widgets.addTooltipText(List.of(Component.translatable("gui.enigmaticlegacy.jei.spellstone_crafting.all_different")), 124, 0, 8, 8);
        }
        // The background already contains the slot frames; EMI slots are 18x18 around the 16x16 item.
        for (int i = 0; i < POSITIONS.length; i++) {
            widgets.addSlot(this.inputs.get(i), POSITIONS[i][0] - 1, POSITIONS[i][1] - 1).drawBack(false);
        }
        widgets.addSlot(this.outputs.get(0), 57, 58).drawBack(false).recipeContext(this);
    }
}
