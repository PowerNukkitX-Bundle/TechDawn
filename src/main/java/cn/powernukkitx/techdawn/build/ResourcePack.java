package cn.powernukkitx.techdawn.build;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

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
        var zipOutputStream = new ZipOutputStream(new FileOutputStream("target/TeachDawn.mcpack"));
        Files.walk(folderPath).forEach(each -> {
            if (Files.isRegularFile(each)) {
                try {
                    zipOutputStream.putNextEntry(new ZipEntry(folderPath.relativize(each).toString()));
                    zipOutputStream.write(Files.readAllBytes(each));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (each.toString().endsWith(".png")) {
                    textureList.add(folderPath.relativize(each).toString().replace('\\', '/').replace(".png", ""));
                }
            }
        });
        zipOutputStream.putNextEntry(new ZipEntry("textures/texture_list.json"));
        zipOutputStream.write(new GsonBuilder().setPrettyPrinting().create().toJson(textureList).getBytes(StandardCharsets.UTF_8));
        zipOutputStream.flush();
        zipOutputStream.close();
    }
}
