package dev.rosenoire.serverwipe.foundation.ability;

import dev.rosenoire.serverwipe.api.cooldowns.PlayerCooldowns;
import dev.rosenoire.serverwipe.common.index.ModRegistries;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import static dev.rosenoire.serverwipe.common.ServerWipe.geode;

public record Ability() {
    public Identifier identifier() {
        return ModRegistries.ABILITIES.getId(this);
    }

    public String translationKey() {
        return identifier().toTranslationKey("ability");
    }

    public Identifier iconTexture() {
        return geode.id("ability/" + identifier().getPath());
    }

    public void cooldown(PlayerEntity player, long cooldown) {
        PlayerCooldowns.cooldown(player, identifier(), cooldown);
    }

    public boolean isOnCooldown(PlayerEntity player) {
        return PlayerCooldowns.has(player, identifier());
    }
}
