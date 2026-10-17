package auviotre.enigmatic.legacy.proxy;

import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.api.item.ITaintable;
import auviotre.enigmatic.legacy.client.screen.PermanentDeathScreen;
import auviotre.enigmatic.legacy.contents.item.rings.MinerRing;
import auviotre.enigmatic.legacy.contents.item.spellstones.other.SpellstoneSword;
import auviotre.enigmatic.legacy.registries.EnigmaticAttachments;
import auviotre.enigmatic.legacy.registries.EnigmaticComponents;
import auviotre.enigmatic.legacy.registries.EnigmaticItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import auviotre.enigmatic.legacy.client.item.LegacyItemProperty;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class ClientProxy extends CommonProxy {
    public void clientInit() {
        try {
            Identifier eldritchLocation = EnigmaticLegacy.location("eldritch_open");
            LegacyItemProperty.Function taintedFunc = (stack, level, entity, owner, i) -> ITaintable.isTainted(stack) ? 1.0F : 0.0F;
            LegacyItemProperty.Function eldritchFunc = (stack, level, entity, owner, i) -> stack.getOrDefault(EnigmaticComponents.ELDRITCH_TIMER, 0.0F);
            LegacyItemProperty.register(EnigmaticItems.TWISTED_HEART.get(), ITaintable.LOCATION, taintedFunc);
            LegacyItemProperty.register(EnigmaticItems.PURE_HEART.get(), ITaintable.LOCATION, taintedFunc);
            LegacyItemProperty.register(EnigmaticItems.ABYSSAL_HEART.get(), eldritchLocation, (stack, level, entity, owner, i) -> entity == null ? (ITaintable.isTainted(stack) ? 1.0F : 0.0F) : stack.getOrDefault(EnigmaticComponents.ELDRITCH_TIMER, 0.0F));
            LegacyItemProperty.register(EnigmaticItems.THE_INFINITUM.get(), eldritchLocation, eldritchFunc);
            LegacyItemProperty.register(EnigmaticItems.ELDRITCH_AMULET.get(), eldritchLocation, eldritchFunc);
            LegacyItemProperty.register(EnigmaticItems.DESOLATION_RING.get(), eldritchLocation, eldritchFunc);
            LegacyItemProperty.register(EnigmaticItems.VIOLENCE_SCROLL.get(), eldritchLocation, eldritchFunc);
            LegacyItemProperty.register(EnigmaticItems.CHAOS_ELYTRA.get(), eldritchLocation, eldritchFunc);
            LegacyItemProperty.register(EnigmaticItems.ANNIHILATING_SWORD.get(), eldritchLocation, eldritchFunc);
            LegacyItemProperty.register(EnigmaticItems.ANNIHILATING_SWORD.get(), Identifier.withDefaultNamespace("blocking"), (stack, level, entity, owner, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack && entity.getUsedItemHand() == InteractionHand.OFF_HAND ? 1.0F : 0.0F);
            LegacyItemProperty.register(EnigmaticItems.ANNIHILATING_SWORD.get(), Identifier.withDefaultNamespace("charging"), (stack, level, entity, owner, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack && entity.getUsedItemHand() == InteractionHand.MAIN_HAND ? 1.0F : 0.0F);
            LegacyItemProperty.register(EnigmaticItems.SPELLSTONE_SWORD.get(), Identifier.withDefaultNamespace("blocking"), (stack, level, entity, owner, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            LegacyItemProperty.register(EnigmaticItems.SPELLSTONE_SWORD.get(), Identifier.withDefaultNamespace("out"), (stack, level, entity, owner, i) ->
                    (SpellstoneSword.isResonatingWith(stack, EnigmaticItems.LOST_ENGINE) && entity != null && entity.getMainHandItem() == stack && entity.getData(EnigmaticAttachments.ENIGMATIC_DATA).getEngineHook() != null) ? 1.0F : 0.0F);
            LegacyItemProperty.register(EnigmaticItems.SPELLSTONE_SWORD.get(), Identifier.withDefaultNamespace("charged"), (stack, level, entity, owner, i) ->
                    (SpellstoneSword.isResonatingWith(stack, EnigmaticItems.ANGEL_BLESSING) && stack.getOrDefault(EnigmaticComponents.INT, 0) > 0) ? 1.0F : 0.0F);
            LegacyItemProperty.register(EnigmaticItems.SPELLSTONE_SWORD.get(), Identifier.withDefaultNamespace("pulling"), (stack, level, entity, owner, i) ->
                    (entity != null && SpellstoneSword.isResonatingWith(stack, EnigmaticItems.ANGEL_BLESSING) && entity.isUsingItem() && entity.getUseItem() == stack) ? (float) (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / stack.getUseDuration(entity) : 0.0F);
            LegacyItemProperty.register(EnigmaticItems.SPELLSTONE_SWORD.get(), EnigmaticLegacy.location("form"), (stack, level, entity, owner, i) -> SpellstoneSword.Form.getValue(stack));
            LegacyItemProperty.register(EnigmaticItems.MINER_RING.get(), Identifier.withDefaultNamespace("on"), (stack, level, entity, owner, i) -> MinerRing.getPoint(stack) > 0 ? 1.0F : 0.0F);
            LegacyItemProperty.register(EnigmaticItems.ICHOR_SPEAR.get(), Identifier.withDefaultNamespace("using"), (stack, level, entity, owner, i) -> entity != null && entity.getUseItem().equals(stack) ? 1.0F : 0.0F);
            LegacyItemProperty.register(EnigmaticItems.DRAGON_BREATH_BOW.get(), Identifier.withDefaultNamespace("pulling"), (stack, level, entity, owner, i) -> entity != null && entity.isUsingItem() && entity.getUseItem().equals(stack) ? 1.0F : 0.0F);
            LegacyItemProperty.register(EnigmaticItems.DRAGON_BREATH_BOW.get(), Identifier.withDefaultNamespace("pull"), (stack, level, entity, owner, i) -> (entity == null || entity.getUseItem() != stack) ? 0.0F : (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F);
            LegacyItemProperty.register(EnigmaticItems.ETHEREAL_FORGING_CHARM.get(), Identifier.withDefaultNamespace("active"), (stack, level, entity, owner, i) -> stack.getOrDefault(EnigmaticComponents.BOOLEAN, false) ? 1.0F : 0.0F);
            LegacyItemProperty.register(EnigmaticItems.ETHERIUM_CORE.get(), Identifier.withDefaultNamespace("active"), (stack, level, entity, owner, i) -> stack.getOrDefault(EnigmaticComponents.BOOLEAN, false) ? 1.0F : 0.0F);
            LegacyItemProperty.register(EnigmaticItems.SACRED_CHALICE.get(), Identifier.withDefaultNamespace("filled"), (stack, level, entity, owner, i) -> stack.getOrDefault(EnigmaticComponents.BOOLEAN, false) ? 1.0F : 0.0F);
            LegacyItemProperty.register(EnigmaticItems.EARTH_PROMISE.get(), Identifier.withDefaultNamespace("broken"), (stack, level, entity, owner, i) -> entity instanceof Player player && player.getCooldowns().isOnCooldown(stack) ? 1.0F : 0.0F);
            LegacyItemProperty.register(EnigmaticItems.ETHERIUM_SWORD.get(), Identifier.withDefaultNamespace("blocking"), (stack, level, entity, owner, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            LegacyItemProperty.register(EnigmaticItems.INFERNAL_SHIELD.get(), Identifier.withDefaultNamespace("blocking"), (stack, level, entity, owner, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            EnigmaticItems.SOUL_COMPASS.get().registerVariants();
            LegacyItemProperty.register(EnigmaticItems.ENIGMATIC_EYE.get(), Identifier.withDefaultNamespace("enigmatic_eye_activated"), (stack, level, entity, owner, i) -> EnigmaticItems.ENIGMATIC_EYE.get().getActivationProperty(stack));
        } catch (Exception exception) {
            EnigmaticLegacy.LOGGER.warn("Could not load item models.");
        }
    }

    public boolean isOperatorItemsTabEnabled() {
        return Minecraft.getInstance().options.operatorItemsTab().get();
    }

    public void displayPermanentDeathScreen() {
        if (Minecraft.getInstance().level != null) {
            boolean local = Minecraft.getInstance().isLocalServer();
            Minecraft.getInstance().level.disconnect(Component.translatable("gui.enigmaticlegacy.permanent_death_screen_title"));

            if (local) {
                Minecraft.getInstance().clearClientLevel(new GenericMessageScreen(Component.translatable("menu.savingLevel")));
            } else {
                Minecraft.getInstance().clearClientLevel(new ProgressScreen(true));
            }
        }

        PermanentDeathScreen screen = new PermanentDeathScreen(new SelectWorldScreen(new TitleScreen()), Component.translatable("gui.enigmaticlegacy.permanent_death_screen_title"), Component.translatable("message.enigmaticlegacy.permanent_death_screen"));
        PermanentDeathScreen.active = screen;
        Minecraft.getInstance().setScreen(screen);
    }

    public String getClientUsername() {
        return Minecraft.getInstance().getUser().getName();
    }
}
