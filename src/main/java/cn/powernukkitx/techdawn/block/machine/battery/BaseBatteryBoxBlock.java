package cn.powernukkitx.techdawn.block.machine.battery;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockEntityHolder;
import cn.nukkit.block.BlockSolid;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Materials;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import cn.powernukkitx.techdawn.blockentity.battery.BaseBatteryBoxBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BaseBatteryBoxBlock extends BlockSolid implements CustomBlock, BlockEntityHolder<BaseBatteryBoxBlockEntity> {
    @Override
    public String getName() {
        return CustomBlock.super.getName();
    }

    @Override
    public String getNamespaceId() {
        return "techdawn:base_battery_box";
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        return CustomBlockDefinition.builder(this,
                Materials.builder()
                        .up(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-basic_electric_machine_top")
                        .east(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-redstone_battery_box_side")
                        .west(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-redstone_battery_box_side")
                        .north(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-redstone_battery_box_side")
                        .south(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-redstone_battery_box_side")
                        .down(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-basic_machine")
                ).build();
    }

    @Override
    public int getId() {
        return CustomBlock.super.getId();
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.IRON_BLOCK_COLOR;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(@NotNull Item item, @Nullable Player player) {
        if (player != null) {
            var be = getOrCreateBlockEntity();
            be.setStoredEnergy(be.getStoredEnergy() + (Math.random() - 0.5) * 200);
            player.addWindow(be.getDisplayInventory());
            return true;
        }
        return false;
    }

    @NotNull
    @Override
    public Class<? extends BaseBatteryBoxBlockEntity> getBlockEntityClass() {
        return BaseBatteryBoxBlockEntity.class;
    }

    @NotNull
    @Override
    public String getBlockEntityType() {
        return "TechDawn_BaseBatteryBoxBlock";
    }

    @Override
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        getOrCreateBlockEntity();
        return super.place(item, block, target, face, fx, fy, fz, player);
    }
}