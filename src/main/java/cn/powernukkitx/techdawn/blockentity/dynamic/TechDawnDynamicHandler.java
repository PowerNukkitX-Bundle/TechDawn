package cn.powernukkitx.techdawn.blockentity.dynamic;

import cn.nukkit.math.BlockFace;

public interface TechDawnDynamicHandler {
    /**
     * @param amount        given energy
     * @param directionFace the direction that the energy goes
     * @return the rest energy
     */
    @SuppressWarnings("UnusedReturnValue")
    double handleDynamicTransferring(double amount, BlockFace directionFace);
}
