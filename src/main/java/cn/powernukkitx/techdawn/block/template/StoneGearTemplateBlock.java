package cn.powernukkitx.techdawn.block.template;

import cn.nukkit.block.customblock.CustomBlock;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class StoneGearTemplateBlock extends BaseTemplateBlock {
    @SuppressWarnings("unused")
    public StoneGearTemplateBlock() {
        this(0);
    }

    public StoneGearTemplateBlock(int meta) {
        super(meta);
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:stone_gear_template";
    }
}
