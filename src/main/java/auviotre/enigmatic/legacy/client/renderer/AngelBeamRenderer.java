package auviotre.enigmatic.legacy.client.renderer;

import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.contents.entity.projectile.AngelBeam;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AngelBeamRenderer extends EntityRenderer<AngelBeam, AngelBeamRenderer.State> {
    private static final Identifier TEXTURE = EnigmaticLegacy.location("textures/entity/projectiles/angel_beam.png");

    public AngelBeamRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, int alpha, float u, float v) {
        consumer.addVertex(pose, x, y, z).setColor(255, 255, 255, alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(15728880).setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    protected int getBlockLightLevel(AngelBeam entity, BlockPos pos) {
        return 15;
    }

    protected int getSkyLightLevel(AngelBeam entity, BlockPos pos) {
        return 15;
    }

    public boolean shouldRender(AngelBeam entity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    public State createRenderState() {
        return new State();
    }

    public void extractRenderState(AngelBeam beam, State state, float partialTick) {
        super.extractRenderState(beam, state, partialTick);
        state.delta = new Vec3(beam.getBeginning()).subtract(beam.getEyePosition(partialTick));
        state.flicker = beam.tickCount % 4 < 2;
    }

    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        Vec3 delta = state.delta;
        float len = (float) delta.length();
        delta = delta.normalize();
        float phi = (float) Math.acos(delta.y);
        float theta = (float) Math.atan2(delta.z, delta.x);
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees((float) Math.toDegrees(Math.PI / 2 - theta)));
        poseStack.mulPose(Axis.XP.rotationDegrees((float) Math.toDegrees(phi)));
        float timer = state.ageInTicks * 8.0F;
        float f7 = timer * 0.05F * -1.5F;
        float f8 = (float) Math.pow((48.0F - state.ageInTicks) / 48.0F, 0.6);
        int alpha = Math.clamp((int) (f8 * 256), 0, 255);
        float f11 = Mth.cos(f7 + (float) (Math.PI * 3.0 / 4.0)) * 0.282F;
        float f12 = Mth.sin(f7 + (float) (Math.PI * 3.0 / 4.0)) * 0.282F;
        float f13 = Mth.cos(f7 + (float) (Math.PI / 4)) * 0.282F;
        float f14 = Mth.sin(f7 + (float) (Math.PI / 4)) * 0.282F;
        float f15 = Mth.cos(f7 + (float) (Math.PI * 5.0F / 4.0F)) * 0.282F;
        float f16 = Mth.sin(f7 + (float) (Math.PI * 5.0F / 4.0F)) * 0.282F;
        float f17 = Mth.cos(f7 + (float) (Math.PI * 7.0F / 4.0F)) * 0.282F;
        float f18 = Mth.sin(f7 + (float) (Math.PI * 7.0F / 4.0F)) * 0.282F;
        float f19 = Mth.cos(f7 + (float) Math.PI) * 0.2F;
        float f20 = Mth.sin(f7 + (float) Math.PI) * 0.2F;
        float f21 = Mth.cos(f7 + 0.0F) * 0.2F;
        float f22 = Mth.sin(f7 + 0.0F) * 0.2F;
        float f23 = Mth.cos(f7 + (float) (Math.PI / 2)) * 0.2F;
        float f24 = Mth.sin(f7 + (float) (Math.PI / 2)) * 0.2F;
        float f25 = Mth.cos(f7 + (float) (Math.PI * 3.0 / 2.0)) * 0.2F;
        float f26 = Mth.sin(f7 + (float) (Math.PI * 3.0 / 2.0)) * 0.2F;
        float v1 = 0.0F;
        float v2 = 1.0F;
        float yOffset = state.flicker ? 0.5F : 0.0F;
        collector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucent(TEXTURE), (pose, consumer) -> {
            vertex(consumer, pose, f19, len, f20, alpha, 0.4999F, v2);
            vertex(consumer, pose, f19, 0.0F, f20, alpha, 0.4999F, v1);
            vertex(consumer, pose, f21, 0.0F, f22, alpha, 0.0F, v1);
            vertex(consumer, pose, f21, len, f22, alpha, 0.0F, v2);
            vertex(consumer, pose, f19, len, f20, alpha, 0.4999F, v2);
            vertex(consumer, pose, f21, len, f22, alpha, 0.0F, v2);
            vertex(consumer, pose, f21, 0.0F, f22, alpha, 0.0F, v1);
            vertex(consumer, pose, f19, 0.0F, f20, alpha, 0.4999F, v1);
            vertex(consumer, pose, f23, len, f24, alpha, 0.4999F, v2);
            vertex(consumer, pose, f23, 0.0F, f24, alpha, 0.4999F, v1);
            vertex(consumer, pose, f25, 0.0F, f26, alpha, 0.0F, v1);
            vertex(consumer, pose, f25, len, f26, alpha, 0.0F, v2);
            vertex(consumer, pose, f23, len, f24, alpha, 0.4999F, v2);
            vertex(consumer, pose, f25, len, f26, alpha, 0.0F, v2);
            vertex(consumer, pose, f25, 0.0F, f26, alpha, 0.0F, v1);
            vertex(consumer, pose, f23, 0.0F, f24, alpha, 0.4999F, v1);

            vertex(consumer, pose, f11, len, f12, alpha, 0.5F, yOffset + 0.5F);
            vertex(consumer, pose, f13, len, f14, alpha, 1.0F, yOffset + 0.5F);
            vertex(consumer, pose, f17, len, f18, alpha, 1.0F, yOffset);
            vertex(consumer, pose, f15, len, f16, alpha, 0.5F, yOffset);
        });
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }

    @OnlyIn(Dist.CLIENT)
    public static class State extends EntityRenderState {
        public Vec3 delta = Vec3.ZERO;
        public boolean flicker;
    }
}
