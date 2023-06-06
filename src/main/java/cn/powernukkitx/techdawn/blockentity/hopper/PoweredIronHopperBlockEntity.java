package cn.powernukkitx.techdawn.blockentity.hopper;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockComposter;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityHopper;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.event.block.HopperSearchItemEvent;
import cn.nukkit.event.inventory.InventoryMoveItemEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.RecipeInventoryHolder;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.*;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.hopper.PoweredIronHopperBlock;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("#getName")
public class PoweredIronHopperBlockEntity extends BaseHopperBlockEntity {
    private final cn.nukkit.math.BlockVector3 temporalVector = new BlockVector3();
    private final AxisAlignedBB pickupArea;

    public PoweredIronHopperBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.pickupArea = generatePickupArea();
    }

    @Override
    protected boolean checkBlockStateValid(@NotNull BlockState levelBlockState) {
        return levelBlockState.getBlock() instanceof PoweredIronHopperBlock;
    }

    @Contract(pure = true, value = "-> new")
    @Override
    protected SimpleAxisAlignedBB generatePickupArea() {
        return new SimpleAxisAlignedBB(this.x, this.y - 1, this.z, this.x + 1, this.y, this.z + 1);
    }

    @Override
    public InventoryHolder getMinecartInvPickupFrom() {
        return super.getMinecartInvPushTo();
    }

    @Override
    public void setMinecartInvPickupFrom(InventoryHolder minecartInvPickupFrom) {
        super.setMinecartInvPushTo(minecartInvPickupFrom);
    }

    @Override
    public InventoryHolder getMinecartInvPushTo() {
        return super.getMinecartInvPickupFrom();
    }

    @Override
    public void setMinecartInvPushTo(InventoryHolder minecartInvPushTo) {
        super.setMinecartInvPickupFrom(minecartInvPushTo);
    }

    @Override
    public boolean onUpdate() {
        if (this.closed) {
            return false;
        }

        if (isOnTransferCooldown()) {
            this.transferCooldown--;
            return true;
        }

        if (isDisabled()) {
            return false;
        }

        Block blockSide = this.getSide(BlockFace.DOWN).getTickCachedLevelBlock();
        BlockEntity blockEntity = this.level.getBlockEntity(temporalVector.setComponentsAdding(this, BlockFace.DOWN));

        boolean changed = pushItems() || pushItemsIntoMinecart();

        HopperSearchItemEvent event = new HopperSearchItemEvent(this, false);
        this.server.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            if (blockEntity instanceof InventoryHolder || blockSide instanceof BlockComposter) {
                changed = pullItems(this, this) || changed;
            } else {
                changed = pullItemsFromMinecart() || pickupItems(this, this, pickupArea) || changed;
            }
        }

        if (changed) {
            this.setTransferCooldown(this.getCooldownTick());
            setDirty();
        }

        uiManger.update();

        return true;
    }

    protected boolean _pullItems(@NotNull InventoryHolder hopperHolder, Position hopperPos) {
        var hopperInv = hopperHolder.getInventory();

        if (hopperInv.isFull())
            return false;

        Block blockSide = hopperPos.getSide(BlockFace.DOWN).getTickCachedLevelBlock();
        BlockEntity blockEntity = hopperPos.level.getBlockEntity(new Vector3().setComponentsAdding(hopperPos, BlockFace.DOWN));

        if (blockEntity instanceof InventoryHolder) {
            if (blockEntity instanceof BlockEntityHopper) {
                if (blockSide.getPropertyValue(CommonBlockProperties.FACING_DIRECTION) == BlockFace.UP)
                    return false;
            }

            Inventory inv = blockEntity instanceof RecipeInventoryHolder recipeInventoryHolder ? recipeInventoryHolder.getProductView() : ((InventoryHolder) blockEntity).getInventory();

            for (int i = 0; i < inv.getSize(); i++) {
                Item item = inv.getItem(i);

                if (!item.isNull()) {
                    Item itemToAdd = item.clone();
                    itemToAdd.count = 1;

                    if (!hopperInv.canAddItem(itemToAdd))
                        continue;

                    InventoryMoveItemEvent ev = new InventoryMoveItemEvent(inv, hopperInv, hopperHolder, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                    Server.getInstance().getPluginManager().callEvent(ev);

                    if (ev.isCancelled())
                        continue;

                    Item[] items = hopperInv.addItem(itemToAdd);

                    if (items.length >= 1)
                        continue;

                    item.count--;

                    inv.setItem(i, item);
                    return true;
                }
            }
        } else if (blockSide instanceof BlockComposter blockComposter) {
            if (blockComposter.isFull()) {
                //检查是否能输入
                if (!hopperInv.canAddItem(blockComposter.getOutPutItem()))
                    return false;

                //Will call BlockComposterEmptyEvent
                var item = blockComposter.empty();

                if (item == null || item.isNull())
                    return false;

                Item itemToAdd = item.clone();
                itemToAdd.count = 1;

                Item[] items = hopperInv.addItem(itemToAdd);

                return items.length < 1;
            }
        }
        return false;
    }

    @Override
    public boolean pullItems(@NotNull InventoryHolder hopperHolder, Position hopperPos) {
        var r = _pullItems(hopperHolder, hopperPos);
        uiManger.update(true);
        return r;
    }

    @Override
    public int getCooldownTick() {
        return 8;
    }

    @Override
    public boolean isBlockEntityValid() {
        return super.getBlock() instanceof PoweredIronHopperBlock;
    }

    @Override
    protected String getUITitle() {
        return "ui.techdawn.powered_iron_hopper";
    }

    @Override
    public String getName() {
        return "TechDawn_PoweredIronHopperBlockEntity";
    }
}
