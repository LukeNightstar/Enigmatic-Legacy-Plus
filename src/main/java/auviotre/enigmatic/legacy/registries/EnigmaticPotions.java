package auviotre.enigmatic.legacy.registries;

import auviotre.enigmatic.legacy.EnigmaticLegacy;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class EnigmaticPotions {
    public static final List<Holder<Potion>> ULTIMATE_POTIONS = new ArrayList<>();

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, EnigmaticLegacy.MODID);
    public static final DeferredHolder<Potion, Potion> HASTE = POTIONS.register("haste", () -> new Potion("haste", new MobEffectInstance(MobEffects.HASTE, 3600)));
    public static final DeferredHolder<Potion, Potion> LONG_HASTE = POTIONS.register("long_haste", () -> new Potion("long_haste", new MobEffectInstance(MobEffects.HASTE, 9600)));
    public static final DeferredHolder<Potion, Potion> STRONG_HASTE = POTIONS.register("strong_haste", () -> new Potion("strong_haste", new MobEffectInstance(MobEffects.HASTE, 1800, 1)));
    public static final DeferredHolder<Potion, Potion> MOLTEN_HEART = POTIONS.register("molten_heart", () -> new Potion("molten_heart", new MobEffectInstance(EnigmaticEffects.MOLTEN_HEART, 3600)));
    public static final DeferredHolder<Potion, Potion> LONG_MOLTEN_HEART = POTIONS.register("long_molten_heart", () -> new Potion("long_molten_heart", new MobEffectInstance(EnigmaticEffects.MOLTEN_HEART, 9600)));
    // Ultimate Potions
    public static final DeferredHolder<Potion, Potion> ULTIMATE_NIGHT_VISION = POTIONS.register("ultimate_night_vision", () -> new Potion("ultimate_night_vision", new MobEffectInstance(MobEffects.NIGHT_VISION, 18000)));
    public static final DeferredHolder<Potion, Potion> ULTIMATE_INVISIBILITY = POTIONS.register("ultimate_invisibility", () -> new Potion("ultimate_invisibility", new MobEffectInstance(MobEffects.INVISIBILITY, 18000)));
    public static final DeferredHolder<Potion, Potion> ULTIMATE_LEAPING = POTIONS.register("ultimate_leaping", () -> new Potion("ultimate_leaping", new MobEffectInstance(MobEffects.JUMP_BOOST, 3000, 2)));
    public static final DeferredHolder<Potion, Potion> ULTIMATE_FIRE_RESISTANCE = POTIONS.register("ultimate_fire_resistance", () -> new Potion("ultimate_fire_resistance", new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 18000)));
    public static final DeferredHolder<Potion, Potion> ULTIMATE_SWIFTNESS = POTIONS.register("ultimate_swiftness", () -> new Potion("ultimate_swiftness", new MobEffectInstance(MobEffects.SPEED, 3000, 2)));
    public static final DeferredHolder<Potion, Potion> ULTIMATE_SLOWNESS = POTIONS.register("ultimate_slowness", () -> new Potion("ultimate_slowness", new MobEffectInstance(MobEffects.SLOWNESS, 3000, 4)));
    public static final DeferredHolder<Potion, Potion> ULTIMATE_TURTLE_MASTER = POTIONS.register("ultimate_turtle_master", () -> new Potion("ultimate_turtle_master", new MobEffectInstance(MobEffects.SLOWNESS, 1000, 5), new MobEffectInstance(MobEffects.RESISTANCE, 1000, 3)));
    public static final DeferredHolder<Potion, Potion> ULTIMATE_WATER_BREATHING = POTIONS.register("ultimate_water_breathing", () -> new Potion("ultimate_water_breathing", new MobEffectInstance(MobEffects.WATER_BREATHING, 18000)));
    public static final DeferredHolder<Potion, Potion> ULTIMATE_HEALING = POTIONS.register("ultimate_healing", () -> new Potion("ultimate_healing", new MobEffectInstance(MobEffects.INSTANT_HEALTH, 1, 2)));
    public static final DeferredHolder<Potion, Potion> ULTIMATE_HARMING = POTIONS.register("ultimate_harming", () -> new Potion("ultimate_harming", new MobEffectInstance(MobEffects.INSTANT_DAMAGE, 1, 2)));
    public static final DeferredHolder<Potion, Potion> ULTIMATE_POISON = POTIONS.register("ultimate_poison", () -> new Potion("ultimate_poison", new MobEffectInstance(MobEffects.POISON, 1200, 2)));
    public static final DeferredHolder<Potion, Potion> ULTIMATE_REGENERATION = POTIONS.register("ultimate_regeneration", () -> new Potion("ultimate_regeneration", new MobEffectInstance(MobEffects.REGENERATION, 1200, 2)));
    public static final DeferredHolder<Potion, Potion> ULTIMATE_STRENGTH = POTIONS.register("ultimate_strength", () -> new Potion("ultimate_strength", new MobEffectInstance(MobEffects.STRENGTH, 3000, 2)));
    public static final DeferredHolder<Potion, Potion> ULTIMATE_WEAKNESS = POTIONS.register("ultimate_weakness", () -> new Potion("ultimate_weakness", new MobEffectInstance(MobEffects.WEAKNESS, 9000)));
    public static final DeferredHolder<Potion, Potion> ULTIMATE_LUCK = POTIONS.register("ultimate_luck", () -> new Potion("ultimate_luck", new MobEffectInstance(MobEffects.LUCK, 10800)));
    public static final DeferredHolder<Potion, Potion> ULTIMATE_SLOW_FALLING = POTIONS.register("ultimate_slow_falling", () -> new Potion("ultimate_slow_falling", new MobEffectInstance(MobEffects.SLOW_FALLING, 9000)));
}
