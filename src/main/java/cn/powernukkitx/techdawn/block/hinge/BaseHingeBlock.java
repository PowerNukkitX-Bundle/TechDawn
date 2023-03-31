package cn.powernukkitx.techdawn.block.hinge;

import cn.nukkit.Player;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.block.customblock.data.Component;
import cn.nukkit.block.customblock.data.Materials;
import cn.nukkit.block.customblock.data.Permutation;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.BooleanBlockProperty;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3f;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.block.TechDawnWorkableBlock;
import cn.powernukkitx.techdawn.util.InventoryUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegister(CustomBlock.class)
public class BaseHingeBlock extends BlockTransparentMeta implements CustomBlock, TechDawnWorkableBlock {
    public static final BooleanBlockProperty TRANSPOSED = new BooleanBlockProperty("transposed", false);
    public static final BooleanBlockProperty WORKING = new BooleanBlockProperty("working", false);
    public static final BlockProperties PROPERTIES = new BlockProperties(CommonBlockProperties.PILLAR_AXIS, WORKING, TRANSPOSED);

    @SuppressWarnings("unused")
    public BaseHingeBlock() {
        super(0);
    }

    @SuppressWarnings("unused")
    public BaseHingeBlock(int meta) {
        super(meta);
    }

    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @NotNull
    @Override
    public String getNamespaceId() {
        return "techdawn:base_hinge";
    }

    @Override
    public CustomBlockDefinition getDefinition() {
        var workingMaterial = Materials.builder().any(Materials.RenderMethod.BLEND, getTextureName() + "-dyn");
        var staticMaterial = Materials.builder().any(Materials.RenderMethod.BLEND, getTextureName() + "_static");
        return CustomBlockDefinition
                .builder(this, staticMaterial)
                .geometry("geometry.techdawn.hinge")
                .selectionBox(new Vector3f(-8f, 2, -2f), new Vector3f(16f, 12f, 4f))
                .collisionBox(new Vector3f(-8f, 2, -2f), new Vector3f(16f, 12f, 4f))
                .permutations(new Permutation(Component.builder().materialInstances(staticMaterial).build(),
                        "q.block_property('working') == false && q.block_property('transposed') == false"),
                        new Permutation(Component.builder().materialInstances(staticMaterial).rotation(new Vector3f(90)).build(),
                                "q.block_property('working') == false && q.block_property('transposed') == true"),
                        new Permutation(Component.builder().materialInstances(workingMaterial).build(),
                                "q.block_property('working') == true && q.block_property('transposed') == false"),
                        new Permutation(Component.builder().materialInstances(workingMaterial).rotation(new Vector3f(90)).build(),
                                "q.block_property('working') == true && q.block_property('transposed') == true"))
                .build();
    }

    public boolean getWorkingProperty() {
        return getPropertyValue(WORKING);
    }

    public void setWorkingProperty(boolean working) {
        var same = getWorkingProperty() == working;
        setPropertyValue(WORKING, working);
        if (!same) {
            level.setBlock(this, this, true, false);
        }
    }

    public String getTextureName() {
        return "techdawn-blocks-hinge-base_hinge";
    }

    @Override
    public String getName() {
        return CustomBlock.super.getName();
    }

    @Override
    public int getId() {
        return CustomBlock.super.getId();
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(@NotNull Item item, @Nullable Player player) {
        if (player != null && InventoryUtil.ensurePlayerSafeForCustomInv(player)) {
            if (!player.isSneaking()) {
                this.setWorkingProperty(!getWorkingProperty());
                System.out.println(this.getWorkingProperty());
            } else {
                this.setPropertyValue(TRANSPOSED, !getPropertyValue(TRANSPOSED));
                level.setBlock(this, this, true, false);
                System.out.println(this.getPropertyValue(TRANSPOSED));
            }
            return true;
        }
        return false;
    }
}
