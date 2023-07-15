package cn.powernukkitx.techdawn.block.machine.recipe;

import cn.nukkit.api.UsedByReflection;
import cn.nukkit.block.customblock.CustomBlock;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.blockentity.recipe.BasicElectricExtractorBlockEntity;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class BasicElectricExtractorBlock extends BaseElectricExtractorBlock {
    @UsedByReflection
    public BasicElectricExtractorBlock() {
        super();
    }

    @UsedByReflection
    public BasicElectricExtractorBlock(int meta) {
        super(meta);
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:basic_electric_extractor";
    }

    @NotNull
    @Override
    public Class<BasicElectricExtractorBlockEntity> getBlockEntityClass() {
        return BasicElectricExtractorBlockEntity.class;
    }

    @NotNull
    @Override
    public String getBlockEntityType() {
        return "TechDawn_BasicElectricExtractorBlock";
    }
}
