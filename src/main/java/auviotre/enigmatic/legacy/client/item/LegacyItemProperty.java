package auviotre.enigmatic.legacy.client.item;

import auviotre.enigmatic.legacy.EnigmaticLegacy;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 26.1.2 port: {@code ItemProperties.register} was removed together with model overrides.
 * The old per-item predicate functions are kept here and exposed to the item model definitions
 * (assets/enigmaticlegacyplus/items) through a single range_dispatch property:
 * <pre>{"property": "enigmaticlegacyplus:legacy", "item": "&lt;item id&gt;", "predicate": "&lt;old predicate id&gt;"}</pre>
 * Values are clamped to [0, 1] like the old ClampedItemPropertyFunction.
 */
@OnlyIn(Dist.CLIENT)
public record LegacyItemProperty(Identifier item, Identifier predicate) implements RangeSelectItemModelProperty {
    public static final Identifier TYPE = EnigmaticLegacy.location("legacy");
    public static final MapCodec<LegacyItemProperty> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("item").forGetter(LegacyItemProperty::item),
            Identifier.CODEC.fieldOf("predicate").forGetter(LegacyItemProperty::predicate)
    ).apply(instance, LegacyItemProperty::new));
    private static final Map<Identifier, Map<Identifier, Function>> FUNCTIONS = new ConcurrentHashMap<>();

    public static void register(ItemLike item, Identifier predicate, Function function) {
        Identifier id = BuiltInRegistries.ITEM.getKey(item.asItem());
        FUNCTIONS.computeIfAbsent(id, key -> new ConcurrentHashMap<>()).put(predicate, function);
    }

    public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        Map<Identifier, Function> functions = FUNCTIONS.get(this.item);
        Function function = functions == null ? null : functions.get(this.predicate);
        if (function == null) return 0.0F;
        if (level == null && owner != null && owner.level() instanceof ClientLevel clientLevel) level = clientLevel;
        return Mth.clamp(function.get(stack, level, owner == null ? null : owner.asLivingEntity(), owner, seed), 0.0F, 1.0F);
    }

    public MapCodec<LegacyItemProperty> type() {
        return MAP_CODEC;
    }

    @FunctionalInterface
    public interface Function {
        float get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, @Nullable ItemOwner owner, int seed);
    }
}
