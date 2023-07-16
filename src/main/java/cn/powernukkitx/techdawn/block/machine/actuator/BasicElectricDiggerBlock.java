package cn.powernukkitx.techdawn.block.machine.actuator;

import cn.nukkit.api.UsedByReflection;
import cn.nukkit.block.customblock.CustomBlock;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.blockentity.actuator.BaseElectricDiggerBlockEntity;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class BasicElectricDiggerBlock extends BaseElectricDiggerBlock {
    @UsedByReflection
    public BasicElectricDiggerBlock() {
        super();
    }

    @UsedByReflection
    public BasicElectricDiggerBlock(int meta) {
        super(meta);
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:basic_electric_digger";
    }

    @NotNull
    @Override
    public Class<BaseElectricDiggerBlockEntity> getBlockEntityClass() {
        return BaseElectricDiggerBlockEntity.class;
    }

    @NotNull
    @Override
    public String getBlockEntityType() {
        return "TechDawn_BasicElectricDiggerBlock";
    }
}
