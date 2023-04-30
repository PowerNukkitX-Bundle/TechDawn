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
import cn.powernukkitx.techdawn.block.machine.dynamic.BaseVerticalUpSteeringGearBoxBlock;
import cn.powernukkitx.techdawn.energy.DynamicManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("TechDawn_BaseVerticalSteeringGearBoxBlockEntity")
public class BaseVerticalSteeringGearBoxBlockEntity extends BlockEntity implements TechDawnDynamicHandler, TransposableBlockEntity {
    private int hingeBlockIdCache;
    private double transferRate = Double.NaN;
    private Boolean isUp = null;

    public BaseVerticalSteeringGearBoxBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    @NotNull
    public String getName() {
        return "TechDawn_BaseVerticalSteeringGearBoxBlockEntity";
    }

    @Override
    public boolean isBlockEntityValid() {
        return super.getBlock() instanceof BaseVerticalUpSteeringGearBoxBlock;
    }

    @Override
    public BaseVerticalUpSteeringGearBoxBlock getBlock() {
        return (BaseVerticalUpSteeringGearBoxBlock) super.getBlock();
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

    public boolean isUp() {
        if (isUp == null) {
            return isUp = this.namedTag.getBoolean("is_up");
        }
        return isUp;
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
        if (directionFace == block.getBlockFace().getOpposite()) {
            return block.isTransposed() == isTransposed;
        } else if ((directionFace == BlockFace.DOWN && isUp()) || (directionFace == BlockFace.UP && !isUp())) {
            if (block.getBlockFace().getZOffset() != 0) {
                return block.isTransposed() != isTransposed;
            } else {
                return block.isTransposed() == isTransposed;
            }
        }
        return false;
    }

    @Override
    public double handleDynamicTransferring(double amount, BlockFace direction) {
        var block = getBlock();
        var blockFace = block.getBlockFace();
        if (blockFace.getOpposite() == direction) {
            direction = isUp() ? BlockFace.UP : BlockFace.DOWN;
        } else {
            direction = blockFace;
        }
        var xOffset = direction.getXOffset();
        var yOffset = direction.getYOffset();
        var zOffset = direction.getZOffset();
        var hingePositions = new ArrayList<Position>();
        TechDawnDynamicHandler targetGearBox = null;
        amount = Math.min(amount, getTransferRate());
        var transposed = block.isTransposed();
        // 如果方块面向南北，那么上下的转置情况会跟水平的转置相反
        if (blockFace.getZOffset() != 0 && direction.getYOffset() != 0) {
            transposed = !transposed;
        }
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
}
