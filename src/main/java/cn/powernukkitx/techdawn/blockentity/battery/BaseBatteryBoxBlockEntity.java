package cn.powernukkitx.techdawn.blockentity.battery;

import cn.nukkit.Server;
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
import cn.powernukkitx.techdawn.energy.EnergyNetworkManager;
import cn.powernukkitx.techdawn.energy.RF;
import cn.powernukkitx.techdawn.item.icon.ChargeIconItem;
import com.google.common.util.concurrent.AtomicDouble;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import me.iwareq.fakeinventories.CustomInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("TechDawn_BaseBatteryBoxBlock")
public class BaseBatteryBoxBlockEntity extends BlockEntity implements EnergyHolder {
    private final AtomicDouble storedEnergy;
    private final Object2IntMap<CustomInventory> displayInventories = new Object2IntOpenHashMap<>();
    private final Deque<BlockFace> outputFaces = new ArrayDeque<>(List.of(BlockFace.values()));

    public BaseBatteryBoxBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.storedEnergy = new AtomicDouble(0);
        loadNBT();
        EnergyNetworkManager.putMachineAt(getFloorX(), getFloorY(), getFloorZ(), getLevel());
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

    public double getOutputPerTick() {
        return 40;
    }

    @NotNull
    public CustomInventory getDisplayInventory() {
        var displayInventory = new CustomInventory(InventoryType.CHEST, "ui.techdawn.base_battery_box");
        displayInventory.setItem(13, ChargeIconItem.ofRF(getStoredEnergy(), getMaxStorage()));
        displayInventory.setDefaultItemHandler(((item, event) -> event.setCancelled()));
        displayInventories.put(displayInventory, Server.getInstance().getTick());
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
        if (this.closed) {
            return false;
        }
        // debug
        setStoredEnergy(getStoredEnergy() + 0.1);
        // 向外提供能量
        var energyOutput = Math.min(getOutputPerTick(), getStoredEnergy());
        faceLoop: for (var face : outputFaces) {
            var network = EnergyNetworkManager.findAt(getFloorX() + face.getXOffset(), getFloorY() + face.getYOffset(), getFloorZ() + face.getZOffset(), getLevel());
            if (network == null) continue;
            var machines = network.getSortedMachines();
            // 虽然自己给自己点看起来有点愚蠢，但是这是最高效的实现
            for (var machine : machines) {
                var vacancy = Math.max(machine.getMaxStorage() - machine.getStoredEnergy(), 0);
                if (vacancy < 0.0001)
                    continue;
                var energyToOutput = Math.min(energyOutput, vacancy);
                energyOutput -= energyToOutput;
                machine.inputInto(RF.getInstance(), energyToOutput);
                this.outputFrom(RF.getInstance(), energyToOutput);
                if (energyOutput < 0.0001)
                    break faceLoop;
            }
        }
        // 这样可以让电在六个面可能的情况下均分
        outputFaces.add(outputFaces.poll());
        // 更新UI
        var currentTick = Server.getInstance().getTick();
        for (var iterator = displayInventories.object2IntEntrySet().iterator(); iterator.hasNext(); ) {
            var entry = iterator.next();
            var each = entry.getKey();
            var createTick = entry.getIntValue();
            if (currentTick - createTick > 3 && each.getViewers().size() == 0) {
                iterator.remove();
                continue;
            }
            each.setItem(13, ChargeIconItem.ofRF(getStoredEnergy(), getMaxStorage()));
            each.sendSlot(13, each.getViewers());
        }
        return true;
    }

    @Override
    public void close() {
        if (!this.closed) {
            EnergyNetworkManager.removeMachineAt(getFloorX(), getFloorY(), getFloorZ(), getLevel());
        }
        super.close();
    }
}
