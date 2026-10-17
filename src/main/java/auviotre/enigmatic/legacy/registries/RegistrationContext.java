package auviotre.enigmatic.legacy.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

/**
 * 26.1.2 port: item and block properties must carry their registry id before construction.
 * Items and blocks of this mod build their own properties in no-arg constructors, so the
 * registration helpers record the id being constructed here and the properties factories read it.
 */
public final class RegistrationContext {
    private static final ThreadLocal<ResourceKey<Item>> CURRENT_ITEM = new ThreadLocal<>();
    private static final ThreadLocal<ResourceKey<Block>> CURRENT_BLOCK = new ThreadLocal<>();

    private RegistrationContext() {
    }

    public static <T extends Item> T constructItem(Identifier id, Supplier<T> supplier) {
        ResourceKey<Item> previous = CURRENT_ITEM.get();
        CURRENT_ITEM.set(ResourceKey.create(Registries.ITEM, id));
        try {
            return supplier.get();
        } finally {
            CURRENT_ITEM.set(previous);
        }
    }

    public static <T extends Block> T constructBlock(Identifier id, Supplier<T> supplier) {
        ResourceKey<Block> previous = CURRENT_BLOCK.get();
        CURRENT_BLOCK.set(ResourceKey.create(Registries.BLOCK, id));
        try {
            return supplier.get();
        } finally {
            CURRENT_BLOCK.set(previous);
        }
    }

    public static Item.Properties itemProperties() {
        Item.Properties properties = new Item.Properties();
        ResourceKey<Item> key = CURRENT_ITEM.get();
        if (key == null) throw new IllegalStateException("Item properties created outside of item registration");
        return properties.setId(key);
    }

    public static ResourceKey<Block> blockKey() {
        ResourceKey<Block> key = CURRENT_BLOCK.get();
        if (key == null) throw new IllegalStateException("Block properties created outside of block registration");
        return key;
    }
}
