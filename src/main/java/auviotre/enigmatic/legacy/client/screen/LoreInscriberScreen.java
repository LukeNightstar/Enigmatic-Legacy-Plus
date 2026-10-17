package auviotre.enigmatic.legacy.client.screen;

import net.minecraft.client.renderer.RenderPipelines;
import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.contents.gui.LoreInscriberMenu;
import auviotre.enigmatic.legacy.packets.server.LoreInscriberRenamePacket;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import net.neoforged.neoforge.client.network.ClientPacketDistributor;
public class LoreInscriberScreen extends AbstractContainerScreen<LoreInscriberMenu> implements ContainerListener {
    private static final Identifier TEXTURE = EnigmaticLegacy.location("textures/gui/lore_inscriber.png");
    private EditBox name;

    public LoreInscriberScreen(LoreInscriberMenu container, Inventory Inventory, Component title) {
        super(container, Inventory, title);
        this.titleLabelX = 60;
    }

    protected void subInit() {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.name = new EditBox(this.font, i + 55, j + 30, 95, 12, Component.translatable("gui.enigmaticlegacy.lore_inscriber"));
        this.name.setCanLoseFocus(false);
        this.name.setTextColor(-1);
        this.name.setTextColorUneditable(-1);
        this.name.setBordered(false);
        this.name.setMaxLength(128);
        this.name.setResponder(this::onNameChanged);
        this.addWidget(this.name);
        this.setInitialFocus(this.name);
    }

    protected void init() {
        super.init();
        this.subInit();
        this.menu.addSlotListener(this);
    }

    public void removed() {
        super.removed();
        this.menu.removeSlotListener(this);
    }

    public void render(GuiGraphicsExtractor graphics, int x, int y, float partialTick) {
        super.render(graphics, x, y, partialTick);
        this.renderNameField(graphics, x, y, partialTick);
        this.renderTooltip(graphics, x, y);
    }

    protected void renderBg(GuiGraphicsExtractor graphics, float partialTicks, int x, int y) {
        RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, i, j, (float) 0, (float) 0, this.imageWidth, this.imageHeight, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, i + 52, j + 26, (float) 0, (float) this.imageHeight + (this.menu.getSlot(0).hasItem() ? 0 : 16), 102, 16, 256, 256);
        if (this.menu.getSlot(0).hasItem() && !this.menu.getSlot(1).hasItem()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, i + 71, j + 49, (float) this.imageWidth, (float) 0, 28, 21, 256, 256);
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256 && this.minecraft.player != null) this.minecraft.player.closeContainer();
        return this.name.keyPressed(keyCode, scanCode, modifiers) || this.name.canConsumeInput() || super.keyPressed(keyCode, scanCode, modifiers);
    }

    public void resize(Minecraft minecraft, int width, int height) {
        String s = this.name.getValue();
        this.init(minecraft, width, height);
        this.name.setValue(s);
    }

    protected void renderLabels(GuiGraphicsExtractor graphics, int x, int y) {
        graphics.text(this.font, this.title, 52, 13, 0xFF404040);
    }

    public void renderNameField(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        this.name.render(graphics, mouseX, mouseY, partialTicks);
    }

    private void onNameChanged(String input) {
        Slot slot = this.menu.getSlot(0);
        if (slot.hasItem()) {
            String s = input;
            if (!slot.getItem().has(DataComponents.CUSTOM_NAME) && input.equals(slot.getItem().getHoverName().getString())) {
                s = "";
            }

            if (this.menu.setItemName(s)) {
                ClientPacketDistributor.sendToServer(new LoreInscriberRenamePacket(s));
            }
        }
    }

    public void slotChanged(AbstractContainerMenu menu, int id, ItemStack stack) {
        if (id == 0) {
            this.name.setValue(stack.isEmpty() ? "" : stack.getHoverName().getString());
            this.name.setEditable(!stack.isEmpty());
            this.setFocused(this.name);
        }
    }

    public void dataChanged(AbstractContainerMenu menu, int i, int i1) {
    }
}
