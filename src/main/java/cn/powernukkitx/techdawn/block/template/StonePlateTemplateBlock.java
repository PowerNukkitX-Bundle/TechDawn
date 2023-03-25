package cn.powernukkitx.techdawn.block.template;

import cn.nukkit.block.customblock.CustomBlock;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class StonePlateTemplateBlock extends BaseTemplateBlock {
    @SuppressWarnings("unused")
    public StonePlateTemplateBlock() {
        this(0);
    }

    public StonePlateTemplateBlock(int meta) {
        super(meta);
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:stone_plate_template";
    }
}
