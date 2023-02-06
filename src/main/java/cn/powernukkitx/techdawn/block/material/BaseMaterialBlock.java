package cn.powernukkitx.techdawn.block.material;

import cn.nukkit.block.BlockIron;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.powernukkitx.techdawn.data.TechDawnHardness;
import org.jetbrains.annotations.NotNull;

public abstract class BaseMaterialBlock extends BlockIron implements CustomBlock, TechDawnHardness {
    @Override
    public int getId() {
        return CustomBlock.super.getId();
    }

    @Override
    public String getName() {
        return CustomBlock.class.getName();
    }

    @NotNull
    @Override
    public abstract String getNamespaceId();

    @Override
    public CustomBlockDefinition getDefinition() {
        return CustomBlockDefinition.builder(this, "techdawn-blocks-material-" +
                getNamespaceId().substring(getNamespaceId().lastIndexOf(":") + 1))
                .customBuild(nbt -> {
                    var list = new ListTag<>("block_tags");
                    list.add(new StringTag("", "metal"));
                    list.add(new StringTag("", "iron_pick_diggable"));
                    nbt.getCompound("components").putList(list);
                });
    }
}
