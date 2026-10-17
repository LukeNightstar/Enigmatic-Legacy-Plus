package auviotre.enigmatic.legacy.contents.item.generic;

import auviotre.enigmatic.legacy.api.item.IItemHelper;
import auviotre.enigmatic.legacy.handlers.EnigmaticHandler;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CurioAttributeModifiers;
import top.theillusivec4.curios.api.CuriosResources;
import top.theillusivec4.curios.api.CuriosSlotTypes;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.Map;

public class BaseCurioItem extends BaseItem implements ICurioItem {

    public BaseCurioItem() {
        super(IItemHelper.singleProperties());
    }

    public BaseCurioItem(Properties properties) {
        super(properties);
    }

    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    public boolean canEquip(SlotContext context, ItemStack stack) {
        return !EnigmaticHandler.hasCurio(context.entity(), this) && ICurioItem.super.canEquip(context, stack);
    }

    /**
     * 26.1.2 port: Curios 15 applies (and lists in tooltips) only these default modifiers, and its default implementation
     * never calls {@link #getAttributeModifiers(SlotContext, net.minecraft.resources.Identifier, ItemStack)}.
     * This feeds that method into the new path, per slot type the stack fits. The slot context has no wearer here
     * (as in ICurio#getDefaultCurioAttributeModifiers), so implementations must treat a null entity as a player.
     */
    @Override
    public CurioAttributeModifiers getDefaultCurioAttributeModifiers(ItemStack stack) {
        CurioAttributeModifiers.Builder builder = CurioAttributeModifiers.builder();
        // Curios' own default reads only the client slot list, which is empty on a dedicated server. Prefer the server
        // list (loaded wherever a server runs, including singleplayer) and fall back to the synced client list.
        Map<String, ISlotType> slots = CuriosSlotTypes.getItemSlotTypes(stack, false);
        if (slots.isEmpty()) slots = CuriosSlotTypes.getItemSlotTypes(stack, true);
        for (String slot : slots.keySet()) {
            SlotContext context = new SlotContext(slot, null, -1, false, true);
            this.getAttributeModifiers(context, CuriosResources.resource(slot), stack)
                    .forEach((attribute, modifier) -> builder.addModifier(attribute, modifier, slot));
        }
        return builder.build();
    }
}
