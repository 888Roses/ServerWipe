package dev.rosenoire.serverwipe.api.cooldowns;

import dev.rosenoire.serverwipe.cca.CooldownInfo;
import dev.rosenoire.serverwipe.cca.PlayerCooldownsComponent;
import dev.rosenoire.serverwipe.common.index.ModEntityComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;

@SuppressWarnings("unused")
public interface PlayerCooldowns {
    private static PlayerCooldownsComponent playerCooldown(PlayerEntity player) {
        return player.getComponent(ModEntityComponents.PLAYER_COOLDOWNS);
    }

    static Optional<CooldownInfo> get(PlayerEntity player, Identifier identifier) {
        return Optional.ofNullable(playerCooldown(player).cooldowns.getOrDefault(identifier, null));
    }

    static boolean has(PlayerEntity player, Identifier identifier) {
        return get(player, identifier).isPresent();
    }

    static void set(PlayerEntity player, Identifier identifier, CooldownInfo info) {
        PlayerCooldownsComponent component = playerCooldown(player);
        component.cooldowns.put(identifier, info);
        component.sync();
    }

    static void set(PlayerEntity player, Identifier identifier, long time) {
        set(player, identifier, new CooldownInfo(player.getEntityWorld().getTime(), time));
    }

    static void cooldown(PlayerEntity player, Identifier identifier, long cooldown) {
        long time = player.getEntityWorld().getTime();
        set(player, identifier, new CooldownInfo(time, time + cooldown));
    }
}