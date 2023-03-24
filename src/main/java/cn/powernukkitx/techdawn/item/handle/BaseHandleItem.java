package cn.powernukkitx.techdawn.item.handle;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolid;
import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustom;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockFace;
import cn.powernukkitx.techdawn.data.TechDawnHardness;
import org.jetbrains.annotations.NotNull;

public abstract class BaseHandleItem extends ItemCustom {
    public BaseHandleItem(@NotNull String id) {
        super(id, null, "techdawn-items-misc-" + id.substring(id.lastIndexOf(":") + 1)
                .replace("_item", ""));
    }

    @Override
    public CustomItemDefinition getDefinition() {
        return CustomItemDefinition.simpleBuilder(this, ItemCreativeCategory.ITEMS)
                .tag(getTags().split(" +")).build();
    }

    @NotNull
    public String getTags() {
        return "";
    }

    public abstract Block getBlock();

    @Override
    public boolean onActivate(Level level, Player player, Block block, Block target, BlockFace face, double fx, double fy, double fz) {
        if (player.isSpectator() || player.isAdventure()) {
            return false;
        }
        if (target instanceof BlockSolid) {
            if (!player.isCreative()) {
                player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            }
            // place block
            var b = getBlock();
            b.setX(target.getX() + face.getXOffset());
            b.setY(target.getY() + face.getYOffset());
            b.setZ(target.getZ() + face.getZOffset());
            b.setLevel(level);
            b.place(this, b, target, face, fx, fy, fz, player);
            return true;
        }
        return false;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }
}
