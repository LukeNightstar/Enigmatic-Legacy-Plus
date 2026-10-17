package auviotre.enigmatic.legacy.client.renderer;

import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.client.renderer.model.PiglinWandererModel;
import auviotre.enigmatic.legacy.contents.entity.PiglinWanderer;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.piglin.AdultPiglinModel;
import net.minecraft.client.model.monster.piglin.PiglinModel;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.PiglinRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.PiglinRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CrossbowItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PiglinWandererRender extends HumanoidMobRenderer<PiglinWanderer, PiglinRenderState, PiglinModel> {
    private static final Identifier TEXTURE = EnigmaticLegacy.location("textures/entity/piglin_wanderer.png");

    public PiglinWandererRender(EntityRendererProvider.Context context) {
        super(context, new PiglinWandererModel(context.bakeLayer(ModelLayers.PIGLIN_BRUTE)), new PiglinWandererModel(context.bakeLayer(ModelLayers.PIGLIN_BRUTE)), 0.5F, PiglinRenderer.PIGLIN_CUSTOM_HEAD_TRANSFORMS);
        this.addLayer(new HumanoidArmorLayer<>(this, ArmorModelSet.bake(ModelLayers.PIGLIN_BRUTE_ARMOR, context.getModelSet(), AdultPiglinModel::new), context.getEquipmentRenderer()));
    }

    public Identifier getTextureLocation(PiglinRenderState state) {
        return TEXTURE;
    }

    public PiglinRenderState createRenderState() {
        return new PiglinRenderState();
    }

    public void extractRenderState(PiglinWanderer entity, PiglinRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.armPose = entity.getArmPose();
        state.maxCrossbowChageDuration = CrossbowItem.getChargeDuration(entity.getUseItem(), entity);
        state.isConverting = entity.isConverting();
    }

    protected boolean isShaking(PiglinRenderState state) {
        return super.isShaking(state) || state.isConverting;
    }
}
