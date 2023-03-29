package cn.powernukkitx.techdawn.blockentity.battery;

import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.energy.EnergyHolder;
import cn.nukkit.energy.EnergyType;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.fakeInv.CustomInventory;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.blockentity.MachineBlockEntity;
import cn.powernukkitx.techdawn.energy.EnergyNetworkManager;
import cn.powernukkitx.techdawn.energy.RF;
import cn.powernukkitx.techdawn.item.icon.ChargeIconItem;
import cn.powernukkitx.techdawn.util.BlockFaceIterator;
import org.jetbrains.annotations.NotNull;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("TechDawn_BaseBatteryBoxBlock")
public class BaseBatteryBoxBlockEntity extends MachineBlockEntity implements EnergyHolder {
    private final BlockFaceIterator blockFaceIterator = new BlockFaceIterator();

    public BaseBatteryBoxBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @NotNull
    @Override
    public String getName() {
        return "TechDawn_BaseBatteryBoxBlock";
    }

    @Override
    public boolean isBlockEntityValid() {
        return getBlock().getId() == Block.CUSTOM_BLOCK_ID_MAP.get("techdawn:base_battery_box");
    }

    @Override
    public boolean canAcceptInput(EnergyType energyType) {
        return energyType.canConvertTo(RF.getInstance());
    }

    @Override
    public boolean canAcceptInput(EnergyType energyType, BlockFace face) {
        return energyType.canConvertTo(RF.getInstance());
    }

    @Override
    public boolean canProvideOutput(EnergyType energyType) {
        return energyType.canConvertTo(RF.getInstance());
    }

    @Override
    public boolean canProvideOutput(EnergyType energyType, BlockFace face) {
        return energyType.canConvertTo(RF.getInstance());
    }

    @Override
    public double getMaxStorage() {
        return 10000;
    }

    public double getOutputPerTick() {
        return 50;
    }

    protected String getUITitle() {
        return "ui.techdawn.base_battery_box";
    }

    @NotNull
    public CustomInventory generateUI() {
        var displayInventory = new CustomInventory(InventoryType.CHEST, getUITitle());
        displayInventory.setItem(13, ChargeIconItem.ofRF(getStoredEnergy(), getMaxStorage()));
        displayInventory.setDefaultItemHandler(((item, event) -> event.setCancelled()));
        return displayInventory;
    }

    public void updateUI(@NotNull cn.powernukkitx.fakeInv.CustomInventory inventory, boolean immediately) {
        inventory.setItem(13, ChargeIconItem.ofRF(getStoredEnergy(), getMaxStorage()));
        inventory.sendSlot(13, inventory.getViewers());
    }

    @Override
    public boolean onUpdate() {
        if (this.closed) {
            return false;
        }
        // 向外提供能量
        EnergyNetworkManager.outputEnergyAt(this, getOutputPerTick(), blockFaceIterator);
        return super.onUpdate();
    }
}
