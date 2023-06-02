package cn.powernukkitx.techdawn.cmd;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.tree.ParamList;
import cn.nukkit.command.utils.CommandLogger;
import cn.powernukkitx.techdawn.Main;
import cn.powernukkitx.techdawn.util.HttpUtil;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class UpdateTechDawnCommand extends Command {
    public UpdateTechDawnCommand() {
        super("update-techdawn", "Update techdawn");
        this.setPermission("techdawn.all");
        this.commandParameters.clear();
        this.commandParameters.put("default",
                new CommandParameter[]{
                        CommandParameter.newEnum("operation", new String[]{"update", "update-and-stop"})
                });
        this.enableParamTree();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public int execute(CommandSender sender, String commandLabel, Map.Entry<String, ParamList> result, CommandLogger log) {
        var list = result.getValue();
        String operation = list.getResult(0);
        if (!"update".equals(operation) && !"update-and-stop".equals(operation)) {
            log.addError("Invalid operation: " + operation).output();
            return 0;
        }
        var currentJarModifiedTime = Main.INSTANCE.getFile().lastModified();
        var currentMcPackModifiedTime = new File("./resource_packs/TechDawn.mcpack").lastModified();
        var jarUpdated = false;
        var mcPackUpdated = false;
        try {
            var data = HttpUtil.get("https://www.powernukkitx.com/api/git/latest-build/PowerNukkitX/TechDawn");
            var jsonObject = JsonParser.parseString(data).getAsJsonObject();
            for (var each : jsonObject.entrySet()) {
                if (each.getValue().getAsJsonObject().get("name").getAsString().equals("ModPack")) {
                    if (jarUpdated) continue;
                    var latestJarModifiedTime = each.getValue().getAsJsonObject().get("createAt").getAsLong();
                    if (latestJarModifiedTime > currentJarModifiedTime) {
                        log.addSuccess("New version found!").output();
                        log.addSuccess("Downloading...").output();
                        var oldFile = new File(Main.INSTANCE.getFile().getParentFile(), "TechDawn.old.jar");
                        Files.move(Main.INSTANCE.getFile().toPath(), oldFile.toPath());
                        HttpUtil.downloadAndDecompressZip(Main.INSTANCE.getFile().getParentFile(), "https://powernukkitx.com/api/download/" +
                                each.getValue().getAsJsonObject().get("downloadId").getAsLong());
                        log.addSuccess("Downloaded!").output();
                        jarUpdated = true;
                        oldFile.delete();
                    }
                } else if (each.getValue().getAsJsonObject().get("name").getAsString().equals("ResourcePack")) {
                    if (mcPackUpdated) continue;
                    var latestMcPackModifiedTime = each.getValue().getAsJsonObject().get("createAt").getAsLong();
                    if (latestMcPackModifiedTime > currentMcPackModifiedTime) {
                        log.addSuccess("Downloading...").output();
                        var oldFile = new File("./resource_packs/TechDawn.old.mcpack");
                        Files.move(new File("./resource_packs/TechDawn.mcpack").toPath(), oldFile.toPath());
                        HttpUtil.downloadAndDecompressZip(new File("./resource_packs"), "https://powernukkitx.com/api/download/" +
                                each.getValue().getAsJsonObject().get("downloadId").getAsLong());
                        log.addSuccess("Downloaded!").output();
                        mcPackUpdated = true;
                        oldFile.delete();
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            log.addError("Failed to get latest version: " + e.getMessage()).output();
            e.printStackTrace();
            return 0;
        }
        if (jarUpdated || mcPackUpdated) {
            log.addSuccess("Updated!").output();
            if ("update-and-stop".equals(operation)) {
                Main.INSTANCE.getServer().shutdown();
            }
        }
        return 1;
    }
}
