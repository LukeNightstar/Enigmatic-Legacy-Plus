package auviotre.enigmatic.legacy.contents.item.amulets;

import auviotre.enigmatic.legacy.client.ClientAccess;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.RegistryOps;
import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.api.SubscribeConfig;
import auviotre.enigmatic.legacy.api.item.IItemHelper;
import auviotre.enigmatic.legacy.contents.item.generic.BaseCurioItem;
import auviotre.enigmatic.legacy.handlers.EnigmaticHandler;
import auviotre.enigmatic.legacy.handlers.TooltipHandler;
import auviotre.enigmatic.legacy.registries.EnigmaticAttributes;
import auviotre.enigmatic.legacy.registries.EnigmaticComponents;
import auviotre.enigmatic.legacy.registries.EnigmaticItems;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;
import java.util.ArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import org.jspecify.annotations.Nullable;

public class EldritchAmulet extends BaseCurioItem {
    public static ModConfigSpec.IntValue attackDamage;
    public static ModConfigSpec.IntValue lifeSteal;
    public static ModConfigSpec.BooleanValue keepInventory;

    public EldritchAmulet() {
        super(IItemHelper.singleProperties().fireResistant().rarity(Rarity.EPIC).component(EnigmaticComponents.ELDRITCH, true));
    }

    @SubscribeConfig
    public static void onConfig(ModConfigSpec.Builder builder, ModConfig.Type type) {
        builder.translation("item.enigmaticlegacyplus.eldritch_amulet").push("abyssItems.eldritchAmulet");
        attackDamage = builder.defineInRange("attackDamage", 20, 0, 100);
        lifeSteal = builder.defineInRange("lifeSteal", 15, 0, 100);
        keepInventory = builder.define("keepInventory", true);
        builder.pop(2);
    }

    // 26.1.2 port: armor and offhand moved from Inventory into the entity equipment
    private static final Map<String, List<EquipmentSlot>> EQUIPMENT_SECTIONS = Map.of(
            "Armor", List.of(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD),
            "Offhand", List.of(EquipmentSlot.OFFHAND));

    private static boolean shouldVanish(ServerPlayer player, ItemStack stack) {
        return !stack.isEmpty() && !player.level().getGameRules().get(GameRules.KEEP_INVENTORY) && EnchantmentHelper.has(stack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP);
    }

