package cn.powernukkitx.techdawn.listener;

import cn.nukkit.Server;
import cn.nukkit.block.BlockCauldron;
import cn.nukkit.blockproperty.value.CauldronLiquid;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.level.Sound;
import cn.nukkit.level.particle.WaterParticle;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class CauldronListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCauldronUse(@NotNull PlayerInteractEvent event) {
        if (event.getAction() != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getItem() == null || event.getItem().isNull()) {
            return;
        }
        if (event.getBlock() instanceof BlockCauldron cauldron && cauldron.getCauldronLiquid() == CauldronLiquid.WATER) {
            var be = cauldron.getBlockEntity();
            if (be != null && be.hasPotion()) {
                return;
            }
            var recipe = Server.getInstance().getCraftingManager().matchModProcessRecipe("washing",
                    List.of(event.getItem()));
            if (recipe == null) {
                return;
            }
            var result = recipe.getResult();
            var itemInHand = event.getPlayer().getInventory().getItemInHand();
            if (!recipe.matchItems(List.of(itemInHand))) {
                return;
            }
            int fillLevel = cauldron.getFillLevel();
            if (fillLevel > 0) {
                var level = cauldron.getLevel();
                var waterHeight = 0.25 + fillLevel * 0.1;
                cauldron.setFillLevel(fillLevel - 1, event.getPlayer());
                level.setBlock(cauldron, cauldron, true, true);
                level.dropItem(cauldron.add(0.5, waterHeight, 0.5), result);
                itemInHand.setCount(itemInHand.getCount() - 1);
                event.getPlayer().getInventory().setItemInHand(itemInHand);
                level.addSound(cauldron.add(0.5, 0.5, 0.5), Sound.CAULDRON_TAKEWATER);
                for (var i = 0; i < 4; i++) {
                    var particle = new WaterParticle(cauldron.add(ThreadLocalRandom.current().nextDouble(0.25, 0.75),
                            waterHeight,
                            ThreadLocalRandom.current().nextDouble(0.25, 0.75)));
                    level.addParticle(particle);
                }
            }
        }
    }
}
