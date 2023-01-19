package cn.powernukkitx.techdawn.tool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

/**
 * 用于去除24位png图片背景中的黑影
 */
public class RemoveBgTool {
    public static void main(String[] args) throws IOException {
        Path path;
        if (args.length > 0) {
            path = Path.of(args[0]);
        } else {
            throw new IllegalArgumentException("No path specified");
        }
        var image = ImageIO.read(path.toFile());
        var width = image.getWidth();
        var height = image.getHeight();
        var outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        var black = Color.BLACK.getRGB();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                var color = image.getRGB(x, y);
                if (color != 0 && color != black) {
                    outputImage.setRGB(x, y, color);
                }
            }
        }
        ImageIO.write(outputImage, "png", path.toFile());
    }
}
