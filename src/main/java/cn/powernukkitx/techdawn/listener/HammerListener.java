package cn.powernukkitx.techdawn.listener;

import cn.nukkit.block.*;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.item.Item;
import cn.powernukkitx.techdawn.item.hammer.BaseHammer;
import org.jetbrains.annotations.NotNull;

public final class HammerListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(@NotNull BlockBreakEvent event) {
        if (!event.getInstaBreak() && event.getItem() instanceof BaseHammer) {
            if (event.getDrops().length == 1) {
                var block = event.getBlock();
                if (block instanceof BlockGravel) {
                    event.setDrops(new Item[]{Item.fromString("minecraft:sand")});
                } else if (block instanceof BlockStone || block instanceof BlockBlackstone || block instanceof BlockCobblestone
                        || block instanceof BlockDeepslate || block instanceof BlockDeepslateCobbled) {
                    event.setDrops(new Item[]{Item.fromString("minecraft:gravel")});
                }
            }
        }
    }
}
