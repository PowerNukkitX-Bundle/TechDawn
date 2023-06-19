package cn.powernukkitx.techdawn.blockentity.hopper;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.hopper.PoweredWoodHopperBlock;
import org.jetbrains.annotations.NotNull;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("#getName")
public class PoweredWoodHopperBlockEntity extends PoweredIronHopperBlockEntity {
    public PoweredWoodHopperBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected boolean checkBlockStateValid(@NotNull BlockState levelBlockState) {
        return levelBlockState.getBlock() instanceof PoweredWoodHopperBlock;
    }

    @Override
    public int getCooldownTick() {
        return 48; // 8 * 6
    }

    @Override
    public boolean isBlockEntityValid() {
        return super.getBlock() instanceof PoweredWoodHopperBlock;
    }

    @Override
    protected String getUITitle() {
        return "ui.techdawn.powered_wood_hopper";
    }

    @Override
    public String getName() {
        return "TechDawn_PoweredWoodHopperBlockEntity";
    }
}
