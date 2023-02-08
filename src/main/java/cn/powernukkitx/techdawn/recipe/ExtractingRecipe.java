package cn.powernukkitx.techdawn.recipe;

import cn.nukkit.inventory.ModProcessRecipe;
import cn.nukkit.inventory.recipe.DefaultDescriptor;
import cn.nukkit.inventory.recipe.ItemDescriptor;
import cn.nukkit.inventory.recipe.ItemTagDescriptor;
import cn.nukkit.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ExtractingRecipe implements ModProcessRecipe {
    private final ItemDescriptor input;
    private final ItemDescriptor extracted;
    private final Item output;

    public ExtractingRecipe(Item input, Item extracted, Item output) {
        this.input = new DefaultDescriptor(input);
        this.extracted = new DefaultDescriptor(extracted);
        this.output = output;
    }

    public ExtractingRecipe(String inputTag, String extractedTag, Item output) {
        this.input = new ItemTagDescriptor(inputTag, 1);
        this.extracted = new ItemTagDescriptor(extractedTag, 1);
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
