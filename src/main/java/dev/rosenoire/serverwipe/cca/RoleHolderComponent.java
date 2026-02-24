package dev.rosenoire.serverwipe.cca;

import dev.rosenoire.serverwipe.api.cca.components.PlayerComponent;
import dev.rosenoire.serverwipe.common.index.ModEntityComponents;
import dev.rosenoire.serverwipe.common.index.ModRoles;
import dev.rosenoire.serverwipe.foundation.role.Role;
import dev.rosenoire.serverwipe.foundation.role.Roles;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.Optional;

public class RoleHolderComponent extends PlayerComponent implements CommonTickingComponent {
    /// Do NOT set the role directly! Please use [#setRole(Role)] instead!
    private @Nullable Role role;

    //region constructor
    public RoleHolderComponent(PlayerEntity player) {
        super(player);
    }

    @Override
    public ComponentKey<? extends PlayerComponent> getComponentKey() {
        return ModEntityComponents.ROLE_HOLDER;
    }
    //endregion

    //region data
    @Override
    public void readData(ReadView readView) {
        readRoleData(readView);
    }

    @Override
    public void writeData(WriteView writeView) {
        WriteView roleView = writeView.get("role");
        role().ifPresent(role -> {
            WriteView writable = roleView.get(role.identifier().toString());
            role.writeData(writable);
        });
    }

    private void readRoleData(ReadView readView) {
        ReadView roleView = readView.getReadView("role");
        if (!roleView.keys().isEmpty()) {
            Optional<String> potentialName = roleView.keys().stream().findFirst();

            if (potentialName.isEmpty()) {
                return;
            }

            String roleName = potentialName.get();
            @Nullable Identifier roleIdentifier = Identifier.tryParse(roleName);

            if (roleIdentifier == null) {
                return;
            }

            Optional<Role.Builder<Role>> potentialRole = Roles.getRoleBuilder(roleIdentifier);

            if (potentialRole.isEmpty()) {
                return;
            }

            this.role = potentialRole.get().build(this, Roles.getIdentifier(potentialRole.get()));
            this.role.readData(roleView.getReadView(roleName));
        }
    }
    //endregion

    //region access

    /// The current role of this player. Empty when the player doesn't have a role.
    public Optional<Role> role() {
        return Optional.ofNullable(role);
    }
    //endregion

    //region control
    public <T extends Role> void setRole(@NotNull Role.Builder<T> role) {
        this.role = role.build(this, Roles.getIdentifier(role));
        sync();
    }

    public void removeRole() {
        this.role = null;
        sync();
    }
    //endregion

    //region ticking
    @Override
    public void tick() {
        role().ifPresent(role -> role.tick(player.getEntityWorld().isClient()));
    }
    //endregion
}
