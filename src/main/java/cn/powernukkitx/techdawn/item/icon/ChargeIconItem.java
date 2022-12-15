package cn.powernukkitx.techdawn.item.icon;

import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustom;
import cn.nukkit.item.customitem.ItemCustomTool;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;
import cn.powernukkitx.techdawn.annotation.AutoRegister;
import org.jetbrains.annotations.NotNull;

@AutoRegister(ItemCustom.class)
public final class ChargeIconItem extends ItemCustomTool {
    public ChargeIconItem() {
        super("techdawn:icon_charge", "ChargeIconItem", "techdawn-items-icon-charged");
    }

    @Override
    public CustomItemDefinition getDefinition() {
        return CustomItemDefinition.toolBuilder(this, ItemCreativeCategory.ITEMS)
                .build();
    }

    @Override
    public int getMaxDurability() {
        return 11;
    }

    @Override
    public int getTier() {
        return ItemCustomTool.TIER_DIAMOND;
    }

    @Override
    public int getAttackDamage() {
        return 0;
    }

    @Override
    public int getEnchantAbility() {
        return 0;
    }

    @Override
    public boolean isSword() {
        return true;
    }

    @NotNull
    public static ChargeIconItem ofRF(double currentRF, double maxRF) {
        ChargeIconItem item = new ChargeIconItem();
        item.setCustomName(String.format("§a%.2f§7/§c%.2f§r RF", currentRF, maxRF));
        item.setDamage(11 - (int) ((currentRF / maxRF) * 10));
        return item;
    }
}
