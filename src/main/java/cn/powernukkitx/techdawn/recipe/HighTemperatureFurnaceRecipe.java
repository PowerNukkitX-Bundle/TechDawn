package cn.powernukkitx.techdawn.recipe;

import cn.nukkit.inventory.ModProcessRecipe;
import cn.nukkit.inventory.recipe.DefaultDescriptor;
import cn.nukkit.inventory.recipe.ItemDescriptor;
import cn.nukkit.inventory.recipe.ItemTagDescriptor;
import cn.nukkit.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class HighTemperatureFurnaceRecipe implements ModProcessRecipe, VarProcessingTick {
    private final ItemDescriptor input;
    private final int processingTick;
    private final Item output;

    public HighTemperatureFurnaceRecipe(Item input, int processingTick, Item output) {
        this.input = new DefaultDescriptor(input);
        this.processingTick = processingTick;
        this.output = output;
    }

    public HighTemperatureFurnaceRecipe(String inputTag, int processingTick, Item output) {
        this.input = new ItemTagDescriptor(inputTag, 1);
        this.processingTick = processingTick;
        this.output = output;
    }

    @Override
    public String getCategory() {
        return "high_temperature_furnace";
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

    public int getProcessingTick() {
        return processingTick;
    }
}
