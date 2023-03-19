package cn.powernukkitx.techdawn.item.stick;

import cn.nukkit.item.customitem.CustomItem;
import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustom;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.util.ItemUtil;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class AntisepticWoodStick extends ItemCustom implements CustomItem {
    public AntisepticWoodStick() {
        super("techdawn:antiseptic_wood_stick", null, "techdawn-items-stick-antiseptic_wood_stick");
    }

    @Override
    public CustomItemDefinition getDefinition() {
        ItemUtil.registerFuel("techdawn:antiseptic_wood_stick", 100);
        return CustomItemDefinition.simpleBuilder(this, ItemCreativeCategory.ITEMS)
                .tag(getTags().split(" +"))
                .customBuild(nbt -> {
                    var component = nbt.getCompound("components");
                    component.putCompound("minecraft:fuel", new CompoundTag().putFloat("duration", 100));
                });
    }

    public String getTags() {
        return "antiseptic_wood_stick stick wood";
    }
}
