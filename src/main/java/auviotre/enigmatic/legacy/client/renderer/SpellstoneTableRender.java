package auviotre.enigmatic.legacy.client.renderer;

import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.client.renderer.model.SpellstoneModel;
import auviotre.enigmatic.legacy.contents.block.entity.SpellstoneTableEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class SpellstoneTableRender implements BlockEntityRenderer<SpellstoneTableEntity, SpellstoneTableRender.State> {
    private static final Identifier TEXTURE = EnigmaticLegacy.location("textures/models/misc/spellstone_table_cube.png");
    private final SpellstoneModel model;

    public SpellstoneTableRender(BlockEntityRendererProvider.Context context) {
        this.model = new SpellstoneModel(context.bakeLayer(SpellstoneModel.LAYER));
    }

    public State createRenderState() {
        return new State();
    }

    public void extractRenderState(SpellstoneTableEntity entity, State state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(entity, state, partialTicks, cameraPosition, breakProgress);
        state.render = entity.render;
        state.time = entity.time + partialTicks;
        state.rot = Mth.lerp(partialTicks, entity.oRot, entity.rot);
    }

    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (!state.render) return;
        poseStack.pushPose();
        poseStack.translate(0.5F, 1.08F + Mth.sin(state.time * 0.1F) * 0.025F, 0.5F);
        poseStack.mulPose(Axis.ZP.rotation(0.25F + state.rot));
        poseStack.mulPose(Axis.XP.rotation(0.1F - state.rot));
        poseStack.mulPose(Axis.YP.rotation(state.rot));
        collector.submitModel(this.model, Unit.INSTANCE, poseStack, RenderTypes.entityTranslucent(TEXTURE), state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, 0, state.breakProgress);
        poseStack.popPose();
    }

    @OnlyIn(Dist.CLIENT)
    public static class State extends BlockEntityRenderState {
        public boolean render;
        public float time;
        public float rot;
    }
}
