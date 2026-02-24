package dev.rosenoire.serverwipe.foundation.ability;

import dev.rosenoire.serverwipe.common.ServerWipe;
import net.minecraft.util.Identifier;

import java.util.Optional;

import static dev.rosenoire.serverwipe.common.ServerWipe.geode;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface AbilityHolder {
    Ability[] abilities();

    AbilityHandler[] abilityHandlers();

    //region control
    default AbilityHandler[] createAbilityHandlers() {
        return new AbilityHandler[abilities().length];
    }

    default boolean registerAbilityHandler(int index, AbilityHandler handler) {
        if (index < 0 || index >= abilities().length) {
            return false;
        }

        abilityHandlers()[index] = handler;
        return true;
    }

    default boolean registerAbilityHandler(Identifier identifier, AbilityHandler handler) {
        return getAbilityIndex(identifier).map(index -> registerAbilityHandler(index, handler)).orElse(false);
    }

    default boolean registerAbilityHandler(String name, AbilityHandler handler) {
        return registerAbilityHandler(geode.id(name), handler);
    }

    default boolean registerAbilityHandler(Ability ability, AbilityHandler handler) {
        return registerAbilityHandler(ability.identifier(), handler);
    }
    //endregion

    //region handle
    default boolean handleAbility(int index) {
        geode.logger.info("Index: {}", index);

        if (index < 0 || index >= abilities().length) {
            return false;
        }

        return getAbility(index)
                .map(ability -> {
                    Optional.ofNullable(abilityHandlers()[index]).ifPresent(x -> x.handle(ability));
                    return true;
                })
                .orElse(false);
    }

    default boolean handleAbility(Identifier identifier) {
        return getAbilityIndex(identifier).map(this::handleAbility).orElse(false);
    }

    default boolean handleAbility(String identifier) {
        return handleAbility(geode.id(identifier));
    }

    default boolean handleAbility(Ability ability) {
        return handleAbility(ability.identifier());
    }
    //endregion

    //region access
    default Optional<Ability> getAbility(int index) {
        return Optional.ofNullable(index < 0 || index >= abilities().length ? null : abilities()[index]);
    }

    default Optional<Ability> getAbility(Identifier identifier) {
        return getAbilityIndex(identifier).flatMap(this::getAbility);
    }

    default Optional<Ability> getAbility(String name) {
        return getAbility(geode.id(name));
    }

    default boolean hasAbility(Identifier identifier) {
        return getAbilityIndex(identifier).isPresent();
    }

    default Optional<Integer> getAbilityIndex(Identifier identifier) {
        Ability[] abilities = abilities();
        for (int i = 0; i < abilities.length; i++) {
            Ability ability = abilities[i];
            if (ability.identifier().equals(identifier)) {
                return Optional.of(i);
            }
        }

        return Optional.empty();
    }
    //endregion
}
