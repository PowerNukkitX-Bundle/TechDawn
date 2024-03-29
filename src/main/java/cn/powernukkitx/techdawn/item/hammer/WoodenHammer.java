package cn.powernukkitx.techdawn.item.hammer;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.item.ItemConstants;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class WoodenHammer extends BaseHammer {

    public WoodenHammer() {
        super("techdawn:wooden_hammer", "techdawn-items-hammer-wooden_hammer");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_WOODEN;
    }

    @Override
    public int getTier() {
        return TIER_WOODEN;
    }

    @Override
    public int getMaxDurability() {
        return ItemConstants.DURABILITY_WOODEN;
    }

    @NotNull
    @Override
    public String getTags() {
        return "wooden_hammer hammer minecraft:wooden_tier";
    }

    @Override
    public int getAttackDamage() {
        return 2;
    }
}
