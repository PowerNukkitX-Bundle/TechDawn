package cn.powernukkitx.techdawn.item.dust;

import cn.nukkit.item.customitem.CustomItem;
import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.util.ItemUtil;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class CoalDust extends BaseDust {
    public CoalDust() {
        super("techdawn:coal_dust");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_COAL;
    }

    @NotNull
    @Override
    public String getTags() {
        return "coal_dust dust";
    }

    @Override
    public CustomItemDefinition getDefinition() {
        ItemUtil.registerFuel("techdawn:coal_dust", 2000);
        return CustomItemDefinition.simpleBuilder(this, ItemCreativeCategory.ITEMS)
                .tag(getTags().split(" +"))
                .customBuild(nbt -> {
                    var component = nbt.getCompound("components");
                    component.putCompound("minecraft:fuel", new CompoundTag().putFloat("duration", 2000));
                });
    }
}
