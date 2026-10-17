package auviotre.enigmatic.legacy.client.handlers;

import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.entity.player.PlayerModelType;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import auviotre.enigmatic.legacy.client.renderer.EnigmaticRenderData;
import auviotre.enigmatic.legacy.client.item.SpelltunerTint;
import auviotre.enigmatic.legacy.client.item.LegacyItemProperty;
import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.api.item.IItemHelper;
import auviotre.enigmatic.legacy.client.KeyHandler;
import auviotre.enigmatic.legacy.client.particles.IchorParticle;
import auviotre.enigmatic.legacy.client.particles.StarDustParticle;
import auviotre.enigmatic.legacy.client.particles.StarlightParticle;
import auviotre.enigmatic.legacy.client.renderer.*;
import auviotre.enigmatic.legacy.client.renderer.layer.EnigmaticElytraLayer;
import auviotre.enigmatic.legacy.client.renderer.layer.EtheriumShieldLayer;
import auviotre.enigmatic.legacy.client.renderer.model.EngineHookModel;
import auviotre.enigmatic.legacy.client.renderer.model.ExplorerMarkerModel;
import auviotre.enigmatic.legacy.client.renderer.model.IchorSpriteModel;
import auviotre.enigmatic.legacy.client.renderer.model.SpellstoneModel;
import auviotre.enigmatic.legacy.client.screen.AntiqueBagScreen;
import auviotre.enigmatic.legacy.client.screen.LoreInscriberScreen;
import auviotre.enigmatic.legacy.client.screen.SpellstoneTableScreen;
import auviotre.enigmatic.legacy.client.screen.button.EnderChestInventoryButton;
import auviotre.enigmatic.legacy.client.screen.button.MagnetRingInventoryButton;
import auviotre.enigmatic.legacy.contents.item.charms.HellBladeCharm;
import auviotre.enigmatic.legacy.contents.item.generic.BaseItem;
import auviotre.enigmatic.legacy.contents.item.spellstones.IllusionLantern;
import auviotre.enigmatic.legacy.contents.item.spellstones.other.SpellstoneSword;
import auviotre.enigmatic.legacy.contents.item.tools.AnnihilatingSword;
import auviotre.enigmatic.legacy.contents.item.tools.TotemOfMalice;
import auviotre.enigmatic.legacy.registries.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ItemLike;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.client.ICuriosScreen;

import net.minecraft.client.model.Model;
import net.minecraft.world.item.Item;
@Mod(value = EnigmaticLegacy.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = EnigmaticLegacy.MODID, value = Dist.CLIENT)
public class ClientSetupHandler {
    @SubscribeEvent(priority = EventPriority.LOW)
    private static void onInventoryGuiInit(ScreenEvent.Init.@NotNull Post event) {
        Screen screen = event.getScreen();
        if (screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen || screen instanceof ICuriosScreen) {
            AbstractContainerScreen<?> gui = (AbstractContainerScreen<?>) screen;
            boolean isCreative = screen instanceof CreativeModeInventoryScreen;
            event.addListener(MagnetRingInventoryButton.getInstance(gui, isCreative));
            event.addListener(EnderChestInventoryButton.getInstance(gui, isCreative));
        }
    }

    @SubscribeEvent
    private static void registerItemDecorator(@NotNull RegisterItemDecorationsEvent event) {
        event.register(EnigmaticItems.HELL_BLADE_CHARM, new HellBladeCharm.Decorator());
        event.register(EnigmaticItems.TOTEM_OF_MALICE, new TotemOfMalice.Decorator());
        event.register(EnigmaticItems.ILLUSION_LANTERN, new IllusionLantern.Decorator());
        event.register(EnigmaticItems.SPELLSTONE_SWORD, new SpellstoneSword.Decorator());
        event.register(EnigmaticItems.ANNIHILATING_SWORD, new AnnihilatingSword.Decorator());
        for (ItemLike item : IItemHelper.UNKNOWN_ITEMS) event.register(item, new BaseItem.UnknownDecorator());
        event.register(EnigmaticBlocks.STARLIGHT_BLOCK, new BaseItem.WIPDecorator());
    }

    @SubscribeEvent
    private static void registerItemTints(RegisterColorHandlersEvent.@NotNull ItemTintSources event) {
        event.register(SpelltunerTint.TYPE, SpelltunerTint.MAP_CODEC);
    }

    @SubscribeEvent
    private static void registerItemModelProperties(@NotNull RegisterRangeSelectItemModelPropertyEvent event) {
        event.register(LegacyItemProperty.TYPE, LegacyItemProperty.MAP_CODEC);
    }

    @SubscribeEvent
    private static void registerRenderStateModifiers(@NotNull RegisterRenderStateModifiersEvent event) {
        EnigmaticRenderData.register(event);
    }

    @SubscribeEvent
    private static void registerScreens(@NotNull RegisterMenuScreensEvent event) {
        event.register(EnigmaticMenus.ANTIQUE_BAG_MENU.get(), AntiqueBagScreen::new);
        event.register(EnigmaticMenus.LORE_INSCRIBER_MENU.get(), LoreInscriberScreen::new);
        event.register(EnigmaticMenus.SPELLSTONE_TABLE_MENU.get(), SpellstoneTableScreen::new);
    }

