package dev.rosenoire.serverwipe.foundation.role;

import dev.rosenoire.serverwipe.common.index.ModRegistries;
import net.minecraft.util.Identifier;

import java.util.Optional;

public interface Roles {
    static <T extends Role> Optional<Role.Builder<T>> getRoleBuilder(Identifier identifier) {
        //noinspection unchecked
        return Optional.ofNullable((Role.Builder<T>) ModRegistries.ROLES.get(identifier));
    }
    static <T extends Role> Optional<Role.Builder<T>> getRoleBuilder(Role role) {
        return getRoleBuilder(role.identifier());
    }
}
