package auviotre.enigmatic.legacy.mixin.compat;

import auviotre.enigmatic.legacy.compat.thirst.ThirstClientCompat;
import auviotre.enigmatic.legacy.contents.item.potions.ForbiddenJuice;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "cn.mlus.thirst.foundation.gui.ThirstBarRenderer")
public class MixinThirstBarRenderer {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private static void renderMix(int width, int height, GuiGraphicsExtractor guiGraphics, CallbackInfo info) {
        if (ForbiddenJuice.isForbiddenCursed(Minecraft.getInstance().player)) {
            ThirstClientCompat.renderBar(width, height, guiGraphics);
            info.cancel();
        }
    }
}
