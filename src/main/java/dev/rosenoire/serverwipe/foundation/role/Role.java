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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unused")
public abstract class Role implements AbilityHolder {
    private final RoleHolderComponent roleHolder;
    private final Identifier identifier;

    private final Ability[] abilities;
    private final AbilityHandler[] abilityHandlers;
    private final BakedRoleModel bakedRoleModel;

    public Role(RoleHolderComponent roleHolder, Identifier identifier, Ability[] abilities, BakedRoleModel bakedRoleModel) {
        this.roleHolder = roleHolder;
        this.identifier = identifier;

        this.abilities = abilities;
        this.abilityHandlers = createAbilityHandlers();

        this.bakedRoleModel = bakedRoleModel;
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

    public BakedRoleModel bakedRoleModel() {return bakedRoleModel;}
    //endregion

    //region control
    public final void sync() {
        roleHolder().sync();
    }
    //endregion

    //region building
    public static <T extends Role> Builder<T> builder(Constructor<T> constructor) {
        return new Builder<>(constructor);
    }

    @FunctionalInterface
    public interface Constructor<T extends Role> {
        T construct(RoleHolderComponent component, Identifier identifier, Ability[] abilities, BakedRoleModel bakedRoleModel);
    }

    public static final class Builder<T extends Role> {
        private final Constructor<T> constructor;
        private final List<Ability> abilities = new ArrayList<>();
        private Identifier identifier;

        private Identifier baseModelName;
        private Identifier baseAnimationName;
        private Identifier baseTextureName;
       private MovementAnimations movementAnimations;

        private Builder(Constructor<T> constructor) {
            this.constructor = constructor;
        }

        public T construct(RoleHolderComponent component) {
            return constructor.construct(component, identifier, abilities.toArray(Ability[]::new), new BakedRoleModel(baseModelName, baseAnimationName, baseTextureName, movementAnimations));
        }

        public Builder<T> withIdentifier(Identifier identifier) {
            this.identifier = identifier;
            return this;
        }

        public Builder<T> withAbility(Ability ability) {
            abilities.add(ability);
            return this;
        }

        public Builder<T> withBaseModelName(Identifier name) {
            this.baseModelName = name;
            return this;
        }

        public Builder<T> withBaseAnimationName(Identifier name) {
            this.baseAnimationName = name;
            return this;
        }

        public Builder<T> withBaseTextureName(Identifier name) {
            this.baseTextureName = name;
            return this;
        }

        public Builder<T> withMovementAnimations(Function<MovementAnimationsBuilder, MovementAnimations> movementAnimations) {
            this.movementAnimations = movementAnimations.apply(MovementAnimationsBuilder.builder());
            return this;
        }

        public String translationKey() {
            return this.identifier.toTranslationKey("role");
        }
    }
    //endregion
}