    @SubscribeEvent
    private static void registerParticleProvider(@NotNull RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(EnigmaticParticles.SPELL.get(), SpellParticle.MobEffectProvider::new);
        event.registerSpriteSet(EnigmaticParticles.ICHOR.get(), IchorParticle.Provider::new);
        event.registerSpriteSet(EnigmaticParticles.ICHOR_CURSE.get(), SpellParticle.Provider::new);
        event.registerSpriteSet(EnigmaticParticles.VIOLENCE_CURSE.get(), SpellParticle.Provider::new);
        event.registerSpriteSet(EnigmaticParticles.ETHER.get(), EndRodParticle.Provider::new);
        event.registerSpriteSet(EnigmaticParticles.STARLIGHT.get(), StarlightParticle.Provider::new);
        event.registerSpriteSet(EnigmaticParticles.BLUE_STAR_DUST.get(), StarDustParticle.Provider::new);
        event.registerSpriteSet(EnigmaticParticles.RED_STAR_DUST.get(), StarDustParticle.Provider::new);
        event.registerSpriteSet(EnigmaticParticles.PURPLE_STAR_DUST.get(), StarDustParticle.Provider::new);
    }

    @SubscribeEvent
    private static void registerEntityRenderers(EntityRenderersEvent.@NotNull RegisterRenderers event) {
        event.registerEntityRenderer(EnigmaticEntities.PERMANENT_ITEM_ENTITY.get(), PermanentItemRenderer::new);
        event.registerEntityRenderer(EnigmaticEntities.COBWEB_BALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EnigmaticEntities.DRAGON_BREATH_ARROW.get(), DragonBreathArrowRender::new);
        event.registerEntityRenderer(EnigmaticEntities.THROWN_ICHOR_SPEAR.get(), ThrownIchorSpearRenderer::new);
        event.registerEntityRenderer(EnigmaticEntities.ICHOR_SPRITE.get(), IchorSpriteRenderer::new);
        event.registerEntityRenderer(EnigmaticEntities.SOUL_FLAME_BALL.get(), SoulFlameBallRenderer::new);
        event.registerEntityRenderer(EnigmaticEntities.ULTIMATE_WITHER_SKULL.get(), UltimateWitherSkullRenderer::new);
        event.registerEntityRenderer(EnigmaticEntities.EXPLORER_MARKER.get(), ExplorerMarkerRender::new);
        event.registerEntityRenderer(EnigmaticEntities.STARLIGHT_METEOR.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EnigmaticEntities.ABYSS_PROJECTILE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EnigmaticEntities.ENGINE_HOOK.get(), EngineHookRenderer::new);
        event.registerEntityRenderer(EnigmaticEntities.ANGEL_BEAM.get(), AngelBeamRenderer::new);
        event.registerEntityRenderer(EnigmaticEntities.PIGLIN_WANDERER.get(), PiglinWandererRender::new);
        event.registerBlockEntityRenderer(EnigmaticBlockEntities.DIMENSIONAL_ANCHOR_ENTITY.get(), DimensionalAnchorRender::new);
        event.registerBlockEntityRenderer(EnigmaticBlockEntities.SPELLSTONE_TABLE_ENTITY.get(), SpellstoneTableRender::new);
    }

    @SubscribeEvent
    private static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SpellstoneModel.LAYER, SpellstoneModel::createLayer);
        event.registerLayerDefinition(ExplorerMarkerModel.LAYER, ExplorerMarkerModel::createLayer);
        event.registerLayerDefinition(EngineHookModel.LAYER, EngineHookModel::createLayer);
        event.registerLayerDefinition(IchorSpriteModel.LAYER, IchorSpriteModel::createBodyLayer);
    }

    @SubscribeEvent
    private static void registerKeybindings(@NotNull RegisterKeyMappingsEvent event) {
        event.register(KeyHandler.SPELLSTONE.get());
        event.register(KeyHandler.SCROLL.get());
        event.register(KeyHandler.ENDER_RING.get());
    }

    @SubscribeEvent
    private static void registerClientExtensions(@NotNull RegisterClientExtensionsEvent event) {
        event.registerItem(new SpellstoneSword.ClientExtension(), EnigmaticItems.SPELLSTONE_SWORD);
    }

    @SubscribeEvent
    private static void addLayers(EntityRenderersEvent.AddLayers event) {
        for (PlayerModelType skin : event.getSkins()) {
            AvatarRenderer<AbstractClientPlayer> renderer = event.getPlayerRenderer(skin);
            if (renderer == null) continue;
            renderer.addLayer(new EnigmaticElytraLayer<>(renderer, event.getEntityModels()));
            renderer.addLayer(new EtheriumShieldLayer(renderer, event.getEntityModels(), skin == PlayerModelType.SLIM));
        }
        ArmorStandRenderer armorStand = event.getRenderer(EntityType.ARMOR_STAND);
        if (armorStand != null) armorStand.addLayer(new EnigmaticElytraLayer<>(armorStand, event.getEntityModels()));
    }
}