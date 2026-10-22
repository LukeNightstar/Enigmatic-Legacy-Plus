package auviotre.enigmatic.legacy.contents.item.spellstones.other;

import auviotre.enigmatic.legacy.api.item.IItemHelper;
import auviotre.enigmatic.legacy.contents.item.generic.BaseItem;
import net.minecraft.world.item.Rarity;

public class Spellcore extends BaseItem {
    public Spellcore() {
        // Fork change: stacks to 16, like the Heart of the Earth.
        super(IItemHelper.properties(16).rarity(Rarity.RARE).fireResistant());
    }
}
