package cn.powernukkitx.techdawn.recipe;

import cn.nukkit.inventory.ModProcessRecipe;
import cn.nukkit.inventory.recipe.DefaultDescriptor;
import cn.nukkit.inventory.recipe.ItemDescriptor;
import cn.nukkit.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class WashingRecipe implements ModProcessRecipe {
    private final ItemDescriptor input;
    private final Item output;
    private final int outputBounce;

    public WashingRecipe(ItemDescriptor input, Item output, int outputBounce) {
        this.input = input;
        this.output = output;
        this.outputBounce = outputBounce;
    }

    public WashingRecipe(Item input, Item output, int outputBounce) {
        this.input = new DefaultDescriptor(input);
        this.output = output;
        this.outputBounce = outputBounce;
    }

    @Override
    public String getCategory() {
        return "washing";
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
        if (outputBounce != 0) {
            var bounce = ThreadLocalRandom.current().nextInt(outputBounce);
            if (bounce != 0) {
                var outputCopy = output.clone();
                outputCopy.setCount(output.getCount() + bounce);
                return outputCopy;
            }
        }
        return output;
    }
}
