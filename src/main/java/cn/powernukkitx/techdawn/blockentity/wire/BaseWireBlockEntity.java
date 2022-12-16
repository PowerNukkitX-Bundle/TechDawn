package cn.powernukkitx.techdawn.blockentity.wire;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.wire.BaseWireBlock;
import cn.powernukkitx.techdawn.energy.EnergyNetworkManager;
import org.jetbrains.annotations.NotNull;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("TechDawn_BaseWireBlock")
public class BaseWireBlockEntity extends BlockEntity {
    private boolean isClosing;

    public BaseWireBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        isClosing = false;
        EnergyNetworkManager.putWireAt(getFloorX(), getFloorY(), getFloorZ(), getLevel());
    }

    @NotNull
    @Override
    public String getName() {
        return "TechDawn_BaseWireBlock";
    }

    @Override
    public boolean isBlockEntityValid() {
        return getBlock() instanceof BaseWireBlock;
    }

    @Override
    public void close() {
        if (!this.closed) {
            this.isClosing = true;
            EnergyNetworkManager.removeWireAt(getFloorX(), getFloorY(), getFloorZ(), getLevel());
        }
        super.close();
    }

    public boolean isClosing() {
        return isClosing;
    }
}
