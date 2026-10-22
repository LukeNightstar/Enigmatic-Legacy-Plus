package auviotre.enigmatic.legacy.api.item;

import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.registries.EnigmaticComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ITaintable {
    Identifier LOCATION = EnigmaticLegacy.location("tainted");

    static boolean isTainted(ItemStack stack) {
        return stack.getOrDefault(EnigmaticComponents.TAINTABLE.get(), false);
    }

    boolean canTaint(Player player);

    default void handleTaintable(ItemStack stack, Player player) {
        if (canTaint(player)) {
            if (!stack.getOrDefault(EnigmaticComponents.TAINTABLE.get(), false)) {
                stack.set(EnigmaticComponents.TAINTABLE.get(), true);
            }
        } else {
            // Fork change: drop the component instead of storing false, so untainted stacks merge with fresh ones.
            if (stack.has(EnigmaticComponents.TAINTABLE.get())) {
                stack.remove(EnigmaticComponents.TAINTABLE.get());
            }
        }
    }
}