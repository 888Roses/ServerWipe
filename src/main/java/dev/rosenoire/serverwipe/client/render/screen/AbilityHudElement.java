package dev.rosenoire.serverwipe.client.render.screen;

import dev.rosenoire.serverwipe.api.client.render.PlayerHudElement;
import dev.rosenoire.serverwipe.api.cooldowns.PlayerCooldowns;
import dev.rosenoire.serverwipe.cca.CooldownInfo;
import dev.rosenoire.serverwipe.cca.RoleHolderComponent;
import dev.rosenoire.serverwipe.common.index.ModEntityComponents;
import dev.rosenoire.serverwipe.foundation.ability.Ability;
import dev.rosenoire.serverwipe.foundation.role.Role;
import net.collectively.geode.math.math;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;

import java.util.Optional;

public class AbilityHudElement extends PlayerHudElement {
    public static final int SIZE = 24;

    @Override
    public void render(MinecraftClient client, ClientPlayerEntity player, TextRenderer textRenderer, DrawContext context, RenderTickCounter tickCounter) {
        RoleHolderComponent component = player.getComponent(ModEntityComponents.ROLE_HOLDER);
        Optional<Role> potentialRole = component.role();

        if (potentialRole.isEmpty()) {
            return;
        }

        Role role = potentialRole.get();
        Ability[] abilities = role.abilities();

        if (abilities == null || abilities.length == 0) {
            return;
        }

        int x = context.getScaledWindowWidth() - SIZE - 12;
        int y = context.getScaledWindowHeight() - SIZE - 12;

        for (int i = 0; i < abilities.length; i++) {
            Ability ability = abilities[i];

            if (ability == null) {
                continue;
            }

            drawAbility(client, player, textRenderer, context, ability, i, x, y);
            x -= SIZE + 2;
        }
    }

    private void drawAbility(MinecraftClient client, ClientPlayerEntity player, TextRenderer textRenderer, DrawContext context, Ability ability, int i, int x, int y) {
        long currentTime = player.getEntityWorld().getTime();
        double cooldownProgress = PlayerCooldowns.get(player, ability.identifier())
                .map(info -> info.getProgress(currentTime))
                .orElse(1d);
        int height = math.clamp(math.round((float) cooldownProgress * SIZE), 0, SIZE);

        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ability.iconTexture(), x, y, SIZE, SIZE, 0xff222222);
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ability.iconTexture(), SIZE, SIZE, 0, 0, x, y, SIZE, height, 0xffffffff);

        Text boundKey = Text.literal(getHotbarKeyTranslationKey(client, i));
        int width = textRenderer.getWidth(boundKey);

        for (int sx = -1; sx <= 1; sx++) {
            for (int sy = -1; sy <= 1; sy++) {
                context.drawText(textRenderer, boundKey, x + SIZE / 2 - width / 2 + sx, y - 12 + sy, 0xff000000, false);
            }
        }

        context.drawText(textRenderer, boundKey, x + SIZE / 2 - width / 2, y - 12, 0xffffffff, false);
    }

    private String getHotbarKeyTranslationKey(MinecraftClient client, int index) {
        return client.options.hotbarKeys[index].getBoundKeyLocalizedText().getString();
    }
}
