package cn.powernukkitx.techdawn;

import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.energy.EnergyRegistry;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginBase;
import cn.powernukkitx.techdawn.block.anvil.BaseAnvilBlock;
import cn.powernukkitx.techdawn.block.machine.battery.BaseBatteryBoxBlock;
import cn.powernukkitx.techdawn.block.wire.BaseWireBlock;
import cn.powernukkitx.techdawn.blockentity.battery.BaseBatteryBoxBlockEntity;
import cn.powernukkitx.techdawn.energy.RF;
import cn.powernukkitx.techdawn.item.icon.ChargeIconItem;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public final class Main extends PluginBase {
    @Override
    public void onLoad() {
        EnergyRegistry.registerEnergyType(RF.getInstance());
        BlockEntity.registerBlockEntity("TechDawn_BaseBatteryBoxBlock", BaseBatteryBoxBlockEntity.class);
        try {
            Item.registerCustomItem(List.of(
                    ChargeIconItem.class
            ));
            Block.registerCustomBlock(List.of(
                    BaseWireBlock.class,
                    BaseAnvilBlock.class,
                    BaseBatteryBoxBlock.class
            ));
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
