package cn.powernukkitx.techdawn.block.anvil;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparent;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Materials;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Sound;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class BaseAnvilBlock extends BlockTransparent implements CustomBlock {
    @Override
    public String getName() {
        return CustomBlock.super.getName();
    }

    @Override
    public String getNamespaceId() {
        return "techdawn:base_anvil";
    }

    public String getTextureName() {
        return "techdawn-blocks-anvil-base_anvil";
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        return CustomBlockDefinition
                .builder(this, Materials.builder().any(Materials.RenderMethod.ALPHA_TEST, getTextureName()))
                .geometry("geometry.techdawn.anvil")
                .build();
    }

    @Override
    public int getId() {
        return CustomBlock.super.getId();
    }

    @Override
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        if (player == null) {
            this.getLevel().addSound(this, Sound.RANDOM_ANVIL_LAND, 1.0F, 0.8F);
        } else {
            Collection<Player> players = this.getLevel().getChunkPlayers(this.getChunkX(), this.getChunkZ()).values();
            players.remove(player);
            if (!players.isEmpty()) {
                this.getLevel().addSound(this, Sound.RANDOM_ANVIL_LAND, 1.0F, 0.8F, players);
            }
        }
        return super.place(item, block, target, face, fx, fy, fz, player);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.GRAY_BLOCK_COLOR;
    }

    @Override
    public int getLightFilter() {
        return 4;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public int getToolTier() {
        return ItemTool.TIER_IRON;
    }

    @Override
    public double getHardness() {
        return 5;
    }

    @Override
    public double getResistance() {
        return 6000;
    }
}
