package cn.powernukkitx.techdawn.util;

import cn.nukkit.Server;
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
        try (var recipeReader = new InputStreamReader(s)){
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

    private static Item getItemFromJson(@NotNull JsonObject obj) {
        if (!obj.has("id")) {
            return Item.get(0);
        } else {
            return Item.fromString(obj.get("id").getAsString());
        }
    }
}
