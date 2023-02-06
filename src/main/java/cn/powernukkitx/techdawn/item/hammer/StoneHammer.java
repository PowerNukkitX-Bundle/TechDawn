package cn.powernukkitx.techdawn.item.hammer;

import cn.nukkit.item.customitem.CustomItem;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import cn.powernukkitx.techdawn.annotation.AutoRegisterData;
import cn.powernukkitx.techdawn.item.ItemConstants;
import org.jetbrains.annotations.NotNull;

@AutoRegister(CustomItem.class)
@AutoRegisterData("stone_hammer minecraft:stone_tier")
public class StoneHammer extends BaseHammer {

    public StoneHammer() {
        super("techdawn:stone_hammer", "techdawn-items-hammer-stone_hammer");
    }

    @Override
    public int getHardnessTier() {
        return HARDNESS_STONE;
    }

    @Override
    public int getTier() {
        return TIER_STONE;
    }

    @Override
    public int getMaxDurability() {
        return ItemConstants.DURABILITY_STONE;
    }

    @NotNull
    @Override
    public String getTags() {
        return "stone_hammer hammer minecraft:stone_tier";
    }

    @Override
    public int getAttackDamage() {
        return 3;
    }
}
