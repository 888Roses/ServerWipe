package dev.rosenoire.serverwipe.api.timer;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.rosenoire.serverwipe.common.ServerWipe;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.NbtWriteView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ErrorReporter;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.timer.Timer;
import net.minecraft.world.timer.TimerCallback;
import net.minecraft.world.timer.TimerCallbackSerializer;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface TimedAction extends TimerCallback<MinecraftServer> {
    static <T extends TimedAction> TimerCallbackSerializer<MinecraftServer> register(Identifier identifier, MapCodec<T> codec) {
        return TimerCallbackSerializer.INSTANCE.registerSerializer(identifier, codec);
    }

    private static Optional<Timer<MinecraftServer>> getTimer(World world) {
        if (world instanceof ServerWorld serverWorld) {
            MinecraftServer server = serverWorld.getServer();

            if (server == null) {
                return Optional.empty();
            }

            ServerWorldProperties properties = server.getSaveProperties().getMainWorldProperties();
            return Optional.of(properties.getScheduledEvents());
        }

        return Optional.empty();
    }

    static void cancel(World world, String uniqueName) {
        getTimer(world).ifPresent(timer -> timer.remove(uniqueName));
    }

    static void add(World world, long delay, String uniqueName, TimedAction action) {
        addAtTime(world, world.getTime() + delay, uniqueName, action);
    }

    static void addAtTime(World world, long time, String uniqueName, TimedAction action) {
        getTimer(world).ifPresent(timer -> timer.setEventIfAbsent(uniqueName, time, action));
    }

    static <T extends TimedAction> MapCodec<T> createCodec(
            BiConsumer<T, WriteView> write,
            Function<NbtCompound, T> read
    ) {
        return RecordCodecBuilder.mapCodec(instance -> instance
                .group(Codecs.NBT_ELEMENT.fieldOf("nbt").forGetter((T callback) -> {
                    NbtWriteView nbtWriteView = NbtWriteView.create(ErrorReporter.EMPTY);
                    write.accept(callback, nbtWriteView);
                    return nbtWriteView.getNbt();
                }))
                .apply(instance, (NbtElement nbtElement) -> {
                    Optional<NbtCompound> nbt = nbtElement.asCompound();

                    if (nbt.isEmpty()) {
                        ServerWipe.geode.logger.error("Tried reading from TimedAction codec with non nbt compound nbt element!");
                        return null;
                    }

                    return read.apply(nbt.get());
                })
        );
    }
}
