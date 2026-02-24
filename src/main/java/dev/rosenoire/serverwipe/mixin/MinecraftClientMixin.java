package dev.rosenoire.serverwipe.mixin;

import dev.rosenoire.serverwipe.cca.RoleHolderComponent;
import dev.rosenoire.serverwipe.common.index.ModEntityComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    @Final
    public GameOptions options;

    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Inject(at = @At("HEAD"), method = "handleInputEvents")
    private void serverWipe$handleInputEvents(CallbackInfo ci) {
        if (player != null && (player.isCreative() || player.isSpectator())) {
            return;
        }

        for (int index = 0; index < 9; index++) {
            if (options.hotbarKeys[index].wasPressed()) {
                RoleHolderComponent component = player.getComponent(ModEntityComponents.ROLE_HOLDER);

                int effectiveIndex = index;
                component.role().map(role -> role.handleAbility(effectiveIndex));
            }
        }
    }
}
