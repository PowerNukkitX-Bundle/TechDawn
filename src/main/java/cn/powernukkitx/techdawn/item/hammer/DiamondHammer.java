package cn.powernukkitx.techdawn.item.hammer;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.item.ItemConstants;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class DiamondHammer extends BaseHammer {

    public DiamondHammer() {
        super("techdawn:diamond_hammer", "techdawn-items-hammer-diamond_hammer");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_DIAMOND;
    }

    @Override
    public int getTier() {
        return TIER_DIAMOND;
    }

    @Override
    public int getMaxDurability() {
        return ItemConstants.DURABILITY_DIAMOND;
    }

    @NotNull
    @Override
    public String getTags() {
        return "diamond_hammer hammer minecraft:diamond_tier";
    }

    @Override
    public int getAttackDamage() {
        return 5;
    }
}
