package cn.powernukkitx.techdawn.item.crushed_ore;

import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustom;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;
import org.jetbrains.annotations.NotNull;

public abstract class BaseCrushedOre extends ItemCustom {
    public BaseCrushedOre(@NotNull String id) {
        super(id, null, "techdawn-items-crushed_ore-" + id.substring(id.lastIndexOf(":") + 1));
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
