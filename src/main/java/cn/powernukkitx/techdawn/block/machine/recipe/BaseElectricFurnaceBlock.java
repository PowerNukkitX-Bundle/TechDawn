package cn.powernukkitx.techdawn.block.machine.recipe;

import cn.nukkit.Player;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Materials;
import cn.nukkit.block.customblock.data.Permutation;
import cn.nukkit.block.customblock.data.Permutations;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import me.iwareq.fakeinventories.CustomInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegister(CustomBlock.class)
public class BaseElectricFurnaceBlock extends BlockSolidMeta implements CustomBlock {
    public static final BlockProperties PROPERTIES = new BlockProperties(CommonBlockProperties.DIRECTION);

    public BaseElectricFurnaceBlock() {
        this(0);
    }

    public BaseElectricFurnaceBlock(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return CustomBlock.super.getName();
    }

    @Override
    public String getNamespaceId() {
        return "techdawn:base_electric_furnace";
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        return CustomBlockDefinition.builder(this, Materials.builder()
                .up(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-basic_electric_machine_top")
                .south(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-basic_electric_furnace_off")
                .any(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-basic_machine"))
                .build();
    }

    @Override
    public int getId() {
        return CustomBlock.super.getId();
    }

    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(@NotNull Item item, @Nullable Player player) {
        var customInv = new CustomInventory(InventoryType.FURNACE, "Electric Furnace");
        if (player != null) {
            player.addWindow(customInv);
        }
        return true;
    }
}
