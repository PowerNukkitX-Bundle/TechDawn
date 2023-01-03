package cn.powernukkitx.techdawn.multi;

import cn.nukkit.block.Block;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.Faceable;

public final class SingleBlockMatcher implements StructPointMatcher {
    private final Block block;

    public SingleBlockMatcher(Block block) {
        this.block = block;
    }

    @Override
    public boolean matchAt(Block worldBlock) {
        return Block.equals(block, worldBlock);
    }

    @Override
    public boolean canRotate() {
        return block instanceof Faceable;
    }

    @Override
    public void rotate(BlockFace from, BlockFace to) {
        if (block instanceof Faceable faceable) {
            faceable.setBlockFace(MultiBlockStruct.rotate(faceable.getBlockFace(), from, to));
        }
    }

    @Override
    public StructPointMatcher copy() {
        return new SingleBlockMatcher(block.clone());
    }
}
