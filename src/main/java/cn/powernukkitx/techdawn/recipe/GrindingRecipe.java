package cn.powernukkitx.techdawn.recipe;

import cn.nukkit.inventory.ModProcessRecipe;
import cn.nukkit.inventory.recipe.DefaultDescriptor;
import cn.nukkit.inventory.recipe.ItemDescriptor;
import cn.nukkit.inventory.recipe.ItemTagDescriptor;
import cn.nukkit.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class GrindingRecipe implements ModProcessRecipe {
    private final ItemDescriptor input;
    private final Item output;

    public GrindingRecipe(Item input, Item output) {
        this.input = new DefaultDescriptor(input);
        this.output = output;
    }

    public GrindingRecipe(String inputTag, Item output) {
        this.input = new ItemTagDescriptor(inputTag, 1);
        this.output = output;
    }

    @Override
    public String getCategory() {
        return "grinding";
    }

    @NotNull
    @Override
    public List<ItemDescriptor> getIngredients() {
        return List.of(input);
    }

    @NotNull
    @Override
    public List<Item> getExtraResults() {
        return List.of();
    }

    @Override
    public Item getResult() {
        return this.output;
    }
}
