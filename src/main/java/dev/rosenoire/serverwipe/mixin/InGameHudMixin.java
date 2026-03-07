package dev.rosenoire.serverwipe.mixin;

import dev.rosenoire.serverwipe.cca.RoleHolderComponent;
import dev.rosenoire.serverwipe.common.index.ModEntityComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Unique
    private static Optional<ClientPlayerEntity> getPlayer() {
        return Optional.ofNullable(MinecraftClient.getInstance().player);
    }

    @Inject(at = @At("HEAD"), method = "renderMainHud", cancellable = true)
    private void serverWipe$renderMainHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        getPlayer().ifPresent(player -> {
            if (player.isCreative() || player.isSpectator()) {
                return;
            }

            if (player.getComponent(ModEntityComponents.ROLE_HOLDER).role().isPresent()) {
                ci.cancel();
            }
        });
    }
}
