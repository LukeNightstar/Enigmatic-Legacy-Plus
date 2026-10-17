package auviotre.enigmatic.legacy.contents.capability;

import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;

import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.contents.attachement.AntiqueBagInventory;
import auviotre.enigmatic.legacy.registries.EnigmaticAttachments;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class AntiqueBagCapability implements IAntiqueBagHandler {
    public static final Identifier ID = EnigmaticLegacy.location("antique_bag_inventory");
    AntiqueBagInventory inventory;
    LivingEntity owner;

    public AntiqueBagCapability(final LivingEntity entity) {
        this.owner = entity;
        this.inventory = entity.getData(EnigmaticAttachments.ANTIQUE_BAG_INVENTORY);
    }

    public void reset() {
        this.inventory.init();
    }

    public int getSlots() {
        return AntiqueBagInventory.SLOTS_COUNT;
    }

    public ItemStack getBook(int index) {
        return this.inventory.getStackHandler().getStack(index);
    }

    public void setBook(int index, ItemStack book) {
        this.inventory.getStackHandler().setStack(index, book);
    }

    public ItemStack findBook(Item book) {
        AntiqueBagInventory.BookHandler handler = this.getInventory();
        for (int i = 0; i < handler.size(); i++) {
            ItemStack stack = handler.getStack(i);
            if (stack.is(book)) return stack;
        }
        return ItemStack.EMPTY;
    }

    public LivingEntity getOwner() {
        return owner;
    }

    public AntiqueBagInventory.BookHandler getInventory() {
        return this.inventory.getStackHandler();
    }

    public Tag writeTag() {
        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, this.owner.registryAccess());
        this.inventory.serialize(output);
        return output.buildResult();
    }

    public void readTag(Tag nbt) {
        this.inventory.deserialize(TagValueInput.create(ProblemReporter.DISCARDING, this.owner.registryAccess(), (CompoundTag) nbt));
    }
}
