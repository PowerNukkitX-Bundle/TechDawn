package cn.powernukkitx.techdawn;

import cn.nukkit.block.Block;
import cn.nukkit.plugin.PluginBase;
import cn.powernukkitx.techdawn.block.wire.BaseWireBlock;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public final class Main extends PluginBase {
    @Override
    public void onLoad() {
        try {
            Block.registerCustomBlock(List.of(
                    BaseWireBlock.class
            ));
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
