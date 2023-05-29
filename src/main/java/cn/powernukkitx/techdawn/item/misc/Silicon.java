package cn.powernukkitx.techdawn.item.misc;

import cn.nukkit.item.customitem.CustomItem;
import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustom;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.data.TechDawnHardness;
import cn.powernukkitx.techdawn.util.ItemUtil;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class Silicon extends ItemCustom implements TechDawnHardness {
    public Silicon() {
        super("techdawn:silicon", null, "techdawn-items-misc-silicon");
    }

    @Override
    public CustomItemDefinition getDefinition() {
        ItemUtil.registerFuel("techdawn:silicon", 2400);
        return CustomItemDefinition.simpleBuilder(this, ItemCreativeCategory.ITEMS)
                .tag(getTags().split(" +"))
                .customBuild(nbt -> {
                    var component = nbt.getCompound("components");
                    component.putCompound("minecraft:fuel", new CompoundTag().putFloat("duration", 0));
                });
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_SILICON;
    }

    public String getTags() {
        return "silicon";
    }
}
