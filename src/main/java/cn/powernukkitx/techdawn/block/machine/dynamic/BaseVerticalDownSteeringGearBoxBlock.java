package cn.powernukkitx.techdawn.block.machine.dynamic;

import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.data.Materials;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class BaseVerticalDownSteeringGearBoxBlock extends BaseVerticalUpSteeringGearBoxBlock {
    @SuppressWarnings("unused")
    public BaseVerticalDownSteeringGearBoxBlock() {
        super(0);
    }

    @SuppressWarnings("unused")
    public BaseVerticalDownSteeringGearBoxBlock(int meta) {
        super(meta);
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:base_vertical_down_steering_gear_box";
    }

    @Override
    protected Materials createMaterials() {
        return Materials.builder().north(Materials.RenderMethod.OPAQUE, getMainTextureName() + "_front")
                .down(Materials.RenderMethod.OPAQUE, getMainTextureName() + "_front")
                .any(Materials.RenderMethod.OPAQUE, getMainTextureName() + "_back");
    }

    @Override
    protected Materials createTransposedMaterials() {
        return Materials.builder().north(Materials.RenderMethod.OPAQUE, getMainTextureName() + "_front_transposed")
                .down(Materials.RenderMethod.OPAQUE, getMainTextureName() + "_front_transposed")
                .any(Materials.RenderMethod.OPAQUE, getMainTextureName() + "_back");
    }

    protected CompoundTag createBlockEntityNBT() {
        return new CompoundTag().putString("hinge_type", "techdawn:antiseptic_wood_hinge")
                .putDouble("transfer_rate", 4.5)
                .putBoolean("is_up", false);
    }
}
