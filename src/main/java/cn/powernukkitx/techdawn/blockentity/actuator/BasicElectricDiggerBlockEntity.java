package cn.powernukkitx.techdawn.blockentity.actuator;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.machine.actuator.BasicElectricDiggerBlock;
import org.jetbrains.annotations.NotNull;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("#getName")
public class BasicElectricDiggerBlockEntity extends BaseElectricDiggerBlockEntity {
    public BasicElectricDiggerBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public boolean isBlockEntityValid() {
        return getLevelBlock() instanceof BasicElectricDiggerBlock;
    }

    @NotNull
    @Override
    public String getName() {
        return "TechDawn_BasicElectricDiggerBlock";
    }

    @Override
    public BasicElectricDiggerBlock getBlock() {
        return (BasicElectricDiggerBlock) super.getBlock();
    }

    @NotNull
    @Override
    protected String getUITitle() {
        if (isPlayerOffline()) {
            return "ui.techdawn.basic_electric_digger.offline";
        }
        return "ui.techdawn.basic_electric_digger";
    }
}
