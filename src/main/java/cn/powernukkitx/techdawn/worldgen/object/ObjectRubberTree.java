package cn.powernukkitx.techdawn.worldgen.object;

import cn.nukkit.block.Block;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.math.NukkitRandom;
import cn.powernukkitx.techdawn.block.plant.RubberLeavesBlock;
import cn.powernukkitx.techdawn.block.plant.RubberLogBlock;
import org.jetbrains.annotations.NotNull;

public class ObjectRubberTree {
    private final BlockState TrunkBlock;
    private final BlockState TrunkBlockSapping;
    private final BlockState LeafBlock;
    private final int saplingId;
    private final int leafId;
    private final int trunkId;

    public ObjectRubberTree() {
        var trunk = new RubberLogBlock();
        trunk.setNature(true);
        TrunkBlock = new RubberLogBlock().getCurrentState();
        var trunkSapping = new RubberLogBlock();
        trunkSapping.setNature(true);
        trunkSapping.setSapping(true);
        TrunkBlockSapping = trunkSapping.getCurrentState();
        LeafBlock = new RubberLeavesBlock().getCurrentState();
        this.saplingId = BlockState.of("techdawn:rubber_sapling_block").getBlockId();
        this.leafId = LeafBlock.getBlockId();
        this.trunkId = TrunkBlock.getBlockId();
    }

    public static void growTree(ChunkManager level, int x, int y, int z, NukkitRandom random) {
        var tree = new ObjectRubberTree();
        if (tree.canPlaceObject(level, x, y, z)) {
            tree.placeObject(level, x, y, z, random);
        }
    }

    protected boolean overridable(int id) {
        if (id == this.saplingId || id == this.trunkId || id == this.leafId) return true;
        return switch (id) {
            case Block.AIR, Block.SAPLING, Block.LOG, Block.LEAVES, Block.SNOW_LAYER, Block.LOG2, Block.LEAVES2 -> true;
            default -> false;
        };
    }

    public int getTreeHeight() {
        return 7;
    }

    public boolean canPlaceObject(ChunkManager level, int x, int y, int z) {
        int radiusToCheck = 0;
        for (int yy = 0; yy < this.getTreeHeight() + 3; ++yy) {
            if (yy == 1 || yy == this.getTreeHeight()) {
                ++radiusToCheck;
            }
            for (int xx = -radiusToCheck; xx < (radiusToCheck + 1); ++xx) {
                for (int zz = -radiusToCheck; zz < (radiusToCheck + 1); ++zz) {
                    if (!this.overridable(level.getBlockIdAt(x + xx, y + yy, z + zz))) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void placeObject(ChunkManager level, int x, int y, int z, NukkitRandom random) {
        this.placeTrunk(level, x, y, z, random, this.getTreeHeight() - 1);

        for (int yy = y - 3 + this.getTreeHeight(); yy <= y + this.getTreeHeight(); ++yy) {
            double yOff = yy - (y + this.getTreeHeight());
            int mid = (int) (1 - yOff / 2);
            for (int xx = x - mid; xx <= x + mid; ++xx) {
                int xOff = Math.abs(xx - x);
                for (int zz = z - mid; zz <= z + mid; ++zz) {
                    int zOff = Math.abs(zz - z);
                    if (xOff == mid && zOff == mid && (yOff == 0 || random.nextBoundedInt(2) == 0)) {
                        continue;
                    }
                    var id = level.getBlockIdAt(xx, yy, zz);
                    if (!Block.isSolid(id) && this.trunkId != id) {
                        level.setBlockStateAt(xx, yy, zz, this.LeafBlock);
                    }
                }
            }
        }
    }

    protected void placeTrunk(@NotNull ChunkManager level, int x, int y, int z, NukkitRandom random, int trunkHeight) {
        // The base dirt block
        level.setBlockAt(x, y - 1, z, Block.DIRT);

        for (int yy = 0; yy < trunkHeight; ++yy) {
            int blockId = level.getBlockIdAt(x, y + yy, z);
            if (this.overridable(blockId)) {
                if (random.nextFloat() < 0.75) {
                    level.setBlockStateAt(x, y + yy, z, this.TrunkBlock);
                } else {
                    level.setBlockStateAt(x, y + yy, z, this.TrunkBlockSapping);
                }
            }
        }
    }
}
