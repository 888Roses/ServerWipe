package dev.rosenoire.serverwipe.common.index;

import dev.rosenoire.serverwipe.foundation.role.Role;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.SimpleRegistry;

public interface ModRegistries {
    /// [SimpleRegistry] containing every registered role.
    SimpleRegistry<Role.Builder<? extends Role>> ROLES = FabricRegistryBuilder
            .createSimple(ModRegistryKeys.ROLES)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();

    static void register() {
    }
}
