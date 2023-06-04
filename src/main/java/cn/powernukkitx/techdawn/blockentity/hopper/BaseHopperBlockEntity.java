package cn.powernukkitx.techdawn.blockentity.hopper;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityHopper;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.fakeInv.CustomInventory;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.hopper.BaseHopperBlock;
import cn.powernukkitx.techdawn.util.InventoryUtil;
import cn.powernukkitx.techdawn.util.UIManger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("#getName")
public class BaseHopperBlockEntity extends BlockEntityHopper {
    protected final UIManger uiManger = new UIManger(this::generateUI, this::updateUI);

    public BaseHopperBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Contract(pure = true, value = "-> new")
    @Override
    protected SimpleAxisAlignedBB generatePickupArea() {
        return new SimpleAxisAlignedBB(this.x, this.y, this.z, this.x + 1, this.y + 2, this.z + 1);
    }

    @Override
    protected void checkDisabled() {
        if (getBlock() instanceof BaseHopperBlock blockHopper) {
            setDisabled(!(blockHopper).isEnabled());
        }
    }

    @Override
    protected boolean checkBlockStateValid(@NotNull BlockState levelBlockState) {
        return levelBlockState.getBlock() instanceof BaseHopperBlock;
    }

    @Override
    public int getCooldownTick() {
        return 24;
    }

    @Override
    public boolean isBlockEntityValid() {
        return super.getBlock() instanceof BaseHopperBlock;
    }

    @Override
    public String getName() {
        return "TechDawn_BaseHopperBlockEntity";
    }

    @NotNull
    public CustomInventory generateUI() {
        var customInv = new cn.powernukkitx.fakeInv.CustomInventory(InventoryType.HOPPER, "ui.techdawn.base_hopper");
        for (var i = 0; i < 5; i++) {
            int finalI = i;
            customInv.setItem(i, inventory.getItem(i), (item, inventoryTransactionEvent) -> {
                // TODO: 2022/12/20 阻止潜在的多人刷物品
                inventory.setItem(finalI, InventoryUtil.getSlotTransactionResult(customInv, inventoryTransactionEvent));
            });
        }
        customInv.setDefaultItemHandler((item, inventoryTransactionEvent) -> inventoryTransactionEvent.setCancelled());
        return customInv;
    }

    public void updateUI(@NotNull CustomInventory customInv, boolean immediately) {
        for (var i = 0; i < 5; i++) {
            int finalI = i;
            customInv.setItem(i, inventory.getItem(i), (item, inventoryTransactionEvent) -> {
                // TODO: 2022/12/20 阻止潜在的多人刷物品
                inventory.setItem(finalI, InventoryUtil.getSlotTransactionResult(customInv, inventoryTransactionEvent));
            });
        }
        customInv.sendContents(customInv.getViewers());
    }

    @NotNull
    public CustomInventory getDisplayInventory() {
        return uiManger.open();
    }

    @Override
    public boolean pushItemsIntoMinecart() {
        var r = super.pushItemsIntoMinecart();
        this.uiManger.update(true);
        return r;
    }

    @Override
    public boolean pushItems() {
        var r = super.pushItems();
        this.uiManger.update(true);
        return r;
    }

    @Override
    public boolean pullItemsFromMinecart() {
        var r = super.pullItemsFromMinecart();
        this.uiManger.update(true);
        return r;
    }

    @Override
    public boolean pullItems(InventoryHolder hopperHolder, Position hopperPos) {
        var r = super.pullItems(hopperHolder, hopperPos);
        this.uiManger.update(true);
        return r;
    }

    @Override
    public boolean pickupItems(InventoryHolder hopperHolder, Position hopperPos, AxisAlignedBB pickupArea) {
        var r = super.pickupItems(hopperHolder, hopperPos, pickupArea);
        this.uiManger.update(true);
        return r;
    }
}
