package cn.powernukkitx.techdawn.entity.windmill;

import cn.nukkit.entity.custom.CustomEntity;
import cn.nukkit.entity.custom.CustomEntityDefinition;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;

@AutoRegister(CustomEntity.class)
public class AntisepticWoodWindmillEntity extends BaseWindmillEntity {
    public final static CustomEntityDefinition def = CustomEntityDefinition.builder()
            .identifier("techdawn:antiseptic_wood_windmill")
            .summonable(true)
            .spawnEgg(false)
            .build();

    public AntisepticWoodWindmillEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public CustomEntityDefinition getDefinition() {
        return def;
    }
}
