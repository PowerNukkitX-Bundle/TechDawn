package cn.powernukkitx.techdawn.item.misc;

import cn.nukkit.item.customitem.CustomItem;
import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustom;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.data.TechDawnHardness;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class ScreenMesh extends ItemCustom implements TechDawnHardness {
    public ScreenMesh() {
        super("techdawn:screen_mesh", null, "techdawn-items-misc-screen_mesh");
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

    @Override
    public int getHardnessTier() {
        return 1;
    }

    public String getTags() {
        return "screen_mesh";
    }
}
