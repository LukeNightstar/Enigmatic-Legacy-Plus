package auviotre.enigmatic.legacy.contents.capability;

import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import auviotre.enigmatic.legacy.contents.attachement.AntiqueBagInventory;

public interface IAntiqueBagHandler {
    void reset();

    int getSlots();

    ItemStack getBook(int index);

    void setBook(int index, ItemStack book);

    ItemStack findBook(Item book);

    // boolean hasFlower();

    // void tickFlowers();

    LivingEntity getOwner();

    AntiqueBagInventory.BookHandler getInventory();

    Tag writeTag();

    void readTag(Tag tag);
}
