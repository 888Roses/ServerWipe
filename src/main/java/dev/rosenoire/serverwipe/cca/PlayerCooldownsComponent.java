package dev.rosenoire.serverwipe.cca;

import com.mojang.serialization.Codec;
import dev.rosenoire.serverwipe.api.cca.components.PlayerComponent;
import dev.rosenoire.serverwipe.common.index.ModEntityComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.HashMap;
import java.util.Map;

public final class PlayerCooldownsComponent extends PlayerComponent implements CommonTickingComponent {
    public final Map<Identifier, CooldownInfo> cooldowns = new HashMap<>();

    //region construction
    public PlayerCooldownsComponent(PlayerEntity player) {
        super(player);
    }

    @Override
    public ComponentKey<? extends PlayerComponent> getComponentKey() {
        return ModEntityComponents.PLAYER_COOLDOWNS;
    }
    //endregion

    //region data
    public static final Codec<Map<Identifier, CooldownInfo>> CODEC = Codec.dispatchedMap(Identifier.CODEC, key -> CooldownInfo.CODEC);

    @Override
    public void readData(ReadView readView) {
        cooldowns.clear();
        readView.read("cooldowns", CODEC).ifPresent(cooldowns::putAll);
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.put("cooldowns", CODEC, cooldowns);
    }
    //endregion

    //region tick
    @Override
    public void tick() {
        long time = world().getTime();

        for (Map.Entry<Identifier, CooldownInfo> entry : cooldowns.entrySet()) {
            CooldownInfo info = entry.getValue();

            if (info.validate(time)) {
                cooldowns.remove(entry.getKey());
                sync();
                break;
            }
        }
    }
    //endregion
}

