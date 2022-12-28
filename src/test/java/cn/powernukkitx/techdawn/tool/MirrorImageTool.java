package cn.powernukkitx.techdawn.tool;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MirrorImageTool {

    public static void main(String[] args) {
        var name = "tough_copper_hammer";
        mirrorImage("src/main/resourcePack/textures/items/hammer/"+name+".png", "src/main/resourcePack/textures/items/hammer/"+name+".png");
    }

    /**
     * 图片镜像翻转
     *
     * @param source 原图片路径
     * @param target 翻转后图片输出路径
     */
    public static void mirrorImage(String source, String target) {
        File file;
        BufferedImage image;
        try {
            file = new File(source);
            image = ImageIO.read(file);

            int width = image.getWidth();
            int height = image.getHeight();

            for (int j = 0; j < height; j++) {
                int l = 0, r = width - 1;
                while (l < r) {
                    int pl = image.getRGB(l, j);
                    int pr = image.getRGB(r, j);
                    image.setRGB(l, j, pr);
                    image.setRGB(r, j, pl);
                    l++;
                    r--;
                }
            }

            file = new File(target);
            ImageIO.write(image, getSuffix(source), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    private static String getSuffix(@NotNull String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
