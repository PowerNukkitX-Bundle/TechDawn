package cn.powernukkitx.techdawn;

import cn.nukkit.block.Block;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.energy.EnergyRegistry;
import cn.nukkit.item.Item;
import cn.nukkit.item.customitem.ItemCustom;
import cn.nukkit.plugin.PluginBase;
import cn.powernukkitx.techdawn.energy.RF;

import java.lang.reflect.InvocationTargetException;

import static cn.powernukkitx.techdawn.util.RegistryManifestUtil.registryManifestDataOf;
import static cn.powernukkitx.techdawn.util.RegistryManifestUtil.registryManifestOf;

public final class Main extends PluginBase {
    @Override
    public void onLoad() {
        EnergyRegistry.registerEnergyType(RF.getInstance());
        registerBlockEntity();
        try {
            Item.registerCustomItem(registryManifestOf(ItemCustom.class));
            Block.registerCustomBlock(registryManifestOf(CustomBlock.class));
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerBlockEntity() {
        var classList = registryManifestOf(BlockEntity.class);
        var nameList = registryManifestDataOf(BlockEntity.class);
        for (int i = 0, len = classList.size(); i < len; i++) {
            var clazz = classList.get(i);
            var name = nameList.get(i);
            BlockEntity.registerBlockEntity(name, clazz);
        }
    }
}
