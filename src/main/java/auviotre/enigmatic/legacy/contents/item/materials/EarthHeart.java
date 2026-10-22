package auviotre.enigmatic.legacy.contents.item.materials;

import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.api.item.IItemHelper;
import auviotre.enigmatic.legacy.api.item.ITaintable;
import auviotre.enigmatic.legacy.contents.item.generic.BaseItem;
import auviotre.enigmatic.legacy.contents.item.tools.ExecutionAxe;
import auviotre.enigmatic.legacy.handlers.EnigmaticHandler;
import auviotre.enigmatic.legacy.handlers.TooltipHandler;
import auviotre.enigmatic.legacy.registries.EnigmaticItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;
import java.util.ArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import org.jspecify.annotations.Nullable;

public class EarthHeart extends BaseItem implements ITaintable {
    public EarthHeart() {
        // Fork change: stacks to 16, like its fragment.
        super(IItemHelper.properties(16).rarity(Rarity.UNCOMMON));
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slotId) {
        if (entity instanceof Player player && !level.isClientSide())
            this.handleTaintable(stack, player);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        List<Component> list = new ArrayList<>();
        try {
            if (ITaintable.isTainted(stack)) TooltipHandler.line(list, "tooltip.enigmaticlegacy.tainted");
        } finally {
            list.forEach(tooltip);
        }
    }

    public boolean canTaint(Player player) {
        return EnigmaticHandler.isTheOne(player);
    }

    public static class Fragment extends BaseItem {
        public Fragment() {
            super(IItemHelper.properties().stacksTo(16).rarity(Rarity.UNCOMMON));
        }
    }

    @Mod(value = EnigmaticLegacy.MODID)
    @EventBusSubscriber(modid = EnigmaticLegacy.MODID)
    public static class Events {

        @SubscribeEvent
        private static void onDrops(@NotNull LivingDropsEvent event) {
            LivingEntity killed = event.getEntity();
            DamageSource source = event.getSource();
            if (killed instanceof Monster && event.isRecentlyHit() && source.getEntity() instanceof Player player && EnigmaticHandler.isTheOne(player)) {
                BlockPos blockPos = killed.blockPosition();
                int lootingLevel = ExecutionAxe.getLootingLevel(player, player.level());
                if (killed.level().dimension().equals(Level.OVERWORLD) && !killed.level().canSeeSky(blockPos)) {
                    if (blockPos.getY() <= 0 && player.getRandom().nextInt(1000) < 30 + lootingLevel * 15) {
                        ItemEntity itemEntity = new ItemEntity(killed.level(), killed.getX(), killed.getY(), killed.getZ(), EnigmaticItems.EARTH_HEART_FRAGMENT.toStack());
                        itemEntity.setPickUpDelay(10);
                        event.getDrops().add(itemEntity);
                    }
                }
            }
        }
    }
}
