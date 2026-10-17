package auviotre.enigmatic.legacy.compat.appleskin;

import net.minecraft.client.renderer.RenderPipelines;
import auviotre.enigmatic.legacy.client.handlers.ClientEventHandler;
import auviotre.enigmatic.legacy.contents.item.food.ForbiddenFruit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import squeek.appleskin.api.event.HUDOverlayEvent;
import squeek.appleskin.api.event.TooltipOverlayEvent;
import squeek.appleskin.helpers.TextureHelper;

public class AppleSkinCompatHandler {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onTooltipRender(TooltipOverlayEvent.@NotNull Render event) {
        if (!ForbiddenFruit.isForbiddenCursed(Minecraft.getInstance().player)) return;
        event.setCanceled(true);
        GuiGraphicsExtractor graphics = event.guiGraphics;
        int x = event.x;
        int y = event.y;
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ClientEventHandler.FORBIDDEN_FOOD_FULL_SPRITE, x, y, 9, 9);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TextureHelper.MOD_ICONS, x, y + 10, 21.0F, 34.0F, 7, 7, 256, 256);
        graphics.pose().pushMatrix();
        graphics.pose().translate(x + 7, y + 10);
        graphics.pose().scale(0.75F, 0.75F);
        graphics.text(Minecraft.getInstance().font, "x0", 2, 1, -5592406);
        graphics.pose().popMatrix();
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onHUDRender(HUDOverlayEvent.@NotNull Saturation event) {
        if (!ForbiddenFruit.isForbiddenCursed(Minecraft.getInstance().player)) return;
        event.setCanceled(true);
    }
}
