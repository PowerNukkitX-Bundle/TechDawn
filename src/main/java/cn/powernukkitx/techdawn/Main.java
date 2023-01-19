package cn.powernukkitx.techdawn;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.energy.EnergyRegistry;
import cn.nukkit.inventory.ItemTag;
import cn.nukkit.item.Item;
import cn.nukkit.item.customitem.ItemCustom;
import cn.nukkit.plugin.PluginBase;
import cn.powernukkitx.techdawn.energy.RF;
import cn.powernukkitx.techdawn.item.hammer.BaseHammer;
import cn.powernukkitx.techdawn.listener.HammerListener;
import cn.powernukkitx.techdawn.util.RecipeUtil;

import java.io.IOException;
import java.util.Arrays;

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
        registerItemTag();
        try {
            RecipeUtil.registerForgingRecipes();
            RecipeUtil.registerFurnaceRecipes();
            RecipeUtil.registerHighTemperatureFurnaceRecipes();
        } catch (IOException e) {
            getLogger().error("Failed to register recipes.", e);
        }
    }

    @Override
    public void onEnable() {
        registerListeners();
    }

    private void registerItemTag() {
        var classList = registryManifestOf(ItemCustom.class);
        var tagList = registryManifestDataOf(ItemCustom.class);
        for (int i = 0, len = classList.size(); i < len; i++) {
            var clazz = classList.get(i);
            var rawTag = tagList.get(i).trim();
            // 额外附带上锤子的通用标签
            if (rawTag.contains("hammer")) {
                rawTag = rawTag + " " + BaseHammer.TAG;
            }
            var tags = rawTag.split(" +");
            if (tags.length == 0) continue;
            try {
                var method = clazz.getDeclaredConstructor();
                method.setAccessible(true);
                ItemTag.registerItemTag(method.newInstance().getNamespaceId(), Arrays.asList(tags));
            } catch (Exception e) {
                getLogger().warning("Failed to register item tag for: " + clazz.getSimpleName());
            }
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

    private void registerListeners() {
        Server.getInstance().getPluginManager().registerEvents(new HammerListener(), this);
    }
}
