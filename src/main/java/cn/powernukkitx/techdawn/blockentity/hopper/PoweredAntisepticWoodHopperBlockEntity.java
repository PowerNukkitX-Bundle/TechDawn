package cn.powernukkitx.techdawn.blockentity.hopper;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.hopper.PoweredAntisepticWoodHopperBlock;
import org.jetbrains.annotations.NotNull;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("#getName")
public class PoweredAntisepticWoodHopperBlockEntity extends PoweredIronHopperBlockEntity {
    public PoweredAntisepticWoodHopperBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected boolean checkBlockStateValid(@NotNull BlockState levelBlockState) {
        return levelBlockState.getBlock() instanceof PoweredAntisepticWoodHopperBlock;
    }

    @Override
    public int getCooldownTick() {
        return 12; // 8 * 1.5
    }

    @Override
    public boolean isBlockEntityValid() {
        return super.getBlock() instanceof PoweredAntisepticWoodHopperBlock;
    }

    @Override
    protected String getUITitle() {
        return "ui.techdawn.powered_antiseptic_wood_hopper";
    }

    @Override
    public String getName() {
        return "TechDawn_PoweredAntisepticWoodHopperBlockEntity";
    }
}
