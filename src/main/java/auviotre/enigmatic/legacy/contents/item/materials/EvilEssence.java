package auviotre.enigmatic.legacy.contents.item.materials;

import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.api.item.IItemHelper;
import auviotre.enigmatic.legacy.contents.item.generic.BaseCursedItem;
import auviotre.enigmatic.legacy.contents.item.tools.ExecutionAxe;
import auviotre.enigmatic.legacy.handlers.EnigmaticHandler;
import auviotre.enigmatic.legacy.registries.EnigmaticItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import org.jetbrains.annotations.NotNull;

public class EvilEssence extends BaseCursedItem {
    public EvilEssence() {
        super(IItemHelper.properties(16).rarity(Rarity.UNCOMMON));
    }

    @Mod(value = EnigmaticLegacy.MODID)
    @EventBusSubscriber(modid = EnigmaticLegacy.MODID)
    public static class Events {
        // Fork change: this used to be a mixin at the tail of WitherBoss.dropCustomDeathLoot, which mods such as
        // Wither Reincarnated cancel at its head, so the essence never dropped alongside them.
        // The mixin only ran when the Wither dropped loot at all, but LivingDropsEvent fires even with doMobLoot off.
        @SubscribeEvent
        private static void onDrops(@NotNull LivingDropsEvent event) {
            LivingEntity killed = event.getEntity();
            if (killed instanceof WitherBoss && killed.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT) && event.isRecentlyHit() && event.getSource().getEntity() instanceof LivingEntity attacker && EnigmaticHandler.isTheCursedOne(attacker)) {
                int count = attacker.getRandom().nextInt(4) + 1;
                int lootingLevel = ExecutionAxe.getLootingLevel(attacker, killed.level());
                // nextInt(0) throws, so a ring with lootingBonus = 0 and no Looting would lose the drop.
                if (lootingLevel > 0) count += attacker.getRandom().nextInt(lootingLevel);
                ItemEntity itemEntity = new ItemEntity(killed.level(), killed.getX(), killed.getY(), killed.getZ(), EnigmaticItems.EVIL_ESSENCE.toStack(count));
                itemEntity.setDefaultPickUpDelay();
                itemEntity.setExtendedLifetime();
                event.getDrops().add(itemEntity);
            }
        }
    }
}
