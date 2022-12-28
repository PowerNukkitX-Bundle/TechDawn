package cn.powernukkitx.techdawn.item.hammer;

import cn.nukkit.item.customitem.ItemCustom;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.item.ItemConstants;
import org.jetbrains.annotations.NotNull;

@AutoRegister(ItemCustom.class)
@AutoRegisterData("stainless_steel_hammer minecraft:netherite_tier")
public class StainlessSteelHammer extends BaseHammer {

    public StainlessSteelHammer() {
        super("techdawn:stainless_steel_hammer", "techdawn-items-hammer-stainless_steel_hammer");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_STAINLESS_STEEL;
    }

    @Override
    public int getTier() {
        return TIER_NETHERITE;
    }

    @Override
    public int getMaxDurability() {
        return ItemConstants.DURABILITY_NETHERITE;
    }

    @NotNull
    @Override
    public String getTags() {
        return "stainless_steel_hammer minecraft:netherite_tier";
    }

    @Override
    public int getAttackDamage() {
        return 6;
    }
}
