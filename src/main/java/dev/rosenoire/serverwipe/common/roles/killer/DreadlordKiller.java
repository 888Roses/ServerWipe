package dev.rosenoire.serverwipe.common.roles.killer;

import dev.rosenoire.serverwipe.cca.RoleHolderComponent;
import dev.rosenoire.serverwipe.foundation.ability.Ability;
import dev.rosenoire.serverwipe.foundation.role.BakedRoleModel;
import dev.rosenoire.serverwipe.foundation.role.KillerRole;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;

public class DreadlordKiller extends KillerRole {
    //region constructor
    public DreadlordKiller(RoleHolderComponent roleHolder, Identifier identifier, Ability[] abilities, BakedRoleModel bakedRoleModel) {
        super(roleHolder, identifier, abilities, bakedRoleModel);
    }
    //endregion

    //region data
    @Override
    public void writeData(WriteView writeView) {

    }

    @Override
    public void readData(ReadView readView) {

    }
    //endregion
}