package cn.powernukkitx.techdawn.item.bottle;

import cn.nukkit.item.customitem.CustomItem;
import cn.nukkit.item.customitem.CustomItemDefinition;
import cn.nukkit.item.customitem.ItemCustomEdible;
import cn.nukkit.item.customitem.data.ItemCreativeCategory;
import cn.nukkit.item.food.Food;
import cn.nukkit.item.food.FoodEffective;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.potion.Effect;
import cn.powernukkitx.techdawn.Main;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public abstract class BaseBottle extends ItemCustomEdible implements CustomItem {
    public BaseBottle(@NotNull String id, @NotNull String textureSubName) {
        super(id, null, "techdawn-items-" + textureSubName);
    }

    @Override
    public Map.Entry<Plugin, Food> getFood() {
        var food = new FoodEffective(0, 0);
        for (Effect effect : getEffects()) {
            food.addEffect(effect);
        }
        food.addRelative(getNamespaceId(), getDamage(), Main.INSTANCE);
        return new AbstractMap.SimpleImmutableEntry<>(Main.INSTANCE, food);
    }

    public List<Effect> getEffects() {
        return List.of();
    }

    @Override
    public CustomItemDefinition getDefinition() {
        return CustomItemDefinition.edibleBuilder(this, ItemCreativeCategory.EQUIPMENT)
                .tag(getTags().split(" +"))
                .customBuild(nbt -> {
                    var component = nbt.getCompound("components");
                    component.putCompound("minecraft:fuel", new CompoundTag().putFloat("duration", 1));
                });
    }

    @Override
    public boolean canAlwaysEat() {
        return true;
    }

    @Override
    public boolean isDrink() {
        return true;
    }

    @NotNull
    public String getTags() {
        return "";
    }
}
