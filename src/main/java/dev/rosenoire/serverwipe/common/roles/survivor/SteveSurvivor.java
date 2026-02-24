package dev.rosenoire.serverwipe.common.roles.survivor;

import dev.rosenoire.serverwipe.api.cooldowns.PlayerCooldowns;
import dev.rosenoire.serverwipe.cca.CooldownInfo;
import dev.rosenoire.serverwipe.cca.RoleHolderComponent;
import dev.rosenoire.serverwipe.common.index.ModAbilities;
import dev.rosenoire.serverwipe.foundation.ability.Ability;
import dev.rosenoire.serverwipe.foundation.role.SurvivorRole;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SteveSurvivor extends SurvivorRole {
    public SteveSurvivor(RoleHolderComponent roleHolder, Identifier identifier, Ability[] abilities) {
        super(roleHolder, identifier, abilities);
        registerAbilityHandler(ModAbilities.STEVE_SWORD, this::handleAttackAbility);
    }

    @Override
    public void writeData(WriteView writeView) {
    }

    @Override
    public void readData(ReadView readView) {
    }

    private void handleAttackAbility(Ability ability) {
        if (ability.isOnCooldown(player())) {
            return;
        }

        player().sendMessage(Text.literal("Attacked!"), false);
        ability.cooldown(player(), 20);
    }
}
