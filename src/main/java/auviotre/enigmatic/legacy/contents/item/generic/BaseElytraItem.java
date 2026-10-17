package auviotre.enigmatic.legacy.contents.item.generic;

import auviotre.enigmatic.legacy.client.ClientAccess;
import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.contents.attachement.EnigmaticData;
import auviotre.enigmatic.legacy.handlers.EnigmaticHandler;
import auviotre.enigmatic.legacy.packets.server.UpdateElytraBoostPacket;
import auviotre.enigmatic.legacy.registries.EnigmaticAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

/**
 * 26.1.2 port: gliding uses the vanilla {@link DataComponents#GLIDER} component instead of Caelus.
 * The chest slot is handled by vanilla; curio slots are handled by {@code MixinLivingEntityGlide}.
 */
public abstract class BaseElytraItem extends BaseCurioItem {
    @OnlyIn(Dist.CLIENT)
    private boolean isBoosting;

    public BaseElytraItem(Properties properties) {
        super(properties.component(DataComponents.GLIDER, Unit.INSTANCE)
                .component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.CHEST)
                        .setEquipSound(SoundEvents.ARMOR_EQUIP_ELYTRA).setDamageOnHurt(false).build()));
    }

    public static ItemStack getElytra(LivingEntity entity) {
        Optional<ICuriosItemHandler> curios = CuriosApi.getCuriosInventory(entity);
        if (curios.isPresent()) {
            Optional<SlotResult> firstCurio = curios.get().findFirstCurio(stack -> stack.getItem() instanceof BaseElytraItem);
            if (firstCurio.isPresent()) return firstCurio.get().stack();
        }
        ItemStack itemBySlot = entity.getItemBySlot(EquipmentSlot.CHEST);
        return itemBySlot.getItem() instanceof BaseElytraItem ? itemBySlot : ItemStack.EMPTY;
    }

    /**
     * {@return an elytra worn in a curio slot that can currently be used to glide, or an empty stack}
     */
    public static ItemStack getGlidingCurio(LivingEntity entity) {
        return CuriosApi.getCuriosInventory(entity)
                .flatMap(curios -> curios.findFirstCurio(stack -> stack.getItem() instanceof BaseElytraItem
                        && LivingEntity.canGlideUsing(stack, EquipmentSlot.CHEST)))
                .map(SlotResult::stack).orElse(ItemStack.EMPTY);
    }

    protected abstract boolean flyingBoost(Player player);

    @OnlyIn(Dist.CLIENT)
    protected abstract void addParticle(Player player);

    @OnlyIn(Dist.CLIENT)
    public void handleBoosting(Player player) {
        if (ClientAccess.player() != player) return;
        if (Minecraft.getInstance().options.keyJump.isDown() && flyingBoost(player)) {
            if (!isBoosting) ClientPacketDistributor.sendToServer(new UpdateElytraBoostPacket(isBoosting = true));
        } else if (isBoosting) ClientPacketDistributor.sendToServer(new UpdateElytraBoostPacket(isBoosting = false));
    }

    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_ELYTRA.value(), 1F, 1F);
    }

    public boolean canEquip(SlotContext context, ItemStack stack) {
        return super.canEquip(context, stack) && getElytra(context.entity()).isEmpty();
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot slot, LivingEntity entity) {
        return super.canEquip(stack, slot, entity) && EnigmaticHandler.canUse(entity, stack) && getElytra(entity).isEmpty();
    }

    @Mod(value = EnigmaticLegacy.MODID)
    @EventBusSubscriber(modid = EnigmaticLegacy.MODID)
    public static class Events {
        @SubscribeEvent
        private static void onTick(PlayerTickEvent.@NotNull Pre event) {
            Player player = event.getEntity();
            EnigmaticData data = player.getData(EnigmaticAttachments.ENIGMATIC_DATA);
            ItemStack stack = getElytra(player);
            if (stack.isEmpty() || !LivingEntity.canGlideUsing(stack, EquipmentSlot.CHEST)) return;
            BaseElytraItem elytra = (BaseElytraItem) stack.getItem();
            if (player instanceof ServerPlayer && data.isElytraBoosting()) {
                elytra.flyingBoost(player);
                int flightTicks = player.getFallFlyingTicks();
                int nextFlightTick = flightTicks + 1;
                if (nextFlightTick % 5 == 0) stack.hurtAndBreak(1, player, EquipmentSlot.CHEST);
            }
        }

        @SubscribeEvent
        @OnlyIn(Dist.CLIENT)
        private static void onClientTick(PlayerTickEvent.@NotNull Pre event) {
            Player player = event.getEntity();
            if (!player.level().isClientSide()) return;
            EnigmaticData data = player.getData(EnigmaticAttachments.ENIGMATIC_DATA);
            ItemStack stack = getElytra(player);
            if (player.isFallFlying() && stack.getItem() instanceof BaseElytraItem elytra) elytra.handleBoosting(player);
            if (data.isElytraBoosting()) {
                if (!player.isFallFlying()) {
                    if (player == ClientAccess.player())
                        if (stack.getItem() instanceof BaseElytraItem elytra)
                            elytra.handleBoosting(player);
                        else data.setElytraBoosting(false);
                    return;
                }
                if (stack.getItem() instanceof BaseElytraItem elytra) elytra.addParticle(player);
            }
        }
    }
}
