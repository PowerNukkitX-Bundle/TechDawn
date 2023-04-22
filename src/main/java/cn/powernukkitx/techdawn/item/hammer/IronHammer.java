package cn.powernukkitx.techdawn.item.hammer;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.item.ItemConstants;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class IronHammer extends BaseHammer {

    public IronHammer() {
        super("techdawn:iron_hammer", "techdawn-items-hammer-iron_hammer");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_IRON;
    }

    @Override
    public int getTier() {
        return TIER_IRON;
    }

    @Override
    public int getMaxDurability() {
        return ItemConstants.DURABILITY_IRON;
    }

    @NotNull
    @Override
    public String getTags() {
        return "iron_hammer hammer minecraft:iron_tier";
    }

    @Override
    public int getAttackDamage() {
        return 4;
    }
}
