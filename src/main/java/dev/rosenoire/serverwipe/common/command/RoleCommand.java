package dev.rosenoire.serverwipe.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.rosenoire.serverwipe.cca.RoleHolderComponent;
import dev.rosenoire.serverwipe.common.index.ModEntityComponents;
import dev.rosenoire.serverwipe.common.index.ModRegistries;
import dev.rosenoire.serverwipe.foundation.role.Role;
import dev.rosenoire.serverwipe.foundation.role.Roles;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.visitor.NbtTextFormatter;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.storage.NbtWriteView;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ErrorReporter;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import static dev.rosenoire.serverwipe.common.ServerWipe.geode;

public class RoleCommand {
    private static final String NAME = "name";
    private static final String PLAYER = "player";

    private static final SuggestionProvider<CommandSource> SUGGESTION_PROVIDER = SuggestionProviders.register(
            geode.id("roles"),
            (context, builder) -> CommandSource.suggestFromIdentifier(
                    ModRegistries.ROLES.getKeys().stream(),
                    builder,
                    RegistryKey::getValue,
                    identifier -> Text.literal(identifier.toString())
            )
    );

    @SuppressWarnings("unused")
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager
                .literal("server_wipe").then(CommandManager
                        .literal("role").then(CommandManager
                                .literal("set").then(CommandManager
                                        .argument(PLAYER, EntityArgumentType.player())
                                        .then(CommandManager
                                                .argument(NAME, IdentifierArgumentType.identifier())
                                                .suggests(SuggestionProviders.cast(SUGGESTION_PROVIDER))
                                                .executes(RoleCommand::set)
                                        )
                                )
                        ).then(CommandManager
                                .literal("remove").then(CommandManager
                                        .argument(PLAYER, EntityArgumentType.player())
                                        .executes(RoleCommand::remove)
                                )
                        ).then(CommandManager
                                .literal("get").then(CommandManager
                                        .argument(PLAYER, EntityArgumentType.player())
                                        .executes(RoleCommand::get)
                                )
                        )
                )
        );
    }

    private static int get(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        PlayerEntity player = EntityArgumentType.getPlayer(context, PLAYER);
        RoleHolderComponent component = player.getComponent(ModEntityComponents.ROLE_HOLDER);

        if (component.role().isEmpty()) {
            context.getSource().sendMessage(Text.literal("Role for " + player.getStringifiedName() + ": null"));
            return 0;
        }

        Role role = component.role().get();

        MutableText messageText = Text.literal(player.getStringifiedName());
        messageText.append(" has role \"");
        messageText.append(Text.literal(role.identifier().toString()).formatted(Formatting.AQUA));
        messageText.append("\": ");

        NbtWriteView writeView = NbtWriteView.create(ErrorReporter.EMPTY);
        role.writeData(writeView);
        messageText.append(NbtHelper.toPrettyPrintedText(writeView.getNbt()));

        context.getSource().sendMessage(messageText);
        return 1;
    }

    private static int remove(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        PlayerEntity player = EntityArgumentType.getPlayer(context, PLAYER);
        RoleHolderComponent component = player.getComponent(ModEntityComponents.ROLE_HOLDER);

        if (component.role().isEmpty()) {
            context.getSource().sendError(Text.literal("Cannot remove empty role for " + player.getStringifiedName() + "!"));
            return 0;
        }

        component.removeRole();
        context.getSource().sendMessage(Text.literal("Removed role for " + player.getStringifiedName()));
        return 1;
    }

    private static int set(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        PlayerEntity player = EntityArgumentType.getPlayer(context, PLAYER);
        Identifier roleIdentifier = IdentifierArgumentType.getIdentifier(context, NAME);
        RoleHolderComponent component = player.getComponent(ModEntityComponents.ROLE_HOLDER);

        return Roles.getRoleBuilder(roleIdentifier).map(role -> {
            component.setRole(role);

            MutableText message = Text.literal("Set role of " + player.getStringifiedName() + " to ");
            message.append(Text.literal(roleIdentifier.toString()).formatted(Formatting.GREEN));
            context.getSource().sendMessage(message);
            return 1;
        }).orElseGet(() -> {
            MutableText message = Text.literal("Couldn't find role with identifier ");
            message.append(Text.literal(roleIdentifier.toString()).formatted(Formatting.UNDERLINE));
            context.getSource().sendError(message);
            return 0;
        });
    }
}
