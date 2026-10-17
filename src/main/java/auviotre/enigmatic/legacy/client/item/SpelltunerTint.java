package auviotre.enigmatic.legacy.client.item;

import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.contents.item.spellstones.other.Spelltuner;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

/**
 * 26.1.2 port: item color handlers became item tint sources referenced from the item model definition.
 */
@OnlyIn(Dist.CLIENT)
public record SpelltunerTint() implements ItemTintSource {
    public static final Identifier TYPE = EnigmaticLegacy.location("spelltuner");
    public static final MapCodec<SpelltunerTint> MAP_CODEC = MapCodec.unit(new SpelltunerTint());

    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        return ARGB.opaque(Spelltuner.getColor(stack));
    }

    public MapCodec<SpelltunerTint> type() {
        return MAP_CODEC;
    }
}
