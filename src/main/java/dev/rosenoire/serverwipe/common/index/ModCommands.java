package dev.rosenoire.serverwipe.common.index;

import dev.rosenoire.serverwipe.common.command.RoleCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public interface ModCommands {
    static void register() {
        CommandRegistrationCallback.EVENT.register(RoleCommand::register);
    }
}
