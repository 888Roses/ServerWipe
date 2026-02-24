package dev.rosenoire.serverwipe.common.index;

import dev.rosenoire.serverwipe.foundation.ability.Ability;
import net.minecraft.registry.Registry;

import static dev.rosenoire.serverwipe.common.ServerWipe.geode;

public interface ModAbilities {
    Ability STEVE_SWORD = register("steve_sword", new Ability());

    static void register() {}

    static Ability register(String name, Ability ability) {
        return Registry.register(ModRegistries.ABILITIES, geode.id(name), ability);
    }
}
