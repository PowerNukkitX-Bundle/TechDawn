package cn.powernukkitx.techdawn.blockentity.actuator;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.block.BlockBricks;
import cn.nukkit.block.BlockLog;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.AngryVillagerParticle;
import cn.nukkit.level.particle.GenericParticle;
import cn.nukkit.level.particle.Particle;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.machine.actuator.CharcoalPileIgniterBlock;
import cn.powernukkitx.techdawn.multi.MultiBlockStruct;
import cn.powernukkitx.techdawn.multi.StructPointMatcher;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

@AutoRegister(BlockEntity.class)
@AutoRegisterData("#getName")
public class CharcoalPileIgniterBlockEntity extends BlockEntity {
    public static MultiBlockStruct CHARCOAL_PILE_STRUCT = null;

    protected int burningTick = 0;

    private static class LogBlockMatcher implements StructPointMatcher {

        @Override
        public boolean matchAt(Block worldBlock) {
            return worldBlock.getId() == 0 || worldBlock instanceof BlockLog;
        }

        @Override
        public boolean canRotate() {
            return false;
        }

        @Override
        public void rotate(BlockFace from, BlockFace to) {

        }

        @Override
        public LogBlockMatcher copy() {
            return this;
        }
    }

    private static class SolidBlockMatcher implements StructPointMatcher {

        @Override
        public boolean matchAt(Block worldBlock) {
            return worldBlock.isFullBlock() && worldBlock.isSolid() && worldBlock.getBurnAbility() == 0 && !worldBlock.isTransparent();
        }

        @Override
        public boolean canRotate() {
            return false;
        }

        @Override
        public void rotate(BlockFace from, BlockFace to) {

        }

        @Override
        public SolidBlockMatcher copy() {
            return this;
        }
    }

    private static class IgniterBlockMatcher implements StructPointMatcher {

        @Override
        public boolean matchAt(Block worldBlock) {
            return worldBlock instanceof CharcoalPileIgniterBlock;
        }

        @Override
        public boolean canRotate() {
            return false;
        }

        @Override
        public void rotate(BlockFace from, BlockFace to) {

        }

        @Override
        public IgniterBlockMatcher copy() {
            return this;
        }
    }

    public static MultiBlockStruct getCharcoalPileStruct() {
        if (CHARCOAL_PILE_STRUCT != null) return CHARCOAL_PILE_STRUCT;
        CHARCOAL_PILE_STRUCT = new MultiBlockStruct(BlockFace.NORTH);
        var bricks = new BlockBricks();
        // 底层全铺砖
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                CHARCOAL_PILE_STRUCT.add(x, 0, z, bricks);
            }
        }
        // 中间
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                for (int y = 1; y <= 3; y++) {
                    // 四个角什么都不放
                    if (x == -3 && z == -3) continue;
                    if (x == -3 && z == 3) continue;
                    if (x == 3 && z == -3) continue;
                    if (x == 3 && z == 3) continue;
                    // 周围边上一圈放SolidBlockMatcher
                    if (x == -3 || x == 3 || z == -3 || z == 3) {
                        CHARCOAL_PILE_STRUCT.add(x, y, z, new SolidBlockMatcher());
                        continue;
                    }
                    // 其余地方放原木
                    CHARCOAL_PILE_STRUCT.add(x, y, z, new LogBlockMatcher());
                }
            }
        }
        // 顶层中间点火器，其他地方SolidBlock
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (x == 0 && z == 0) {
                    CHARCOAL_PILE_STRUCT.add(x, 4, z, new IgniterBlockMatcher());
                } else {
                    CHARCOAL_PILE_STRUCT.add(x, 4, z, new SolidBlockMatcher());
                }
            }
        }
        return CHARCOAL_PILE_STRUCT;
    }

    public CharcoalPileIgniterBlockEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public boolean isBlockEntityValid() {
        return super.getBlock() instanceof CharcoalPileIgniterBlock;
    }

    @Override
    public CharcoalPileIgniterBlock getBlock() {
        return (CharcoalPileIgniterBlock) super.getBlock();
    }

    @NotNull
    @Override
    public String getName() {
        return "TechDawn_CharcoalPileIgniterBlock";
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putInt("burningTick", this.burningTick);
    }

    @Override
    public void loadNBT() {
        super.loadNBT();
        if (this.namedTag.contains("burningTick")) {
            this.burningTick = this.namedTag.getInt("burningTick");
        } else {
            this.burningTick = 0;
        }
    }

    public boolean isStructureValid() {
        return getCharcoalPileStruct().match(this.level, this.getFloorX(), this.getFloorY() - 4, this.getFloorZ());
    }

    public boolean ignite() {
        if (this.burningTick == 0) {
            if (!isStructureValid()) {
                this.level.addParticle(new AngryVillagerParticle(this.add(0.5, 1.2, 0.5)));
            } else {
                this.burningTick = 3000;
                this.level.addSound(this, Sound.FIRE_IGNITE);
                this.setDirty();
                this.scheduleUpdate();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onUpdate() {
        if (this.closed) {
            return false;
        }
        if (this.burningTick > 0) {
            var serverTick = Server.getInstance().getTick();
            this.burningTick--;
            this.getBlock().setWorkingProperty(this.burningTick > 0);
            if ((serverTick & 31) == 0) { // % 32 == 0
                level.addParticle(new GenericParticle(this.add(0.5, 1, 0.5), Particle.TYPE_CAMPFIRE_SMOKE));
            }
            if ((serverTick & 127) == 0) { // % 128 == 0
                if (!isStructureValid()) {
                    this.burningTick = 0;
                    this.getBlock().setWorkingProperty(false);
                    return false;
                }
            }
            if (this.burningTick == 0) {
                this.level.addSound(this, Sound.EXTINGUISH_CANDLE);
                var charcoal = Item.get(Item.COAL, 1, 1);
                var air = new BlockAir();
                for (int dx = -2; dx <= 2; dx++) {
                    for (int dz = -2; dz <= 2; dz++) {
                        for (int dy = 1; dy <= 3; dy++) {
                            var block = this.level.getBlock(this.getFloorX() + dx, this.getFloorY() - dy, this.getFloorZ() + dz);
                            if (block instanceof BlockLog) {
                                charcoal.setCount(ThreadLocalRandom.current().nextInt(1, 3));
                                this.level.dropItem(new Vector3(this.getFloorX() + dx, this.getFloorY() - dy, this.getFloorZ() + dz),
                                        charcoal, Vector3.ZERO);
                                this.level.setBlock(this.getFloorX() + dx, this.getFloorY() - dy, this.getFloorZ() + dz, air, true, false);
                            }
                        }
                    }
                }
                return false;
            }
            return true;
        }
        return false;
    }
}
