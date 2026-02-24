package dev.rosenoire.serverwipe.foundation.role;

import dev.rosenoire.serverwipe.cca.RoleHolderComponent;
import dev.rosenoire.serverwipe.foundation.ability.Ability;
import dev.rosenoire.serverwipe.foundation.ability.AbilityHandler;
import dev.rosenoire.serverwipe.foundation.ability.AbilityHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.apache.commons.lang3.function.TriFunction;

import java.util.ArrayList;
import java.util.List;

public abstract class Role implements AbilityHolder {
    private final RoleHolderComponent roleHolder;
    private final Identifier identifier;

    private final Ability[] abilities;
    private final AbilityHandler[] abilityHandlers;

    public Role(RoleHolderComponent roleHolder, Identifier identifier, Ability[] abilities) {
        this.roleHolder = roleHolder;
        this.identifier = identifier;

        this.abilities = abilities;
        this.abilityHandlers = createAbilityHandlers();
    }

    //region behavior
    public void tick(boolean isClient) {
    }
    //endregion

    //region data
    public abstract void writeData(WriteView writeView);

    public abstract void readData(ReadView readView);
    //endregion

    //region access
    public final Identifier identifier() {
        return this.identifier;
    }

    public final RoleHolderComponent roleHolder() {
        return this.roleHolder;
    }

    public final PlayerEntity player() {
        return roleHolder().player;
    }

    public final World world() {
        return player().getEntityWorld();
    }

    public String translationKey() {
        return this.identifier.toTranslationKey("role");
    }

    @Override
    public Ability[] abilities() {
        return abilities;
    }

    @Override
    public AbilityHandler[] abilityHandlers() {
        return abilityHandlers;
    }
    //endregion

    //region control
    public final void sync() {
        roleHolder().sync();
    }
    //endregion

    //region building
    public static <T extends Role> Builder<T> builder(TriFunction<RoleHolderComponent, Identifier, Ability[], T> constructor) {
        return new Builder<>(constructor);
    }

    public static final class Builder<T extends Role> {
        private final TriFunction<RoleHolderComponent, Identifier, Ability[], T> constructor;
        private final List<Ability> abilities = new ArrayList<>();
        private Identifier identifier;

        private Builder(TriFunction<RoleHolderComponent, Identifier, Ability[], T> constructor) {
            this.constructor = constructor;
        }

        public T construct(RoleHolderComponent component) {
            return constructor.apply(component, identifier, abilities.toArray(Ability[]::new));
        }

        public Builder<T> withIdentifier(Identifier identifier) {
            this.identifier = identifier;
            return this;
        }

        public Builder<T> withAbility(Ability ability) {
            abilities.add(ability);
            return this;
        }

        public String translationKey() {
            return this.identifier.toTranslationKey("role");
        }
    }
    //endregion
}
