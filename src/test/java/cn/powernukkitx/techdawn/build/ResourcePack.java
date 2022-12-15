package cn.powernukkitx.techdawn.build;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ResourcePack {
    public static void main(String[] args) throws IOException {
        var folder = new File("src/main/resourcePack");
        if (!folder.exists()) {
            return;
        }
        var folderPath = folder.toPath();
        var textureList = new JsonArray();
        var textureDataObj = new JsonObject(); // inside terrain_texture.json
        var itemTextureObj = new JsonObject(); // inside item_texture.json
        var zipOutputStream = new ZipOutputStream(new FileOutputStream("target/TechDawn.mcpack"));
        try (var stream = Files.walk(folderPath)) {
            stream.forEach(each -> {
                if (Files.isRegularFile(each)) {
                    var relativePath = folderPath.relativize(each).toString().replace('\\', '/');

                    if ("textures/terrain_texture.json".equals(relativePath) || "textures/item_texture.json".equals(relativePath)) {
                        return;
                    }

                    try {
                        zipOutputStream.putNextEntry(new ZipEntry(relativePath));
                        zipOutputStream.write(Files.readAllBytes(each));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (each.toString().endsWith(".png")) {
                        var tmpPath = relativePath.replace(".png", "");
                        textureList.add(tmpPath);
                        // add entry of terrain_texture.json
                        if (tmpPath.startsWith("textures/blocks")) {
                            var textureData = new JsonObject();
                            var textures = new JsonObject();
                            textures.addProperty("path", tmpPath);
                            textureData.add("textures", textures);
                            textureDataObj.add(tmpPath.replace("/", "-").replaceFirst("textures", "techdawn"), textureData);
                        }
                        // add entry of item_texture.json
                        if (tmpPath.startsWith("textures/items")) {
                            var textureData = new JsonObject();
                            var textures = new JsonObject();
                            textures.addProperty("path", tmpPath);
                            textureData.add("textures", textures);
                            itemTextureObj.add(tmpPath.replace("/", "-").replaceFirst("textures", "techdawn"), textureData);
                        }
                    }
                }
            });
        }
        // 读取、更新并写入terrain_texture.json
        var gson = new GsonBuilder().setPrettyPrinting().create();
        var terrainTexture = gson.fromJson(
                Files.readString(folderPath.resolve("textures/terrain_texture.json"), StandardCharsets.UTF_8),
                JsonObject.class
        );
        var originTextureDataObj = terrainTexture.getAsJsonObject("texture_data");
        textureDataObj.entrySet().forEach(each -> originTextureDataObj.add(each.getKey(), each.getValue()));
        terrainTexture.add("texture_data", originTextureDataObj);
        zipOutputStream.putNextEntry(new ZipEntry("textures/terrain_texture.json"));
        zipOutputStream.write(gson.toJson(terrainTexture).getBytes(StandardCharsets.UTF_8));
        // 读取、更新并写入item_texture.json
        var itemTexture = gson.fromJson(
                Files.readString(folderPath.resolve("textures/item_texture.json"), StandardCharsets.UTF_8),
                JsonObject.class
        );
        var originItemTextureObj = itemTexture.getAsJsonObject("texture_data");
        itemTextureObj.entrySet().forEach(each -> originItemTextureObj.add(each.getKey(), each.getValue()));
        itemTexture.add("texture_data", originItemTextureObj);
        zipOutputStream.putNextEntry(new ZipEntry("textures/item_texture.json"));
        zipOutputStream.write(gson.toJson(itemTexture).getBytes(StandardCharsets.UTF_8));
        // 生成texture_list.json
        zipOutputStream.putNextEntry(new ZipEntry("textures/texture_list.json"));
        zipOutputStream.write(new GsonBuilder().setPrettyPrinting().create().toJson(textureList).getBytes(StandardCharsets.UTF_8));
        zipOutputStream.flush();
        zipOutputStream.close();
    }
}
