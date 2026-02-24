package dev.rosenoire.serverwipe.common.index;

import dev.rosenoire.serverwipe.common.roles.survivor.SteveSurvivor;
import dev.rosenoire.serverwipe.foundation.role.Role;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static dev.rosenoire.serverwipe.common.ServerWipe.geode;

public interface ModRoles {
    Role.Builder<SteveSurvivor> STEVE = register(
            "steve",
            Role.builder(SteveSurvivor::new)
                    .withAbility(ModAbilities.STEVE_SWORD)
    );

    static void register() {}

    static <T extends Role> Role.Builder<T> register(String name, Role.Builder<T> builder) {
        Identifier identifier = geode.id(name);
        return Registry.register(ModRegistries.ROLES, identifier, builder.withIdentifier(identifier));
    }
}
