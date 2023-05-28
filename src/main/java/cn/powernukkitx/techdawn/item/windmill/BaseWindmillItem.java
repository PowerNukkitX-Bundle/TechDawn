package cn.powernukkitx.techdawn.item.windmill;

import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustom;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;
import org.jetbrains.annotations.NotNull;

public abstract class BaseWindmillItem extends ItemCustom {
    public BaseWindmillItem(@NotNull String id) {
        super(id, null, "techdawn-items-misc-" + id.substring(id.lastIndexOf(":") + 1)
                .replace("_item", ""));
    }

    @Override
    public CustomItemDefinition getDefinition() {
        return CustomItemDefinition.simpleBuilder(this, ItemCreativeCategory.ITEMS)
                .tag(getTags().split(" +")).build();
    }

    @NotNull
    public String getTags() {
        return "";
    }
}
