package cn.powernukkitx.techdawn.block.machine.battery;

import cn.nukkit.block.customblock.CustomBlock;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.blockentity.battery.BasicRedstoneBatteryBoxBlockEntity;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class BasicRedstoneBatteryBoxBlock extends BaseBatteryBoxBlock {
    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:basic_redstone_battery_box";
    }

    @NotNull
    @Override
    public Class<BasicRedstoneBatteryBoxBlockEntity> getBlockEntityClass() {
        return BasicRedstoneBatteryBoxBlockEntity.class;
    }

    @NotNull
    @Override
    public String getBlockEntityType() {
        return "TechDawn_BasicRedstoneBatteryBoxBlock";
    }
}
