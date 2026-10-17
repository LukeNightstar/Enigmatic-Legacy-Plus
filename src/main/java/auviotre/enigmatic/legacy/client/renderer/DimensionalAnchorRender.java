package auviotre.enigmatic.legacy.client.renderer;

import auviotre.enigmatic.legacy.contents.block.DimensionalAnchor;
import auviotre.enigmatic.legacy.contents.block.entity.DimensionalAnchorEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class DimensionalAnchorRender implements BlockEntityRenderer<DimensionalAnchorEntity, DimensionalAnchorRender.State> {
    public DimensionalAnchorRender(BlockEntityRendererProvider.Context context) {
    }

    public State createRenderState() {
        return new State();
    }

    public void extractRenderState(DimensionalAnchorEntity entity, State state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(entity, state, partialTicks, cameraPosition, breakProgress);
        state.facesToShow.clear();
        if (entity.getBlockState().getValue(DimensionalAnchor.CHARGE) > 0) {
            for (Direction direction : Direction.values()) {
                if (entity.shouldRenderFace(direction)) state.facesToShow.add(direction);
            }
        }
    }

    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.facesToShow.isEmpty()) return;
        collector.submitCustomGeometry(poseStack, RenderTypes.endPortal(), (pose, consumer) -> {
            float down = 0.375F;
            float up = 1.0F - (1F / 16F);
            renderFace(state, pose, consumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, Direction.SOUTH);
            renderFace(state, pose, consumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Direction.NORTH);
            renderFace(state, pose, consumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.EAST);
            renderFace(state, pose, consumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.WEST);
            renderFace(state, pose, consumer, 0.0F, 1.0F, down, down, 0.0F, 0.0F, 1.0F, 1.0F, Direction.DOWN);
            renderFace(state, pose, consumer, 0.1F, 0.9F, up, up, 0.9F, 0.9F, 0.1F, 0.1F, Direction.UP);
        });
    }

    private static void renderFace(State state, PoseStack.Pose pose, VertexConsumer consumer, float x0, float x1, float y0, float y1, float z0, float z1, float z2, float z3, Direction direction) {
        if (state.facesToShow.contains(direction)) {
            consumer.addVertex(pose, x0, y0, z0);
            consumer.addVertex(pose, x1, y0, z1);
            consumer.addVertex(pose, x1, y1, z2);
            consumer.addVertex(pose, x0, y1, z3);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class State extends BlockEntityRenderState {
        public final Set<Direction> facesToShow = EnumSet.noneOf(Direction.class);
    }
}
