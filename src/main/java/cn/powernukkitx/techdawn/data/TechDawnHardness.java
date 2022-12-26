package cn.powernukkitx.techdawn.data;

public interface TechDawnHardness {
    int getHardnessTier();

    default boolean isProcessorBlock() {
        return false;
    }

    default boolean isProcessorItem() {
        return false;
    }
}
