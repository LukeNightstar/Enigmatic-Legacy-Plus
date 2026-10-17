package auviotre.enigmatic.legacy.client.renderer;

import auviotre.enigmatic.legacy.EnigmaticLegacy;
import auviotre.enigmatic.legacy.contents.item.etherium.EtheriumProperties;
import auviotre.enigmatic.legacy.contents.item.generic.BaseElytraItem;
import auviotre.enigmatic.legacy.registries.EnigmaticAttachments;
import com.google.common.reflect.TypeToken;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.renderstate.AvatarRenderStateModifier;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import top.theillusivec4.curios.api.CuriosApi;

/**
 * 26.1.2 port: render layers only receive render states, so entity data they need is copied
 * into the state's render data when it is extracted.
 */
@OnlyIn(Dist.CLIENT)
public final class EnigmaticRenderData {
    /** The elytra to draw with {@code EnigmaticElytraLayer}, empty if none or hidden by its curio slot. */
    public static final ContextKey<ItemStack> ELYTRA = new ContextKey<>(EnigmaticLegacy.location("elytra"));
    public static final ContextKey<Boolean> ETHERIUM_SHIELD = new ContextKey<>(EnigmaticLegacy.location("etherium_shield"));
    /** Second, smaller name line shown to Insignia wearers who can see true names. */
    public static final ContextKey<Component> TRUE_NAME = new ContextKey<>(EnigmaticLegacy.location("true_name"));

    private EnigmaticRenderData() {
    }

    public static void register(RegisterRenderStateModifiersEvent event) {
        event.registerEntityModifier(new TypeToken<LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?>>() {
        }, (entity, state) -> state.setRenderData(ELYTRA, visibleElytra(entity)));
        event.registerAvatarEntityModifier(new AvatarRenderStateModifier() {
            public <T extends Avatar & ClientAvatarEntity> void accept(T avatar, AvatarRenderState state) {
                // Replaces Caelus' RenderCapeEvent: no cape while any Enigmatic elytra is worn.
                if (!BaseElytraItem.getElytra(avatar).isEmpty()) state.showCape = false;
                state.setRenderData(ETHERIUM_SHIELD, avatar instanceof Player player && (EtheriumProperties.hasShield(player)
                        || player.getData(EnigmaticAttachments.ENIGMATIC_DATA).getEtherealShield() > 0));
            }
        });
    }

    private static ItemStack visibleElytra(LivingEntity entity) {
        boolean hidden = CuriosApi.getCuriosInventory(entity)
                .flatMap(handler -> handler.findFirstCurio(stack -> stack.getItem() instanceof BaseElytraItem))
                .map(curio -> !curio.slotContext().visible()).orElse(false);
        return hidden ? ItemStack.EMPTY : BaseElytraItem.getElytra(entity);
    }
}
