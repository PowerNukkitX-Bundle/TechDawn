package cn.powernukkitx.techdawn.multi;

import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.BlockVector3;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class MultiBlockStruct {
    private final Map<BlockVector3, StructPointMatcher> structMap;
    private final Map<BlockFace, MultiBlockStruct> transposingCache;
    private final BlockFace facingDirection;

    public MultiBlockStruct(BlockFace facingDirection) {
        if (facingDirection == BlockFace.UP || facingDirection == BlockFace.DOWN)
            throw new IllegalArgumentException("Facing direction cannot be up or down");
        this.structMap = new HashMap<>();
        this.transposingCache = new WeakHashMap<>(6, 0.99f);
        this.facingDirection = facingDirection;
    }

    public BlockFace getFacingDirection() {
        return facingDirection;
    }

    public MultiBlockStruct add(int x, int y, int z, Block block) {
        structMap.put(new BlockVector3(x, y, z), new SingleBlockMatcher(block));
        return this;
    }

    public MultiBlockStruct add(int x, int y, int z, StructPointMatcher matcher) {
        structMap.put(new BlockVector3(x, y, z), matcher);
        return this;
    }

    public MultiBlockStruct transpose(BlockFace newFacingDirection) {
        if (newFacingDirection == this.facingDirection) return this;
        if (newFacingDirection == BlockFace.UP || newFacingDirection == BlockFace.DOWN)
            throw new IllegalArgumentException("Facing direction cannot be up or down");
        return transposingCache.computeIfAbsent(newFacingDirection, face -> {
            var newStruct = new MultiBlockStruct(face);
            // Rotate all squares in the x-z plane, we have no rotate method
            for (var entry : structMap.entrySet()) {
                var pos = entry.getKey();
                var matcher = entry.getValue().copy();
                if (matcher.canRotate()) {
                    matcher.rotate(this.facingDirection, face);
                }
                var newPos = rotate(pos, this.facingDirection, newFacingDirection);
                newStruct.add(newPos.x, newPos.y, newPos.z, matcher);
            }
            return newStruct;
        });
    }

    public boolean match(Position position) {
        return match(position.getLevel(), position.getFloorX(), position.getFloorY(), position.getFloorZ());
    }

    public boolean match(Level level, int centerX, int centerY, int centerZ) {
        for (var entry : structMap.entrySet()) {
            var pos = entry.getKey();
            var matcher = entry.getValue();
            var x = centerX + pos.x;
            var y = centerY + pos.y;
            var z = centerZ + pos.z;
            var blockAt = level.getBlock(x, y, z);
            if (!matcher.matchAt(blockAt)) {
                return false;
            }
        }
        return true;
    }

    @NotNull
    public static BlockVector3 rotate(BlockVector3 pos, BlockFace fromDirection, BlockFace toDirection) {
        var x = pos.getX();
        var y = pos.getY();
        var z = pos.getZ();
        if (fromDirection == toDirection) return pos.clone();
        switch (fromDirection) {
            case NORTH -> {
                switch (toDirection) {
                    case EAST -> {
                        return new BlockVector3(z, y, -x);
                    }
                    case SOUTH -> {
                        return new BlockVector3(-x, y, -z);
                    }
                    case WEST -> {
                        return new BlockVector3(-z, y, x);
                    }
                }
            }
            case EAST -> {
                switch (toDirection) {
                    case SOUTH -> {
                        return new BlockVector3(z, y, -x);
                    }
                    case WEST -> {
                        return new BlockVector3(-x, y, -z);
                    }
                    case NORTH -> {
                        return new BlockVector3(-z, y, x);
                    }
                }
            }
            case SOUTH -> {
                switch (toDirection) {
                    case WEST -> {
                        return new BlockVector3(z, y, -x);
                    }
                    case NORTH -> {
                        return new BlockVector3(-x, y, -z);
                    }
                    case EAST -> {
                        return new BlockVector3(-z, y, x);
                    }
                }
            }
            case WEST -> {
                switch (toDirection) {
                    case NORTH -> {
                        return new BlockVector3(z, y, -x);
                    }
                    case EAST -> {
                        return new BlockVector3(-x, y, -z);
                    }
                    case SOUTH -> {
                        return new BlockVector3(-z, y, x);
                    }
                }
            }
        }
        throw new IllegalArgumentException("Invalid direction");
    }

    public static BlockFace rotate(BlockFace face, BlockFace fromDirection, BlockFace toDirection) {
        if (face == fromDirection) return toDirection;
        if (face == fromDirection.getOpposite()) return toDirection.getOpposite();
        return BlockFace.fromHorizontalAngle(face.getHorizontalAngle() + toDirection.getHorizontalAngle() - fromDirection.getHorizontalAngle());
    }

    @Override
    public String toString() {
        return "MultiBlockStruct{" +
                "structMap=" + structMap +
                ", transposingCache=" + transposingCache +
                ", facingDirection=" + facingDirection +
                '}';
    }
}
