package auviotre.enigmatic.legacy.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * 26.1.2 port: NeoForge no longer strips {@code @OnlyIn} members, so common classes must not mention
 * client types in a way the bytecode verifier has to resolve (e.g. passing a LocalPlayer as a Player).
 * Common code reaches the client player/level through these methods; this class is only loaded when
 * one of them actually runs, which only happens on the client.
 */
public final class ClientAccess {
    private ClientAccess() {
    }

    public static @Nullable Player player() {
        return Minecraft.getInstance().player;
    }

    public static @Nullable Level level() {
        return Minecraft.getInstance().level;
    }
}
