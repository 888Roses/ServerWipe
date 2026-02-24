package dev.rosenoire.serverwipe.client.index;

import dev.rosenoire.serverwipe.client.render.screen.AbilityHudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;

import static dev.rosenoire.serverwipe.client.ServerWipeClient.geode;

public interface ModHudElements {
    static void register() {
        register("abilities", new AbilityHudElement());
    }

    private static void register(String name, HudElement hudElement) {
        HudElementRegistry.addFirst(geode.id(name), hudElement);
    }
}
