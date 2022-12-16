package cn.powernukkitx.techdawn.build;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class RegistryManifestGenerator {
    public static final File sourceDir = new File("src/main/java");
    public static final File javaFile = new File("target/generated-sources/auto-register/cn/powernukkitx/techdawn/RegistryManifest.java");
    public static final String Template = """
            package cn.powernukkitx.techdawn;
                        
            import java.util.List;
            %s
            public final class RegistryManifest {
                public RegistryManifest() {}
                
            %s
            %s}
            """;

    public static void main(String[] args) {
        ensureDirExists();
        var folderPath = sourceDir.toPath();
        Set<String> packageSet = new HashSet<>();
        Map<String, List<String>> registryMap = new HashMap<>();
        Map<String, List<String>> registerDataMap = new HashMap<>();
        try (var stream = Files.walk(folderPath)) {
            stream.forEach(path -> {
                if (!Files.isRegularFile(path)) return;
                try {
                    var code = Files.readString(path);
                    var annotationIndex = code.indexOf("@AutoRegister(");
                    if (annotationIndex == -1) return;
                    var annotationEndIndex = code.indexOf(")", annotationIndex);
                    if (annotationEndIndex == -1) return;
                    var annotationValue = code.substring(annotationIndex + 14, annotationEndIndex - 6);
                    var packageIndex = code.indexOf("package");
                    if (packageIndex == -1) return;
                    var packageEndIndex = code.indexOf(";", packageIndex);
                    if (packageEndIndex == -1) return;
                    var packageName = code.substring(packageIndex + 8, packageEndIndex);
                    packageSet.add(packageName);
                    registryMap.computeIfAbsent(annotationValue, k -> new ArrayList<>()).add(path.getFileName().toString().replace(".java", ""));
                    var registerDataIndex = code.indexOf("@AutoRegisterData(");
                    if (registerDataIndex != -1) {
                        var registerDataEndIndex = code.indexOf(")", registerDataIndex);
                        if (registerDataEndIndex != -1) {
                            var registerDataValue = code.substring(registerDataIndex + 18, registerDataEndIndex);
                            registerDataMap.computeIfAbsent(annotationValue, k -> new ArrayList<>()).add(registerDataValue);
                            return;
                        }
                    }
                    registerDataMap.computeIfAbsent(annotationValue, k -> new ArrayList<>()).add("\"\"");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        var importCodeBuilder = new StringBuilder();
        for (var each : packageSet) {
            importCodeBuilder.append("import ").append(each).append(".*;\n");
        }
        var registryCodeBuilder = new StringBuilder();
        for (var categoryEntry : registryMap.entrySet()) {
            var category = categoryEntry.getKey();
            registryCodeBuilder.append("    public static final List<Class<?>> ").append(category).append(" = List.of(\n")
                    .append(categoryEntry.getValue().stream().map(each -> "        " + each + ".class")
                            .collect(Collectors.joining(",\n"))).append("\n    );\n\n");
        }
        var registerDataCodeBuilder = new StringBuilder();
        for (var categoryEntry : registerDataMap.entrySet()) {
            var category = categoryEntry.getKey();
            registerDataCodeBuilder.append("    public static final List<String> ").append(category).append("_Data = List.of(\n")
                    .append(categoryEntry.getValue().stream().map(each -> "        " + each)
                            .collect(Collectors.joining(",\n"))).append("\n    );\n\n");
        }
        writeJavaFile(Template.formatted(importCodeBuilder, registryCodeBuilder, registerDataCodeBuilder));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void ensureDirExists() {
        File parent = javaFile.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void clearOldFile() {
        if (javaFile.exists()) {
            javaFile.delete();
        }
    }

    public static void writeJavaFile(String source) {
        try {
            if (Files.exists(javaFile.toPath()) && Files.readString(javaFile.toPath()).equals(source)) return;
            clearOldFile();
            Files.writeString(javaFile.toPath(), source);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
