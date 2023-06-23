package cn.powernukkitx.techdawn.block.machine.actuator;

import cn.nukkit.Player;
import cn.nukkit.block.BlockEntityHolder;
import cn.nukkit.block.BlockSolid;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Materials;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.utils.BlockColor;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.blockentity.actuator.StoneDiggerBlockEntity;
import cn.powernukkitx.techdawn.util.InventoryUtil;
import cn.powernukkitx.techdawn.util.LevelUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegister(CustomBlock.class)
public class StoneDiggerBlock extends BlockSolid implements CustomBlock, BlockEntityHolder<StoneDiggerBlockEntity> {
    @Override
    public String getName() {
        return CustomBlock.super.getName();
    }

    @Override
    @NotNull
    public String getNamespaceId() {
        return "techdawn:stone_digger";
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        return CustomBlockDefinition.builder(this, Materials.builder()
                        .up(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-stone_machine_top")
                        .down(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-stone_digger_bottom")
                        .any(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-stone_digger_side"))
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
    public Class<? extends StoneDiggerBlockEntity> getBlockEntityClass() {
        return StoneDiggerBlockEntity.class;
    }

    @NotNull
    @Override
    public String getBlockEntityType() {
        return "TechDawn_StoneDiggerBlock";
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
            if (item.canBePlaced()) LevelUtil.resendAroundBlocks(this, player);
            return true;
        }
        return false;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            var be = getOrCreateBlockEntity();
            level.scheduleBlockEntityUpdate(be);
        }
        return super.onUpdate(type);
    }
}
