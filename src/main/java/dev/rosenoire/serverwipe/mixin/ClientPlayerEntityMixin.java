package dev.rosenoire.serverwipe.mixin;

import dev.rosenoire.serverwipe.cca.RoleHolderComponent;
import dev.rosenoire.serverwipe.common.index.ModEntityComponents;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Inject(at = @At("TAIL"), method = "tickMovementInput")
    private void serverWipe$tickMovementInput(CallbackInfo ci) {
        ClientPlayerEntity clientPlayer = (ClientPlayerEntity) (Object) this;

        if (clientPlayer.isCreative() || clientPlayer.isSpectator()) {
            return;
        }

        RoleHolderComponent component = clientPlayer.getComponent(ModEntityComponents.ROLE_HOLDER);
        component.role().ifPresent(role -> {
            if (!role.canJump()) {
                clientPlayer.setJumping(false);
            }
        });
    }
}
