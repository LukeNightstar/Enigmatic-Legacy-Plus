package auviotre.enigmatic.legacy.client.renderer;

import auviotre.enigmatic.legacy.contents.entity.projectile.SoulFlameBall;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.BlockPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * The 1.21.1 renderer drew nothing (its body was commented out); the projectile is shown by its particles only.
 */
@OnlyIn(Dist.CLIENT)
public class SoulFlameBallRenderer extends EntityRenderer<SoulFlameBall, EntityRenderState> {
    public SoulFlameBallRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    protected int getBlockLightLevel(SoulFlameBall entity, BlockPos pos) {
        return 15;
    }

    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}
