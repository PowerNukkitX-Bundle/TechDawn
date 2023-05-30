package cn.powernukkitx.techdawn.item.plant;

import cn.nukkit.block.Block;
import cn.nukkit.item.customitem.CustomItem;
import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustom;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.block.plant.RubberSaplingBlock;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class RubberSapling extends ItemCustom {
    public RubberSapling() {
        super("techdawn:rubber_sapling", null, "techdawn-items-misc-rubber_sapling");
        this.block = new RubberSaplingBlock();
    }

    @Override
    public CustomItemDefinition getDefinition() {
        return CustomItemDefinition.simpleBuilder(this, ItemCreativeCategory.NATURE)
                .creativeGroup("itemGroup.name.sapling")
                .tag(getTags().split(" +")).build();
    }

    @NotNull
    public String getTags() {
        return "sapling rubber_sapling produce_rubber";
    }

    @Override
    public Block getBlock() {
        return new RubberSaplingBlock();
    }
}
