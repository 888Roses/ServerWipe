package dev.rosenoire.serverwipe.common.index;

import dev.rosenoire.serverwipe.foundation.ability.Ability;
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

    /// [SimpleRegistry] containing every registered ability.
    SimpleRegistry<Ability> ABILITIES = FabricRegistryBuilder
            .createSimple(ModRegistryKeys.ABILITIES)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();

    static void register() {
    }
}
