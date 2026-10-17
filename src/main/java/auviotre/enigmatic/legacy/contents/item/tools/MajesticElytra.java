package auviotre.enigmatic.legacy.contents.item.tools;

import auviotre.enigmatic.legacy.registries.EnigmaticTags;
import net.minecraft.core.particles.PowerParticleOption;
import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.api.SubscribeConfig;
import auviotre.enigmatic.legacy.api.item.IItemHelper;
import auviotre.enigmatic.legacy.contents.item.generic.BaseElytraItem;
import auviotre.enigmatic.legacy.handlers.EnigmaticHandler;
import auviotre.enigmatic.legacy.handlers.TooltipHandler;
import auviotre.enigmatic.legacy.registries.EnigmaticAttributes;
import auviotre.enigmatic.legacy.registries.EnigmaticEnchantments;
import auviotre.enigmatic.legacy.registries.EnigmaticItems;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;
import java.util.ArrayList;
import net.minecraft.server.level.ServerLevel;
import org.jspecify.annotations.Nullable;

public class MajesticElytra extends BaseElytraItem {
    public static ModConfigSpec.IntValue resistanceModifier;

    public MajesticElytra() {
        super(IItemHelper.singleProperties().fireResistant().rarity(Rarity.RARE).durability(3476).enchantable(24).repairable(EnigmaticTags.Items.ETHERIUM_TOOL_MATERIALS));
    }

    @SubscribeConfig
    public static void onConfig(ModConfigSpec.Builder builder, ModConfig.Type type) {
        builder.translation("item.enigmaticlegacyplus.majestic_elytra").push("else.majesticElytra");
        resistanceModifier = builder.defineInRange("resistanceModifier", 75, 0, 95);
        builder.pop(2);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        List<Component> list = new ArrayList<>();
        try {
            TooltipHandler.line(list);
            TooltipHandler.line(list, "tooltip.enigmaticlegacy.majesticElytra1");
            TooltipHandler.line(list, "tooltip.enigmaticlegacy.majesticElytra2");
            TooltipHandler.line(list, "tooltip.enigmaticlegacy.majesticElytra3");
            if (Minecraft.getInstance().level != null) {
                var holder = EnigmaticHandler.get(Minecraft.getInstance().level, Registries.ENCHANTMENT, EnigmaticEnchantments.ETHERIC_RESONANCE);
                if (stack.getEnchantmentLevel(holder) > 0)
                    TooltipHandler.line(list, "tooltip.enigmaticlegacy.majesticElytraBuff", ChatFormatting.GOLD, String.format("%d%%", resistanceModifier.get()));
            }
            if (stack.isEnchanted()) TooltipHandler.line(list);
        } finally {
            list.forEach(tooltip);
        }
    }

    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext context, Identifier id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> attributes = HashMultimap.create();
        attributes.put(EnigmaticAttributes.ETHERIUM_SHIELD, new AttributeModifier(IItemHelper.getLocation(this), 0.04, AttributeModifier.Operation.ADD_VALUE));
        return attributes;
    }

    public boolean flyingBoost(@NotNull Player player) {
        if (player.isFallFlying()) {
            Vec3 lookAngle = player.getLookAngle().scale(0.64F);
            Vec3 movement = player.getDeltaMovement().scale(0.48F);
            player.setDeltaMovement(movement.add(lookAngle));
            return true;
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public void addParticle(Player player) {
        int amount = 3;
        double rangeModifier = 0.1;
        for (int counter = 0; counter <= amount; counter++) {
            Vec3 pos = player.position();
            pos = pos.add(Math.random() - 0.5, -1.0 + Math.random() - 0.5, Math.random() - 0.5);
            player.level().addParticle(PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1.0F), true, false, pos.x, pos.y, pos.z, ((Math.random() - 0.5D) * 2.0D) * rangeModifier, ((Math.random() - 0.5D) * 2.0D) * rangeModifier, ((Math.random() - 0.5D) * 2.0D) * rangeModifier);
        }
    }

    public void curioTick(@NotNull SlotContext context, ItemStack stack) {
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slotId) {
        if (entity instanceof LivingEntity livingEntity && !level.isClientSide()) {
        }
    }

    @Mod(value = EnigmaticLegacy.MODID)
    @EventBusSubscriber(modid = EnigmaticLegacy.MODID)
    public static class Events {
        @SubscribeEvent
        private static void onHurt(LivingDamageEvent.@NotNull Pre event) {
            if (event.getNewDamage() >= Float.MAX_VALUE) return;
            LivingEntity entity = event.getEntity();
            if (!getElytra(entity).is(EnigmaticItems.MAJESTIC_ELYTRA)) return;
            DamageSource source = event.getSource();
            float modifier = 1.0F - 0.01F * resistanceModifier.get();
            if (!source.is(Tags.DamageTypes.IS_TECHNICAL) && entity.isFallFlying())
                event.setNewDamage(event.getNewDamage() * modifier);
        }
    }
}
