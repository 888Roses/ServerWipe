package dev.rosenoire.serverwipe.api.cca.components;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public abstract class PlayerComponent implements Component, AutoSyncedComponent {
    public final PlayerEntity player;

    //region construction
    public PlayerComponent(PlayerEntity player) {
        this.player = player;
    }
    public abstract ComponentKey<? extends PlayerComponent> getComponentKey();
    //endregion

    //region data
    public final void sync() {
        getComponentKey().sync(player);
    }
    //endregion

    //region access
    public final World world(){return player.getEntityWorld();}
    //endregion
}
