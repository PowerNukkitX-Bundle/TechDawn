package cn.powernukkitx.techdawn.blockentity.dynamic;

import cn.nukkit.math.BlockFace;

public interface TechDawnDynamicHandler {
    /**
     * @param amount        given energy, if it's 0, it means stopping
     * @param directionFace the direction that the energy goes
     * @return the rest energy
     */
    @SuppressWarnings("UnusedReturnValue")
    double handleDynamicTransferring(double amount, BlockFace directionFace);

    /**
     * @param directionFace the direction that the energy goes
     * @param isTransposed whether the block is transposed
     * @return whether this handler can handle such energy transferring
     */
    boolean isDirectionAcceptable(BlockFace directionFace, boolean isTransposed);
}
