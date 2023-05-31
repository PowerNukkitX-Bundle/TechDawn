package cn.powernukkitx.techdawn.recipe;

import cn.nukkit.inventory.ModProcessRecipe;
import cn.nukkit.inventory.recipe.ItemDescriptor;
import cn.nukkit.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class ExtractingRecipe implements ModProcessRecipe {
    private final ItemDescriptor input;
    private final ItemDescriptor extracted;
    private final Item output;
    private final List<ItemDescriptor> ingredients;

    public ExtractingRecipe(ItemDescriptor input, ItemDescriptor extracted, Item output) {
        this.input = input;
        this.extracted = extracted;
        this.output = output;
        this.ingredients = new ArrayList<>(2);
        if (this.input.getCount() != 0) {
            this.ingredients.add(this.input);
        }
        if (this.extracted.getCount() != 0) {
            this.ingredients.add(this.extracted);
        }
    }

    @Override
    public String getCategory() {
        return "extracting";
    }

    @NotNull
    @Override
    public List<ItemDescriptor> getIngredients() {
        return this.ingredients;
    }

    public ItemDescriptor getInput() {
        return input;
    }

    public ItemDescriptor getExtracted() {
        return extracted;
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
