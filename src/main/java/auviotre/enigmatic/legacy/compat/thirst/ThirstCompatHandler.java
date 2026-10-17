package auviotre.enigmatic.legacy.compat.thirst;

import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.contents.item.potions.ForbiddenJuice;
import auviotre.enigmatic.legacy.registries.EnigmaticItems;
import cn.mlus.thirst.foundation.common.capability.ModAttachment;
import cn.mlus.thirst.foundation.common.event.RegisterThirstValueEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.NotNull;

public class ThirstCompatHandler {
    public static final Identifier FORBIDDEN_DRINK_FULL_SPRITE = EnigmaticLegacy.location("hud/forbidden_drink_full");
    public static final Identifier FORBIDDEN_DRINK_HALF_SPRITE = EnigmaticLegacy.location("hud/forbidden_drink_half");
    public static final Identifier FORBIDDEN_DRINK_EMPTY_SPRITE = EnigmaticLegacy.location("hud/forbidden_drink_empty");

    public static void setHellBladeRecover(Player player) {
        var data = player.getData(ModAttachment.PLAYER_THIRST);
        data.setQuenched(data.getThirst());
    }

    @SubscribeEvent
    public void onRegisterThirst(@NotNull RegisterThirstValueEvent event) {
        event.addFood(EnigmaticItems.ICHOROOT.get(), 2, 3);
        event.addFood(EnigmaticItems.ASTRAL_FRUIT.get(), 3, 8);
        event.addFood(EnigmaticItems.FORBIDDEN_FRUIT.get(), 2, 1);
        event.addFood(EnigmaticItems.ENCHANTED_ASTRAL_FRUIT.get(), 3, 8);
        event.addDrink(EnigmaticItems.RECALL_POTION.get(), 6, 8);
        event.addDrink(EnigmaticItems.WORMHOLE_POTION.get(), 6, 8);
        event.addDrink(EnigmaticItems.ICHOR_BOTTLE.get(), 6, 10);
        event.addDrink(EnigmaticItems.ENCHANTED_ICHOR_BOTTLE.get(), 6, 10);
        event.addDrink(EnigmaticItems.REDEMPTION_POTION.get(), 6, 10);
        event.addDrink(EnigmaticItems.UNHOLY_GRAIL.get(), 1, 2);
        event.addDrink(EnigmaticItems.TWISTED_POTION.get(), 5, 3);
        event.addDrink(EnigmaticItems.ICHOR_CURSE_BOTTLE.get(), 4, 3);
        event.addDrink(EnigmaticItems.FORBIDDEN_JUICE.get(), 2, 2);
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.@NotNull Pre event) {
        lockThirstData(event.getEntity());
    }

    @SubscribeEvent
    public void onPlayerTicked(PlayerTickEvent.@NotNull Post event) {
        lockThirstData(event.getEntity());
    }

    private void lockThirstData(@NotNull Player player) {
        var data = player.getData(ModAttachment.PLAYER_THIRST);
        if (ForbiddenJuice.isForbiddenCursed(player)) {
            data.setThirst(20);
            data.setQuenched(0);
        }
    }
}
