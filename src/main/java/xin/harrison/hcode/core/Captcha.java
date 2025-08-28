package xin.harrison.hcode.core;

import xin.harrison.hcode.utils.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.SecureRandom;

/**
 * 验证码生成器
 *
 * @author Harrison
 * @version 1.0.0
 * @since 2025/8/2
 */
public class Captcha {

    private static final String CHAR_STRING = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz";
    private static final int WIDTH = 120;
    private static final int HEIGHT = 40;

    /**
     * 生成验证码图片
     *
     * @param text 验证码文本
     * @return 验证码图片
     */
    public static BufferedImage captchaImage(String text) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 背景
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 干扰线
        SecureRandom rand = new SecureRandom();
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < 5; i++) {
            int x1 = rand.nextInt(WIDTH);
            int y1 = rand.nextInt(HEIGHT);
            int x2 = rand.nextInt(WIDTH);
            int y2 = rand.nextInt(HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }

        // 字体
        g.setFont(new Font(Font.SERIF, Font.BOLD, 24));
        for (int i = 0; i < text.length(); i++) {
            g.setColor(new Color(rand.nextInt(200), rand.nextInt(100), rand.nextInt(200)));
            g.drawString(String.valueOf(text.charAt(i)), 20 * i + 10, 30);
        }

        g.dispose();
        return image;
    }

    /**
     * 获取验证码图片的base64编码
     *
     * @return 验证码图片的base64编码
     */
    public static String captchaBase64() {
        return ImageUtil.toBase64(captchaImage(randomText(4)));
    }

    /**
     * 生成随机文本
     *
     * @param length 文本长度
     * @return 随机文本
     */
    public static String randomText(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CHAR_STRING.charAt(random.nextInt(CHAR_STRING.length())));
        }
        return sb.toString();
    }
}