package cn.powernukkitx.techdawn;

import cn.nukkit.block.Block;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.energy.EnergyRegistry;
import cn.nukkit.item.Item;
import cn.nukkit.item.customitem.ItemCustom;
import cn.nukkit.plugin.PluginBase;
import cn.powernukkitx.techdawn.energy.RF;
import cn.powernukkitx.techdawn.util.RecipeUtil;

import java.io.IOException;

import static cn.powernukkitx.techdawn.util.RegistryManifestUtil.registryManifestDataOf;
import static cn.powernukkitx.techdawn.util.RegistryManifestUtil.registryManifestOf;

public final class Main extends PluginBase {
    public static Main INSTANCE = null;

    public Main() {
        INSTANCE = this;
    }

    @Override
    public void onLoad() {
        EnergyRegistry.registerEnergyType(RF.getInstance());
        registerBlockEntity();
        Item.registerCustomItem(registryManifestOf(ItemCustom.class));
        Block.registerCustomBlock(registryManifestOf(CustomBlock.class));
        try {
            RecipeUtil.registerForgingRecipes();
        } catch (IOException e) {
            getLogger().error("Failed to register recipes.", e);
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
