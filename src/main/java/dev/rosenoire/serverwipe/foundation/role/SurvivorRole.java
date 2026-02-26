package dev.rosenoire.serverwipe.foundation.role;

import dev.rosenoire.serverwipe.cca.RoleHolderComponent;
import dev.rosenoire.serverwipe.foundation.ability.Ability;
import net.minecraft.util.Identifier;

@SuppressWarnings("unused")
public abstract class SurvivorRole extends Role {
    public SurvivorRole(RoleHolderComponent roleHolder, Identifier identifier, Ability[] abilities, BakedRoleModel bakedRoleModel) {
        super(roleHolder, identifier, abilities, bakedRoleModel);
    }
}
