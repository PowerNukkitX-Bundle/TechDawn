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
import cn.powernukkitx.techdawn.blockentity.recipe.StoneExtractorBlockEntity;
import cn.powernukkitx.techdawn.util.InventoryUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegister(CustomBlock.class)
public class StoneExtractorBlock extends BlockSolid implements CustomBlock, BlockEntityHolder<StoneExtractorBlockEntity> {
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

    @Override
    public BlockColor getColor() {
        return BlockColor.STONE_BLOCK_COLOR;
    }

    @NotNull
    @Override
    public Class<? extends StoneExtractorBlockEntity> getBlockEntityClass() {
        return StoneExtractorBlockEntity.class;
    }

    @NotNull
    @Override
    public String getBlockEntityType() {
        return "TechDawn_StoneExtractorBlock";
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(@NotNull Item item, @Nullable Player player) {
        if (player != null && InventoryUtil.ensurePlayerSafeForCustomInv(player)) {
            var be = getOrCreateBlockEntity();
            player.addWindow(be.getDisplayInventory());
            be.requestUIUpdateImmediately();
            return true;
        }
        return false;
    }
}