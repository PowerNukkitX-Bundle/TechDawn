package cn.powernukkitx.techdawn.blockentity.recipe;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.machine.recipe.BasicElectricGrinderBlock;
import org.jetbrains.annotations.NotNull;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("#getName")
public class BasicElectricGrinderBlockEntity extends BaseElectricGrinderBlockEntity {
    public BasicElectricGrinderBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public boolean isBlockEntityValid() {
        return getLevelBlock() instanceof BasicElectricGrinderBlock;
    }

    @NotNull
    @Override
    public String getName() {
        return "TechDawn_BasicElectricGrinderBlock";
    }

    @Override
    public BasicElectricGrinderBlock getBlock() {
        return (BasicElectricGrinderBlock) super.getBlock();
    }

    @NotNull
    @Override
    protected String getUITitle() {
        return "ui.techdawn.basic_electric_grinder";
    }
}
