package cn.powernukkitx.techdawn.blockentity.battery;

import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.energy.EnergyHolder;
import cn.nukkit.energy.EnergyType;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.energy.RF;
import cn.powernukkitx.techdawn.item.icon.ChargeIconItem;
import com.google.common.util.concurrent.AtomicDouble;
import me.iwareq.fakeinventories.CustomInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("TechDawn_BaseBatteryBoxBlock")
public class BaseBatteryBoxBlockEntity extends BlockEntity implements EnergyHolder {
    private final AtomicDouble storedEnergy;
    private final Set<CustomInventory> displayInventories = new HashSet<>();

    public BaseBatteryBoxBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.storedEnergy = new AtomicDouble(1234);
        loadNBT();
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
    public void inputInto(EnergyType energyType, double v) {
        if (energyType.canConvertTo(RF.getInstance())) {
            setStoredEnergy(getStoredEnergy() + energyType.convertTo(RF.getInstance(), v));
        }
    }

    @Override
    public void outputFrom(EnergyType energyType, double v) {
        if (energyType.canConvertTo(RF.getInstance())) {
            setStoredEnergy(getStoredEnergy() - energyType.convertTo(RF.getInstance(), v));
        }
    }

    @Nullable
    @Override
    public EnergyType getStoredEnergyType() {
        return RF.getInstance();
    }

    @Override
    public double getMaxStorage() {
        return 10000;
    }

    @Override
    public double getStoredEnergy() {
        return storedEnergy.get();
//         return this.namedTag.getDouble("StoredEnergy");
    }

    public void setStoredEnergy(double rf) {
        if (Math.abs(rf - storedEnergy.get()) > 0.0001) {
            this.storedEnergy.set(Math.max(Math.min(rf, getMaxStorage()), 0));
            this.setDirty();
            this.scheduleUpdate();
        }
//        this.namedTag.putDouble("StoredEnergy", rf);
    }

    @NotNull
    public CustomInventory getDisplayInventory() {
        var displayInventory = new CustomInventory(InventoryType.CHEST, "ui.techdawn.base_battery_box");
        displayInventory.setItem(13, ChargeIconItem.ofRF(getStoredEnergy(), getMaxStorage()));
        displayInventory.setDefaultItemHandler(((item, event) -> event.setCancelled()));
        displayInventories.add(displayInventory);
        return displayInventory;
    }

    @Override
    protected void initBlockEntity() {
        if (!this.namedTag.contains("StoredEnergy")) {
            this.namedTag.putDouble("StoredEnergy", 0);
        }
        super.initBlockEntity();
    }

    @Override
    public void saveNBT() {
        this.namedTag.putDouble("StoredEnergy", this.storedEnergy.get());
        super.saveNBT();
    }

    @Override
    public void loadNBT() {
        if (this.namedTag.contains("StoredEnergy")) {
            var tmp = this.namedTag.getDouble("StoredEnergy");
            setStoredEnergy(tmp);
        }
        super.loadNBT();
    }

    @Override
    public boolean onUpdate() {
        for (Iterator<CustomInventory> iterator = displayInventories.iterator(); iterator.hasNext(); ) {
            var each = iterator.next();
            if (each.getViewers().size() == 0) {
                iterator.remove();
                continue;
            }
            each.setItem(13, ChargeIconItem.ofRF(getStoredEnergy(), getMaxStorage()));
            each.sendSlot(13, each.getViewers());
        }
        return false;
    }
}
