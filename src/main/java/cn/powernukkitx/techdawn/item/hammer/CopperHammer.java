package cn.powernukkitx.techdawn.item.hammer;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.item.ItemConstants;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("copper_hammer minecraft:iron_tier")
public class CopperHammer extends BaseHammer {

    public CopperHammer() {
        super("techdawn:copper_hammer", "techdawn-items-hammer-copper_hammer");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_COPPER;
    }

    @Override
    public int getTier() {
        return TIER_IRON;
    }

    @Override
    public int getMaxDurability() {
        return ItemConstants.DURABILITY_COPPER;
    }

    @NotNull
    @Override
    public String getTags() {
        return "copper_hammer minecraft:iron_tier";
    }

    @Override
    public int getAttackDamage() {
        return 4;
    }
}
