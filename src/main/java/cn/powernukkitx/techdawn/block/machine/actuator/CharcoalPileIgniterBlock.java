package cn.powernukkitx.techdawn.block.machine.actuator;

import cn.nukkit.Player;
import cn.nukkit.block.BlockEntityHolder;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Component;
import cn.nukkit.block.customblock.data.Materials;
import cn.nukkit.block.customblock.data.Permutation;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.BooleanBlockProperty;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.block.TechDawnWorkableBlock;
import cn.powernukkitx.techdawn.blockentity.actuator.CharcoalPileIgniterBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegister(CustomBlock.class)
public class CharcoalPileIgniterBlock extends BlockSolidMeta implements CustomBlock, BlockEntityHolder<CharcoalPileIgniterBlockEntity>, TechDawnWorkableBlock {
    public static final BooleanBlockProperty WORKING_PROPERTY = new BooleanBlockProperty("working", false);
    public static final BlockProperties PROPERTIES = new BlockProperties(WORKING_PROPERTY);

    @SuppressWarnings("unused")
    public CharcoalPileIgniterBlock() {
        this(0);
    }

    public CharcoalPileIgniterBlock(int meta) {
        super(meta);
    }

    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public String getName() {
        return CustomBlock.super.getName();
    }

    @Override
    @NotNull
    public String getNamespaceId() {
        return "techdawn:charcoal_pile_igniter";
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        var workingMaterial = Materials.builder()
                .up(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-charcoal_pile_igniter_on")
                .any(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-charcoal_pile_igniter_side");
        var waitingMaterial = Materials.builder()
                .up(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-charcoal_pile_igniter_off")
                .any(Materials.RenderMethod.OPAQUE, "techdawn-blocks-machine-charcoal_pile_igniter_side");
        return CustomBlockDefinition.builder(this, Materials.builder())
                .permutations(new Permutation(Component.builder().materialInstances(waitingMaterial).build(),
                                "q.block_property('working') == 0"),
                        new Permutation(Component.builder().materialInstances(workingMaterial).build(),
                                "q.block_property('working') == 1"))
                .customBuild(nbt -> nbt.getCompound("components").remove("minecraft:material_instances"));
    }

    @Override
    public int getId() {
        return CustomBlock.super.getId();
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.ORANGE_BLOCK_COLOR;
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

    @NotNull
    @Override
    public Class<? extends CharcoalPileIgniterBlockEntity> getBlockEntityClass() {
        return CharcoalPileIgniterBlockEntity.class;
    }

    @NotNull
    @Override
    public String getBlockEntityType() {
        return "TechDawn_CharcoalPileIgniterBlock";
    }

    @Override
    public boolean onActivate(@NotNull Item item, @Nullable Player player) {
        if (player != null) {
            if (item.getId() == Item.FLINT_AND_STEEL) {
                var success = getOrCreateBlockEntity().ignite();
                if (!success) return true;
                item.setDamage(item.getDamage() + 1);
                if (item.getDamage() >= item.getMaxDurability()) {
                    item.setCount(0);
                }
            } else {
                return false;
            }
            player.getInventory().setItemInHand(item);
            return true;
        }
        return false;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }
}
