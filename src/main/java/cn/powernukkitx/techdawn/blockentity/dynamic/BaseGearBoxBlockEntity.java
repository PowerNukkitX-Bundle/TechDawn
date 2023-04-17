package cn.powernukkitx.techdawn.blockentity.dynamic;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.energy.EnergyHolder;
import cn.nukkit.energy.EnergyType;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.machine.dynamic.BaseGearBoxBlock;
import cn.powernukkitx.techdawn.energy.DynamicManager;
import cn.powernukkitx.techdawn.energy.Rotation;
import com.google.common.util.concurrent.AtomicDouble;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.concurrent.atomic.AtomicInteger;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("TechDawn_BaseGearBoxBlockEntity")
public class BaseGearBoxBlockEntity extends BlockEntity implements EnergyHolder, TechDawnDynamicHandler, TransposableBlockEntity {
    private static final AtomicInteger SPACE_TIME = new AtomicInteger(0);
    protected final AtomicDouble storedEnergy;
    private int hingeBlockIdCache;
    private final int spaceTime;
    private double transferRate;
    private int lastFullUpdateTick;

    public BaseGearBoxBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        storedEnergy = new AtomicDouble(0);
        spaceTime = SPACE_TIME.getAndIncrement() % 4;
        lastFullUpdateTick = Server.getInstance().getTick();
        transferRate = Double.NaN;
    }

    @Override
    @NotNull
    public String getName() {
        return "TechDawn_BaseGearBoxBlockEntity";
    }

    @Override
    public boolean isBlockEntityValid() {
        return super.getBlock() instanceof BaseGearBoxBlock;
    }

    @Override
    public BaseGearBoxBlock getBlock() {
        return (BaseGearBoxBlock) super.getBlock();
    }

    @Override
    public boolean canAcceptInput(EnergyType energyType) {
        return energyType.canConvertTo(Rotation.getInstance());
    }

    @Override
    public boolean canAcceptInput(EnergyType energyType, BlockFace face) {
        if (!canAcceptInput(energyType)) {
            return false;
        }
        return isRotatableFace(getBlock(), face);
    }

    @Override
    public boolean canProvideOutput(EnergyType energyType) {
        return energyType.canConvertTo(Rotation.getInstance());
    }

    @Override
    public boolean canProvideOutput(EnergyType energyType, BlockFace face) {
        if (!canProvideOutput(energyType)) {
            return false;
        }
        return isRotatableFace(getBlock(), face);
    }

    protected boolean isRotatableFace(@NotNull BaseGearBoxBlock gearBoxBlock, @NotNull BlockFace face) {
        return switch (gearBoxBlock.getBlockFace()) {
            case NORTH, SOUTH:
                yield gearBoxBlock.isTransposed() ? face == BlockFace.UP || face == BlockFace.DOWN : face == BlockFace.EAST || face == BlockFace.WEST;
            case EAST, WEST:
                yield gearBoxBlock.isTransposed() ? face == BlockFace.UP || face == BlockFace.DOWN : face == BlockFace.NORTH || face == BlockFace.SOUTH;
            case UP, DOWN:
                yield gearBoxBlock.isTransposed() ? face == BlockFace.NORTH || face == BlockFace.SOUTH : face == BlockFace.EAST || face == BlockFace.WEST;
        };
    }

    public @NotNull EnumSet<BlockFace> getOutputFaces() {
        var gearBoxBlock = getBlock();
        return switch (gearBoxBlock.getBlockFace()) {
            case NORTH, SOUTH:
                yield gearBoxBlock.isTransposed() ? EnumSet.of(BlockFace.UP, BlockFace.DOWN) : EnumSet.of(BlockFace.EAST, BlockFace.WEST);
            case EAST, WEST:
                yield gearBoxBlock.isTransposed() ? EnumSet.of(BlockFace.UP, BlockFace.DOWN) : EnumSet.of(BlockFace.NORTH, BlockFace.SOUTH);
            case UP, DOWN:
                yield gearBoxBlock.isTransposed() ? EnumSet.of(BlockFace.NORTH, BlockFace.SOUTH) : EnumSet.of(BlockFace.EAST, BlockFace.WEST);
        };
    }

    @Override
    public void inputInto(EnergyType energyType, double amount) {
        var finalAmount = amount;
        if (energyType != Rotation.getInstance()) {
            finalAmount = energyType.convertTo(Rotation.getInstance(), amount);
        }
        setStoredEnergy(finalAmount + getStoredEnergy());
    }

    @Override
    public void outputFrom(EnergyType energyType, double amount) {
        var finalAmount = amount;
        if (energyType != Rotation.getInstance()) {
            finalAmount = energyType.convertTo(Rotation.getInstance(), amount);
        }
        setStoredEnergy(getStoredEnergy() - finalAmount);
    }

    @Nullable
    @Override
    public EnergyType getStoredEnergyType() {
        return Rotation.getInstance();
    }

    @Override
    public double getMaxStorage() {
        return getTransferRate() * 4;
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

    @Override
    public boolean isTransposed() {
        return this.level.getBlockStateAt((int) this.x, (int) this.y, (int) this.z).getPropertyValue(BaseGearBoxBlock.TRANSPOSED);
    }

    public double getTransferRate() {
        if (Double.isNaN(transferRate)) {
            return transferRate = this.namedTag.getDouble("transfer_rate");
        }
        return transferRate;
    }

    protected boolean isHingeBlock(int blockId, int dx, int dy, int dz, boolean transposed) {
        if (this.hingeBlockIdCache == 0) {
            if (this.namedTag.contains("hinge_type")) {
                var namespaceId = this.namedTag.getString("hinge_type");
                this.hingeBlockIdCache = Block.CUSTOM_BLOCK_ID_MAP.get(namespaceId);
            }
        }
        if (this.hingeBlockIdCache != blockId) return false;
        var blockState = this.level.getBlockStateAt((int) this.x + dx, (int) this.y + dy, (int) this.z + dz);
        return blockState.getPropertyValue(BaseGearBoxBlock.TRANSPOSED) == transposed;
    }

    @Override
    public double handleDynamicTransferring(double amount, BlockFace direction) {
        for (var each : this.getOutputFaces()) {
            var tmpBlockEntity = this.level.getBlockEntity(new Position((int) this.x + each.getXOffset(), (int) this.y + each.getYOffset(), (int) this.z + each.getZOffset()));
            if (tmpBlockEntity instanceof EnergyHolder energyHolder && energyHolder.canAcceptInput(Rotation.getInstance(), each.getOpposite())) {
                var availableStorageSpace = energyHolder.getMaxStorage() - energyHolder.getStoredEnergy();
                var energyToTransfer = Math.min(amount, availableStorageSpace);
                energyHolder.inputInto(Rotation.getInstance(), energyToTransfer);
                amount -= energyToTransfer;
            }
        }
        return amount;
    }

    @Override
    public boolean onUpdate() {
        var currentTick = Server.getInstance().getTick();
        if (((currentTick + spaceTime) & 3) == 0) { // 错开更新时间避免峰值卡顿
            var energyStorage = getStoredEnergy();
            if (energyStorage > getTransferRate()) {
                setStoredEnergy(energyStorage - getTransferRate());
                energyStorage = getTransferRate();
            } else {
                setStoredEnergy(0);
            }
            var face = getBlock().getBlockFace();
            var xOffset = face.getXOffset();
            var yOffset = face.getYOffset();
            var zOffset = face.getZOffset();
            var hingePositions = new ArrayList<Position>();
            TechDawnDynamicHandler targetGearBox = null;
            var transposed = getBlock().isTransposed();
            for (int i = 0; i < 8; i++) {
                var pos = new Position((int) this.x + xOffset, (int) this.y + yOffset, (int) this.z + zOffset);
                var blockId = this.level.getBlockIdAt((int) this.x + xOffset, (int) this.y + yOffset, (int) this.z + zOffset);
                if (!hingePositions.isEmpty() && this.level.getBlockEntity(pos) instanceof TechDawnDynamicHandler energyHolder) {
                    if (energyHolder instanceof TransposableBlockEntity transposableBlockEntity && (this.isTransposed() != transposableBlockEntity.isTransposed())) {
                        break;
                    }
                    targetGearBox = energyHolder;
                    break;
                }
                if (!isHingeBlock(blockId, xOffset, yOffset, zOffset, transposed)) {
                    break;
                }
                hingePositions.add(pos);
                xOffset += face.getXOffset();
                yOffset += face.getYOffset();
                zOffset += face.getZOffset();
            }
            DynamicManager.requestUpdateHinge(hingePositions, this.level.getName(), energyStorage > 0);
            // 检测铰链链接的目标并输出
            if (targetGearBox != null && energyStorage > 0) {
                targetGearBox.handleDynamicTransferring(energyStorage, face);
            }
            // 记录最后一次有能量变化的更新
            if (energyStorage > 0)
                lastFullUpdateTick = currentTick;
        }
        return getStoredEnergy() > 0 || currentTick - lastFullUpdateTick < 4;
    }
}
