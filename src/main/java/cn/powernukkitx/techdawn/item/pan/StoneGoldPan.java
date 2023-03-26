package cn.powernukkitx.techdawn.item.pan;

import cn.nukkit.item.ItemTool;
import cn.nukkit.item.customitem.CustomItem;
import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustomTool;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.data.TechDawnHardness;
import cn.powernukkitx.techdawn.item.ItemConstants;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class StoneGoldPan extends ItemCustomTool implements TechDawnHardness {
    public StoneGoldPan() {
        this("techdawn:stone_gold_pan");
    }

    public StoneGoldPan(@NotNull String id) {
        super(id, null, "techdawn-items-tools-pan-" + id.substring(id.lastIndexOf(":") + 1));
    }

    @Override
    public CustomItemDefinition getDefinition() {
        return CustomItemDefinition.toolBuilder(this, ItemCreativeCategory.EQUIPMENT)
                .creativeGroup("itemGroup.name.shovel")
                .allowOffHand(true)
                .tag(getTags().split(" +"))
                .build();
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_STONE;
    }

    @Override
    public int getMaxDurability() {
        return ItemConstants.DURABILITY_STONE / 2;
    }

    @Override
    public int getTier() {
        return ItemTool.TIER_STONE;
    }

    @Override
    public boolean isShovel() {
        return true;
    }

    @NotNull
    public String getTags() {
        return "stone_gold_pan gold_pan";
    }
}
