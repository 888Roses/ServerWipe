package dev.rosenoire.serverwipe.foundation.role;

import dev.rosenoire.serverwipe.cca.RoleHolderComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public abstract class Role {
    private final RoleHolderComponent roleHolder;
    private final Identifier identifier;

    public Role(RoleHolderComponent roleHolder, Identifier identifier) {
        this.roleHolder = roleHolder;
        this.identifier = identifier;
    }

    //region behavior
    public void tick(boolean isClient) {}
    //endregion

    //region data
    public abstract void writeData(WriteView writeView);
    public abstract void readData(ReadView readView);
    //endregion

    //region access
    public final Identifier identifier() {return this.identifier;}
    public final RoleHolderComponent roleHolder() {return this.roleHolder;}
    public final PlayerEntity player() {return roleHolder().player;}
    public final World world() {return player().getEntityWorld();}
    //endregion

    //region control
    public final void sync() {roleHolder().sync();}
    //endregion

    //region building
    @FunctionalInterface
    public interface Builder<T extends Role> {
        T build(RoleHolderComponent component, Identifier identifier);
    }
    //endregion
}
