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
        var textureDataObj = new JsonObject();
        var zipOutputStream = new ZipOutputStream(new FileOutputStream("target/TechDawn.mcpack"));
        Files.walk(folderPath).forEach(each -> {
            if (Files.isRegularFile(each)) {
                var relativePath = folderPath.relativize(each).toString().replace('\\', '/');

                if ("textures/terrain_texture.json".equals(relativePath)) {
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

                    var textureData = new JsonObject();
                    var textures = new JsonObject();
                    textures.addProperty("path", tmpPath);
                    textureData.add("textures", textures);
                    textureDataObj.add(tmpPath.replace("/", "-").replaceFirst("textures", "techdawn"), textureData);
                }
            }
        });
        // 读取、更新并写入terrain_texture.json
        var gson = new GsonBuilder().setPrettyPrinting().create();
        var terrainTexture = gson.fromJson(
                Files.readString(folderPath.resolve("textures/terrain_texture.json"), StandardCharsets.UTF_8),
                JsonObject.class
        );
        var originTextureDataObj = terrainTexture.getAsJsonObject("texture_data");
        textureDataObj.entrySet().forEach(each -> {
            originTextureDataObj.add(each.getKey(), each.getValue());
        });
        terrainTexture.add("texture_data", originTextureDataObj);
        zipOutputStream.putNextEntry(new ZipEntry("textures/terrain_texture.json"));
        zipOutputStream.write(gson.toJson(terrainTexture).getBytes(StandardCharsets.UTF_8));
        // 生成texture_list.json
        zipOutputStream.putNextEntry(new ZipEntry("textures/texture_list.json"));
        zipOutputStream.write(new GsonBuilder().setPrettyPrinting().create().toJson(textureList).getBytes(StandardCharsets.UTF_8));
        zipOutputStream.flush();
        zipOutputStream.close();
    }
}
