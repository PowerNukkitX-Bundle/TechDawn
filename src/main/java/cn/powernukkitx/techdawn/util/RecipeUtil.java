package cn.powernukkitx.techdawn.util;

import cn.nukkit.Server;
import cn.nukkit.inventory.*;
import cn.nukkit.inventory.recipe.DefaultDescriptor;
import cn.nukkit.inventory.recipe.ItemDescriptor;
import cn.nukkit.inventory.recipe.ItemTagDescriptor;
import cn.nukkit.item.Item;
import cn.powernukkitx.techdawn.Main;
import cn.powernukkitx.techdawn.recipe.*;
import com.google.gson.*;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public final class RecipeUtil {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().create();

    private RecipeUtil() {
        throw new UnsupportedOperationException();
    }

    public static void registerForgingRecipes() throws IOException {
        var s = Main.class.getResourceAsStream("/recipe/forging.json");
        if (s == null) {
            Main.INSTANCE.getLogger().warning("Failed to load forging recipes");
            return;
        }
        var manager = Server.getInstance().getCraftingManager();
        try (var recipeReader = new InputStreamReader(s)) {
            var arr = JsonParser.parseReader(recipeReader).getAsJsonArray();
            for (var each : arr) {
                var recipeObj = each.getAsJsonObject();
                var input = recipeObj.get("input").getAsJsonObject();
                var template = recipeObj.get("template") instanceof JsonObject jsonObject ? jsonObject : null;
                var output = recipeObj.get("output").getAsJsonObject();
                if (jsonObjectContains(input, "type", "item")) {
                    if (template == null)
                        manager.registerModProcessRecipe(new ForgingRecipe(getItemFromJson(input), null, getItemFromJson(output)));
                    else {
                        var templateItem = getItemFromJson(template);
                        manager.registerModProcessRecipe(new ForgingRecipe(getItemFromJson(input), templateItem, getItemFromJson(output)));
                    }
                } else {
                    if (template == null)
                        manager.registerModProcessRecipe(new ForgingRecipe(input.get("tag").getAsString(), null, getItemFromJson(output)));
                    else {
                        var templateItem = getItemFromJson(template);
                        manager.registerModProcessRecipe(new ForgingRecipe(input.get("tag").getAsString(), templateItem, getItemFromJson(output)));
                    }
                }
            }
        }
    }

    public static void registerFurnaceRecipes() throws IOException {
        var s = Main.class.getResourceAsStream("/recipe/furnace.json");
        if (s == null) {
            Main.INSTANCE.getLogger().warning("Failed to load furnace recipes");
            return;
        }
        var manager = Server.getInstance().getCraftingManager();
        try (var recipeReader = new InputStreamReader(s)) {
            var arr = JsonParser.parseReader(recipeReader).getAsJsonArray();
            for (var each : arr) {
                var recipeObj = each.getAsJsonObject();
                var canInBlastFurnace = jsonObjectContains(recipeObj, "blastFurnace", true);
                var canInSmoker = jsonObjectContains(recipeObj, "smoker", true);
                var input = recipeObj.get("input").getAsJsonObject();
                var output = recipeObj.get("output").getAsJsonObject();
                var xp = recipeObj.has("xp") ? recipeObj.get("xp").getAsDouble() : 0;
                if (jsonObjectContains(input, "type", "item")) {
                    registerRecipe(manager::registerFurnaceRecipe, new FurnaceRecipe(getItemFromJson(output), getItemFromJson(input)), xp);
                    if (canInBlastFurnace) {
                        registerRecipe(manager::registerBlastFurnaceRecipe, new BlastFurnaceRecipe(getItemFromJson(output), getItemFromJson(input)), xp);
                    }
                    if (canInSmoker) {
                        registerRecipe(manager::registerSmokerRecipe, new SmokerRecipe(getItemFromJson(output), getItemFromJson(input)), xp);
                    }
                } else {
                    throw new IllegalArgumentException("Tag input for furnace is not supported yet");
                }
            }
        }
    }

    public static void registerHighTemperatureFurnaceRecipes() throws IOException {
        var s = Main.class.getResourceAsStream("/recipe/high_temperature_furnace.json");
        if (s == null) {
            Main.INSTANCE.getLogger().warning("Failed to load furnace recipes");
            return;
        }
        var manager = Server.getInstance().getCraftingManager();
        try (var recipeReader = new InputStreamReader(s)) {
            var arr = JsonParser.parseReader(recipeReader).getAsJsonArray();
            for (var each : arr) {
                var recipeObj = each.getAsJsonObject();
                var input = recipeObj.get("input").getAsJsonObject();
                var output = recipeObj.get("output").getAsJsonObject();
                var processingTick = recipeObj.get("processingTick").getAsInt();
                var xp = recipeObj.has("xp") ? recipeObj.get("xp").getAsDouble() : 0;
                if (jsonObjectContains(input, "type", "item")) {
                    registerRecipe(manager::registerModProcessRecipe, new HighTemperatureFurnaceRecipe(getItemFromJson(input), processingTick, getItemFromJson(output)), xp);
                } else {
                    throw new IllegalArgumentException("Tag input for furnace is not supported yet");
                }
            }
        }
    }

    public static void registerExtractingRecipes() throws IOException {
        var s = Main.class.getResourceAsStream("/recipe/extracting.json");
        if (s == null) {
            Main.INSTANCE.getLogger().warning("Failed to load extracting recipes");
            return;
        }
        var manager = Server.getInstance().getCraftingManager();
        try (var recipeReader = new InputStreamReader(s)) {
            var arr = JsonParser.parseReader(recipeReader).getAsJsonArray();
            for (var each : arr) {
                var recipeObj = each.getAsJsonObject();
                var inputs = recipeObj.get("input").getAsJsonArray();
                var input = inputs.get(1).getAsJsonObject();
                var extracted = inputs.get(0).getAsJsonObject();
                var output = recipeObj.get("output").getAsJsonObject();
                ItemDescriptor inputDes;
                ItemDescriptor extractedDes;
                if (jsonObjectContains(input, "type", "item")) {
                    inputDes = new DefaultDescriptor(getItemFromJson(input));
                } else {
                    inputDes = new ItemTagDescriptor(input.get("tag").getAsString(), 1);
                }
                if (jsonObjectContains(extracted, "type", "item")) {
                    extractedDes = new DefaultDescriptor(getItemFromJson(extracted));
                } else {
                    extractedDes = new ItemTagDescriptor(extracted.get("tag").getAsString(), 1);
                }
                manager.registerModProcessRecipe(new ExtractingRecipe(inputDes, extractedDes, getItemFromJson(output)));
            }
        }
    }

    public static void registerGrindingRecipes() throws IOException {
        var s = Main.class.getResourceAsStream("/recipe/grinding.json");
        if (s == null) {
            Main.INSTANCE.getLogger().warning("Failed to load grinding recipes");
            return;
        }
        var manager = Server.getInstance().getCraftingManager();
        try (var recipeReader = new InputStreamReader(s)) {
            var arr = JsonParser.parseReader(recipeReader).getAsJsonArray();
            for (var each : arr) {
                var recipeObj = each.getAsJsonObject();
                var input = recipeObj.get("input").getAsJsonObject();
                var output = recipeObj.get("output").getAsJsonObject();
                if (jsonObjectContains(input, "type", "item")) {
                    manager.registerModProcessRecipe(new GrindingRecipe(getItemFromJson(input), getItemFromJson(output)));
                } else {
                    manager.registerModProcessRecipe(new GrindingRecipe(input.get("tag").getAsString(), getItemFromJson(output)));
                }
            }
        }
    }

    /*
    // json like this
     [
       {
         "input": {
           "A": {
             "type": "item",
             "id": "techdawn:annealed_copper_ingot"
           },
           "B": {
             "type": "item",
             "id": "minecraft:iron_ingot"
           }
         },
         "output": {
           "type": "item",
           "id": "minecraft:gold_ingot"
         },
         "shape": {
           "ABA",
           " A ",
           "BA "
         }
       }
     ]
    */
    public static void registerShapedRecipes() throws IOException {
        var s = Main.class.getResourceAsStream("/recipe/shaped.json5");
        if (s == null) {
            Main.INSTANCE.getLogger().warning("Failed to load shaped recipes");
            return;
        }
        var manager = Server.getInstance().getCraftingManager();
        try (var recipeReader = GSON.newJsonReader(new InputStreamReader(s))) {
            var arr = JsonParser.parseReader(recipeReader).getAsJsonArray();
            for (var each : arr) {
                var recipeObj = each.getAsJsonObject();
                var input = recipeObj.get("input").getAsJsonObject();
                var output = recipeObj.get("output").getAsJsonObject();
                var shape = recipeObj.get("shape").getAsJsonArray();
                var shapeArr = new String[shape.size()];
                for (int i = 0; i < shape.size(); i++) {
                    shapeArr[i] = shape.get(i).getAsString();
                }
                var map = new Char2ObjectOpenHashMap<ItemDescriptor>(4);
                for (var entry : input.entrySet()) {
                    var key = entry.getKey().charAt(0);
                    var value = entry.getValue().getAsJsonObject();
                    if (jsonObjectContains(value, "type", "item")) {
                        map.put(key, new DefaultDescriptor(getItemFromJson(value)));
                    } else {
                        map.put(key, new ItemTagDescriptor(value.get("tag").getAsString(), 1));
                    }
                }
                manager.registerShapedRecipe(new ShapedRecipe(null, 1, getItemFromJson(output), shapeArr, map, List.of()));
            }
        }
    }

    public static void registerShapelessRecipes() throws IOException {
        var s = Main.class.getResourceAsStream("/recipe/shapeless.json5");
        if (s == null) {
            Main.INSTANCE.getLogger().warning("Failed to load shapeless recipes");
            return;
        }
        var manager = Server.getInstance().getCraftingManager();
        try (var recipeReader = GSON.newJsonReader(new InputStreamReader(s))) {
            var arr = JsonParser.parseReader(recipeReader).getAsJsonArray();
            for (var each : arr) {
                var recipeObj = each.getAsJsonObject();
                var input = recipeObj.get("input").getAsJsonArray();
                var output = recipeObj.get("output").getAsJsonObject();
                var list = new ArrayList<ItemDescriptor>(input.size());
                for (var eachInput : input) {
                    var value = eachInput.getAsJsonObject();
                    if (jsonObjectContains(value, "type", "item")) {
                        var item = getItemFromJson(value);
                        if (item.getCount() > 1) {
                            var singleItem = item.clone();
                            singleItem.setCount(1);
                            for (int i = 0; i < item.getCount(); i++) {
                                list.add(new DefaultDescriptor(singleItem));
                            }
                        } else {
                            list.add(new DefaultDescriptor(item));
                        }
                    } else {
                        if (value.get("count") instanceof JsonPrimitive primitive && primitive.isNumber()) {
                            var count = primitive.getAsInt();
                            if (count > 1) {
                                for (int i = 0; i < count; i++) {
                                    list.add(new ItemTagDescriptor(value.get("tag").getAsString(), 1));
                                }
                            } else {
                                list.add(new ItemTagDescriptor(value.get("tag").getAsString(), 1));
                            }
                        } else {
                            list.add(new ItemTagDescriptor(value.get("tag").getAsString(), 1));
                        }
                    }
                }
                manager.registerShapelessRecipe(new ShapelessRecipe(UUID.randomUUID().toString(), 1, getItemFromJson(output), list));
            }
        }
    }

    public static void registerWashRecipes() throws IOException {
        var s = Main.class.getResourceAsStream("/recipe/washing.json");
        if (s == null) {
            Main.INSTANCE.getLogger().warning("Failed to load grinding recipes");
            return;
        }
        var manager = Server.getInstance().getCraftingManager();
        try (var recipeReader = new InputStreamReader(s)) {
            var arr = JsonParser.parseReader(recipeReader).getAsJsonArray();
            for (var each : arr) {
                var recipeObj = each.getAsJsonObject();
                var input = recipeObj.get("input").getAsJsonObject();
                var output = recipeObj.get("output").getAsJsonObject();
                var outputBounce = recipeObj.get("outputBounce") instanceof JsonPrimitive primitive &&
                        primitive.isNumber() ? primitive.getAsInt() : 0;
                if (jsonObjectContains(input, "type", "item")) {
                    manager.registerModProcessRecipe(new WashingRecipe(getItemFromJson(input), getItemFromJson(output), outputBounce));
                } else {
                    manager.registerModProcessRecipe(new WashingRecipe(new ItemTagDescriptor(input.get("tag").getAsString(), 1), getItemFromJson(output), outputBounce));
                }
            }
        }
    }

    private static boolean jsonObjectContains(@NotNull JsonObject obj, String key, String value) {
        return obj.has(key) &&
                obj.get(key) instanceof JsonPrimitive primitive &&
                primitive.isString() &&
                primitive.getAsString().equals(value);
    }

    private static boolean jsonObjectContains(@NotNull JsonObject obj, String key, int value) {
        return obj.has(key) &&
                obj.get(key) instanceof JsonPrimitive primitive &&
                primitive.isNumber() &&
                primitive.getAsInt() == value;
    }

    private static boolean jsonObjectContains(@NotNull JsonObject obj, String key, boolean value) {
        return obj.has(key) &&
                obj.get(key) instanceof JsonPrimitive primitive &&
                primitive.isBoolean() &&
                primitive.getAsBoolean() == value;
    }

    private static Item getItemFromJson(@NotNull JsonObject obj) {
        Item item;
        if (!obj.has("id")) {
            item = Item.get(0);
        } else {
            item = Item.fromString(obj.get("id").getAsString());
        }
        if (obj.has("count")) {
            item.setCount(obj.get("count").getAsInt());
        }
        return item;
    }

    private static <T extends Recipe> void registerRecipe(@NotNull Consumer<T> registry, T recipe, double xp) {
        registry.accept(recipe);
        if (xp > 0) {
            Server.getInstance().getCraftingManager().setRecipeXp(recipe, xp);
        }
    }
}
