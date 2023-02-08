package cn.powernukkitx.techdawn.entity.handle;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.custom.CustomEntity;
import cn.nukkit.entity.custom.CustomEntityDefinition;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.network.protocol.PlaySoundPacket;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.block.handle.BaseHandleBlock;

@AutoRegister(CustomEntity.class)
public class BaseHandleEntity extends Entity implements CustomEntity {
    public final static CustomEntityDefinition def = CustomEntityDefinition.builder()
            .identifier("techdawn:base_handle")
            .summonable(true)
            .spawnEgg(false)
            .build();

    protected int rotatingTick = 0;

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
        if (rotatingTick != 0) {
            if (rotatingTick >= getInteractTick() - 1) {
                if (yaw > 180) {
                    yaw -= 360;
                }
                updateMovement();
            }
            --this.rotatingTick;
        }
        if (!(getTickCachedLevelBlock() instanceof BaseHandleBlock)) {
            close();
        }
        return super.onUpdate(currentTick);
    }

    protected String getSound() {
        return "techdawn.wood_handle";
    }

    protected int getInteractTick() {
        return 20;
    }

    public void onPlayerInteract(Player player) {
        if (rotatingTick != 0) {
            return;
        }
        rotatingTick = getInteractTick();
        this.yaw += 30;
        if (player != null) {
            var pk = new EntityEventPacket();
            pk.eid = player.getId();
            pk.event = EntityEventPacket.ARM_SWING;
            player.dataPacket(pk);
            Server.broadcastPacket(player.getViewers().values(), pk);
        }
        {
            var pk = new PlaySoundPacket();
            pk.name = getSound();
            pk.volume = 1;
            pk.pitch = 1;
            pk.x = this.getFloorX();
            pk.y = this.getFloorY();
            pk.z = this.getFloorZ();
            this.level.addChunkPacket(this.getChunkX(), this.getChunkZ(), pk);
        }
    }
}
