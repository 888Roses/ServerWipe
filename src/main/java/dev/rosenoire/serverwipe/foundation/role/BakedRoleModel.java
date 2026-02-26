package dev.rosenoire.serverwipe.foundation.role;

import net.minecraft.util.Identifier;

public record BakedRoleModel(
        Identifier baseModelName,
        Identifier baseAnimationName,
        Identifier baseTextureName,
        MovementAnimations movementAnimations
) {
}