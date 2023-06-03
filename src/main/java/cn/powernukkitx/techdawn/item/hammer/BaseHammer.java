package cn.powernukkitx.techdawn.item.hammer;

import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustomTool;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;
import cn.powernukkitx.techdawn.data.TechDawnHardness;
import org.jetbrains.annotations.NotNull;

public abstract class BaseHammer extends ItemCustomTool implements TechDawnHardness {
    public static final String TAG = "minecraft:digger minecraft:is_tool minecraft:is_pickaxe is_hammer ";

    public BaseHammer(@NotNull String id, @NotNull String textureName) {
        super(id, null, textureName);
    }

    @Override
    public CustomItemDefinition getDefinition() {
        return CustomItemDefinition.toolBuilder(this, ItemCreativeCategory.EQUIPMENT)
                .addExtraBlock("minecraft:sand", Math.max(getTier() - 1, 1))
                .addExtraBlock("minecraft:gravel", Math.max(getTier() - 1, 1))
                .creativeGroup("itemGroup.name.pickaxe")
                .allowOffHand(true)
                .handEquipped(true)
                .tag((TAG + getTags()).split(" +"))
                .build();
    }

    @Override
    public boolean isProcessorItem() {
        return true;
    }

    @Override
    public boolean isPickaxe() {
        return true;
    }

    @Override
    public abstract int getTier();

    @Override
    public abstract int getMaxDurability();

    @NotNull
    public abstract String getTags();

    @Override
    public abstract int getAttackDamage();
}
