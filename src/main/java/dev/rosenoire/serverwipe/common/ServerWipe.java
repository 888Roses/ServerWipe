package dev.rosenoire.serverwipe.common;

import dev.rosenoire.serverwipe.common.index.*;
import net.collectively.geode.Geode;
import net.fabricmc.api.ModInitializer;

public class ServerWipe implements ModInitializer {
    public static final String MOD_ID = "server_wipe";
    public static final Geode geode = Geode.create(MOD_ID);

    @Override
    public void onInitialize() {
        ModRegistryKeys.register();
        ModRegistries.register();

        ModTags.register();
        ModRoles.register();

        ModCommands.register();

        geode.register();
    }
}
