package auviotre.enigmatic.legacy.contents.item.potions;

import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.Consumable;
import auviotre.enigmatic.legacy.api.item.IItemHelper;
import auviotre.enigmatic.legacy.contents.item.generic.BaseDrinkableItem;
import auviotre.enigmatic.legacy.handlers.EnigmaticHandler;
import auviotre.enigmatic.legacy.handlers.TooltipHandler;
import auviotre.enigmatic.legacy.registries.EnigmaticComponents;
import auviotre.enigmatic.legacy.registries.EnigmaticEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;
import java.util.ArrayList;

public class IchorBottle extends BaseDrinkableItem {
    public static final FoodProperties FOOD_PROPERTIES = new FoodProperties.Builder().alwaysEdible().build();
    public static final Consumable FOOD_PROPERTIES_CONSUMABLE = Consumables.defaultDrink()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.ABSORPTION, 1200, 4)))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 2)))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 3200)))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(EnigmaticEffects.PURE_RESISTANCE, 1200, 2)))
            .build();
    private final boolean enchanted;

    public IchorBottle(boolean enchanted) {
        super(IItemHelper.singleProperties().craftRemainder(Items.GLASS_BOTTLE).food(FOOD_PROPERTIES, FOOD_PROPERTIES_CONSUMABLE).rarity(enchanted ? Rarity.EPIC : Rarity.RARE)
                .component(EnigmaticComponents.CURSED, enchanted).component(EnigmaticComponents.BLESSED, enchanted));
        this.enchanted = enchanted;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        List<Component> list = new ArrayList<>();
        try {
            if (this.enchanted) {
                TooltipHandler.line(list, "tooltip.enigmaticlegacy.ichorBottle1");
                TooltipHandler.line(list, "tooltip.enigmaticlegacy.ichorBottle2");
                TooltipHandler.line(list);
                TooltipHandler.cursedOnly(list, stack);
            }
        } finally {
            list.forEach(tooltip);
        }
    }

    public void onConsumed(Level level, Player player, ItemStack stack) {
        if (this.enchanted) {
            EnigmaticHandler.unlockSpecialSlot("charm", player, IItemHelper.getLocation(this));
        }
        applyConsumption(level, player, stack);
    }

    public boolean isFoil(ItemStack stack) {
        return this.enchanted;
    }
}
