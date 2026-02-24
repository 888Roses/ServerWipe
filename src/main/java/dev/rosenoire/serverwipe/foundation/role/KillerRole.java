package dev.rosenoire.serverwipe.foundation.role;

import dev.rosenoire.serverwipe.cca.RoleHolderComponent;
import net.minecraft.util.Identifier;

public abstract class KillerRole extends Role {
    public KillerRole(RoleHolderComponent roleHolder, Identifier identifier) {
        super(roleHolder, identifier);
    }
}
