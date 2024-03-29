package cn.powernukkitx.techdawn.entity.handle;

import cn.nukkit.Player;
import cn.nukkit.energy.EnergyHolder;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.custom.CustomEntity;
import cn.nukkit.entity.custom.CustomEntityDefinition;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.PlaySoundPacket;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.block.handle.BaseHandleBlock;
import cn.powernukkitx.techdawn.energy.Rotation;
import cn.powernukkitx.techdawn.util.LevelUtil;

import java.util.concurrent.ThreadLocalRandom;

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
            var be = this.level.getBlockEntity(this.asBlockVector3().add(0, -1));
            if (be instanceof EnergyHolder nbe && nbe.canAcceptInput(Rotation.getInstance(), BlockFace.UP)) {
                nbe.inputInto(Rotation.getInstance(), 1.5);
            }
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

    protected float getBrokenRate() {
        return 0.0f;
    }

    public void onPlayerInteract(Player player) {
        if (rotatingTick != 0) {
            return;
        }
        if (ThreadLocalRandom.current().nextFloat() < getBrokenRate()) {
            this.level.addSound(this, Sound.RANDOM_BREAK);
            if (player != null) {
                this.getTickCachedLevelBlock().onBreak(player.getInventory().getItemInHand());
            } else {
                this.getTickCachedLevelBlock().onBreak(null);
            }
        }
        rotatingTick = getInteractTick();
        this.yaw += 30;
        LevelUtil.sendSwingArm(player);
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
