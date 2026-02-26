package dev.rosenoire.serverwipe.api.entity;

import net.collectively.geode.helpers.EntityHelper;
import net.collectively.geode.types.double3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.List;

public abstract class AbstractGenericEntity extends Entity {
    public AbstractGenericEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }

    //region access
    public double3 position(){return new double3(getEntityPos());}
    public double3 lastPosition(){return EntityHelper.getLastPosition(this);}
    public double3 eyePosition(){return EntityHelper.getEyePosition(this);}
    public List<Entity> entitiesAround(double radius){return EntityHelper.getEntitiesAround(this,radius);}
    public World world(){return getEntityWorld();}
    //endregion
}
