package cn.powernukkitx.techdawn.entity.handle;

import cn.nukkit.entity.custom.CustomEntity;
import cn.nukkit.entity.custom.CustomEntityDefinition;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;

@AutoRegister(CustomEntity.class)
public class AntisepticWoodHandleEntity extends BaseHandleEntity implements CustomEntity {
    public final static CustomEntityDefinition def = CustomEntityDefinition.builder()
            .identifier("techdawn:antiseptic_wood_handle")
            .summonable(true)
            .spawnEgg(false)
            .build();

    public AntisepticWoodHandleEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public CustomEntityDefinition getDefinition() {
        return def;
    }
}
