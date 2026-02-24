package dev.rosenoire.serverwipe.foundation.role;

import dev.rosenoire.serverwipe.cca.RoleHolderComponent;
import dev.rosenoire.serverwipe.foundation.ability.Ability;
import net.minecraft.util.Identifier;

public abstract class KillerRole extends Role {
    public KillerRole(RoleHolderComponent roleHolder, Identifier identifier, Ability[] abilities) {
        super(roleHolder, identifier, abilities);
    }
}
