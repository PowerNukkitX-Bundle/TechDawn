package cn.powernukkitx.techdawn;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.energy.EnergyRegistry;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.custom.CustomEntity;
import cn.nukkit.entity.provider.CustomClassEntityProvider;
import cn.nukkit.inventory.ItemTag;
import cn.nukkit.item.Item;
import cn.nukkit.item.customitem.CustomItem;
import cn.nukkit.level.Level;
import cn.nukkit.plugin.PluginBase;
import cn.powernukkitx.techdawn.energy.RF;
import cn.powernukkitx.techdawn.item.hammer.BaseHammer;
import cn.powernukkitx.techdawn.listener.*;
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
        Item.registerCustomItem(registryManifestOf(CustomItem.class));
        registerEntity();
        Block.registerCustomBlock(registryManifestOf(CustomBlock.class));
        registerItemTag();
    }

    @Override
    public void onEnable() {
        try {
            RecipeUtil.registerForgingRecipes();
            RecipeUtil.registerFurnaceRecipes();
            RecipeUtil.registerHighTemperatureFurnaceRecipes();
            RecipeUtil.registerExtractingRecipes();
            RecipeUtil.registerGrindingRecipes();
            RecipeUtil.registerWashRecipes();
            RecipeUtil.registerShapedRecipes();
            RecipeUtil.registerShapelessRecipes();
            Server.getInstance().getCraftingManager().rebuildPacket();
        } catch (IOException e) {
            getLogger().error("Failed to register recipes.", e);
        }
        registerRandomTickableBlock();
        registerListeners();
    }

    private void registerItemTag() {
        var classList = registryManifestOf(CustomItem.class);
        var tagList = registryManifestDataOf(CustomItem.class);
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

    @SuppressWarnings("unchecked")
    private void registerEntity() {
        var classList = registryManifestOf(CustomEntity.class);
        for (var each : classList) {
            Entity.registerCustomEntity(new CustomClassEntityProvider((Class<? extends Entity>) each));
        }
    }

    private void registerListeners() {
        Server.getInstance().getPluginManager().registerEvents(new HammerListener(), this);
        Server.getInstance().getPluginManager().registerEvents(new GoldPanListener(), this);
        Server.getInstance().getPluginManager().registerEvents(new TarBottleListener(), this);
        Server.getInstance().getPluginManager().registerEvents(new CauldronListener(), this);
        Server.getInstance().getPluginManager().registerEvents(new InventoriesListener(), this);
        Server.getInstance().getScheduler().scheduleDelayedRepeatingTask(this, new TickListener(this), 1, 1);
    }

    private void registerRandomTickableBlock() {
        Level.setCanRandomTick(BlockState.of("techdawn:tar_impregnated_wood_plank").getBlockId(), true);
    }
}
