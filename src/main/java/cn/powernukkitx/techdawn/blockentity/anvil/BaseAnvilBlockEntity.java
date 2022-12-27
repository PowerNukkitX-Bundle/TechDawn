package cn.powernukkitx.techdawn.blockentity.anvil;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.LavaParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.anvil.BaseAnvilBlock;
import cn.powernukkitx.techdawn.data.TechDawnHardness;
import cn.powernukkitx.techdawn.entity.DisplayItemEntity;
import cn.powernukkitx.techdawn.util.InventoryUtil;
import cn.powernukkitx.techdawn.util.LevelUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static cn.nukkit.inventory.BaseInventory.AIR_ITEM;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("TechDawn_BaseAnvilBlock")
public class BaseAnvilBlockEntity extends BlockEntity implements TechDawnHardness {
    @NotNull
    protected Item anvilItem = AIR_ITEM;
    @Nullable
    protected DisplayItemEntity itemEntity = null;

    public BaseAnvilBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @NotNull
    @Override
    public String getName() {
        return "TechDawn_BaseAnvilBlock";
    }

    @Override
    public boolean isBlockEntityValid() {
        return getBlock().getClass() == BaseAnvilBlock.class;
    }

    @Override
    public int getHardnessTier() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isProcessorBlock() {
        return true;
    }

    public boolean onActive(@NotNull Item item, @NotNull Player player) {
        // 强制刷新客户端防止错误渲染的虚方块
        if (!item.isNull()) LevelUtil.resendAroundBlocks(this);
        // 防止点击过快
        if (!InventoryUtil.ensurePlayerSafeForCustomInv(player)) return true;
        if (item.isNull()) { // 空手交互
            if (!anvilItem.isNull()) {
                if (itemEntity == null) {
                    this.scheduleUpdate();
                    return false;
                } else {
                    level.dropItem(itemEntity, anvilItem);
                    anvilItem = AIR_ITEM;
                    this.scheduleUpdate();
                    return true;
                }
            }
        } else {
            if (anvilItem.isNull()) {
                anvilItem = item.clone();
                anvilItem.setCount(1);
                item.setCount(item.getCount() - 1);
                if (player.getInventory().getItemInHand().equals(item, true, true)) {
                    player.getInventory().setItemInHand(item);
                } else {
                    player.getInventory().remove(anvilItem);
                }
                this.scheduleUpdate();
                return true;
            } else {
                var recipe = Server.getInstance().getCraftingManager().matchModProcessRecipe("forging", List.of(anvilItem));
                if (recipe != null) {
                    anvilItem = recipe.getResult();
                    level.addParticle(new LavaParticle(this.add(0.5, 1, 0.5)));
                    this.scheduleUpdate();
                    return true;
                }
                return item instanceof TechDawnHardness;
            }
        }
        return false;
    }

    /**
     * 在锻造砧方块实体的更新逻辑中我们只写刷新渲染的逻辑，然后减少tick次数以提高性能
     */
    @Override
    public boolean onUpdate() {
        if (this.closed) {
            return false;
        }
        if (anvilItem.isNull()) {
            anvilItem = AIR_ITEM;
        }
        if (anvilItem.getId() == 0) {
            if (itemEntity != null) {
                itemEntity.close();
                itemEntity = null;
            }
        } else {
            if (itemEntity == null) {
                itemEntity = DisplayItemEntity.createAndDisplay(this.add(0.5, 0.7, 0.5), anvilItem);
            } else if (!itemEntity.getItem().equals(anvilItem, true, true)) {
                itemEntity.close();
                itemEntity = DisplayItemEntity.createAndDisplay(this.add(0.5, 0.7, 0.5), anvilItem);
            }
        }
        return false;
    }

    @Override
    public void onBreak() {
        if (!anvilItem.isNull()) {
            level.dropItem(this.add(0.5, 0.7, 0.5), anvilItem);
        }
        if (itemEntity != null) {
            itemEntity.close();
        }
        super.onBreak();
    }
}
