package cn.powernukkitx.techdawn.block.machine.recipe;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockEntityHolder;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Materials;
import cn.nukkit.block.customblock.data.Permutation;
import cn.nukkit.block.customblock.data.Permutations;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.BooleanBlockProperty;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.blockentity.recipe.BaseElectricFurnaceBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@AutoRegister(CustomBlock.class)
public class BaseElectricFurnaceBlock extends BlockSolidMeta implements Faceable, CustomBlock, BlockEntityHolder<BaseElectricFurnaceBlockEntity> {
    public static final BooleanBlockProperty WORKING_PROPERTY = new BooleanBlockProperty("working", false);
    public static final BlockProperties PROPERTIES = new BlockProperties(CommonBlockProperties.DIRECTION, WORKING_PROPERTY);

    public BaseElectricFurnaceBlock() {
        this(0);
    }

    public BaseElectricFurnaceBlock(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return CustomBlock.super.getName();
    }

    @Override
    public String getNamespaceId() {
        return "techdawn:base_electric_furnace";
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        var workingMaterial = new CompoundTag("minecraft:material_instances").putCompound("materials", Materials.builder()
                .up(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-basic_electric_machine_top")
                .south(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-basic_electric_furnace_on")
                .any(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-basic_machine")
                .build()).putCompound("mappings", new CompoundTag());
        var waitingMaterial = new CompoundTag("minecraft:material_instances").putCompound("materials", Materials.builder()
                .up(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-basic_electric_machine_top")
                .south(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-basic_electric_furnace_off")
                .any(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-basic_machine")
                .build()).putCompound("mappings", new CompoundTag());
        return CustomBlockDefinition.builder(this, Materials.builder())
                .permutations(new Permutations(
                        Permutation.builder().condition("q.block_property('direction') == 0 && q.block_property('working') == 0")
                                .collision_box_enabled(false)
                                .selection_box_enabled(false)
                                .components(List.of(
                                        waitingMaterial
                                )),
                        Permutation.builder().condition("q.block_property('direction') == 1 && q.block_property('working') == 0")
                                .collision_box_enabled(false)
                                .selection_box_enabled(false)
                                .components(List.of(
                                        new CompoundTag("minecraft:rotation")
                                                .putFloat("x", 0)
                                                .putFloat("y", 270)
                                                .putFloat("z", 0),
                                        waitingMaterial
                                )),
                        Permutation.builder().condition("q.block_property('direction') == 2 && q.block_property('working') == 0")
                                .collision_box_enabled(false)
                                .selection_box_enabled(false)
                                .components(List.of(
                                        new CompoundTag("minecraft:rotation")
                                                .putFloat("x", 0)
                                                .putFloat("y", 180)
                                                .putFloat("z", 0),
                                        waitingMaterial
                                )),
                        Permutation.builder().condition("q.block_property('direction') == 3 && q.block_property('working') == 0")
                                .collision_box_enabled(false)
                                .selection_box_enabled(false)
                                .components(List.of(
                                        new CompoundTag("minecraft:rotation")
                                                .putFloat("x", 0)
                                                .putFloat("y", 90)
                                                .putFloat("z", 0),
                                        waitingMaterial
                                )),
                        Permutation.builder().condition("q.block_property('direction') == 0 && q.block_property('working') == 1")
                                .collision_box_enabled(false)
                                .selection_box_enabled(false)
                                .components(List.of(
                                        workingMaterial
                                )),
                        Permutation.builder().condition("q.block_property('direction') == 1 && q.block_property('working') == 1")
                                .collision_box_enabled(false)
                                .selection_box_enabled(false)
                                .components(List.of(
                                        new CompoundTag("minecraft:rotation")
                                                .putFloat("x", 0)
                                                .putFloat("y", 270)
                                                .putFloat("z", 0),
                                        workingMaterial
                                )),
                        Permutation.builder().condition("q.block_property('direction') == 2 && q.block_property('working') == 1")
                                .collision_box_enabled(false)
                                .selection_box_enabled(false)
                                .components(List.of(
                                        new CompoundTag("minecraft:rotation")
                                                .putFloat("x", 0)
                                                .putFloat("y", 180)
                                                .putFloat("z", 0),
                                        workingMaterial
                                )),
                        Permutation.builder().condition("q.block_property('direction') == 3 && q.block_property('working') == 1")
                                .collision_box_enabled(false)
                                .selection_box_enabled(false)
                                .components(List.of(
                                        new CompoundTag("minecraft:rotation")
                                                .putFloat("x", 0)
                                                .putFloat("y", 90)
                                                .putFloat("z", 0),
                                        workingMaterial
                                ))
                        ))
                .customBuild(nbt -> nbt.getCompound("components").remove("minecraft:material_instances"));
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
    public BlockColor getColor() {
        return BlockColor.IRON_BLOCK_COLOR;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(@NotNull Item item, @Nullable Player player) {
        if (player != null) {
            player.addWindow(getOrCreateBlockEntity().getDisplayInventory());
            return true;
        }
        return false;
    }

    @NotNull
    @Override
    public Class<? extends BaseElectricFurnaceBlockEntity> getBlockEntityClass() {
        return BaseElectricFurnaceBlockEntity.class;
    }

    @NotNull
    @Override
    public String getBlockEntityType() {
        return "TechDawn_BaseElectricFurnaceBlock";
    }

    @Override
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        this.setPropertyValue(CommonBlockProperties.DIRECTION, player != null ? player.getDirection().getOpposite() : BlockFace.NORTH);
        return BlockEntityHolder.setBlockAndCreateEntity(this) != null;
    }

    @Override
    public BlockFace getBlockFace() {
        return getPropertyValue(CommonBlockProperties.DIRECTION);
    }

    @Override
    public void setBlockFace(BlockFace face) {
        setPropertyValue(CommonBlockProperties.DIRECTION, face);
    }

    public boolean getWorkingProperty() {
        return getPropertyValue(WORKING_PROPERTY);
    }

    public void setWorkingProperty(boolean working) {
        var same = getWorkingProperty() == working;
        setPropertyValue(WORKING_PROPERTY, working);
        if (!same) {
            level.setBlock(this, this, true, true);
        }
    }
}
