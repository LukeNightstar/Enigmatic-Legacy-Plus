package auviotre.enigmatic.legacy.client.renderer;

import net.minecraft.client.renderer.item.ItemModelResolver;
import auviotre.enigmatic.legacy.api.item.IPermanentCrystal;
import auviotre.enigmatic.legacy.api.item.ISharableItem;
import auviotre.enigmatic.legacy.contents.entity.misc.PermanentItemEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemClusterRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

@OnlyIn(Dist.CLIENT)
public class PermanentItemRenderer extends EntityRenderer<PermanentItemEntity, PermanentItemRenderer.State> {
    private final ItemModelResolver itemModelResolver;
    private final RandomSource random = RandomSource.create();

    public PermanentItemRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemModelResolver = context.getItemModelResolver();
        this.shadowRadius = 0.15F;
        this.shadowStrength = 0.75F;
    }

    public State createRenderState() {
        return new State();
    }

    public void extractRenderState(PermanentItemEntity entity, State state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        ItemStack stack = entity.getItem();
        state.hidden = isHiddenFromLocalPlayer(entity, stack);
        state.crystal = stack.getItem() instanceof IPermanentCrystal;
        state.shouldSpread = IClientItemExtensions.of(stack).shouldSpreadAsEntity(stack);
        state.hover = Mth.sin((entity.getAge() + partialTicks) / 10.0F + entity.hoverStart) * 0.1F + 0.1F;
        state.spin = entity.getItemHover(partialTicks);
        state.extractItemGroupRenderState(entity, stack, this.itemModelResolver);
    }

    private static boolean isHiddenFromLocalPlayer(PermanentItemEntity entity, ItemStack stack) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || !player.isAlive() && Math.sqrt(entity.distanceToSqr(player.getX(), player.getEyeY(), player.getZ())) <= 1.0)
            return true;
        if (stack.getItem() instanceof ISharableItem) {
            return entity.acceptors == null || !entity.acceptors.contains(player.getUUID());
        }
        return false;
    }

    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.hidden || state.item.isEmpty()) return;
        poseStack.pushPose();
        if (state.crystal) {
            poseStack.scale(1.25F, 1.25F, 1.25F);
            poseStack.translate(0, -0.1125D, 0);
        }
        AABB boundingBox = state.item.getModelBoundingBox();
        float minOffsetY = -((float) boundingBox.minY) + 0.0625F;
        poseStack.translate(0.0F, state.hover + minOffsetY, 0.0F);
        poseStack.mulPose(Axis.YP.rotation(state.spin));
        ItemEntityRenderer.submitMultipleFromCount(poseStack, collector, state.lightCoords, state, this.random, boundingBox);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }

    @OnlyIn(Dist.CLIENT)
    public static class State extends ItemClusterRenderState {
        public boolean hidden;
        public boolean crystal;
        public float hover;
        public float spin;
    }
}
