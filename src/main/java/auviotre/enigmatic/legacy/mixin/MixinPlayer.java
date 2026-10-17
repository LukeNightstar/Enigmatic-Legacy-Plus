package auviotre.enigmatic.legacy.mixin;

import auviotre.enigmatic.legacy.handlers.EnigmaticHandler;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import auviotre.enigmatic.legacy.contents.item.spellstones.other.SpellstoneSword;
import auviotre.enigmatic.legacy.contents.item.tools.InfernalShield;
import auviotre.enigmatic.legacy.registries.EnigmaticComponents;
import auviotre.enigmatic.legacy.registries.EnigmaticItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {
    protected MixinPlayer(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
        throw new IllegalStateException("Can't touch this");
    }

    @Inject(method = "hasInfiniteMaterials", at = @At("HEAD"), cancellable = true)
    public void hasInfiniteMaterialsMix(CallbackInfoReturnable<Boolean> info) {
        if (this.getUseItem().is(EnigmaticItems.STARLIGHT_BUCKET)) info.setReturnValue(true);
    }

    @Inject(method = "destroyVanishingCursedItems", at = @At("TAIL"))
    public void onDestroyVanishingCursedItems(CallbackInfo info) {
        for (EnigmaticHandler.CurioSlot slot : EnigmaticHandler.getEquippedCurioSlots(this)) {
            ItemStack stack = slot.get();
            if (!stack.isEmpty() && EnchantmentHelper.has(stack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)) {
                slot.set(ItemStack.EMPTY);
            }
        }
    }

    // 26.1.2 port: Player#sweepAttack was folded into doSweepAttack; mod-triggered sweeps call the same hook.
    @Inject(method = "doSweepAttack(Lnet/minecraft/world/entity/Entity;FLnet/minecraft/world/damagesource/DamageSource;FLnet/minecraft/world/phys/AABB;)V", at = @At("TAIL"))
    public void sweepMix(Entity entity, float baseDamage, DamageSource damageSource, float attackStrengthScale, AABB sweepHitBox, CallbackInfo info) {
        EnigmaticHandler.onSweepAttack((Player) (Object) this);
    }
}
