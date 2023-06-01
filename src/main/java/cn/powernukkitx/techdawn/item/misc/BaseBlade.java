package cn.powernukkitx.techdawn.item.misc;

import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustom;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.data.TechDawnHardness;
import org.jetbrains.annotations.NotNull;

public abstract class BaseBlade extends ItemCustom implements TechDawnHardness {
    public BaseBlade(@NotNull String id) {
        super(id, null, "techdawn-items-misc-" + id.substring(id.lastIndexOf(":") + 1));
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
