package cn.powernukkitx.techdawn.listener;

import cn.nukkit.block.BlockGravel;
import cn.nukkit.block.BlockSand;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.item.Item;
import cn.powernukkitx.techdawn.block.construct.FlintGravelBlock;
import cn.powernukkitx.techdawn.item.pan.StoneGoldPan;
import cn.powernukkitx.techdawn.util.ItemUtil;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class GoldPanListener implements Listener {
    public static final Object2DoubleOpenHashMap<Item> STONE_GOLD_PAN_ITEMS_GRAVEL = new Object2DoubleOpenHashMap<>(Map.of(
            Item.AIR_ITEM, 100.0,
            Item.fromString("minecraft:flint"), 34.5,
            Item.fromString("techdawn:crushed_coal_ore"), 21d,
            Item.fromString("techdawn:crushed_copper_ore"), 7d,
            Item.fromString("techdawn:crushed_gold_ore"), 1.5,
            Item.fromString("techdawn:crushed_iron_ore"), 7.5,
            Item.fromString("techdawn:crushed_lapis_ore"), 1.2,
            Item.fromString("techdawn:crushed_tin_ore"), 6d
    ));

    public static final Object2DoubleOpenHashMap<Item> STONE_GOLD_PAN_ITEMS_SAND = new Object2DoubleOpenHashMap<>(Map.of(
            Item.AIR_ITEM, 80.0,
            Item.fromString("techdawn:crushed_diamond_ore"), 0.15,
            Item.fromString("techdawn:crushed_emerald_ore"), 0.4,
            Item.fromString("techdawn:crushed_redstone_ore"), 9d,
            Item.fromString("techdawn:crushed_coal_ore"), 20d,
            Item.fromString("techdawn:crushed_iron_ore"), 4d,
            Item.fromString("techdawn:crushed_gold_ore"), 2d
    ));

    public static final Object2DoubleOpenHashMap<Item> STONE_GOLD_PAN_ITEMS_FLINT_GRAVEL = new Object2DoubleOpenHashMap<>(Map.of(
            Item.AIR_ITEM, 30d,
            Item.fromString("techdawn:crushed_coal_ore"), 14d,
            Item.fromString("techdawn:crushed_copper_ore"), 7d,
            Item.fromString("techdawn:crushed_gold_ore"), 1.5,
            Item.fromString("techdawn:crushed_iron_ore"), 7.5,
            Item.fromString("techdawn:crushed_lapis_ore"), 1.2,
            Item.fromString("techdawn:crushed_tin_ore"), 6d
    ));

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(@NotNull BlockBreakEvent event) {
        if (!event.getInstaBreak() && event.getItem() instanceof StoneGoldPan) {
            if (event.getDrops().length == 1) {
                if (event.getBlock() instanceof BlockGravel) {
                    event.setDrops(new Item[]{ItemUtil.randomItem(STONE_GOLD_PAN_ITEMS_GRAVEL)});
                } else if (event.getBlock() instanceof BlockSand) {
                    event.setDrops(new Item[]{ItemUtil.randomItem(STONE_GOLD_PAN_ITEMS_SAND)});
                } else if (event.getBlock() instanceof FlintGravelBlock) {
                    event.setDrops(new Item[]{ItemUtil.randomItem(STONE_GOLD_PAN_ITEMS_FLINT_GRAVEL)});
                }
            }
        }
    }
}
