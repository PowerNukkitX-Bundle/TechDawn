package cn.powernukkitx.techdawn.blockentity.anvil;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.inventory.ItemTag;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.ItemBreakParticle;
import cn.nukkit.level.particle.LavaParticle;
import cn.nukkit.level.particle.SmokeParticle;
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
import java.util.concurrent.ThreadLocalRandom;

import static cn.nukkit.inventory.BaseInventory.AIR_ITEM;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("TechDawn_BaseAnvilBlock")
public class BaseAnvilBlockEntity extends BlockEntity implements TechDawnHardness {
    @NotNull
    protected Item anvilItem = AIR_ITEM;
    @Nullable
    protected DisplayItemEntity itemEntity = null;
    protected int coolDownTick = 0;
    protected boolean isBroken = false;

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

    public BaseAnvilBlock getBlock() {
        return (BaseAnvilBlock) super.getBlock();
    }

    @Override
    public int getHardnessTier() {
        return getBlock().getHardnessTier();
    }

    @Override
    public boolean isProcessorBlock() {
        return true;
    }

    public boolean isBroken() {
        return isBroken;
    }

    public boolean onActive(@NotNull Item item, @NotNull Player player) {
        // 强制刷新客户端防止错误渲染的虚方块
        if (!item.isNull()) LevelUtil.resendAroundBlocks(this);
        // 防止点击过快
        if (!InventoryUtil.ensurePlayerSafeForCustomInv(player)) return true;
        if (!item.isNull()) {
            if (anvilItem.isNull()) {
                anvilItem = item.clone();
                anvilItem.setCount(1);
                item.setCount(item.getCount() - 1);
                if (player.getInventory().getItemInHand().equals(item, true, true)) {
                    player.getInventory().setItemInHand(item);
                } else {
                    player.getInventory().remove(anvilItem);
                }
                this.level.addSound(this.add(0.5, 1.5, 0.5), Sound.RANDOM_POP);
                this.scheduleUpdate();
                return true;
            } else if (coolDownTick <= 0) {
                var recipe = Server.getInstance().getCraftingManager().matchModProcessRecipe("forging", List.of(anvilItem));
                if (recipe != null) {
                    var handItem = player.getInventory().getItemInHand();
                    var offhandItem = player.getOffhandInventory().getItem(0);
                    boolean canProcess = false;
                    int dh = 1;
                    if (handItem instanceof TechDawnHardness hardnessHammer && hardnessHammer.isProcessorItem()
                            && ItemTag.getTagSet(handItem.getNamespaceId()).contains("is_hammer")) {
                        if (hardnessHammer.getHardnessTier() - this.getHardnessTier() > 10) {
                            this.level.addSound(this.add(0.5, 0.5, 0.5), Sound.RANDOM_ANVIL_BREAK);
                            this.isBroken = true;
                            this.level.useBreakOn(this, handItem, player, true);
                            return true;
                        }
                        var inputItemHardnessTier = TechDawnHardness.tryGetHardnessTier(anvilItem);
                        if (inputItemHardnessTier != -1) {
                            dh = TechDawnHardness.calcRecipeHardnessDamage(inputItemHardnessTier, hardnessHammer.getHardnessTier());
                        }
                        if (handItem.getMaxDurability() - handItem.getDamage() >= dh) {
                            handItem.setDamage(handItem.getDamage() + dh);
                            canProcess = true;
                        } else {
                            // TODO 跟踪上游bug: 无法显示自定义物品破坏粒子
                            level.addParticle(new ItemBreakParticle(this.add(0.5, 1.5, 0.5), handItem));
                            level.addSound(this.add(0.5, 1.5, 0.5), Sound.RANDOM_BREAK);
                            handItem = AIR_ITEM;
                        }
                        player.getInventory().setItemInHand(handItem);
                    }
                    if (!canProcess && offhandItem instanceof TechDawnHardness hardnessHammer && hardnessHammer.isProcessorItem()
                            && ItemTag.getTagSet(offhandItem.getNamespaceId()).contains("is_hammer")) {
                        if (hardnessHammer.getHardnessTier() - this.getHardnessTier() > 10) {
                            this.level.addSound(this.add(0.5, 0.5, 0.5), Sound.RANDOM_ANVIL_BREAK);
                            this.isBroken = true;
                            this.level.useBreakOn(this, offhandItem, player, true);
                            return true;
                        }
                        var inputItemHardnessTier = TechDawnHardness.tryGetHardnessTier(anvilItem);
                        if (inputItemHardnessTier != -1) {
                            dh = TechDawnHardness.calcRecipeHardnessDamage(inputItemHardnessTier, hardnessHammer.getHardnessTier());
                        }
                        if (offhandItem.getMaxDurability() - offhandItem.getDamage() >= dh) {
                            offhandItem.setDamage(offhandItem.getDamage() + dh);
                            canProcess = true;
                        } else {
                            level.addParticle(new ItemBreakParticle(this.add(0.5, 1.5, 0.5), offhandItem));
                            level.addSound(this.add(0.5, 1.5, 0.5), Sound.RANDOM_BREAK);
                            offhandItem = AIR_ITEM;
                        }
                        player.getOffhandInventory().setItem(0, offhandItem);
                    }
                    if (canProcess) {
                        anvilItem = recipe.getResult();
                        level.addParticle(new LavaParticle(this.add(0.5, 1.1, 0.5)));
                        level.addSound(this.add(0.5, 1.5, 0.5), Sound.RANDOM_ANVIL_USE);
                        coolDownTick = dh * 2 + 2;
                        this.scheduleUpdate();
                        return true;
                    }
                }
            }
        }
        // 尝试取出物品
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
        if (--coolDownTick > 0) {
            var random = ThreadLocalRandom.current();
            if (random.nextBoolean()) {
                level.addParticle(new SmokeParticle(this.add(random.nextDouble(0.1, 0.9), 1, random.nextDouble(0.1, 0.9))));
            }
            return true;
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
