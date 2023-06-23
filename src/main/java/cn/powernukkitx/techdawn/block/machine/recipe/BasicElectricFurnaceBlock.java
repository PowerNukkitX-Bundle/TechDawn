package cn.powernukkitx.techdawn.block.machine.recipe;

import cn.nukkit.api.UsedByReflection;
import cn.nukkit.block.customblock.CustomBlock;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.blockentity.recipe.BasicElectricFurnaceBlockEntity;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class BasicElectricFurnaceBlock extends BaseElectricFurnaceBlock {
    @UsedByReflection
    public BasicElectricFurnaceBlock() {
        super();
    }

    @UsedByReflection
    public BasicElectricFurnaceBlock(int meta) {
        super(meta);
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:basic_electric_furnace";
    }

    @NotNull
    @Override
    public Class<BasicElectricFurnaceBlockEntity> getBlockEntityClass() {
        return BasicElectricFurnaceBlockEntity.class;
    }

    @NotNull
    @Override
    public String getBlockEntityType() {
        return "TechDawn_BasicElectricFurnaceBlock";
    }
}
