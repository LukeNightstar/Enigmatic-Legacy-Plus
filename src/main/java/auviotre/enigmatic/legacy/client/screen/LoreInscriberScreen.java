package auviotre.enigmatic.legacy.client.screen;

import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.contents.gui.LoreInscriberMenu;
import auviotre.enigmatic.legacy.packets.server.LoreInscriberRenamePacket;
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
        this.addRenderableWidget(this.name);
        this.setInitialFocus(this.name);
    }

    @Override
    protected void init() {
        super.init();
        this.subInit();
        this.menu.addSlotListener(this);
    }

    @Override
    public void removed() {
        super.removed();
        this.menu.removeSlotListener(this);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int x, int y, float partialTicks) {
        super.extractBackground(graphics, x, y, partialTicks);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, i, j, (float) 0, (float) 0, this.imageWidth, this.imageHeight, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, i + 52, j + 26, (float) 0, (float) this.imageHeight + (this.menu.getSlot(0).hasItem() ? 0 : 16), 102, 16, 256, 256);
        if (this.menu.getSlot(0).hasItem() && !this.menu.getSlot(1).hasItem()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, i + 71, j + 49, (float) this.imageWidth, (float) 0, 28, 21, 256, 256);
        }
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.isEscape() && this.minecraft.player != null) this.minecraft.player.closeContainer();
        return this.name.keyPressed(event) || this.name.canConsumeInput() || super.keyPressed(event);
    }

    @Override
    public void resize(int width, int height) {
        String s = this.name.getValue();
        this.init(width, height);
        this.name.setValue(s);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int x, int y) {
        graphics.text(this.font, this.title, 52, 13, 0xFF404040);
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
