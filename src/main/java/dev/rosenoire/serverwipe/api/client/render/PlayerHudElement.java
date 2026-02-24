package dev.rosenoire.serverwipe.api.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerHudElement implements HudElement {
    @Override
    public void render(@NotNull DrawContext context, @NotNull RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        TextRenderer textRenderer = client.textRenderer;

        if (player == null || textRenderer == null) {
            return;
        }

        render(client, player, textRenderer, context, tickCounter);
    }

    public abstract void render(MinecraftClient client, ClientPlayerEntity player, TextRenderer textRenderer, DrawContext context, RenderTickCounter tickCounter);
}
