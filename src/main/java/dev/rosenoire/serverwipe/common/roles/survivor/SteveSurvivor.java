package dev.rosenoire.serverwipe.common.roles.survivor;

import dev.rosenoire.serverwipe.cca.RoleHolderComponent;
import dev.rosenoire.serverwipe.foundation.role.SurvivorRole;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;

public class SteveSurvivor extends SurvivorRole {
    private String message = "Hello, World!";

    public SteveSurvivor(RoleHolderComponent roleHolder, Identifier identifier) {
        super(roleHolder, identifier);
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.putString("message", message);
    }

    @Override
    public void readData(ReadView readView) {
        message = readView.getString("message", "Failed to load message!");
    }

    @Override
    public void tick(boolean isClient) {
        if (player().isSneaking()) {
            sync();
        }
    }
}
