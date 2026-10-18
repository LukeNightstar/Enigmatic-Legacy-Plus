package auviotre.enigmatic.legacy.compat.emi;

import auviotre.enigmatic.legacy.contents.gui.SpellstoneTableMenu;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Menu slots: 0 debris, 1 spellstone, 2 spellcore, 3-9 ingredients, 10 result, 11-46 player inventory.
 * The order of the crafting slots matches SpellstoneCraftingEmiRecipe#getInputs.
 */
public class SpellstoneTableRecipeHandler implements StandardRecipeHandler<SpellstoneTableMenu> {
    private static final int CRAFTING_SLOTS = 10;
    private static final int RESULT_SLOT = 10;

    public List<Slot> getInputSources(SpellstoneTableMenu menu) {
        List<Slot> slots = new ArrayList<>(menu.slots.subList(0, CRAFTING_SLOTS));
        slots.addAll(menu.slots.subList(RESULT_SLOT + 1, menu.slots.size()));
        return slots;
    }

    public List<Slot> getCraftingSlots(SpellstoneTableMenu menu) {
        return menu.slots.subList(0, CRAFTING_SLOTS);
    }

    public @Nullable Slot getOutputSlot(SpellstoneTableMenu menu) {
        return menu.slots.get(RESULT_SLOT);
    }

    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == EMIHandler.SPELLSTONE_CRAFTING;
    }
}
