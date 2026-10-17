package auviotre.enigmatic.legacy.client.screen;

import net.minecraft.client.renderer.RenderPipelines;
import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.contents.gui.SpellstoneTableMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class SpellstoneTableScreen extends AbstractContainerScreen<SpellstoneTableMenu> {
    private static final Identifier TEXTURE = EnigmaticLegacy.location("textures/gui/spellstone_table.png");

    public SpellstoneTableScreen(SpellstoneTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    protected void init() {
        super.init();
        this.titleLabelX += 7;
        this.titleLabelY -= 1;
    }

    public void render(GuiGraphicsExtractor graphics, int x, int y, float partialTick) {
        super.render(graphics, x, y, partialTick);
        this.renderTooltip(graphics, x, y);
    }

    protected void renderBg(@NotNull GuiGraphicsExtractor graphics, float partialTicks, int x, int y) {
        RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, i, j, (float) 0, (float) 0, this.imageWidth, this.imageHeight, 256, 256);
        if (this.menu.hasOutput()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, i + 85, j + 53, (float) 176, (float) 0, 6, 6, 256, 256);
        }
    }
}
