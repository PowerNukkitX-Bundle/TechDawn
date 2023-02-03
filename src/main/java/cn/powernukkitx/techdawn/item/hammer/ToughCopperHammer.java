package cn.powernukkitx.techdawn.item.hammer;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.item.ItemConstants;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("tough_copper_hammer minecraft:stone_tier")
public class ToughCopperHammer extends BaseHammer {

    public ToughCopperHammer() {
        super("techdawn:tough_copper_hammer", "techdawn-items-hammer-tough_copper_hammer");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_TOUGH_COPPER;
    }

    @Override
    public int getTier() {
        return TIER_STONE;
    }

    @Override
    public int getMaxDurability() {
        return ItemConstants.DURABILITY_TOUGH_COPPER;
    }

    @NotNull
    @Override
    public String getTags() {
        return "tough_copper_hammer minecraft:stone_tier";
    }

    @Override
    public int getAttackDamage() {
        return 3;
    }
}
