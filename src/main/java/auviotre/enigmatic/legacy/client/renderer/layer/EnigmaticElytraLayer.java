package auviotre.enigmatic.legacy.client.renderer.layer;

import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.client.renderer.EnigmaticRenderData;
import auviotre.enigmatic.legacy.contents.item.generic.BaseElytraItem;
import auviotre.enigmatic.legacy.registries.EnigmaticItems;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.equipment.ElytraModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class EnigmaticElytraLayer<S extends HumanoidRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {
    private static final Map<Identifier, Identifier> TEXTURE_MAP = ImmutableMap.of(
            EnigmaticItems.MAJESTIC_ELYTRA.getId(), EnigmaticLegacy.location("textures/models/misc/majestic_elytra.png"),
            EnigmaticItems.CHAOS_ELYTRA.getId(), EnigmaticLegacy.location("textures/models/misc/chaos_elytra.png")
    );
    private final ElytraModel elytraModel;
    private final ElytraModel elytraBabyModel;

    public EnigmaticElytraLayer(RenderLayerParent<S, M> layerParent, EntityModelSet modelSet) {
        super(layerParent);
        this.elytraModel = new ElytraModel(modelSet.bakeLayer(ModelLayers.ELYTRA));
        this.elytraBabyModel = new ElytraModel(modelSet.bakeLayer(ModelLayers.ELYTRA_BABY));
    }

    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, S state, float yRot, float xRot) {
        ItemStack elytra = state.getRenderDataOrDefault(EnigmaticRenderData.ELYTRA, ItemStack.EMPTY);
        if (elytra.isEmpty() || !(elytra.getItem() instanceof BaseElytraItem)) return;
        Identifier texture = TEXTURE_MAP.get(BuiltInRegistries.ITEM.getKey(elytra.getItem()));
        if (texture == null) return;
        ElytraModel model = state.isBaby ? this.elytraBabyModel : this.elytraModel;
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 0.125F);
        collector.order(1).submitModel(model, state, poseStack, RenderTypes.armorCutoutNoCull(texture), lightCoords, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
        if (elytra.hasFoil())
            collector.order(2).submitModel(model, state, poseStack, RenderTypes.armorEntityGlint(), lightCoords, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
        poseStack.popPose();
    }
}
