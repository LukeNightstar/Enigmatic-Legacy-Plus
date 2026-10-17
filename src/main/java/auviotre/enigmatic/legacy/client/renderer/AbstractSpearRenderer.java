package auviotre.enigmatic.legacy.client.renderer;

import auviotre.enigmatic.legacy.contents.entity.projectile.AbstractSpear;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractSpearRenderer<T extends AbstractSpear> extends EntityRenderer<T, AbstractSpearRenderer.SpearRenderState> {
    private final ItemModelResolver itemModelResolver;
    private final Vec3 scale;
    private final float offset;

    public AbstractSpearRenderer(EntityRendererProvider.Context context, float offset, Vec3 scale) {
        super(context);
        this.itemModelResolver = context.getItemModelResolver();
        this.shadowRadius = 0.175F;
        this.offset = offset;
        this.scale = scale.scale(0.85);
    }

    public void submit(SpearRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot - 45.0F));
        poseStack.translate(-this.offset, -this.offset, 0);
        poseStack.translate(0, state.bbHeight * 0.2, 0);
        poseStack.scale((float) this.scale.x, (float) this.scale.y, (float) this.scale.z);
        state.item.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }

    public SpearRenderState createRenderState() {
        return new SpearRenderState();
    }

    public void extractRenderState(T spear, SpearRenderState state, float partialTicks) {
        super.extractRenderState(spear, state, partialTicks);
        state.yRot = spear.getYRot(partialTicks);
        state.xRot = spear.getXRot(partialTicks);
        state.bbHeight = spear.getBbHeight();
        this.itemModelResolver.updateForNonLiving(state.item, spear.getItem(), ItemDisplayContext.NONE, spear);
    }

    @OnlyIn(Dist.CLIENT)
    public static class SpearRenderState extends EntityRenderState {
        public final ItemStackRenderState item = new ItemStackRenderState();
        public float yRot;
        public float xRot;
        public float bbHeight;
    }
}
