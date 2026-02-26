package dev.rosenoire.serverwipe.common.index;

import dev.rosenoire.serverwipe.common.roles.killer.DreadlordKiller;
import dev.rosenoire.serverwipe.foundation.role.MovementAnimationsBuilder;
import dev.rosenoire.serverwipe.foundation.role.Role;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static dev.rosenoire.serverwipe.common.ServerWipe.geode;

public interface ModRoles {
    Role.Builder<DreadlordKiller> DREADLORD = register("dreadlord", Role.builder(DreadlordKiller::new)
            .withBaseModelName(geode.id("dreadlord"))
            .withBaseTextureName(geode.id("dreadlord"))
            .withBaseAnimationName(geode.id("dreadlord"))
            .withMovementAnimations(MovementAnimationsBuilder::build)
    );

    static void register() {
    }

    static <T extends Role> Role.Builder<T> register(String name, Role.Builder<T> builder) {
        Identifier identifier = geode.id(name);
        return Registry.register(ModRegistries.ROLES, identifier, builder.withIdentifier(identifier));
    }
}