    public static void storeInventory(ServerPlayer player) {
        RegistryOps<Tag> ops = player.registryAccess().createSerializationContext(NbtOps.INSTANCE);
        CompoundTag tag = new CompoundTag();
        ListTag main = new ListTag();
        NonNullList<ItemStack> items = player.getInventory().getNonEquipmentItems();
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (shouldVanish(player, stack)) stack = ItemStack.EMPTY;
            main.add(ItemStack.OPTIONAL_CODEC.encodeStart(ops, stack).getOrThrow());
            items.set(i, ItemStack.EMPTY);
        }
        tag.put("InventoryMain", main);
        EQUIPMENT_SECTIONS.forEach((key, slots) -> {
            ListTag list = new ListTag();
            for (EquipmentSlot slot : slots) {
                ItemStack stack = player.getItemBySlot(slot);
                if (shouldVanish(player, stack)) stack = ItemStack.EMPTY;
                list.add(ItemStack.OPTIONAL_CODEC.encodeStart(ops, stack).getOrThrow());
                player.setItemSlot(slot, ItemStack.EMPTY);
            }
            tag.put("Inventory" + key, list);
        });
        EnigmaticHandler.getPersistedData(player).put("ELPersistentInventory", tag);
    }

    public static boolean reclaimInventory(ServerPlayer oldPlayer, ServerPlayer newPlayer) {
        Tag maybeTag = EnigmaticHandler.getPersistedData(oldPlayer).get("ELPersistentInventory");
        if (!(maybeTag instanceof CompoundTag tag)) return false;
        EnigmaticHandler.getPersistedData(oldPlayer).remove("ELPersistentInventory");
        RegistryOps<Tag> ops = newPlayer.registryAccess().createSerializationContext(NbtOps.INSTANCE);
        if (tag.get("InventoryMain") instanceof ListTag main) {
            NonNullList<ItemStack> items = newPlayer.getInventory().getNonEquipmentItems();
            for (int i = 0; i < items.size() && i < main.size(); i++)
                items.set(i, ItemStack.OPTIONAL_CODEC.parse(ops, main.get(i)).result().orElse(ItemStack.EMPTY));
        }
        EQUIPMENT_SECTIONS.forEach((key, slots) -> {
            if (tag.get("Inventory" + key) instanceof ListTag list) {
                for (int i = 0; i < slots.size() && i < list.size(); i++)
                    newPlayer.setItemSlot(slots.get(i), ItemStack.OPTIONAL_CODEC.parse(ops, list.get(i)).result().orElse(ItemStack.EMPTY));
            }
        });
        return true;
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        List<Component> list = new ArrayList<>();
        try {
            TooltipHandler.line(list);
            if (Minecraft.getInstance().hasShiftDown()) {
                TooltipHandler.line(list, "tooltip.enigmaticlegacy.eldritchAmulet1");
                TooltipHandler.line(list, "tooltip.enigmaticlegacy.eldritchAmulet2");
                TooltipHandler.line(list, "tooltip.enigmaticlegacy.eldritchAmulet3");
                TooltipHandler.line(list, "tooltip.enigmaticlegacy.eldritchAmulet4");
                TooltipHandler.line(list, "tooltip.enigmaticlegacy.eldritchAmulet5");
                if (EnigmaticHandler.isAbyssBoosted(ClientAccess.player())) {
                    TooltipHandler.line(list);
                    TooltipHandler.line(list, "tooltip.enigmaticlegacy.abyssBoost");
                    TooltipHandler.line(list, "tooltip.enigmaticlegacy.eldritchAmuletBoost", ChatFormatting.GOLD, "80%");
                }
            } else {
                TooltipHandler.holdShift(list);
                String name = stack.get(EnigmaticComponents.AMULET_NAME);
                if (name != null) {
                    TooltipHandler.line(list);
                    TooltipHandler.line(list, "tooltip.enigmaticlegacy.enigmaticAmuletInscription", ChatFormatting.RED, name);
                }
            }
            TooltipHandler.line(list);
            TooltipHandler.worthyOnly(list, stack);
            TooltipHandler.line(list);
            TooltipHandler.line(list, "curios.modifiers.amulet", ChatFormatting.GOLD);
            TooltipHandler.line(list, "tooltip.enigmaticlegacy.enchantersPearl1", ChatFormatting.GOLD, 1);
            TooltipHandler.line(list, "tooltip.enigmaticlegacy.eldritchAmuletStat1", ChatFormatting.GOLD, attackDamage.getAsInt() + "%");
            TooltipHandler.line(list, "tooltip.enigmaticlegacy.eldritchAmuletStat2", ChatFormatting.GOLD, lifeSteal.getAsInt() + "%");
        } finally {
            list.forEach(tooltip);
        }
    }

    public List<Component> getAttributesTooltip(List<Component> tooltips, TooltipContext context, ItemStack stack) {
        tooltips.clear();
        return tooltips;
    }

    public void curioTick(@NotNull SlotContext context, ItemStack stack) {
        LivingEntity entity = context.entity();
        if (!EnigmaticHandler.isTheWorthyOne(entity)) return;
        if (entity instanceof Player) {
            float timer = stack.getOrDefault(EnigmaticComponents.ELDRITCH_TIMER, 0.0F);
            if (timer < 1.0F) stack.set(EnigmaticComponents.ELDRITCH_TIMER, Math.min(1.0F, timer + 0.3F));
        }
        if (entity.tickCount % 5 == 0 && !entity.level().isClientSide()) {
            List<LivingEntity> entities = EnigmaticHandler.getObservedEntities(entity, entity.level(), 2.1F, 64, false);
            for (LivingEntity target : entities) {
                if (EnigmaticHandler.hasCurio(target, this)) continue;
                target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 10, 1));
                target.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 10, 1));
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 10, 1));
            }
        }
        entity.getAttributes().addTransientAttributeModifiers(getModifiers());
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slotId) {
        if (entity instanceof LivingEntity livingEntity && !level.isClientSide()) {
            float timer = stack.getOrDefault(EnigmaticComponents.ELDRITCH_TIMER, 0.0F);
            if ((slotId == EquipmentSlot.MAINHAND) && EnigmaticHandler.isTheWorthyOne(livingEntity))
                stack.set(EnigmaticComponents.ELDRITCH_TIMER, Math.min(1.0F, timer + 0.3F));
            else stack.set(EnigmaticComponents.ELDRITCH_TIMER, Math.max(0.0F, timer - 0.3F));
        }
    }

    public void onUnequip(@NotNull SlotContext context, ItemStack newStack, ItemStack stack) {
        context.entity().getAttributes().removeAttributeModifiers(getModifiers());
        super.onUnequip(context, newStack, stack);
    }

    public Multimap<Holder<Attribute>, AttributeModifier> getModifiers() {
        Multimap<Holder<Attribute>, AttributeModifier> map = HashMultimap.create();
        map.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(IItemHelper.getLocation(this), attackDamage.getAsInt() * 0.01, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        map.put(EnigmaticAttributes.LIFESTEAL, new AttributeModifier(IItemHelper.getLocation(this), lifeSteal.getAsInt() * 0.01, AttributeModifier.Operation.ADD_VALUE));
        return map;
    }

    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext context, Identifier id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> attributes = HashMultimap.create();
        CuriosApi.addSlotModifier(attributes, "charm", IItemHelper.getLocation(this), 1.0, AttributeModifier.Operation.ADD_VALUE);
        return attributes;
    }

    @Mod(value = EnigmaticLegacy.MODID)
    @EventBusSubscriber(modid = EnigmaticLegacy.MODID)
    public static class Events {
        @SubscribeEvent
        private static void onDamaged(LivingDamageEvent.@NotNull Pre event) {
            DamageSource source = event.getSource();
            if (event.getNewDamage() >= Float.MAX_VALUE) return;
            if (source.getEntity() instanceof LivingEntity attacker && EnigmaticHandler.isAbyssBoosted(attacker) && EnigmaticHandler.hasCurio(attacker, EnigmaticItems.ELDRITCH_AMULET)) {
                if (event.getEntity().getHealth() < attacker.getHealth()) {
                    event.setNewDamage(event.getNewDamage() * 1.8F);
                }
            }
        }
    }
}
