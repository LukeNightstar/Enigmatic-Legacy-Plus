package auviotre.enigmatic.legacy.contents.attachement;

import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

public class AntiqueBagInventory implements ValueIOSerializable {
    public static final int SLOTS_COUNT = 12;
    // 26.1.2 port: ItemStackHandler is deprecated for removal; the transfer API handler replaces it.
    BookHandler handler = new BookHandler();

    public BookHandler getStackHandler() {
        return this.handler;
    }

    public void init() {
        for (int i = 0; i < handler.size(); i++) {
            handler.set(i, ItemResource.EMPTY, 0);
        }
    }

    @Override
    public void serialize(ValueOutput output) {
        this.handler.serialize(output);
    }

    @Override
    public void deserialize(ValueInput input) {
        this.handler.deserialize(input);
        // Bags saved by ItemStackHandler stored an "Items" list of slot/stack pairs instead of "stacks".
        input.listOrEmpty("Items", ItemStackWithSlot.CODEC).forEach(slot -> {
            if (slot.isValidInContainer(handler.size())) handler.setStack(slot.slot(), slot.stack());
        });
    }

    public static class BookHandler extends ItemStacksResourceHandler {
        BookHandler() {
            super(SLOTS_COUNT);
        }

        /**
         * The stored stack itself (not a copy), like ItemStackHandler#getStackInSlot.
         */
        public ItemStack getStack(int index) {
            return this.stacks.get(index);
        }

        public void setStack(int index, ItemStack stack) {
            this.set(index, ItemResource.of(stack), stack.getCount());
        }
    }
}
