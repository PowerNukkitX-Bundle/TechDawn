package cn.powernukkitx.techdawn.item.hammer;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.item.ItemConstants;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("#getTags")
public class BronzeHammer extends BaseHammer {

    public BronzeHammer() {
        super("techdawn:bronze_hammer", "techdawn-items-hammer-bronze_hammer");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_BRONZE;
    }

    @Override
    public int getTier() {
        return TIER_IRON;
    }

    @Override
    public int getMaxDurability() {
        return ItemConstants.DURABILITY_BRONZE;
    }

    @NotNull
    @Override
    public String getTags() {
        return "bronze_hammer hammer minecraft:iron_tier";
    }

    @Override
    public int getAttackDamage() {
        return 5;
    }
}
