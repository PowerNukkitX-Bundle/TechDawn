package cn.powernukkitx.techdawn.item.ingot;

import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustom;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.data.TechDawnHardness;
import org.jetbrains.annotations.NotNull;

public abstract class BaseIngot extends ItemCustom implements TechDawnHardness {
    public BaseIngot(@NotNull String id, @NotNull String textureName) {
        super(id, null, textureName);
    }

    @Override
    public CustomItemDefinition getDefinition() {
        return CustomItemDefinition.simpleBuilder(this, ItemCreativeCategory.ITEMS)
                .tag(getTags().split(" +"))
                .customBuild(nbt -> {
                    var component = nbt.getCompound("components");
                    component.putCompound("minecraft:fuel", new CompoundTag().putFloat("duration", 1));
                });
    }

    @NotNull
    public String getTags() {
        return "";
    }
}
