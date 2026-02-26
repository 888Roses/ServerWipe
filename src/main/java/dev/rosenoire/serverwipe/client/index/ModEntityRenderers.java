package dev.rosenoire.serverwipe.client.index;

import dev.rosenoire.serverwipe.client.render.entity.PlayerModelEntityRenderer;
import dev.rosenoire.serverwipe.common.index.ModEntityTypes;
import net.minecraft.client.render.entity.EntityRendererFactories;
import net.minecraft.entity.EntityType;

public interface ModEntityRenderers {
    static void register() {
        EntityRendererFactories.register(ModEntityTypes.PLAYER_MODEL, PlayerModelEntityRenderer::new);
    }
}
