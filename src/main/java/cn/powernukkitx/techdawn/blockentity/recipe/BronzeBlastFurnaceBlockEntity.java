package cn.powernukkitx.techdawn.blockentity.recipe;

import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.construct.BronzeBrickBlock;
import cn.powernukkitx.techdawn.block.construct.BronzeOutletFlueBlock;
import cn.powernukkitx.techdawn.block.machine.recipe.BronzeBlastFurnaceBlock;
import cn.powernukkitx.techdawn.multi.MultiBlockStruct;
import cn.powernukkitx.techdawn.multi.StructPointMatcher;
import org.jetbrains.annotations.NotNull;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("#getName")
public class BronzeBlastFurnaceBlockEntity extends CopperBlastFurnaceBlockEntity {
    public static MultiBlockStruct BRONZE_BLAST_STRUCT = null;

    public BronzeBlastFurnaceBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    private static class BronzeBlastFurnaceMatcher implements StructPointMatcher {
        @NotNull
        private BlockFace facing;

        public BronzeBlastFurnaceMatcher(@NotNull BlockFace facing) {
            this.facing = facing;
        }

        @Override
        public boolean matchAt(Block worldBlock) {
            return worldBlock instanceof BronzeBlastFurnaceBlock cb && cb.getBlockFace() == facing;
        }

        @Override
        public boolean canRotate() {
            return true;
        }

        @Override
        public void rotate(BlockFace from, BlockFace to) {
            facing = MultiBlockStruct.rotate(facing, from, to);
        }

        @Override
        public StructPointMatcher copy() {
            return new BronzeBlastFurnaceMatcher(facing);
        }
    }

    public static MultiBlockStruct getBronzeBlastStruct() {
        if (BRONZE_BLAST_STRUCT == null) BRONZE_BLAST_STRUCT = new MultiBlockStruct(BlockFace.SOUTH)
                // layer 1
                .add(-1, 0, 1, new BronzeBrickBlock())
                .add(0, 0, 1, new BronzeBrickBlock())
                .add(1, 0, 1, new BronzeBrickBlock())
                .add(-1, 0, 0, new BronzeBrickBlock())
                .add(0, 0, 0, new BronzeBrickBlock())
                .add(1, 0, 0, new BronzeBrickBlock())
                .add(-1, 0, -1, new BronzeBrickBlock())
                .add(0, 0, -1, new BronzeBrickBlock())
                .add(1, 0, -1, new BronzeBrickBlock())
                // layer 2
                .add(-1, 1, 1, new BronzeBrickBlock())
                .add(0, 1, 1, new BronzeBlastFurnaceMatcher(BlockFace.SOUTH))
                .add(1, 1, 1, new BronzeBrickBlock())
                .add(-1, 1, 0, new BronzeBrickBlock())
                .add(1, 1, 0, new BronzeBrickBlock())
                .add(-1, 1, -1, new BronzeBrickBlock())
                .add(0, 1, -1, new BronzeBrickBlock())
                .add(1, 1, -1, new BronzeBrickBlock())
                // layer 3
                .add(-1, 2, 1, new BronzeBrickBlock())
                .add(0, 2, 1, new BronzeBrickBlock())
                .add(1, 2, 1, new BronzeBrickBlock())
                .add(-1, 2, 0, new BronzeBrickBlock())
                .add(1, 2, 0, new BronzeBrickBlock())
                .add(-1, 2, -1, new BronzeBrickBlock())
                .add(0, 2, -1, new BronzeBrickBlock())
                .add(1, 2, -1, new BronzeBrickBlock())
                // layer 4
                .add(-1, 3, 1, new BronzeBrickBlock())
                .add(0, 3, 1, new BronzeBrickBlock())
                .add(1, 3, 1, new BronzeBrickBlock())
                .add(-1, 3, 0, new BronzeBrickBlock())
                .add(0, 3, 0, new BronzeOutletFlueBlock())
                .add(1, 3, 0, new BronzeBrickBlock())
                .add(-1, 3, -1, new BronzeBrickBlock())
                .add(0, 3, -1, new BronzeBrickBlock())
                .add(1, 3, -1, new BronzeBrickBlock());
        return BRONZE_BLAST_STRUCT;
    }

    @Override
    public int getSpeedMultiplier() {
        return 3;
    }

    @NotNull
    @Override
    protected MultiBlockStruct getMultiBlockStruct() {
        return getBronzeBlastStruct();
    }

    @NotNull
    @Override
    public String getName() {
        return "TechDawn_BronzeBlastFurnaceBlock";
    }

    @Override
    protected @NotNull String getUITitle(boolean valid) {
        return valid ? "ui.techdawn_vanilla-like.bronze_blast_furnace" : "ui.techdawn_vanilla-like.invalid_structure";
    }

    @Override
    public boolean isBlockEntityValid() {
        return getLevelBlock() instanceof BronzeBlastFurnaceBlock;
    }
}