package cn.powernukkitx.techdawn.block.construct;

import cn.nukkit.block.BlockSolid;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Materials;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class CopperOutletFlueBlock extends BlockSolid implements CustomBlock {
    @Override
    public String getName() {
        return CustomBlock.class.getName();
    }

    @Override
    @NotNull
    public String getNamespaceId() {
        return "techdawn:copper_outlet_flue";
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        return CustomBlockDefinition.builder(this, Materials.builder()
                .up(Materials.RenderMethod.OPAQUE, "techdawn-blocks-construct-copper_outlet_flue")
                .any(Materials.RenderMethod.OPAQUE, "techdawn-blocks-construct-copper_brick")).build();
    }

    @Override
    public int getId() {
        return CustomBlock.super.getId();
    }
}
