package cn.powernukkitx.techdawn.block.machine.recipe;

import cn.nukkit.api.UsedByReflection;
import cn.nukkit.block.customblock.CustomBlock;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.blockentity.recipe.BasicElectricExtractorBlockEntity;
import cn.powernukkitx.techdawn.blockentity.recipe.BasicElectricScreenerBlockEntity;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class BasicElectricScreenerBlock extends BaseElectricScreenerBlock {
    @UsedByReflection
    public BasicElectricScreenerBlock() {
        super();
    }

    @UsedByReflection
    public BasicElectricScreenerBlock(int meta) {
        super(meta);
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:basic_electric_screener";
    }

    @NotNull
    @Override
    public Class<BasicElectricScreenerBlockEntity> getBlockEntityClass() {
        return BasicElectricScreenerBlockEntity.class;
    }

    @NotNull
    @Override
    public String getBlockEntityType() {
        return "TechDawn_BasicElectricScreenerBlock";
    }
}
