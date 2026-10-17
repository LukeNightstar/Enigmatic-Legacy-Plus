package auviotre.enigmatic.legacy.contents.item.charms;

import auviotre.enigmatic.legacy.client.ClientAccess;
import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.api.SubscribeConfig;
import auviotre.enigmatic.legacy.api.item.IItemHelper;
import auviotre.enigmatic.legacy.contents.item.generic.BaseCurioItem;
import auviotre.enigmatic.legacy.handlers.EnigmaticHandler;
import auviotre.enigmatic.legacy.handlers.TooltipHandler;
import auviotre.enigmatic.legacy.registries.EnigmaticItems;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotAttribute;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;
import java.util.ArrayList;

public class ForgerGem extends BaseCurioItem {
    public static ModConfigSpec.IntValue breakChanceModifier;
    public static ModConfigSpec.IntValue extraDurabilityModifier;
    public static ModConfigSpec.IntValue extraMaxDurability;

    public ForgerGem() {
        super(IItemHelper.singleProperties().rarity(Rarity.UNCOMMON).fireResistant());
    }

    @SubscribeConfig
    public static void onConfig(ModConfigSpec.Builder builder, ModConfig.Type type) {
        builder.translation("item.enigmaticlegacyplus.forger_gem").push("else.forgerGem");
        breakChanceModifier = builder.defineInRange("breakChanceModifier", 20, 0, 100);
        extraDurabilityModifier = builder.defineInRange("extraDurabilityModifier", 4, 0, 20);
        extraMaxDurability = builder.defineInRange("extraMaxDurability", 20, 0, 100);
        builder.pop(2);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        List<Component> list = new ArrayList<>();
        try {
            TooltipHandler.line(list);
            if (Minecraft.getInstance().hasShiftDown()) {
                TooltipHandler.line(list, "tooltip.enigmaticlegacy.enchantersPearl1");
                TooltipHandler.line(list);
                TooltipHandler.line(list, "tooltip.enigmaticlegacy.forgerGem1");
                TooltipHandler.line(list, "tooltip.enigmaticlegacy.forgerGem2");
                if (EnigmaticHandler.isTheOne(ClientAccess.player())) {
                    TooltipHandler.line(list);
                    TooltipHandler.line(list, "tooltip.enigmaticlegacy.forgerGem3");
                }
            } else TooltipHandler.holdShift(list);
        } finally {
            list.forEach(tooltip);
        }
    }

    public boolean canEquip(SlotContext context, ItemStack stack) {
        return super.canEquip(context, stack) && !EnigmaticHandler.hasCurio(context.entity(), EnigmaticItems.FORGER_CRYSTAL);
    }

    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext context, Identifier id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> attributes = HashMultimap.create();
        // 26.1.2 port: Curios 15 computes these without a wearer (null entity); curio slots only exist on players.
        if (context.entity() == null || context.entity() instanceof Player) {
            attributes.put(SlotAttribute.getOrCreate("charm"), new AttributeModifier(IItemHelper.getLocation(this), 1.0, AttributeModifier.Operation.ADD_VALUE));
        }
        return attributes;
    }

    public List<Component> getAttributesTooltip(List<Component> tooltips, TooltipContext context, ItemStack stack) {
        tooltips.clear();
        return tooltips;
    }

    public record ToolInfo(int originDurability, int extraDurability) {
        public static final MapCodec<ToolInfo> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.INT.fieldOf("origin").forGetter(ToolInfo::originDurability),
                Codec.INT.fieldOf("extra").forGetter(ToolInfo::extraDurability)
        ).apply(instance, ToolInfo::of));

        public static final Codec<ToolInfo> CODEC = MAP_CODEC.codec();

        public static final StreamCodec<RegistryFriendlyByteBuf, ToolInfo> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, ToolInfo::originDurability,
                ByteBufCodecs.INT, ToolInfo::extraDurability,
                ToolInfo::of);

        public static ToolInfo of(int originDurability, int extraDurability) {
            return new ToolInfo(originDurability, extraDurability);
        }
    }

    public static float modifyAnvilBreakChance(Player player, float chance) {
        if (EnigmaticHandler.hasCurio(player, EnigmaticItems.FORGER_CRYSTAL)) return 0.0F;
        if (EnigmaticHandler.hasCurio(player, EnigmaticItems.FORGER_GEM)) return chance * breakChanceModifier.get() * 0.01F;
        return chance;
    }

    @Mod(value = EnigmaticLegacy.MODID)
    @EventBusSubscriber(modid = EnigmaticLegacy.MODID)
    public static class Events {
    }
}
