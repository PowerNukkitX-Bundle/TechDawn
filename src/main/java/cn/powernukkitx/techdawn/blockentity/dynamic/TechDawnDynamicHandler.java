package cn.powernukkitx.techdawn.blockentity.dynamic;

public interface TechDawnDynamicHandler {
    /**
     * @param amount given energy
     * @return the rest energy
     */
    @SuppressWarnings("UnusedReturnValue")
    double handleDynamicTransferring(double amount);
}
