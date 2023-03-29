package cn.powernukkitx.techdawn.blockentity.battery;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.machine.battery.BasicRedstoneBatteryBoxBlock;
import org.jetbrains.annotations.NotNull;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("#getName")
public class BasicRedstoneBatteryBoxBlockEntity extends BaseBatteryBoxBlockEntity {
    public BasicRedstoneBatteryBoxBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @NotNull
    @Override
    public String getName() {
        return "TechDawn_BasicRedstoneBatteryBoxBlock";
    }

    @Override
    public boolean isBlockEntityValid() {
        return getBlock() instanceof BasicRedstoneBatteryBoxBlock;
    }

    @Override
    public double getMaxStorage() {
        return 200000;
    }

    @Override
    public double getOutputPerTick() {
        return 60;
    }

    @Override
    protected String getUITitle() {
        return "ui.techdawn.basic_redstone_battery_box";
    }
}
