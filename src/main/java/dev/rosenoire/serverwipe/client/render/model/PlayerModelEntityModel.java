package dev.rosenoire.serverwipe.client.render.model;

import dev.rosenoire.serverwipe.common.entity.PlayerModelEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

import static dev.rosenoire.serverwipe.client.ServerWipeClient.geode;

public class PlayerModelEntityModel extends DefaultedEntityGeoModel<PlayerModelEntity> {
    public PlayerModelEntityModel() {
        super(geode.id("dreadlord"));
    }
}
