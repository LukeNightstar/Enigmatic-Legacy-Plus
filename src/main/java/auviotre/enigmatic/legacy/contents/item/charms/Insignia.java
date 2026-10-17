package auviotre.enigmatic.legacy.contents.item.charms;

import auviotre.enigmatic.legacy.api.item.IItemHelper;
import auviotre.enigmatic.legacy.contents.item.generic.BaseCurioItem;
import auviotre.enigmatic.legacy.handlers.EnigmaticHandler;
import auviotre.enigmatic.legacy.handlers.TooltipHandler;
import auviotre.enigmatic.legacy.registries.EnigmaticComponents;
import auviotre.enigmatic.legacy.registries.EnigmaticItems;
import auviotre.enigmatic.legacy.registries.EnigmaticSounds;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.ClientHooks;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.Optional;
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;
import java.util.ArrayList;

public class Insignia extends BaseCurioItem {
    public Insignia() {
        super(IItemHelper.singleProperties().rarity(Rarity.UNCOMMON).component(EnigmaticComponents.BOOLEAN, true));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        List<Component> list = new ArrayList<>();
        try {
            TooltipHandler.line(list);
            if (Minecraft.getInstance().hasShiftDown()) {
                TooltipHandler.line(list, "tooltip.enigmaticlegacy.insignia1");
                TooltipHandler.line(list);
                TooltipHandler.line(list, "tooltip.enigmaticlegacy.insignia2");
                TooltipHandler.line(list, "tooltip.enigmaticlegacy.insignia3");
            } else TooltipHandler.holdShift(list);
            TooltipHandler.line(list);

            this.getCustomName(stack).ifPresent(component -> TooltipHandler.line(list, "tooltip.enigmaticlegacy.insigniaName", component));

            Component option = stack.getOrDefault(EnigmaticComponents.BOOLEAN, true) ? Component.translatable("tooltip.enigmaticlegacy.enabled") : Component.translatable("tooltip.enigmaticlegacy.disabled");
            TooltipHandler.line(list, "tooltip.enigmaticlegacy.insigniaTagDisplay", option);
        } finally {
            list.forEach(tooltip);
        }
    }

    public Optional<Component> getCustomName(ItemStack stack) {
        if (stack.has(DataComponents.CUSTOM_NAME)) {
            return Optional.ofNullable(stack.get(DataComponents.CUSTOM_NAME));
        }
        if (stack.has(DataComponents.ITEM_NAME)) {
            return Optional.ofNullable(stack.get(DataComponents.ITEM_NAME));
        }
        return Optional.empty();
    }

    public boolean canSeeTrueName(@Nullable Player player) {
        return player != null && player.isCreative() || EnigmaticHandler.hasCurio(player, EnigmaticItems.ENIGMATIC_EYE);
    }

    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player.isCrouching()) {
            ItemStack stack = player.getItemInHand(hand);
            RandomSource random = player.getRandom();
            if (stack.getOrDefault(EnigmaticComponents.BOOLEAN, true)) {
                stack.set(EnigmaticComponents.BOOLEAN, false);
                level.playSound(null, player.blockPosition(), EnigmaticSounds.CHARGED_OFF.get(), SoundSource.PLAYERS, 0.8F + random.nextFloat() * 0.2F, 0.8F + random.nextFloat() * 0.2F);
            } else {
                stack.set(EnigmaticComponents.BOOLEAN, true);
                level.playSound(null, player.blockPosition(), EnigmaticSounds.CHARGED_ON.get(), SoundSource.PLAYERS, 0.8F + random.nextFloat() * 0.2F, 0.8F + random.nextFloat() * 0.2F);
            }
            player.swing(hand);
            return InteractionResult.SUCCESS;
        }
        return super.use(level, player, hand);
    }

    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() == null) return InteractionResult.PASS;
        return this.use(context.getLevel(), context.getPlayer(), context.getHand());
    }

    public boolean canEquipFromUse(SlotContext context, ItemStack stack) {
        return false;
    }

    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext context, Identifier id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> attributes = HashMultimap.create();
        CuriosApi.addSlotModifier(attributes, "charm", IItemHelper.getLocation(this), 1.0, AttributeModifier.Operation.ADD_VALUE);
        return attributes;
    }
}
