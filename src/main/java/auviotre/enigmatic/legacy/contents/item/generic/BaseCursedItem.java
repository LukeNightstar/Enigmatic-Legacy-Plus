package auviotre.enigmatic.legacy.contents.item.generic;

import auviotre.enigmatic.legacy.handlers.TooltipHandler;
import auviotre.enigmatic.legacy.registries.EnigmaticComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;
import java.util.ArrayList;

public class BaseCursedItem extends BaseItem {
    public BaseCursedItem(Properties properties) {
        super(properties.component(EnigmaticComponents.CURSED, true));
    }

    public BaseCursedItem(Properties properties, boolean betrayal) {
        super(properties.component(EnigmaticComponents.CURSED, true).component(EnigmaticComponents.BLESSED, betrayal));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        List<Component> list = new ArrayList<>();
        try {
            TooltipHandler.cursedOnly(list, stack);
        } finally {
            list.forEach(tooltip);
        }
    }
}
