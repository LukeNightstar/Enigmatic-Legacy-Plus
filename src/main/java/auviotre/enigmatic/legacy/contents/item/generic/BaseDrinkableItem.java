package auviotre.enigmatic.legacy.contents.item.generic;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

public class BaseDrinkableItem extends BaseItem {

    public BaseDrinkableItem(Properties properties) {
        super(properties);
    }

    public InteractionResult use(Level level, @NotNull Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.canDrink(level, player, stack))
            return ItemUtils.startUsingInstantly(level, player, hand);
        return InteractionResult.PASS;
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            ItemStackTemplate remainder = stack.getCraftingRemainder();
            ItemStack remainingItem = remainder != null ? remainder.create() : ItemStack.EMPTY;
            this.onConsumed(level, player, stack);

            if (player instanceof ServerPlayer serverPlayer) CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);

            player.awardStat(Stats.ITEM_USED.get(this));
            stack.consume(1, player);

            if (!player.hasInfiniteMaterials()) {
                if (stack.isEmpty()) return remainingItem;
                player.getInventory().add(remainingItem);
            }
        }
        entity.gameEvent(GameEvent.DRINK);
        return stack;
    }

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.DRINK;
    }

    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_DRINK.value();
    }

    public boolean canDrink(Level level, Player player, ItemStack stack) {
        return true;
    }

    public void onConsumed(Level level, @NotNull Player player, ItemStack stack) {
    }

    /**
     * 26.1.2 port: replaces the removed Player#eat, applying the stack's food and consume effects without consuming it.
     */
    protected static void applyConsumption(Level level, Player player, ItemStack stack) {
        FoodProperties food = stack.get(DataComponents.FOOD);
        if (food != null) player.getFoodData().eat(food);
        Consumable consumable = stack.get(DataComponents.CONSUMABLE);
        if (consumable != null && !level.isClientSide())
            consumable.onConsumeEffects().forEach(effect -> effect.apply(level, stack, player));
    }
}
