package xin.harrison.hcode.utils;

import xin.harrison.hcode.enums.FormatEnum;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

/**
 * 图片工具类
 *
 * @author Harrison
 * @version 1.0.0
 * @since 2025/8/2
 */
public class ImageUtil {

    /**
     * 将图片写入文件
     *
     * @param image 图片
     * @param path  路径
     * @throws IOException 异常
     */
    public static void writeToFile(BufferedImage image, String path) throws IOException {
        ImageIO.write(image, FormatEnum.Image.PNG.name(), new File(path));
    }

    /**
     * 将图片转为base64
     *
     * @param image 图片流
     * @return base64 字符串
     * @throws IOException 异常
     */
    public static String toBase64(BufferedImage image){
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, FormatEnum.Image.PNG.name(), out);
            return Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}