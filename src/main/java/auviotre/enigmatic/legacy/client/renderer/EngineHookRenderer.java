package auviotre.enigmatic.legacy.client.renderer;

import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.client.renderer.model.EngineHookModel;
import auviotre.enigmatic.legacy.contents.entity.projectile.EngineHook;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
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
import net.minecraft.util.Unit;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class EngineHookRenderer extends EntityRenderer<EngineHook, EngineHookRenderer.State> {
    private static final Identifier CHAIN_TEXTURE = EnigmaticLegacy.location("textures/entity/projectiles/engine_chain.png");
    private static final Identifier TEXTURE = EnigmaticLegacy.location("textures/entity/projectiles/engine_hook.png");
    private final EngineHookModel model;

    public EngineHookRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new EngineHookModel(context.bakeLayer(EngineHookModel.LAYER));
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, float u, float v, int light) {
        consumer.addVertex(pose, x, y, z).setColor(255, 255, 255, 255).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light).setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    protected int getBlockLightLevel(EngineHook entity, BlockPos pos) {
        return 15;
    }

    protected int getSkyLightLevel(EngineHook entity, BlockPos pos) {
        return 15;
    }

    public boolean shouldRender(EngineHook entity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    public State createRenderState() {
        return new State();
    }

    public void extractRenderState(EngineHook hook, State state, float partialTick) {
        super.extractRenderState(hook, state, partialTick);
        state.yRot = Mth.lerp(partialTick, hook.yRotO, hook.getYRot());
        state.xRot = Mth.lerp(partialTick, -hook.xRotO, -hook.getXRot());
        state.chainDelta = null;
        if (hook.getOwner() instanceof LivingEntity owner) {
            float f = owner.getAttackAnim(partialTick);
            float f1 = Mth.sin(Mth.sqrt(f) * (float) Math.PI);
            state.chainDelta = this.getPlayerHandPos(owner, f1, partialTick).subtract(hook.getEyePosition(partialTick));
        }
    }

    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(state.xRot));
        collector.submitModel(this.model, Unit.INSTANCE, poseStack, this.model.renderType(TEXTURE), state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
        poseStack.popPose();
        if (state.chainDelta != null) this.submitChain(state, poseStack, collector);
        super.submit(state, poseStack, collector, camera);
    }

    private void submitChain(State state, PoseStack poseStack, SubmitNodeCollector collector) {
        float pi = (float) Math.PI;
        Vec3 delta = state.chainDelta;
        float len = (float) delta.length();
        delta = delta.normalize();
        float phi = (float) Math.acos(delta.y);
        float theta = (float) Math.atan2(delta.z, delta.x);
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotation(pi / 2 - theta));
        poseStack.mulPose(Axis.XP.rotation(phi));
        float f7 = len * 0.2F;
        float range = 0.09375F;
        float f19 = Mth.cos(f7 + pi) * range;
        float f20 = Mth.sin(f7 + pi) * range;
        float f21 = Mth.cos(f7 + 0.0F) * range;
        float f22 = Mth.sin(f7 + 0.0F) * range;
        float f23 = Mth.cos(f7 + pi / 2) * range;
        float f24 = Mth.sin(f7 + pi / 2) * range;
        float f25 = Mth.cos(f7 + pi * 1.5F) * range;
        float f26 = Mth.sin(f7 + pi * 1.5F) * range;
        float v1Base = 0.0F - state.ageInTicks * 0.01F;
        float v2Base = len / 2.25F - state.ageInTicks * 0.01F;
        int light = state.lightCoords;
        collector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucent(CHAIN_TEXTURE), (pose, consumer) -> {
            float v1 = v1Base;
            float v2 = v2Base;
            vertex(consumer, pose, f19, len, f20, 1.0F, v2, light);
            vertex(consumer, pose, f19, 0.0F, f20, 1.0F, v1, light);
            vertex(consumer, pose, f21, 0.0F, f22, 0.0F, v1, light);
            vertex(consumer, pose, f21, len, f22, 0.0F, v2, light);
            vertex(consumer, pose, f19, len, f20, 1.0F, v2, light);
            vertex(consumer, pose, f21, len, f22, 0.0F, v2, light);
            vertex(consumer, pose, f21, 0.0F, f22, 0.0F, v1, light);
            vertex(consumer, pose, f19, 0.0F, f20, 1.0F, v1, light);
            float offset = 1 / 12.0F;
            v1 += offset;
            v2 += offset;
            vertex(consumer, pose, f23, len, f24, 1.0F, v2, light);
            vertex(consumer, pose, f23, 0.0F, f24, 1.0F, v1, light);
            vertex(consumer, pose, f25, 0.0F, f26, 0.0F, v1, light);
            vertex(consumer, pose, f25, len, f26, 0.0F, v2, light);
            vertex(consumer, pose, f23, len, f24, 1.0F, v2, light);
            vertex(consumer, pose, f25, len, f26, 0.0F, v2, light);
            vertex(consumer, pose, f25, 0.0F, f26, 0.0F, v1, light);
            vertex(consumer, pose, f23, 0.0F, f24, 1.0F, v1, light);
        });
        poseStack.popPose();
    }

    private Vec3 getPlayerHandPos(LivingEntity entity, float attack, float partialTick) {
        int i = entity.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
        if (this.entityRenderDispatcher.options.getCameraType().isFirstPerson() && entity == Minecraft.getInstance().player) {
            float fov = this.entityRenderDispatcher.options.fov().get().intValue();
            double d4 = 960.0F / (double) fov;
            float scale = Minecraft.getInstance().player.getAttackStrengthScale(partialTick);
            Vec3 vec3 = this.entityRenderDispatcher.camera.getNearPlane(fov).getPointOnPlane(i - attack, -1.075F - attack + (float) Math.pow(scale, 3)).scale(d4);
            return entity.getEyePosition(partialTick).add(vec3);
        } else {
            float bodyRot = Mth.lerp(partialTick, entity.yBodyRotO, entity.yBodyRot) * ((float) Math.PI / 180F);
            double d0 = Mth.sin(bodyRot);
            double d1 = Mth.cos(bodyRot);
            float scale = entity.getScale();
            double d2 = i * (entity.isCrouching() ? 0.36 : 0.35) * scale;
            double d3 = (entity.isCrouching() ? 0.63 : 0.75) * scale;
            float f2 = entity.isCrouching() ? -0.22F : 0.0F;
            return entity.getEyePosition(partialTick).add(-d1 * d2 - d0 * d3, f2 - 0.6 * scale, -d0 * d2 + d1 * d3);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class State extends EntityRenderState {
        public float yRot;
        public float xRot;
        public @Nullable Vec3 chainDelta;
    }
}
