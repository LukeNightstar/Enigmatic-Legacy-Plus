package auviotre.enigmatic.legacy;

import auviotre.enigmatic.legacy.contents.item.spellstones.other.SpellstoneSword;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;


public class ELEnumExtensions {
    @OnlyIn(Dist.CLIENT)
    // 26.1.2 port: ArmPose gained an affectsOffhandPose flag; this pose moves both arms like a bow.
    public static final EnumProxy<HumanoidModel.ArmPose> SPELLSTONE_SWORD = new EnumProxy<>(
            HumanoidModel.ArmPose.class, true, true,
            (IArmPoseTransformer) (model, state, arm) -> SpellstoneSword.ClientExtension.animate(model.rightArm, model.leftArm, state, HumanoidArm.RIGHT.equals(arm))
    );
}
