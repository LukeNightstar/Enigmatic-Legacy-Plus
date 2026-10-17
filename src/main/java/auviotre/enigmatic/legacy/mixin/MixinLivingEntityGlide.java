package auviotre.enigmatic.legacy.mixin;

import auviotre.enigmatic.legacy.contents.item.generic.BaseElytraItem;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 26.1.2 port: lets elytras worn in curio slots glide, replacing the Caelus fall flying attribute.
 * Vanilla only looks at equipment slots, which still handles elytras worn in the chest slot.
 */
@Mixin(LivingEntity.class)
public abstract class MixinLivingEntityGlide {
    @Shadow
    protected int fallFlyTicks;

    @Shadow
    protected abstract boolean canGlide();

    @Inject(method = "canGlide", at = @At("RETURN"), cancellable = true)
    private void enigmatic$curioCanGlide(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) return;
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.onGround() || self.isPassenger() || self.hasEffect(MobEffects.LEVITATION)) return;
        if (self instanceof Player player && player.getAbilities().flying) return;
        if (!BaseElytraItem.getGlidingCurio(self).isEmpty()) cir.setReturnValue(true);
    }

    @Inject(method = "updateFallFlying", at = @At("HEAD"), cancellable = true)
    private void enigmatic$curioFallFlying(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.level().isClientSide()) return;
        for (EquipmentSlot slot : EquipmentSlot.VALUES) {
            if (LivingEntity.canGlideUsing(self.getItemBySlot(slot), slot)) return;
        }
        ItemStack elytra = BaseElytraItem.getGlidingCurio(self);
        if (elytra.isEmpty()) return;
        // Vanilla would pick a random equipment slot with a glider to damage, of which there are none here
        self.checkFallDistanceAccumulation();
        if (this.canGlide()) {
            int nextFlightTick = this.fallFlyTicks + 1;
            if (nextFlightTick % 10 == 0) {
                if ((nextFlightTick / 10) % 2 == 0) elytra.hurtAndBreak(1, self, EquipmentSlot.CHEST);
                self.gameEvent(GameEvent.ELYTRA_GLIDE);
            }
        } else self.stopFallFlying();
        ci.cancel();
    }
}
