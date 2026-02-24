package dev.rosenoire.serverwipe.common.index;

import dev.rosenoire.serverwipe.foundation.role.Role;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

import static dev.rosenoire.serverwipe.common.ServerWipe.geode;

public interface ModRegistryKeys {
    RegistryKey<Registry<Role.Builder<? extends Role>>> ROLES = of("role");

    static void register() {}

    private static <T> RegistryKey<Registry<T>> of(String name) {
        return RegistryKey.ofRegistry(geode.id(name));
    }
}
