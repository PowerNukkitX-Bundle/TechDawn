package cn.powernukkitx.techdawn.util;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class RegistryManifestUtil {
    private RegistryManifestUtil() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public static <T> List<Class<? extends T>> registryManifestOf(@NotNull Class<T> clazz) {
        try {
            var registryClass = Class.forName("cn.powernukkitx.techdawn.RegistryManifest");
            var field = registryClass.getField(clazz.getSimpleName());
            var value = field.get(null);
            return (List<Class<? extends T>>) value;
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("No manifest of " + clazz.getName() + " found.", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<String> registryManifestDataOf(@NotNull Class<?> clazz) {
        try {
            var registryClass = Class.forName("cn.powernukkitx.techdawn.RegistryManifest");
            var field = registryClass.getField(clazz.getSimpleName() + "_Data");
            var value = field.get(null);
            return (List<String>) value;
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("No manifest data of " + clazz.getName() + " found.", e);
        }
    }
}
