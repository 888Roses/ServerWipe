package dev.rosenoire.serverwipe.common.index;

import dev.rosenoire.serverwipe.common.roles.survivor.SteveSurvivor;
import dev.rosenoire.serverwipe.foundation.role.Role;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static dev.rosenoire.serverwipe.common.ServerWipe.geode;

public interface ModRoles {
    Role.Builder<SteveSurvivor> STEVE = register("steve", SteveSurvivor::new);

    static void register() {}

    static <T extends Role> Role.Builder<T> register(String name, Role.Builder<T> role) {
        Identifier identifier = geode.id(name);
        return Registry.register(ModRegistries.ROLES, identifier, role);
    }
}
