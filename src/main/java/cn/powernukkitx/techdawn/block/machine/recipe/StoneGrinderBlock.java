package cn.powernukkitx.techdawn.block.machine.recipe;

import cn.nukkit.Player;
import cn.nukkit.block.BlockEntityHolder;
import cn.nukkit.block.BlockSolid;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Materials;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.blockentity.recipe.StoneGrinderBlockEntity;
import cn.powernukkitx.techdawn.util.InventoryUtil;
import cn.powernukkitx.techdawn.util.LevelUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegister(CustomBlock.class)
public class StoneGrinderBlock extends BlockSolid implements CustomBlock, BlockEntityHolder<StoneGrinderBlockEntity> {
    @Override
    public String getName() {
        return CustomBlock.super.getName();
    }

    @Override
    @NotNull
    public String getNamespaceId() {
        return "techdawn:stone_grinder";
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        return CustomBlockDefinition.builder(this, Materials.builder()
                        .up(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-stone_machine_top")
                        .down(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-stone_machine_bottom")
                        .any(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-stone_grinder_side"))
                .build();
    }

    @Override
    public int getId() {
        return CustomBlock.super.getId();
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.STONE_BLOCK_COLOR;
    }

    @NotNull
    @Override
    public Class<? extends StoneGrinderBlockEntity> getBlockEntityClass() {
        return StoneGrinderBlockEntity.class;
    }

    @NotNull
    @Override
    public String getBlockEntityType() {
        return "TechDawn_StoneGrinderBlock";
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(@NotNull Item item, @Nullable Player player) {
        if (player != null && InventoryUtil.ensurePlayerSafeForCustomInv(player)) {
            StoneGrinderBlockEntity be = getOrCreateBlockEntity();
            player.addWindow(be.getDisplayInventory());
            be.requestUIUpdateImmediately();
            if (item.canBePlaced()) LevelUtil.resendAroundBlocks(this, player);
            return true;
        }
        return false;
    }
}
