package cn.powernukkitx.techdawn.blockentity.dynamic;

import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.machine.dynamic.BaseGearBoxBlock;
import cn.powernukkitx.techdawn.block.machine.dynamic.BaseTransposingGearBoxBlock;
import cn.powernukkitx.techdawn.energy.DynamicManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("TechDawn_BaseTransposingGearBoxBlockEntity")
public class BaseTransposingGearBoxBlockEntity extends BlockEntity implements TechDawnDynamicHandler, TransposableBlockEntity {
    private int hingeBlockIdCache;
    private double transferRate = Double.NaN;

    public BaseTransposingGearBoxBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    @NotNull
    public String getName() {
        return "TechDawn_BaseTransposingGearBoxBlockEntity";
    }

    @Override
    public boolean isBlockEntityValid() {
        return super.getBlock() instanceof BaseTransposingGearBoxBlock;
    }

    @Override
    public BaseTransposingGearBoxBlock getBlock() {
        return (BaseTransposingGearBoxBlock) super.getBlock();
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
    public boolean isDirectionAcceptable(BlockFace directionFace, boolean isTransposed) {
        var block = getBlock();
        if (directionFace == block.getBlockFace()) {
            return block.isTransposed() != isTransposed;
        } else {
            return block.isTransposed() == isTransposed;
        }
    }

    @Override
    public double handleDynamicTransferring(double amount, BlockFace direction) {
        var xOffset = direction.getXOffset();
        var yOffset = direction.getYOffset();
        var zOffset = direction.getZOffset();
        var hingePositions = new ArrayList<Position>();
        TechDawnDynamicHandler targetGearBox = null;
        amount = Math.min(amount, getTransferRate());
        var block = getBlock();
        var transposed = block.isTransposed();
        if (direction != block.getBlockFace()) transposed = !transposed;
        for (int i = 0; i < 8; i++) {
            var pos = new Position((int) this.x + xOffset, (int) this.y + yOffset, (int) this.z + zOffset);
            var blockId = this.level.getBlockIdAt((int) this.x + xOffset, (int) this.y + yOffset, (int) this.z + zOffset);
            if (!hingePositions.isEmpty() && this.level.getBlockEntity(pos) instanceof TechDawnDynamicHandler dynamicHandler) {
                if (!dynamicHandler.isDirectionAcceptable(direction, transposed)) {
                    break;
                }
                targetGearBox = dynamicHandler;
                break;
            }
            if (!isHingeBlock(blockId, xOffset, yOffset, zOffset, transposed)) {
                break;
            }
            hingePositions.add(pos);
            xOffset += direction.getXOffset();
            yOffset += direction.getYOffset();
            zOffset += direction.getZOffset();
        }
        DynamicManager.requestUpdateHinge(hingePositions, this.level.getName(), amount > 0);
        // 检测铰链链接的目标并输出
        if (targetGearBox != null) {
            targetGearBox.handleDynamicTransferring(amount, direction);
        }
        return amount;
    }

    @Override
    public boolean onUpdate() {
//        var currentTick = Server.getInstance().getTick();
//        var directions = new ArrayList<BlockFace>(2);
//        if (((currentTick - lastFullUpdateTickFront) & 3) == 0) { // 更新前方正在转动的铰链到停止
//            directions.add(getBlock().getBlockFace());
//        }
//        if (((currentTick - lastFullUpdateTickBack) & 3) == 0) { // 更新后方正在转动的铰链到停止
//            directions.add(getBlock().getBlockFace().getOpposite());
//        }
//        for (var direction : directions) {
//            var xOffset = direction.getXOffset();
//            var yOffset = direction.getYOffset();
//            var zOffset = direction.getZOffset();
//            var hingePositions = new ArrayList<Position>();
//            var transposed = getBlock().isTransposed();
//            for (int i = 0; i < 8; i++) {
//                var pos = new Position((int) this.x + xOffset, (int) this.y + yOffset, (int) this.z + zOffset);
//                var blockId = this.level.getBlockIdAt((int) this.x + xOffset, (int) this.y + yOffset, (int) this.z + zOffset);
//                if (!isHingeBlock(blockId, xOffset, yOffset, zOffset, transposed)) {
//                    break;
//                }
//                hingePositions.add(pos);
//                xOffset += direction.getXOffset();
//                yOffset += direction.getYOffset();
//                zOffset += direction.getZOffset();
//            }
//            DynamicManager.requestUpdateHinge(hingePositions, this.level.getName(), false);
//        }
//        return currentTick - lastFullUpdateTickFront <= 4 || currentTick - lastFullUpdateTickBack <= 4;
        return false;
    }
}
