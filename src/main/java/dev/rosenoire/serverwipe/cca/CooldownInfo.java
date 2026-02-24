package dev.rosenoire.serverwipe.cca;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.collectively.geode.math.math;
import net.minecraft.world.World;

public record CooldownInfo(long from, long to) {
    public static final Codec<CooldownInfo> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.LONG.fieldOf("from").forGetter(CooldownInfo::from),
                    Codec.LONG.fieldOf("to").forGetter(CooldownInfo::to)
            ).apply(instance, CooldownInfo::new)
    );

    public long duration() {
        return math.max(0, to - from);
    }

    public long ticksLeft(long currentTime) {
        return math.max(0, to - currentTime);
    }

    public double getProgress(long currentTime) {
        return 1d - math.clamp01(ticksLeft(currentTime) / (double) duration());
    }

    public boolean validate(long currentTime) {
        return currentTime >= to;
    }
}
