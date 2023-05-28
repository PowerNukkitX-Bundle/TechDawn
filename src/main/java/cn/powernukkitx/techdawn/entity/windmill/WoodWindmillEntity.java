package cn.powernukkitx.techdawn.entity.windmill;

import cn.nukkit.entity.custom.CustomEntity;
import cn.nukkit.entity.custom.CustomEntityDefinition;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;

@AutoRegister(CustomEntity.class)
public class WoodWindmillEntity extends BaseWindmillEntity {
    public final static CustomEntityDefinition def = CustomEntityDefinition.builder()
            .identifier("techdawn:wood_windmill")
            .summonable(true)
            .spawnEgg(false)
            .build();

    public WoodWindmillEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public CustomEntityDefinition getDefinition() {
        return def;
    }

    protected float getMaxOutput() {
        return 2f;
    }

    protected float getMinOutput() {
        return 1f / 3f;
    }
}
