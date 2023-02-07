package cn.powernukkitx.techdawn.entity.handle;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.custom.CustomEntity;
import cn.nukkit.entity.custom.CustomEntityDefinition;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;

@AutoRegister(CustomEntity.class)
public class BaseHandleEntity extends Entity implements CustomEntity {
    public final static CustomEntityDefinition def = CustomEntityDefinition.builder()
            .identifier("techdawn:base_handle")
            .summonable(true)
            .spawnEgg(false)
            .build();

    public BaseHandleEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public float getHeight() {
        return 0f;
    }

    @Override
    public float getWidth() {
        return 0f;
    }

    @Override
    public float getLength() {
        return 0f;
    }

    @Override
    public CustomEntityDefinition getDefinition() {
        return def;
    }

    @Override
    public int getNetworkId() {
        return getDefinition().getRuntimeId();
    }

    @Override
    protected float getGravity() {
        return 0f;
    }

    @Override
    public boolean isImmobile() {
        return true;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        this.yaw += 5;
        if (yaw > 360) {
            yaw -= 360;
        }
        updateMovement();
        return super.onUpdate(currentTick);
    }
}
