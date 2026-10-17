package auviotre.enigmatic.legacy.compat.thirst;

import cn.mlus.thirst.foundation.common.capability.ModAttachment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.profiling.Profiler;

/**
 * 26.1.2 port: split from ThirstCompatHandler, which is registered on both sides; event bus registration
 * reflects over every method, so client types must not appear in its method signatures.
 */
public final class ThirstClientCompat {
    private ThirstClientCompat() {
    }

    public static void renderBar(int width, int height, GuiGraphicsExtractor guiGraphics) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;
        Profiler.get().push("thirst");
        int left = width / 2 + 91; // + ClientConfig.THIRST_BAR_X_OFFSET.get();
        int top = height - minecraft.gui.rightHeight; // + ClientConfig.THIRST_BAR_Y_OFFSET.get();
        minecraft.gui.rightHeight += 10;
        int level = minecraft.player.getData(ModAttachment.PLAYER_THIRST).getThirst();

        for (int i = 0; i < 10; ++i) {
            int idx = i * 2 + 1;
            int x = left - i * 8 - 9;
            int y = top;
            if (minecraft.gui.getGuiTicks() % (level * 3 + 1) == 0) {
                y = top + (minecraft.player.getRandom().nextInt(3) - 1);
            }
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, ThirstCompatHandler.FORBIDDEN_DRINK_EMPTY_SPRITE, x, y, 9, 9);
            if (idx < level) {
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, ThirstCompatHandler.FORBIDDEN_DRINK_FULL_SPRITE, x, y, 9, 9);
            } else if (idx == level) {
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, ThirstCompatHandler.FORBIDDEN_DRINK_HALF_SPRITE, x, y, 9, 9);
            }
        }
        Profiler.get().pop();
    }
}
