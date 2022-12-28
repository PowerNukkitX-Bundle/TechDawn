package cn.powernukkitx.techdawn.util;

import cn.nukkit.Server;
import cn.nukkit.inventory.BlastFurnaceRecipe;
import cn.nukkit.inventory.FurnaceRecipe;
import cn.nukkit.inventory.Recipe;
import cn.nukkit.inventory.SmokerRecipe;
import cn.nukkit.item.Item;
import cn.powernukkitx.techdawn.Main;
import cn.powernukkitx.techdawn.recipe.ForgingRecipe;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public final class RecipeUtil {
    private static final Gson GSON = new Gson();

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
                var output = recipeObj.get("output").getAsJsonObject();
                if (jsonObjectContains(input, "type", "item")) {
                    manager.registerModProcessRecipe(new ForgingRecipe(getItemFromJson(input), getItemFromJson(output)));
                } else {
                    manager.registerModProcessRecipe(new ForgingRecipe(input.get("tag").getAsString(), getItemFromJson(output)));
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
        if (!obj.has("id")) {
            return Item.get(0);
        } else {
            return Item.fromString(obj.get("id").getAsString());
        }
    }

    private static <T extends Recipe> void registerRecipe(@NotNull Consumer<T> registry, T recipe, double xp) {
        registry.accept(recipe);
        if (xp > 0) {
            Server.getInstance().getCraftingManager().setRecipeXp(recipe, xp);
        }
    }
}