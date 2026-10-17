package auviotre.enigmatic.legacy.client.screen;

import net.minecraft.client.renderer.RenderPipelines;
import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.contents.gui.SpellstoneTableMenu;
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

    @Override
    protected void init() {
        super.init();
        this.titleLabelX += 7;
        this.titleLabelY -= 1;
    }

    @Override
    public void extractBackground(@NotNull GuiGraphicsExtractor graphics, int x, int y, float partialTicks) {
        super.extractBackground(graphics, x, y, partialTicks);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, i, j, (float) 0, (float) 0, this.imageWidth, this.imageHeight, 256, 256);
        if (this.menu.hasOutput()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, i + 85, j + 53, (float) 176, (float) 0, 6, 6, 256, 256);
        }
    }
}
