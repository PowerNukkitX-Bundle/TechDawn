package cn.powernukkitx.techdawn.block.plant;

import cn.nukkit.Player;
import cn.nukkit.block.*;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Materials;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.BooleanBlockProperty;
import cn.nukkit.event.level.StructureGrowEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.ListChunkManager;
import cn.nukkit.level.particle.BoneMealParticle;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.util.InventoryUtil;
import cn.powernukkitx.techdawn.worldgen.object.ObjectRubberTree;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

@AutoRegister(CustomBlock.class)
public class RubberSaplingBlock extends BlockFlowable implements CustomBlock {
    public static final BooleanBlockProperty AGED = new BooleanBlockProperty("aged", false);
    public static final BlockProperties PROPERTIES = new BlockProperties(AGED);

    @SuppressWarnings("unused")
    public RubberSaplingBlock() {
        this(0);
    }

    public RubberSaplingBlock(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return CustomBlock.super.getName();
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:rubber_sapling_block";
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        return CustomBlockDefinition
                .builder(this, Materials.builder().any(Materials.RenderMethod.ALPHA_TEST, "techdawn-items-misc-rubber_sapling"))
                .geometry("geometry.techdawn.sapling")
                .selectionBox(new Vector3f(-6, 0, -6), new Vector3f(12, 12, 12))
                .collisionBox(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0))
                .breakTime(0)
                .build();
    }

    @Override
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face, double fx, double fy, double fz, Player player) {
        if (BlockFlower.isSupportValid(down())) {
            this.getLevel().setBlock(block, this, true, true);
            return true;
        }

        return false;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(@NotNull Item item, Player player) {
        if (player != null && !InventoryUtil.ensurePlayerSafeForCustomInv(player)) {
            return false;
        }
        if (item.isFertilizer()) { // BoneMeal and so on
            if (player != null && !player.isCreative()) {
                item.count--;
            }

            this.level.addParticle(new BoneMealParticle(this));
            if (ThreadLocalRandom.current().nextFloat() >= 0.45) {
                return true;
            }

            this.grow();

            return true;
        }
        return false;
    }

    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (!BlockFlower.isSupportValid(down())) {
                this.getLevel().useBreakOn(this);
                return Level.BLOCK_UPDATE_NORMAL;
            }
        } else if (type == Level.BLOCK_UPDATE_RANDOM) { //Growth
            if (getLevel().getFullLight(add(0, 1, 0)) >= BlockCrops.MINIMUM_LIGHT_LEVEL) {
                if (isAged()) {
                    this.grow();
                } else {
                    setAged(true);
                    this.getLevel().setBlock(this, this, true);
                    return Level.BLOCK_UPDATE_RANDOM;
                }
            } else {
                return Level.BLOCK_UPDATE_RANDOM;
            }
        }
        return Level.BLOCK_UPDATE_NORMAL;
    }

    public boolean isAged() {
        return getPropertyValue(AGED);
    }

    public void setAged(boolean aged) {
        setPropertyValue(AGED, aged);
    }

    @Override
    public int getId() {
        return CustomBlock.super.getId();
    }

    private void grow() {
        ListChunkManager chunkManager = new ListChunkManager(this.level);
        Vector3 vector3 = new Vector3(this.x, this.y - 1, this.z);
        ObjectRubberTree.growTree(chunkManager, this.getFloorX(), this.getFloorY(), this.getFloorZ(), new NukkitRandom());
        StructureGrowEvent ev = new StructureGrowEvent(this, chunkManager.getBlocks());
        this.level.getServer().getPluginManager().callEvent(ev);
        if (ev.isCancelled()) {
            return;
        }
        if (this.level.getBlock(vector3).getId() == BlockID.DIRT_WITH_ROOTS) {
            this.level.setBlock(vector3, Block.get(BlockID.DIRT));
        }
        for (Block block : ev.getBlockList()) {
            this.level.setBlock(block, block);
        }
    }

    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public double getHardness() {
        return 0.00001;
    }

    @Override
    public double getResistance() {
        return 0.00001;
    }
}
