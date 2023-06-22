package cn.powernukkitx.techdawn.block.plant;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLog;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Component;
import cn.nukkit.block.customblock.data.Materials;
import cn.nukkit.block.customblock.data.Permutation;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.blockproperty.IntBlockProperty;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemGlassBottle;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3f;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.PlaySoundPacket;
import cn.nukkit.utils.BlockColor;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.item.bottle.SapGlassBottle;
import cn.powernukkitx.techdawn.util.InventoryUtil;
import cn.powernukkitx.techdawn.util.ItemUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

import static cn.powernukkitx.techdawn.util.CustomDefUtil.fromRotation;

@AutoRegister(CustomBlock.class)
public class RubberLogBlock extends BlockLog implements CustomBlock {
    public static final IntBlockProperty STATUS = new IntBlockProperty("status", false, 4);
    public static final BlockProperties PROPERTIES = new BlockProperties(CommonBlockProperties.PILLAR_AXIS, STATUS);

    @SuppressWarnings("unused")
    public RubberLogBlock() {
        super(0);
    }

    @SuppressWarnings("unused")
    public RubberLogBlock(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return CustomBlock.super.getName();
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:rubber_log";
    }

    public boolean isNature() {
        return (getPropertyValue(STATUS) & 0b01) != 0;
    }

    public void setNature(boolean isNature) {
        if (isNature) {
            setPropertyValue(STATUS, getPropertyValue(STATUS) | 0b01);
        } else {
            setPropertyValue(STATUS, getPropertyValue(STATUS) & 0b10);
        }
    }

    public boolean isSapping() {
        return (getPropertyValue(STATUS) & 0b10) != 0;
    }

    public void setSapping(boolean isSapping) {
        if (isSapping) {
            setPropertyValue(STATUS, getPropertyValue(STATUS) | 0b10);
        } else {
            setPropertyValue(STATUS, getPropertyValue(STATUS) & 0b01);
        }
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        ItemUtil.registerFuel("techdawn:rubber_log", 300);
        var normalMaterials = Materials.builder().any(Materials.RenderMethod.OPAQUE, "techdawn-blocks-plant-rubber_log_side")
                .up(Materials.RenderMethod.OPAQUE, "techdawn-blocks-plant-rubber_log_top")
                .down(Materials.RenderMethod.OPAQUE, "techdawn-blocks-plant-rubber_log_top");
        var sappingMaterials = Materials.builder().any(Materials.RenderMethod.OPAQUE, "techdawn-blocks-plant-rubber_log_sap")
                .up(Materials.RenderMethod.OPAQUE, "techdawn-blocks-plant-rubber_log_top")
                .down(Materials.RenderMethod.OPAQUE, "techdawn-blocks-plant-rubber_log_top");
        return CustomBlockDefinition.builder(this, normalMaterials)
                .permutation(new Permutation(Component.builder().materialInstances(normalMaterials)
                        .transformation(fromRotation(new Vector3f(0, 0, 0))).build(),
                        "q.block_property('pillar_axis') == 'y' && q.block_property('status') < 2"))
                .permutation(new Permutation(Component.builder().materialInstances(normalMaterials)
                        .transformation(fromRotation(new Vector3f(0, 90, 90))).build(),
                        "q.block_property('pillar_axis') == 'x' && q.block_property('status') < 2"))
                .permutation(new Permutation(Component.builder().materialInstances(normalMaterials)
                        .transformation(fromRotation(new Vector3f(90, 0, 0))).build(),
                        "q.block_property('pillar_axis') == 'z' && q.block_property('status') < 2"))
                .permutation(new Permutation(Component.builder().materialInstances(sappingMaterials)
                        .transformation(fromRotation(new Vector3f(0, 0, 0))).build(),
                        "q.block_property('pillar_axis') == 'y' && q.block_property('status') >= 2"))
                .permutation(new Permutation(Component.builder().materialInstances(sappingMaterials)
                        .transformation(fromRotation(new Vector3f(0, 90, 90))).build(),
                        "q.block_property('pillar_axis') == 'x' && q.block_property('status') >= 2"))
                .permutation(new Permutation(Component.builder().materialInstances(sappingMaterials)
                        .transformation(fromRotation(new Vector3f(90, 0, 0))).build(),
                        "q.block_property('pillar_axis') == 'z' && q.block_property('status') >= 2"))
                .customBuild(nbt -> {
                    var component = nbt.getCompound("components");
                    component.remove("minecraft:material_instances");
                    component.putCompound("minecraft:fuel", new CompoundTag().putFloat("duration", 300));
                });
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

    public BlockState getStrippedState() {
        return null;
    }

    @Override
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        if (player != null) {
            this.setPropertyValue(CommonBlockProperties.PILLAR_AXIS, face.getAxis());
        }
        System.out.println(getPropertyValue(CommonBlockProperties.PILLAR_AXIS));
        getLevel().setBlock(block, this, true, true);
        return true;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }

    @Override
    public int getBurnChance() {
        return 5;
    }

    @Override
    public int getBurnAbility() {
        return 10;
    }

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public double getResistance() {
        return 2;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_RANDOM) { // Sapping
            if (isNature() && !isSapping() && ThreadLocalRandom.current().nextFloat() < 0.25f) {
                var leafExists = false;
                for (var dy = 0; dy < 8; dy++) {
                    var block = this.level.getBlock(this.getFloorX(), this.getFloorY() + dy, this.getFloorZ());
                    if (block instanceof RubberLeavesBlock) {
                        leafExists = true;
                        break;
                    } else if (block instanceof RubberLogBlock rubberLogBlock && !rubberLogBlock.isNature()) {
                        break;
                    }
                }
                if (leafExists) {
                    setSapping(true);
                    getLevel().setBlock(this, this, true, true);
                } else {
                    return Level.BLOCK_UPDATE_RANDOM;
                }
            } else {
                return Level.BLOCK_UPDATE_RANDOM;
            }
        }
        return Level.BLOCK_UPDATE_NORMAL;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(@NotNull Item item, Player player) {
        if (isSapping() && item instanceof ItemGlassBottle) {
            if (player != null && !InventoryUtil.ensurePlayerSafeForCustomInv(player)) {
                return false;
            }
            {
                var pk = new PlaySoundPacket();
                pk.name = "techdawn.sap_extract";
                pk.volume = 1;
                pk.pitch = 1;
                pk.x = this.getFloorX();
                pk.y = this.getFloorY();
                pk.z = this.getFloorZ();
                this.level.addChunkPacket(this.getChunkX(), this.getChunkZ(), pk);
            }
            setSapping(false);
            getLevel().setBlock(this, this, true, true);
            item.setCount(item.getCount() - 1);
            if (player != null) {
                player.getInventory().addItem(new SapGlassBottle());
            } else {
                level.dropItem(this.add(0.5, 0.5, 0.5), new SapGlassBottle());
            }
            return true;
        }
        return super.onActivate(item, player);
    }
}
