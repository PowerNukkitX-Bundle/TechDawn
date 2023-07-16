package cn.powernukkitx.techdawn.block.machine.recipe;

import cn.nukkit.api.UsedByReflection;
import cn.nukkit.block.customblock.CustomBlock;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.blockentity.recipe.BasicElectricGrinderBlockEntity;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class BasicElectricGrinderBlock extends BaseElectricGrinderBlock {
    @UsedByReflection
    public BasicElectricGrinderBlock() {
        super();
    }

    @UsedByReflection
    public BasicElectricGrinderBlock(int meta) {
        super(meta);
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:basic_electric_grinder";
    }

    @NotNull
    @Override
    public Class<BasicElectricGrinderBlockEntity> getBlockEntityClass() {
        return BasicElectricGrinderBlockEntity.class;
    }

    @NotNull
    @Override
    public String getBlockEntityType() {
        return "TechDawn_BasicElectricGrinderBlock";
    }
}
