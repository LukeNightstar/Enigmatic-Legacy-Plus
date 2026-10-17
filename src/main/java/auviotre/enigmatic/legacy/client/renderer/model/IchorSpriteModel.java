package auviotre.enigmatic.legacy.client.renderer.model;

import auviotre.enigmatic.legacy.EnigmaticLegacy;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import auviotre.enigmatic.legacy.client.renderer.IchorSpriteRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IchorSpriteModel extends EntityModel<IchorSpriteRenderer.State> implements ArmedModel<IchorSpriteRenderer.State> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(EnigmaticLegacy.location("ichor_sprite"), "main");
    private final ModelPart spriteRoot;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart head;

    public IchorSpriteModel(ModelPart root) {
        super(root, RenderTypes::entityTranslucent);
        this.spriteRoot = root.getChild("root");
        this.body = this.spriteRoot.getChild("body");
        this.rightArm = this.body.getChild("right_arm");
        this.leftArm = this.body.getChild("left_arm");
        this.rightWing = this.body.getChild("right_wing");
        this.leftWing = this.body.getChild("left_wing");
        this.head = this.spriteRoot.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition definition = new MeshDefinition();
        PartDefinition root1 = definition.getRoot();
        PartDefinition root = root1.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, -2.5F, 0.0F));
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -5.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 0.0F));
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 10).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 16).addBox(-1.5F, 1.0F, -1.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, 20.0F, 0.0F));
        body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(23, 0).addBox(-1.25F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offset(-1.75F, 0.25F, 0.0F));
        body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(23, 6).addBox(-0.75F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offset(1.75F, 0.25F, 0.0F));
        body.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(16, 14).mirror().addBox(0.0F, 0.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 1.0F, 1.0F));
        body.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(16, 14).addBox(0.0F, 0.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 1.0F, 1.0F));
        return LayerDefinition.create(definition, 32, 32);
    }

    public void setupAnim(IchorSpriteRenderer.State state) {
        super.setupAnim(state);
        float ageInTicks = state.ageInTicks;
        float limbSwingAmount = state.walkAnimationSpeed;
        this.head.yRot = state.yRot * 0.017453292F;
        this.head.xRot = state.xRot * 0.017453292F;
        float f = Mth.cos(ageInTicks * 5.5F * 0.017453292F) * 0.1F;
        this.rightArm.zRot = 0.62831855F + f;
        this.leftArm.zRot = -(0.62831855F + f);

        float f4 = Math.min(limbSwingAmount / 0.3F, 1.0F);
        float scale = state.attackAnimationScale;
        float rot = scale * Mth.lerp(f4, -1.0471976F, -1.134464F);
        this.body.xRot = 0.15707964F;
        this.rightArm.xRot = rot;
        this.leftArm.xRot = rot;
        float f13 = (1.0F - f4) * (1.0F - scale);
        float f14 = (float) (0.43633232F - Math.cos(Math.toRadians(ageInTicks * 9.0F) + 4.712389F) * Math.PI * 0.075F * f13);
        this.leftArm.zRot = -f14;
        this.rightArm.zRot = f14;
        this.rightArm.yRot = 0.27925268F * scale;
        this.leftArm.yRot = -0.27925268F * scale;

        this.leftWing.yRot = 1.0995574F + Mth.cos(ageInTicks * 45.836624F * 0.017453292F) * 0.017453292F * 16.2F;
        this.rightWing.yRot = -this.leftWing.yRot;
        this.leftWing.xRot = 0.47123888F;
        this.leftWing.zRot = -0.47123888F;
        this.rightWing.xRot = 0.47123888F;
        this.rightWing.zRot = 0.47123888F;
    }

    public void translateToHand(IchorSpriteRenderer.State state, HumanoidArm side, PoseStack poseStack) {
        boolean flag = side == HumanoidArm.RIGHT;
        ModelPart modelpart = flag ? this.rightArm : this.leftArm;
        this.root.translateAndRotate(poseStack);
        this.spriteRoot.translateAndRotate(poseStack);
        this.body.translateAndRotate(poseStack);
        modelpart.translateAndRotate(poseStack);
        poseStack.scale(0.55F, 0.55F, 0.55F);
        this.offsetStackPosition(poseStack, flag);
    }

    private void offsetStackPosition(PoseStack poseStack, boolean rightSide) {
        if (rightSide) {
            poseStack.translate(0.046875, -0.15625, 0.078125);
        } else {
            poseStack.translate(-0.046875, -0.15625, 0.078125);
        }
    }
}
