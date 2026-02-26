package dev.rosenoire.serverwipe.cca;

import dev.rosenoire.serverwipe.api.cca.components.PlayerComponent;
import dev.rosenoire.serverwipe.common.ServerWipe;
import dev.rosenoire.serverwipe.common.entity.PlayerModelEntity;
import dev.rosenoire.serverwipe.common.index.ModEntityComponents;
import dev.rosenoire.serverwipe.common.index.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Uuids;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class PlayerModelComponent extends PlayerComponent implements CommonTickingComponent {
    private PlayerModelEntity modelEntity;

    //region construction
    public PlayerModelComponent(PlayerEntity player) {
        super(player);
    }

    @Override
    public ComponentKey<? extends PlayerComponent> getComponentKey() {
        return ModEntityComponents.PLAYER_MODEL;
    }
    //endregion

    //region data
    @Override
    public void readData(ReadView readView) {
        readView.read("modelEntity", Uuids.INT_STREAM_CODEC).ifPresent(uuid -> {
            Entity entity = world().getEntity(uuid);

            if (entity instanceof PlayerModelEntity playerModelEntity) {
                playerModelEntity.setPlayer(player);
                modelEntity = playerModelEntity;
            }
        });
    }

    @Override
    public void writeData(WriteView writeView) {
        if (modelEntity != null) {
            writeView.put("modelEntity", Uuids.INT_STREAM_CODEC, modelEntity.getUuid());
        }
    }
    //endregion

    @Override
    public void tick() {
        if (modelEntity == null) {
            createModelEntity();
            return;
        }

        modelEntity.setPlayer(player);
        sync();
    }

    private void createModelEntity() {
        if (modelEntity != null) {
            return;
        }

        if (world() instanceof ServerWorld serverWorld) {
            modelEntity = new PlayerModelEntity(ModEntityTypes.PLAYER_MODEL, serverWorld);
            modelEntity.setPlayer(player);
            modelEntity.setPosition(player.getEntityPos());
            serverWorld.spawnEntity(modelEntity);
            sync();
        }
    }
}
