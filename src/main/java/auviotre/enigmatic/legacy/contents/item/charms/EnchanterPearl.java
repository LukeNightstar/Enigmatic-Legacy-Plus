package auviotre.enigmatic.legacy.contents.item.charms;

import auviotre.enigmatic.legacy.api.item.IItemHelper;
import auviotre.enigmatic.legacy.contents.item.generic.CursedCurioItem;
import auviotre.enigmatic.legacy.handlers.EnigmaticHandler;
import auviotre.enigmatic.legacy.handlers.TooltipHandler;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotAttribute;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;
import java.util.ArrayList;
public class EnchanterPearl extends CursedCurioItem {
    public EnchanterPearl() {
        super(IItemHelper.singleProperties().rarity(Rarity.RARE).fireResistant());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        List<Component> list = new ArrayList<>();
        try {
            TooltipHandler.line(list);
            if (Minecraft.getInstance().hasShiftDown()) {
                TooltipHandler.line(list, "tooltip.enigmaticlegacy.enchantersPearl1");
                TooltipHandler.line(list);
                TooltipHandler.line(list, "tooltip.enigmaticlegacy.enchantersPearl2");
                TooltipHandler.line(list, "tooltip.enigmaticlegacy.enchantersPearl3");
                TooltipHandler.line(list);
                TooltipHandler.line(list, "tooltip.enigmaticlegacy.enchantersPearl4");
                TooltipHandler.line(list, "tooltip.enigmaticlegacy.enchantersPearl5");
                TooltipHandler.line(list);
                TooltipHandler.line(list, "tooltip.enigmaticlegacy.enchantersPearl6");
            } else TooltipHandler.holdShift(list);
            TooltipHandler.line(list);
            TooltipHandler.cursedOnly(list, stack);
        } finally {
            list.forEach(tooltip);
        }
    }

    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext context, Identifier id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> attributes = HashMultimap.create();
        // 26.1.2 port: Curios 15 computes these without a wearer (null entity); curio slots only exist on players.
        if (context.entity() == null || EnigmaticHandler.canUse(context.entity(), stack))
            attributes.put(SlotAttribute.getOrCreate("charm"), new AttributeModifier(IItemHelper.getLocation(this), 1.0, AttributeModifier.Operation.ADD_VALUE));
        return attributes;
    }

    public List<Component> getAttributesTooltip(@NotNull List<Component> tooltips, TooltipContext context, ItemStack stack) {
        tooltips.clear();
        return tooltips;
    }
}
