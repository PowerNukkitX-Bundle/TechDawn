package cn.powernukkitx.techdawn.block.machine.recipe;

import cn.nukkit.block.BlockSolid;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Materials;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class StoneExtractorBlock extends BlockSolid implements CustomBlock {
    @Override
    public String getName() {
        return CustomBlock.super.getName();
    }

    @Override
    @NotNull
    public String getNamespaceId() {
        return "techdawn:stone_extractor";
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        return CustomBlockDefinition.builder(this, Materials.builder()
                .up(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-stone_machine_top")
                .down(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-stone_machine_bottom")
                .any(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-stone_extractor_side"))
                .build();
    }

    @Override
    public int getId() {
        return CustomBlock.super.getId();
    }
}
