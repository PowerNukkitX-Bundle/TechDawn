package cn.powernukkitx.techdawn.blockentity.recipe;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.machine.recipe.BasicElectricScreenerBlock;
import org.jetbrains.annotations.NotNull;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("#getName")
public class BasicElectricScreenerBlockEntity extends BaseElectricScreenerBlockEntity {
    public BasicElectricScreenerBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public boolean isBlockEntityValid() {
        return getLevelBlock() instanceof BasicElectricScreenerBlock;
    }

    @NotNull
    @Override
    public String getName() {
        return "TechDawn_BasicElectricScreenerBlock";
    }

    @Override
    public BasicElectricScreenerBlock getBlock() {
        return (BasicElectricScreenerBlock) super.getBlock();
    }

    @NotNull
    @Override
    protected String getUITitle() {
        return "ui.techdawn.basic_electric_screener";
    }
}
