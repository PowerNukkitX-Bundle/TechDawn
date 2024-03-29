package cn.powernukkitx.techdawn.blockentity;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.energy.EnergyHolder;
import cn.nukkit.energy.EnergyType;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.fakeInv.CustomInventory;
import cn.powernukkitx.techdawn.energy.EnergyNetworkManager;
import cn.powernukkitx.techdawn.energy.RF;
import cn.powernukkitx.techdawn.util.UIManger;
import com.google.common.util.concurrent.AtomicDouble;
import org.jetbrains.annotations.NotNull;

public abstract class MachineBlockEntity extends BlockEntity implements EnergyHolder {
    private AtomicDouble storedEnergy;

    protected final UIManger uiManger = new UIManger(this::generateUI, this::updateUI);

    public MachineBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        initEnergy();
        loadNBT();
        if (putIntoEnergyNetwork()) {
            EnergyNetworkManager.putMachineAt(getFloorX(), getFloorY(), getFloorZ(), getLevel(), this);
        }
    }

    protected void initEnergy() {
        this.storedEnergy = new AtomicDouble(0);
    }

    @NotNull
    public abstract String getName();

    protected boolean putIntoEnergyNetwork() {
        return true;
    }

    @NotNull
    @Override
    public EnergyType getStoredEnergyType() {
        return RF.getInstance();
    }

    @Override
    public void inputInto(EnergyType energyType, double v) {
        if (energyType.canConvertTo(getStoredEnergyType())) {
            setStoredEnergy(getStoredEnergy() + energyType.convertTo(getStoredEnergyType(), v));
        }
    }

    @Override
    public void outputFrom(EnergyType energyType, double v) {
        if (energyType.canConvertTo(getStoredEnergyType())) {
            setStoredEnergy(getStoredEnergy() - energyType.convertTo(getStoredEnergyType(), v));
        }
    }

    @Override
    public double getStoredEnergy() {
        return storedEnergy.get();
    }

    public void setStoredEnergy(double energy) {
        if (Math.abs(energy - storedEnergy.get()) > 0.0001) {
            this.storedEnergy.set(Math.max(Math.min(energy, getMaxStorage()), 0));
            this.setDirty();
            this.scheduleUpdate();
        }
    }

    @NotNull
    public abstract CustomInventory generateUI();

    public abstract void updateUI(@NotNull CustomInventory inventory, boolean immediately);

    @NotNull
    public CustomInventory getDisplayInventory() {
        return uiManger.open();
    }

    @Override
    protected void initBlockEntity() {
        if (!this.namedTag.contains("StoredEnergy")) {
            this.namedTag.putDouble("StoredEnergy", 0);
        }
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
        // 更新UI
        uiManger.update();
        // 如果机器没电，通常应该什么也不干，所以直接停止方块更新即可
        return getStoredEnergy() != 0;
    }

    @Override
    public void close() {
        if (!this.closed) {
            if (putIntoEnergyNetwork()) {
                EnergyNetworkManager.removeMachineAt(getFloorX(), getFloorY(), getFloorZ(), getLevel());
            }
        }
        super.close();
    }

    /**
     * 尝试立即刷新UI，此操作将仅会更新重要数据，如物品栏内的物品等（防止刷物品）；但不重要信息可能不会被更新。
     */
    public void requestUIUpdateImmediately() {
        uiManger.update(true);
    }
}
