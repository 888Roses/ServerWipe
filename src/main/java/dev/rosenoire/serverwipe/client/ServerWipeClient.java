package dev.rosenoire.serverwipe.client;

import dev.rosenoire.serverwipe.client.index.ModEntityRenderers;
import dev.rosenoire.serverwipe.client.index.ModHudElements;
import dev.rosenoire.serverwipe.common.ServerWipe;
import net.collectively.geode.GeodeClient;
import net.fabricmc.api.ClientModInitializer;

public class ServerWipeClient implements ClientModInitializer {
    public static final GeodeClient geode = GeodeClient.create(ServerWipe.MOD_ID);

    @Override
    public void onInitializeClient() {
        ModHudElements.register();
        ModEntityRenderers.register();

        geode.register();
    }
}
