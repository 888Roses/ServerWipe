package dev.rosenoire.serverwipe.foundation.role;

import dev.rosenoire.serverwipe.cca.RoleHolderComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.function.BiFunction;

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
    public String translationKey() {
        return this.identifier.toTranslationKey("role");
    }
    //endregion

    //region control
    public final void sync() {roleHolder().sync();}
    //endregion

    //region building
    public static <T extends Role> Builder<T> builder(BiFunction<RoleHolderComponent, Identifier, T> constructor) {
        return new Builder<>(constructor);
    }

    public static final class Builder<T extends Role> {
        private final BiFunction<RoleHolderComponent, Identifier, T> constructor;
        private Identifier identifier;

        private Builder(BiFunction<RoleHolderComponent, Identifier, T> constructor) {
            this.constructor = constructor;
        }

        public T construct(RoleHolderComponent component) {
            return constructor.apply(component, identifier);
        }

        public Builder<T> withIdentifier(Identifier identifier) {
            this.identifier = identifier;
            return this;
        }

        public String translationKey() {
            return this.identifier.toTranslationKey("role");
        }
    }
    //endregion
}
