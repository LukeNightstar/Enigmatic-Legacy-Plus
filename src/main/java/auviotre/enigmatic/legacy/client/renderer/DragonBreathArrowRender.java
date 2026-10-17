package auviotre.enigmatic.legacy.client.renderer;

import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.contents.entity.projectile.DragonBreathArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DragonBreathArrowRender extends ArrowRenderer<DragonBreathArrow, ArrowRenderState> {
    public static final Identifier LOCATION = EnigmaticLegacy.location("textures/entity/projectiles/dragon_breath_arrow.png");

    public DragonBreathArrowRender(EntityRendererProvider.Context context) {
        super(context);
    }

    protected Identifier getTextureLocation(ArrowRenderState state) {
        return LOCATION;
    }

    public ArrowRenderState createRenderState() {
        return new ArrowRenderState();
    }
}
