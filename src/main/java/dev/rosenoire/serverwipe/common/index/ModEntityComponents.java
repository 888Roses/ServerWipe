package dev.rosenoire.serverwipe.common.index;

import dev.rosenoire.serverwipe.api.cca.components.PlayerComponent;
import dev.rosenoire.serverwipe.cca.PlayerCooldownsComponent;
import dev.rosenoire.serverwipe.cca.PlayerModelComponent;
import dev.rosenoire.serverwipe.cca.RoleHolderComponent;
import net.minecraft.entity.player.PlayerEntity;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

import java.util.function.Function;

import static dev.rosenoire.serverwipe.common.ServerWipe.geode;

@SuppressWarnings("SameParameterValue")
public class ModEntityComponents implements EntityComponentInitializer {
    public static final ComponentKey<RoleHolderComponent> ROLE_HOLDER = register("role_holder", RoleHolderComponent.class);
    public static final ComponentKey<PlayerCooldownsComponent> PLAYER_COOLDOWNS = register("player_cooldowns", PlayerCooldownsComponent.class);
    public static final ComponentKey<PlayerModelComponent> PLAYER_MODEL = register("player_model", PlayerModelComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registerPlayerComponent(registry, ROLE_HOLDER, RoleHolderComponent::new);
        registerPlayerComponent(registry, PLAYER_COOLDOWNS, PlayerCooldownsComponent::new);
        registerPlayerComponent(registry, PLAYER_MODEL, PlayerModelComponent::new);
    }

    private static <T extends Component> ComponentKey<T> register(String name, Class<T> clazz) {
        return ComponentRegistry.getOrCreate(geode.id(name), clazz);
    }

    private static <T extends PlayerComponent> void registerPlayerComponent(EntityComponentFactoryRegistry registry, ComponentKey<T> key, Function<PlayerEntity, T> constructor) {
        registry.registerFor(PlayerEntity.class, key, constructor::apply);
    }
}
