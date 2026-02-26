package dev.rosenoire.serverwipe.common.index;

import dev.rosenoire.serverwipe.common.entity.PlayerModelEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

import static dev.rosenoire.serverwipe.common.ServerWipe.geode;

public interface ModEntityTypes {
    EntityType<PlayerModelEntity> PLAYER_MODEL = geode.registerEntity(
            "player_model",
            EntityType.Builder.create(PlayerModelEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .eyeHeight(0.25f)
                    .dropsNothing()
                    .maxTrackingRange(10)
                    .trackingTickInterval(1)
                    .makeFireImmune()
    );

    static void register() {
    }
}
