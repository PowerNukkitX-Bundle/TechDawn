package cn.powernukkitx.techdawn.block.plant;

import cn.nukkit.Server;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLeaves;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Materials;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.value.WoodType;
import cn.nukkit.event.block.LeavesDecayEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.Hash;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.item.plant.RubberSapling;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomBlock.class)
public class RubberLeavesBlock extends BlockLeaves implements CustomBlock {
    public static final BlockProperties PROPERTIES = new BlockProperties(BlockLeaves.PERSISTENT, BlockLeaves.UPDATE);

    private static final BlockFace[] VISIT_ORDER = new BlockFace[]{
            BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.DOWN, BlockFace.UP
    };

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:rubber_leaves";
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        return CustomBlockDefinition.builder(this, Materials.builder()
                .any(Materials.RenderMethod.BLEND, "techdawn-blocks-plant-rubber_leaves")).build();
    }

    @Override
    public String getName() {
        return CustomBlock.super.getName();
    }

    @Override
    public int getId() {
        return CustomBlock.super.getId();
    }

    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_RANDOM) {
            if (isCheckDecay()) {
                if (isPersistent() || findLog(this, 7, null)) {
                    setCheckDecay(false);
                    getLevel().setBlock(this, this, false, false);
                } else {
                    LeavesDecayEvent ev = new LeavesDecayEvent(this);
                    Server.getInstance().getPluginManager().callEvent(ev);
                    if (!ev.isCancelled()) {
                        getLevel().useBreakOn(this);
                    }
                }
                return type;
            }
        } else if (type == Level.BLOCK_UPDATE_NORMAL || type == Level.BLOCK_UPDATE_SCHEDULED) {
            if (!isCheckDecay()) {
                setCheckDecay(true);
                getLevel().setBlock(this, this, false, false);
            }

            // Slowly propagates the need to update instead of peaking down the TPS for huge trees
            for (var side : BlockFace.values()) {
                var other = getSide(side);
                if (other instanceof BlockLeaves otherLeave) {
                    if (!otherLeave.isCheckDecay()) {
                        getLevel().scheduleUpdate(otherLeave, 2);
                    }
                }
            }
            return type;
        }
        return type;
    }

    private Boolean findLog(Block current, int distance, Long2LongMap visited) {
        if (visited == null) {
            visited = new Long2LongOpenHashMap();
            visited.defaultReturnValue(-1);
        }
        if (current instanceof RubberLogBlock) {
            return true;
        }
        if (distance == 0 || !(current instanceof BlockLeaves)) {
            return false;
        }
        long hash = Hash.hashBlock(current);
        if (visited.get(hash) >= distance) {
            return false;
        }
        visited.put(hash, distance);
        for (BlockFace face : VISIT_ORDER) {
            if(findLog(current.getSide(face), distance - 1, visited)) {
                return true;
            }
        }
        return false;
    }

    public WoodType getType() {
        return WoodType.OAK;
    }

    protected boolean canDropApple() {
        return true;
    }

    protected Item getSapling() {
        return new RubberSapling();
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Since("1.4.0.0-PN")
    @PowerNukkitOnly
    @Override
    public boolean canHarvest(Item item) {
        return item.isShears();
    }
}
