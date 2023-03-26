package cn.powernukkitx.techdawn.recipe;

import cn.nukkit.inventory.ModProcessRecipe;
import cn.nukkit.inventory.recipe.DefaultDescriptor;
import cn.nukkit.inventory.recipe.ItemDescriptor;
import cn.nukkit.inventory.recipe.ItemTagDescriptor;
import cn.nukkit.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ForgingRecipe implements ModProcessRecipe {
    private final ItemDescriptor input;
    private final @Nullable ItemDescriptor template;
    private final Item output;

    public ForgingRecipe(Item input, Item template, Item output) {
        this.input = new DefaultDescriptor(input);
        this.template = template == null ? null : new DefaultDescriptor(template);
        this.output = output;
    }

    public ForgingRecipe(String inputTag, Item template, Item output) {
        this.input = new ItemTagDescriptor(inputTag, 1);
        this.template = template == null ? null : new DefaultDescriptor(template);
        this.output = output;
    }

    @Override
    public String getCategory() {
        return "forging";
    }

    @NotNull
    @Override
    public List<ItemDescriptor> getIngredients() {
        if (template == null)
            return List.of(input);
        else {
            return List.of(input, template);
        }
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

    @Override
    public boolean matchItems(@NotNull List<Item> inputItems) {
        if (inputItems.size() != getIngredients().size()) return false;
        return ModProcessRecipe.super.matchItems(inputItems);
    }
}
