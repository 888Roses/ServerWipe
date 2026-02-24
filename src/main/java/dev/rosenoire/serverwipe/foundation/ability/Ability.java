package dev.rosenoire.serverwipe.foundation.ability;

import dev.rosenoire.serverwipe.common.index.ModRegistries;
import net.minecraft.util.Identifier;

public record Ability() {
    public Identifier identifier() {
        return ModRegistries.ABILITIES.getId(this);
    }

    public String translationKey() {
        return identifier().toTranslationKey("ability");
    }
}
