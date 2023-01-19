package cn.powernukkitx.techdawn.data;

/**
 * 硬度接口，所有与材质硬度相关的类都应该实现这个接口
 * <p>
 * 有机体的硬度全部为1，其他有机物和无机物的硬度粗略估计为其莫氏硬度*10
 * <br>
 * P.S. 67 is a good number
 */
public interface TechDawnHardness {
    int HARDNESS_WOODEN = 1;
    int HARDNESS_STONE = 28;
    int HARDNESS_COPPER = 30;
    int HARDNESS_TOUGH_COPPER = 28;
    int HARDNESS_ANNEALED_COPPER = 25;
    int HARDNESS_IRON = 35;
    int HARDNESS_STEEL = 38;
    int HARDNESS_DIAMOND = 100;
    int HARDNESS_STAINLESS_STEEL = 60;

    int getHardnessTier();

    default boolean isProcessorBlock() {
        return false;
    }

    default boolean isProcessorItem() {
        return false;
    }

    /**
     * 计算处理配方所需的硬度耐久
     * <br>
     * max(2dh, 1.2<sup>dh</sup>)
     * @param inputHardnessTier     原料硬度
     * @param processorHardnessTier 处理器硬度（锤子等）
     * @return 消耗的耐久
     */
    static int calcRecipeHardnessDamage(int inputHardnessTier, int processorHardnessTier) {
        if (processorHardnessTier >= inputHardnessTier) return 1;
        return (int) Math.max((inputHardnessTier - processorHardnessTier) * 2, Math.pow(1.2, inputHardnessTier - processorHardnessTier));
    }
}
