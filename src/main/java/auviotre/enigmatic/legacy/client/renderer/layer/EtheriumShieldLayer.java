package auviotre.enigmatic.legacy.client.renderer.layer;

import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.client.renderer.EnigmaticRenderData;
import auviotre.enigmatic.legacy.contents.item.etherium.EtheriumArmor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * 26.1.2 port: the player model now poses itself from the render state (arm poses included),
 * so the manual arm pose copy of the 1.21.1 version is gone.
 */
@OnlyIn(Dist.CLIENT)
public class EtheriumShieldLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    private static final Identifier TEXTURE = EnigmaticLegacy.location("textures/models/misc/ultimate_wither_armor.png");
    private final PlayerModel model;

    public EtheriumShieldLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer, EntityModelSet modelSet, boolean slim) {
        super(renderer);
        this.model = new PlayerModel(modelSet.bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slim);
    }

    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
        if (!EtheriumArmor.etheriumShieldRenderLayer.get() || !state.getRenderDataOrDefault(EnigmaticRenderData.ETHERIUM_SHIELD, false))
            return;
        float f = state.ageInTicks;
        float scale = 1.02F;
        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0, (1F - scale) / 2F, 0);
        collector.order(1).submitModel(this.model, state, poseStack, RenderTypes.energySwirl(TEXTURE, this.xOffset(f) % 1.0F, f * 0.01F % 1.0F),
                lightCoords, OverlayTexture.NO_OVERLAY, 0xAAFFFFFF, null, state.outlineColor, null);
        poseStack.popPose();
    }

    protected float xOffset(float tickCount) {
        return Mth.cos(tickCount * 0.02F) * 2.0F;
    }
}
