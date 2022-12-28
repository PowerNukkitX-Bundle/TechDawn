package cn.powernukkitx.techdawn.item.hammer;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockGravel;
import cn.nukkit.block.BlockSand;
import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustomTool;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;
import cn.powernukkitx.techdawn.data.TechDawnHardness;
import org.jetbrains.annotations.NotNull;

import java.util.IdentityHashMap;
import java.util.Map;

public abstract class BaseHammer extends ItemCustomTool implements TechDawnHardness {
    public static final String TAG = "minecraft:digger minecraft:is_tool minecraft:is_pickaxe is_hammer";

    public BaseHammer(@NotNull String id, @NotNull String textureName) {
        super(id, null, textureName);
    }

    @Override
    public CustomItemDefinition getDefinition() {
        return CustomItemDefinition.toolBuilder(this, ItemCreativeCategory.EQUIPMENT)
                .addExtraBlockTags(getMiningSpeedMap())
                .creativeGroup("itemGroup.name.pickaxe")
                .allowOffHand(true)
                .tag((TAG + getTags()).split(" +"))
                .build();
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

    /**
     * @return 此工具除了镐子以外的方块的挖掘加速表
     */
    protected Map<Block, Integer> getMiningSpeedMap() {
        var map = new IdentityHashMap<Block, Integer>(2);
        map.put(new BlockSand(), Math.max(getTier() - 1, 1));
        map.put(new BlockGravel(), Math.max(getTier() - 1, 1));
        return map;
    }
}
