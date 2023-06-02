package cn.powernukkitx.techdawn.util;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.zip.ZipFile;

@SuppressWarnings("ResultOfMethodCallIgnored")
public final class HttpUtil {
    private HttpUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String get(String url) throws IOException, InterruptedException {
        var httpClient = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static void downloadAndDecompressZip(@NotNull File dir, String url) throws IOException, InterruptedException {
        var httpClient = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        var result = httpClient.send(request, HttpResponse.BodyHandlers.ofFile(new File(dir, "tmp.zip").toPath()));
        if (result.statusCode() != 200) {
            throw new IOException("Failed to download file: " + result.statusCode());
        }
        try (var zipFile = new ZipFile(new File(dir, "tmp.zip"))) {
            var entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                var entry = entries.nextElement();
                var entryFile = new File(dir, entry.getName());
                if (entry.isDirectory()) {
                    entryFile.mkdirs();
                } else {
                    entryFile.getParentFile().mkdirs();
                    try (var inputStream = zipFile.getInputStream(entry);
                         var outputStream = new FileOutputStream(new File(dir, entry.getName()))) {
                        inputStream.transferTo(outputStream);
                    }
                }
            }
        }
        new File(dir, "tmp.zip").delete();
    }
}
