package cn.powernukkitx.techdawn.blockentity.recipe;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.machine.recipe.BasicElectricFurnaceBlock;
import org.jetbrains.annotations.NotNull;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("#getName")
public class BasicElectricFurnaceBlockEntity extends BaseElectricFurnaceBlockEntity {
    public BasicElectricFurnaceBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public boolean isBlockEntityValid() {
        return getLevelBlock() instanceof BasicElectricFurnaceBlock;
    }

    @NotNull
    @Override
    public String getName() {
        return "TechDawn_BasicElectricFurnaceBlock";
    }

    @Override
    public BasicElectricFurnaceBlock getBlock() {
        return (BasicElectricFurnaceBlock) super.getBlock();
    }

    @NotNull
    @Override
    protected String getUITitle() {
        return "ui.techdawn.basic_electric_furnace";
    }
}
