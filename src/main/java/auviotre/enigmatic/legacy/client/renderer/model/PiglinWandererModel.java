package auviotre.enigmatic.legacy.client.renderer.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.monster.piglin.AdultPiglinModel;
import net.minecraft.client.renderer.entity.state.PiglinRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PiglinWandererModel extends AdultPiglinModel {
    public PiglinWandererModel(ModelPart part) {
        super(part);
    }

    // 26.1.2 port: PiglinWanderer#getArmPose only returns CROSSBOW_HOLD while charging, so the pose alone marks the spear charge.
    public void setupAnim(PiglinRenderState state) {
        super.setupAnim(state);
        if (state.armPose == PiglinArmPose.CROSSBOW_HOLD) {
            if (state.mainArm == HumanoidArm.LEFT) {
                this.leftArm.xRot = this.leftArm.xRot * 0.16F - (float) Math.PI;
                this.leftArm.yRot = 0.0F;
                this.rightArm.xRot = this.rightArm.xRot * 0.5F - (float) (Math.PI / 10);
                this.rightArm.yRot = 0.0F;
            } else {
                this.rightArm.xRot = this.rightArm.xRot * 0.16F - (float) Math.PI;
                this.rightArm.yRot = 0.0F;
                this.leftArm.xRot = this.leftArm.xRot * 0.5F - (float) (Math.PI / 10);
                this.leftArm.yRot = 0.0F;
            }
        }
    }
}
