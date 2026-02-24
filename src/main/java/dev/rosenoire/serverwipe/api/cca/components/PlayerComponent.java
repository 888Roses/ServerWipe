package dev.rosenoire.serverwipe.api.cca.components;

import net.minecraft.entity.player.PlayerEntity;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public abstract class PlayerComponent implements Component, AutoSyncedComponent {
    public final PlayerEntity player;

    public PlayerComponent(PlayerEntity player) {
        this.player = player;
    }

    public abstract ComponentKey<? extends PlayerComponent> getComponentKey();

    public final void sync() {
        getComponentKey().sync(player);
    }
}
