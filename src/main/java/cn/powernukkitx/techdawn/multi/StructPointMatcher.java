package cn.powernukkitx.techdawn.multi;

import cn.nukkit.block.Block;
import cn.nukkit.math.BlockFace;

/**
 * 检测多方块结构中某个点位的方块是否符合要求
 */
public interface StructPointMatcher {
    boolean matchAt(Block worldBlock);

    boolean canRotate();

    void rotate(BlockFace from, BlockFace to);

    StructPointMatcher copy();
}
