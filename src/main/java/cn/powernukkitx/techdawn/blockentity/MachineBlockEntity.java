package cn.powernukkitx.techdawn.blockentity;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.energy.EnergyHolder;
import cn.nukkit.energy.EnergyType;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.energy.EnergyNetworkManager;
import cn.powernukkitx.techdawn.energy.RF;
import cn.powernukkitx.techdawn.util.UIManger;
import com.google.common.util.concurrent.AtomicDouble;
import me.iwareq.fakeinventories.CustomInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MachineBlockEntity extends BlockEntity implements EnergyHolder {
    private final AtomicDouble storedEnergy;

    protected final UIManger uiManger = new UIManger(this::generateUI, this::updateUI);

    public MachineBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.storedEnergy = new AtomicDouble(0);
        loadNBT();
        if (putIntoEnergyNetwork()) {
            EnergyNetworkManager.putMachineAt(getFloorX(), getFloorY(), getFloorZ(), getLevel(), this);
        }
    }

    @NotNull
    public abstract String getName();

    protected boolean putIntoEnergyNetwork() {
        return true;
    }

    @Nullable
    @Override
    public EnergyType getStoredEnergyType() {
        return RF.getInstance();
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

    @Override
    public double getStoredEnergy() {
        return storedEnergy.get();
    }

    public void setStoredEnergy(double rf) {
        if (Math.abs(rf - storedEnergy.get()) > 0.0001) {
            this.storedEnergy.set(Math.max(Math.min(rf, getMaxStorage()), 0));
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

    public void requestUIUpdateImmediately() {
        uiManger.update(true);
    }
}
