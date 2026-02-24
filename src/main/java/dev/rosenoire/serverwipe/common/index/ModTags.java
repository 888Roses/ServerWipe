package dev.rosenoire.serverwipe.common.index;

import dev.rosenoire.serverwipe.foundation.role.Role;
import net.minecraft.registry.tag.TagKey;

import static dev.rosenoire.serverwipe.common.ServerWipe.geode;

public interface ModTags {
    TagKey<Role.Builder<? extends Role>> SURVIVORS = roleTag("survivors");
    TagKey<Role.Builder<? extends Role>> KILLERS = roleTag("killers");

    static void register() {}

    static TagKey<Role.Builder<? extends Role>> roleTag(String name) {
        return geode.registerTag(ModRegistryKeys.ROLES, name);
    }
}
