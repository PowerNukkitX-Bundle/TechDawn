package cn.powernukkitx.techdawn.data;

/**
 * 硬度接口，所有与材质硬度相关的类都应该实现这个接口
 * <p>
 * 有机体的硬度全部为1，其他有机物和无机物的硬度粗略估计为其莫氏硬度*10
 */
public interface TechDawnHardness {
    int HARDNESS_WOODEN = 1;
    int HARDNESS_STONE = 28;
    int HARDNESS_COPPER = 30;
    int HARDNESS_TOUGH_COPPER = 25;
    int HARDNESS_ANNEALED_COPPER = 22;
    int HARDNESS_IRON = 40;
    int HARDNESS_DIAMOND = 100;
    int HARDNESS_STAINLESS_STEEL = 60;

    int getHardnessTier();

    default boolean isProcessorBlock() {
        return false;
    }

    default boolean isProcessorItem() {
        return false;
    }
}
