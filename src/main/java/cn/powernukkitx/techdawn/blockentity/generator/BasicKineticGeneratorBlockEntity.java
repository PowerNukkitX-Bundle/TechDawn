package cn.powernukkitx.techdawn.blockentity.generator;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.energy.EnergyType;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.machine.generator.BasicKineticGeneratorBlock;
import cn.powernukkitx.techdawn.blockentity.MachineBlockEntity;
import cn.powernukkitx.techdawn.blockentity.TechDawnGenerator;
import cn.powernukkitx.techdawn.blockentity.dynamic.TechDawnDynamicHandler;
import cn.powernukkitx.techdawn.energy.EnergyNetworkManager;
import cn.powernukkitx.techdawn.energy.RF;
import cn.powernukkitx.techdawn.energy.Rotation;
import cn.powernukkitx.techdawn.item.icon.ChargeIconItem;
import cn.powernukkitx.techdawn.util.BlockFaceIterator;
import com.google.common.util.concurrent.AtomicDouble;
import org.jetbrains.annotations.NotNull;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("TechDawn_BasicKineticGeneratorBlock")
public class BasicKineticGeneratorBlockEntity extends MachineBlockEntity implements TechDawnGenerator, TechDawnDynamicHandler {
    private final BlockFaceIterator blockFaceIterator;
    protected AtomicDouble storedRotationEnergy;
    protected double maxRotationEnergy;

    public BasicKineticGeneratorBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.blockFaceIterator = new BlockFaceIterator(getBlock().getBlockFace(), getBlock().getBlockFace().getOpposite());
    }

    @Override
    protected void initEnergy() {
        super.initEnergy();
        this.storedRotationEnergy = new AtomicDouble(0);
        this.maxRotationEnergy = 36;
    }

    @Override
    public boolean isBlockEntityValid() {
        return super.getBlock() instanceof BasicKineticGeneratorBlock;
    }

    @NotNull
    @Override
    public String getName() {
        return "TechDawn_BasicKineticGeneratorBlock";
    }

    @Override
    public boolean canAcceptInput(EnergyType energyType) {
        return energyType.canConvertTo(Rotation.getInstance());
    }

    @Override
    public boolean canAcceptInput(EnergyType energyType, BlockFace face) {
        return canAcceptInput(energyType) && face == getBlock().getBlockFace().getOpposite();
    }

    @Override
    public boolean canProvideOutput(EnergyType energyType) {
        return energyType.canConvertTo(RF.getInstance());
    }

    @Override
    public void inputInto(EnergyType energyType, double v) {
        if (energyType.canConvertTo(Rotation.getInstance())) {
            var result = storedRotationEnergy.addAndGet(v);
            if (result > maxRotationEnergy) {
                storedRotationEnergy.set(maxRotationEnergy);
            }
        } else {
            super.inputInto(energyType, v);
        }
    }

    @Override
    public BasicKineticGeneratorBlock getBlock() {
        return (BasicKineticGeneratorBlock) super.getBlock();
    }

    @Override
    public boolean canProvideOutput(EnergyType energyType, BlockFace face) {
        return canProvideOutput(energyType) && face != getBlock().getBlockFace();
    }

    @Override
    public double getMaxStorage() {
        return 16000;
    }

    @Override
    public double getGeneratingSpeed() {
        return Math.min(18, Math.max(getStoredEnergy(), Math.min(storedRotationEnergy.get(), 9) * 2));
    }

    protected String getUITitle() {
        return "ui.techdawn.basic_kinetic_generator";
    }

    @NotNull
    @Override
    public cn.powernukkitx.fakeInv.CustomInventory generateUI() {
        var customInv = new cn.powernukkitx.fakeInv.CustomInventory(InventoryType.FURNACE, getUITitle());
        var iconItem = ChargeIconItem.ofRF(getStoredEnergy(), getMaxStorage());
        customInv.setItem(0, iconItem);
        customInv.setItem(1, iconItem);
        customInv.setItem(2, iconItem);
        customInv.setDefaultItemHandler((item, inventoryTransactionEvent) -> inventoryTransactionEvent.setCancelled());
        return customInv;
    }

    @Override
    public void updateUI(@NotNull cn.powernukkitx.fakeInv.CustomInventory inventory, boolean immediately) {
        var iconItem = ChargeIconItem.ofRF(getStoredEnergy(), getMaxStorage());
        inventory.setItem(0, iconItem);
        inventory.setItem(1, iconItem);
        inventory.setItem(2, iconItem);
    }

    @Override
    protected void initBlockEntity() {
        super.initBlockEntity();
    }

    @Override
    public void loadNBT() {
        super.loadNBT();
        // load storedRotationEnergy
        if (!this.namedTag.contains("storedRotationEnergy")) {
            storedRotationEnergy.set(0);
        } else {
            storedRotationEnergy.set(this.namedTag.getDouble("storedRotationEnergy"));
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putDouble("storedRotationEnergy", storedRotationEnergy.get());
    }

    @Override
    public boolean onUpdate() {
        if (this.closed) {
            return false;
        }
        var result = super.onUpdate();
        var generatedEnergy = 0d;
        // 动能发电
        if (storedRotationEnergy.get() > 0) {
            var usingRotation = Math.min((getMaxStorage() - getStoredEnergy()) * 0.5, Math.min(storedRotationEnergy.get(), 9));
            if (usingRotation > 0) {
                generatedEnergy = usingRotation * 2;
                storedRotationEnergy.addAndGet(-usingRotation);
                setStoredEnergy(getStoredEnergy() + generatedEnergy);
            }
        }
        // 设置工作状态
        var block = getBlock();
        if (block.getWorkingProperty() != (generatedEnergy > 0)) {
            block.setWorkingProperty(generatedEnergy > 0);
            result = true;
        }
        // 向外提供能量
        var outputEnergy = getGeneratingSpeed();
        if (outputEnergy > 0) {
            EnergyNetworkManager.outputEnergyAt(this, outputEnergy, blockFaceIterator);
        }
        return result;
    }

    @Override
    public double handleDynamicTransferring(double amount, BlockFace directionFace) {
        if (directionFace == getBlock().getBlockFace()) {
            setDirty();
            scheduleUpdate();
            var result = storedRotationEnergy.addAndGet(amount);
            if (result > maxRotationEnergy) {
                storedRotationEnergy.set(maxRotationEnergy);
                return result - maxRotationEnergy;
            }
            return 0;
        }
        return amount;
    }

    @Override
    public boolean isDirectionAcceptable(BlockFace directionFace, boolean isTransposed) {
        return !isTransposed && directionFace == getBlock().getBlockFace();
    }
}
