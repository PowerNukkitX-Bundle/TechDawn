package cn.powernukkitx.techdawn.listener;

import cn.nukkit.block.BlockPlanks;
import cn.nukkit.block.BlockPlanksMangrove;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.inventory.ItemTag;
import cn.nukkit.item.ItemGlassBottle;
import cn.nukkit.level.Sound;
import cn.powernukkitx.techdawn.block.construct.TarImpregnatedWoodPlankBlock;
import cn.powernukkitx.techdawn.util.InventoryUtil;
import cn.powernukkitx.techdawn.util.LevelUtil;

public class TarBottleListener implements Listener {
    @EventHandler
    public void onBottleUse(PlayerInteractEvent event) {
        if (event.getAction() != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null) return;
        var block = event.getBlock();
        if (!(block instanceof BlockPlanks || block instanceof BlockPlanksMangrove)) return;
        if (!InventoryUtil.ensurePlayerSafeForCustomInv(event.getPlayer())) return;
        if (!ItemTag.getTagSet(event.getItem().getNamespaceId()).contains("tar")) return;
        var item = event.getItem();
        item.setCount(item.getCount() - 1);
        var inv = event.getPlayer().getInventory();
        inv.setItemInHand(item);
        var bottle = new ItemGlassBottle();
        if (inv.canAddItem(bottle)) {
            inv.addItem(bottle);
        } else {
            block.level.dropItem(event.getPlayer(), bottle);
        }
        block.level.setBlock(block, new TarImpregnatedWoodPlankBlock());
        LevelUtil.sendSwingArm(event.getPlayer());
        block.level.addSound(block.add(0.5, 0.5, 0.5), Sound.CAULDRON_FILLPOTION);
        event.setCancelled();
    }
}
