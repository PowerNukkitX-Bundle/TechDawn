package cn.powernukkitx.techdawn.recipe;

import cn.nukkit.inventory.ModProcessRecipe;
import cn.nukkit.inventory.recipe.ItemDescriptor;
import cn.nukkit.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ExtractingRecipe implements ModProcessRecipe {
    private final ItemDescriptor input;
    private final ItemDescriptor extracted;
    private final Item output;

    public ExtractingRecipe(ItemDescriptor input, ItemDescriptor extracted, Item output) {
        this.input = input;
        this.extracted = extracted;
        this.output = output;
    }

    @Override
    public String getCategory() {
        return "extracting";
    }

    @NotNull
    @Override
    public List<ItemDescriptor> getIngredients() {
        return List.of(input, extracted);
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
