package dev.rosenoire.serverwipe.foundation.role;

import dev.rosenoire.serverwipe.cca.RoleHolderComponent;
import net.minecraft.util.Identifier;

public abstract class SurvivorRole extends Role {
    public SurvivorRole(RoleHolderComponent roleHolder, Identifier identifier) {
        super(roleHolder, identifier);
    }
}
